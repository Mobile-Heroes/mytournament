package com.mobile.heroes.mytournament

import SessionManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.helpers.MatchDTO
import com.mobile.heroes.mytournament.helpers.TTHelper
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlinx.android.synthetic.main.activity_tournament.*
import kotlinx.android.synthetic.main.activity_tournament.rvTournament
import kotlinx.android.synthetic.main.activity_tournament_next_matches.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


private lateinit var sessionManager: SessionManager
private lateinit var apiClient: ApiClient
private lateinit var listOfMatches: MutableList<Int>
private lateinit var matchesList: MutableList<MatchDTO>

private lateinit var listOfIdTHome: MutableList<Int>
private lateinit var listOfIdTAway: MutableList<Int>

private lateinit var listOfIdUHome: MutableList<Int>
private lateinit var listOfIdUAway: MutableList<Int>


private lateinit var listOfStatsHome: MutableList<TTHelper>
private lateinit var listOfStatsAway: MutableList<TTHelper>


class TournamentNextMatches : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_next_matches)
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        listOfMatches= mutableListOf<Int>()
        listOfIdTHome= mutableListOf<Int>()
        listOfIdTAway= mutableListOf<Int>()        
        listOfIdUAway= mutableListOf<Int>()
        listOfIdUHome= mutableListOf<Int>()
        listOfStatsHome= mutableListOf<TTHelper>()
        listOfStatsAway= mutableListOf<TTHelper>()
        matchesList= mutableListOf<MatchDTO>()

        checkTournamentMatches()
    }


    //Paso 1 Traer una lista de Matches

    private fun checkTournamentMatches(){
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getMatchesByTournament(token = "Bearer ${sessionManager.fetchAuthToken()}",id= "$profileId".toInt()).enqueue(object: Callback<List<MatchResponce>>
        {
            override fun onResponse(call: Call<List<MatchResponce>>, response: Response<List<MatchResponce>>) {
                if (response.body()!!.size >0){
                    for (i in response.body()!!.indices){
                        listOfIdTAway.add(response.body()!!.get(i).idTeamTournamentVisitor.id!!)
                        listOfIdTHome.add(response.body()!!.get(i).idTeamTournamentHome.id!!)
                        var match= MatchDTO(response.body()!!.get(i).date.dateToString("EE dd MMM yyyy"),"","","Ricardo Saprissa","","")
                        matchesList.add(match)
                    }
                }
                println(listOfIdTAway)
                bringIdUsersHome(listOfIdTHome)
            }
            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }

    //Viene con lista de Id Tournament para traer el idUserHome

    private fun bringIdUsersHome(teamTournamenteId: List<Int>){
        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= teamTournamenteId).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                if (response.body()!!.size >0){
                    println(response.body())
                    for (i in response.body()!!.indices){
                        listOfIdUHome.add(response.body()!!.get(i).idUser!!.id!!)
                    }
                }
                bringIdUsersAway(listOfIdTAway)
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }


    //Viene con lista de Id Tournament para traer el idUserAway

    private fun bringIdUsersAway(teamTournamenteId: List<Int>){
        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= teamTournamenteId).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                if (response.body()!!.size >0){
                    for (i in response.body()!!.indices){
                        listOfIdUAway.add(response.body()!!.get(i).idUser!!.id!!)
                    }
                }
                println(listOfIdUAway)
                bringUserStatsHome(listOfIdUHome)
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }





    private fun bringUserStatsHome(listIdAway: List<Int>) {
        var stats= TTHelper("","",0)
            apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<UserStatsResponse>>
            {
                override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                    if (response.body()!!.size >0) {

                        for (i in response.body()!!.indices) {
                            println(response.body()!!.get(i).nickName!!)
                            stats = TTHelper(
                                response.body()!!.get(i).nickName!!,
                                response.body()!!.get(i).icon!!,
                                response.body()!!.get(i).id!!,
                            )
                            listOfStatsHome.add(stats)
                        }
                        listOfStatsHome.sortBy{it.id}
                        bringUserStatsAway(listOfIdUAway)
                    }
                }
                override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                    println(call)
                    println(t)
                    println("error")
                }
            }
            )
        
 
    }


    private fun bringUserStatsAway(listIdAway: List<Int>) {
        var stats= TTHelper("","",0)
        apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0) {
                    for (i in response.body()!!.indices) {
                        stats = TTHelper(
                            response.body()!!.get(i).nickName!!,
                            response.body()!!.get(i).icon!!,
                            response.body()!!.get(i).id!!,
                            )
                        listOfStatsAway.add(stats)
                    }
                    listOfStatsAway.sortBy{it.id}
                    loadMatches()
                }
            }
            override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )


    }


    private fun loadMatches(){
        var nextMatches:  MutableList<NextMatches>
        nextMatches= mutableListOf()
//        matchesList.sortBy { it.infoDate }
        for (i in matchesList.indices){
            var decodedBitmapAway: Bitmap? = listOfStatsAway.get(i).logo.toBitmap()
            var decodedBitmapHome: Bitmap? = listOfStatsHome.get(i).logo.toBitmap()
            if(decodedBitmapAway!=null && decodedBitmapHome!=null){
                val next= NextMatches(matchesList.get(i).infoDate,matchesList.get(i).location,"",listOfStatsHome.get(i).nickName, listOfStatsAway.get(i).nickName,decodedBitmapHome,decodedBitmapAway)
                nextMatches.add(next)
            }
        }
        val adapter =NextMatchesAdapter(nextMatches)
        rvTournamentMatches.adapter=adapter
        rvTournamentMatches.layoutManager= LinearLayoutManager(this)

    }


    fun String.toBitmap(): Bitmap?{
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }

    private fun Date.dateToString(format: String):String{
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }




}
