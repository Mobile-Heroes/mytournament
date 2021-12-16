package com.mobile.heroes.mytournament.tournamentprofile

import SessionManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobile.heroes.mytournament.*
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class TournamentProfileTeamAdapter(
    private var equipos: List<UserStatsResponse>,
    private var idTournament: Int,
    private var organizador: Boolean
) :
    RecyclerView.Adapter<TournamentProfileTeamViewHolder>() {

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TournamentProfileTeamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TournamentProfileTeamViewHolder(
            layoutInflater.inflate(
                R.layout.item_tournament_team,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TournamentProfileTeamViewHolder, position: Int) {
        holder.itemName.text = equipos[position].nickName
        holder.itemTitles.text = equipos[position].titles.toString()

        val imageBytes = Base64.decode(equipos[position].icon, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.itemPicture.setImageBitmap(image)

        holder.itemView.setOnClickListener {
            //Toast.makeText(holder.itemView.context, "Seleccionado en el equipo # ${holder.itemName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ProfileTournamentTeam::class.java)
            intent.putExtra("INTENT_NICK_NAME", equipos[position].nickName)
            intent.putExtra("INTENT_GOALS", equipos[position].goals)
            intent.putExtra("INTENT_TITLES", equipos[position].titles)
            intent.putExtra("INTENT_ID", "$idTournament")
            intent.putExtra("INTENT_TOURNAMENTS", equipos[position].tournaments)
            intent.putExtra("INTENT_TEAM_PICTURE", equipos[position].icon)
            intent.putExtra("INTENT_ID_USER", equipos[position].idUser.toString())
            holder.itemView.context.startActivity(intent)
        }

        System.out.println("Organizador:" + organizador)

        if (organizador == true) {
            holder.itemDelete.setOnClickListener {
                //DENTRO DE ESTE LISTENER SUSTITUIR POR CODIGO PARA REMOVER EQUIPO DEL TORNEO
                val intent = Intent(holder.itemView.context, MainActivity::class.java)
                holder.itemDelete.context.startActivity(intent)

                leaveTheTournament(holder.itemView.context, equipos[position].idUser?.id!!)

                //LOS SIGUIENTES SON ALGUNOS DATOS DEL EQUIPO SELECCIONADO COMO EJEMPLO
                System.out.println("USER ID:" + equipos[position].idUser)
                System.out.println("TOURNAMENT ID:" + idTournament)
            }

        } else {
            holder.itemDelete.setVisibility(View.GONE)
        }
    }

    //CLASS CONTINUE...
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun leaveTheTournament(context: Context, position: Int) {
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(context)
        val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT
        val profileTournamentId = idTournament
        val now = ZonedDateTime.now()
        var TeamTournamentList: List<TeamTournamentResponse>? = null
        var idTeamTournament: Int? = 0
        var countMatches = 0
        var repeter = 0;
        var allMatches: List<MatchResponce>? = null
        lateinit var tournament: TournamentResponse
        lateinit var profileStartDate: Date
        lateinit var date: ZonedDateTime
        lateinit var dateString: String
        lateinit var zoneDateTime: ZonedDateTime

        apiClient
            .getApiService()
            .getOneTournament(
                token = "Bearer ${sessionManager.fetchAuthToken()}",
                profileTournamentId.toString()
            ).enqueue(object : Callback<TournamentResponse> {
                override fun onResponse(
                    call: Call<TournamentResponse>,
                    response: Response<TournamentResponse>
                ) {
                    tournament = response.body()!!
                    profileStartDate = response.body()?.startDate!!
                    date = ZonedDateTime.ofInstant(
                        profileStartDate.toInstant(),
                        ZoneId.systemDefault()
                    )
                    dateString = date.format(formatter)
                    zoneDateTime = ZonedDateTime.parse(dateString)

                    if (now < zoneDateTime) {
                        LoadingScreen.displayLoadingWithText(context, "Please wait...", false)
                        apiClient.getApiService()
                            .getTeamTournamentByTournament(
                                profileTournamentId.toString()
                            ).enqueue(object : Callback<List<TeamTournamentResponse>> {
                                override fun onResponse(
                                    call: Call<List<TeamTournamentResponse>>,
                                    response: Response<List<TeamTournamentResponse>>
                                ) {

                                    TeamTournamentList = response.body()
                                    println("Mae entre y tengo los equipos del torneo, vea la vara:")
                                    println(TeamTournamentList)
                                    println("")

                                    apiClient.getApiService()
                                        .getMatchesByTournament(
                                            token = "Bearer ${sessionManager.fetchAuthToken()}",
                                            Integer.parseInt(profileTournamentId.toString()),
                                            "Scheduled"
                                        ).enqueue(object : Callback<List<MatchResponce>> {
                                            override fun onResponse(
                                                call: Call<List<MatchResponce>>,
                                                response: Response<List<MatchResponce>>
                                            ) {

                                                allMatches = response.body()!!

                                                for (team in TeamTournamentList!!) {
                                                    if (team.idUser?.id == position) {
                                                        println("Mae entre y tengo el equipo que ocupo del torneo, vea:")
                                                        println(team.id)
                                                        println("")
                                                        idTeamTournament = team.id
                                                        break
                                                    }
                                                }

                                                apiClient.getApiService().deleteTeamTournament(
                                                    token = "Bearer ${sessionManager.fetchAuthToken()}",
                                                    idTeamTournament.toString()
                                                ).enqueue(object :
                                                    Callback<TeamTournamentResponse> {
                                                    override fun onResponse(
                                                        call: Call<TeamTournamentResponse>,
                                                        response: Response<TeamTournamentResponse>
                                                    ) {
                                                        println(response.code())
                                                        LoadingScreen.hideLoading()
                                                        println("Mae elimine al grupo del torneo")
                                                        val intent =
                                                            Intent(
                                                                context,
                                                                ProfileTournament::class.java
                                                            )
                                                        intent.putExtra(
                                                            "INTENT_NAME",
                                                            "${tournament.name}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_DESCRIPTION",
                                                            "${tournament.description}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_START_DATE",
                                                            "$profileStartDate"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_FORMAT",
                                                            "${tournament.format}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_ID",
                                                            "${tournament.id}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_PARTICIPANTS",
                                                            "${tournament.participants}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_MATCHES",
                                                            "${tournament.matches}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_ICON",
                                                            "${tournament.icon}"
                                                        )
                                                        intent.putExtra(
                                                            "INTENT_STATUS",
                                                            "${tournament.status}"
                                                        )
                                                        context.startActivity(intent)
                                                    }

                                                    override fun onFailure(
                                                        call: Call<TeamTournamentResponse>,
                                                        t: Throwable
                                                    ) {
                                                        println(call)
                                                        println(t)
                                                        println("error")
                                                        Toast.makeText(
                                                            context,
                                                            "Error 4",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })
                                            }


                                            override fun onFailure(
                                                call: Call<List<MatchResponce>>,
                                                t: Throwable
                                            ) {
                                                println(call)
                                                println(t)
                                                println("error")
                                                Toast.makeText(
                                                    context,
                                                    "Error 3",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        })
                                }

                                override fun onFailure(
                                    call: Call<List<TeamTournamentResponse>>,
                                    t: Throwable
                                ) {
                                    println(call)
                                    println(t)
                                    println("error")
                                    Toast.makeText(
                                        context,
                                        "Error 1",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                    }
                }

                override fun onFailure(call: Call<TournamentResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }

}