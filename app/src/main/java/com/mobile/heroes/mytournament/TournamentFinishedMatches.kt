package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.helpers.MatchDTO
import com.mobile.heroes.mytournament.helpers.TTHelper
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
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

private lateinit var listOfMatchesId: MutableList<Int>
private lateinit var infoMtches: MutableList<String>
private lateinit var infoScore: MutableList<String>
private lateinit var nextMatches:  MutableList<NextMatches>
private lateinit var bottomNavigationView : BottomNavigationView

private var profileName: Any? = ""
private var profileIcon: Any? = ""
private var profileDescription: Any? = ""
private var profileStartDate: Any? = ""
private var profileFormat: Any? = ""
private var profileId: Any? = ""
private var profileParticipants: Any? = ""
private var profileMatches: Any? = ""
private var profileStatus: Any? = ""



class TournamentFinishedMatches : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_finished_matches)

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

        listOfMatchesId= mutableListOf<Int>()
        infoMtches= mutableListOf<String>()
        infoScore= mutableListOf<String>()

        nextMatches= mutableListOf()
        loadIntentExtras()
        bottomNavigationMenu()
        MatchGenerator()

    }


    private fun MatchGenerator(){
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")
        var idUserHome =0
        var idUserVisit =0
        var teamTournamenteIdHome =0
        var teamTournamenteIdAway =0
        var status="Complete"
        var token= "Bearer ${sessionManager.fetchAuthToken()}"

        apiClient.getApiService().getMatchesByTournament( token,"$profileId".toInt(),  status).enqueue(object:
            Callback<List<MatchResponce>>
        {
            override fun onResponse(call: Call<List<MatchResponce>>, response: Response<List<MatchResponce>>) {
                if (response.body()!!.size >0){
                    for (i in response.body()!!.indices){
                        listOfIdTAway.add(response.body()!!.get(i).idTeamTournamentVisitor.id!!)
                        listOfIdTHome.add(response.body()!!.get(i).idTeamTournamentHome.id!!)
                        infoMtches.add(response.body()!!.get(i).date.dateToString("EE dd MMM yyyy"))
                        infoScore.add(response.body()!!.get(i).goalsHome.toString()+"-"+response.body()!!.get(i).goalsAway.toString())
                    }
                    println(listOfIdTHome)
                    for(i in listOfIdTHome.indices){

                        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listOfIdTHome[i]).enqueue(object:
                            Callback<List<TeamTournamentResponse>>
                        {
                            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                                if (response.body()!!.size >0){
                                    idUserHome= (response.body()!!.get(0).idUser!!.id!!)
                                }
                                var statsHome= TTHelper("","",0)
                                apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= idUserHome).enqueue(object:
                                    Callback<List<UserStatsResponse>>
                                {
                                    override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                                        if (response.body()!!.size >0) {
                                            statsHome = TTHelper(response.body()!!.get(0)!!.nickName!!,response.body()!!.get(0)!!.icon!!,1)
                                        }
                                        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listOfIdTAway[i]).enqueue(object:
                                            Callback<List<TeamTournamentResponse>>
                                        {
                                            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                                                if (response.body()!!.size >0){
                                                    idUserVisit= (response.body()!!.get(0).idUser!!.id!!)
                                                }

                                                var statsAway= TTHelper("","",0)
                                                apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= idUserVisit).enqueue(object:
                                                    Callback<List<UserStatsResponse>>
                                                {
                                                    override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                                                        if (response.body()!!.size >0) {
                                                            statsAway = TTHelper(response.body()!!.get(0)!!.nickName!!,response.body()!!.get(0)!!.icon!!,1)
                                                        }
                                                        println(statsHome)
                                                        println(statsAway)
                                                        var decodedBitmapAway: Bitmap? = statsAway.logo.toBitmap()
                                                        var decodedBitmapHome: Bitmap? = statsHome.logo.toBitmap()
                                                        var match= NextMatches(infoMtches.get(i),"Estadio Nacional",
                                                            infoScore.get(i),statsHome.nickName,statsAway.nickName,decodedBitmapHome!!,decodedBitmapAway!!)
                                                        println(match)
                                                        println(nextMatches)
                                                        println("Division-----------------------------------")
                                                        nextMatches.add(match)
                                                        nextMatches.sortBy { it.infoDate}
                                                        val adapter =NextMatchesAdapterWithoutId(nextMatches)
                                                        rvTournamentMatches.adapter=adapter

                                                    }

                                                    override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                                                        println(call)
                                                        println(t)
                                                        println("error")
                                                    }
                                                }
                                                )

                                            }
                                            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                                                println(call)
                                                println(t)
                                                println("error")
                                            }
                                        }
                                        )

                                    }
                                    override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                                        println(call)
                                        println(t)
                                        println("error")
                                    }
                                }
                                )

                            }
                            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                                println(call)
                                println(t)
                                println("error")
                            }
                        }
                        )
                    }
                }

            }
            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }

        )
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

    private fun loadIntentExtras() {
        val bundle = intent.extras
        profileName = bundle?.get("INTENT_NAME")
        profileIcon = bundle?.get("INTENT_ICON")
        profileDescription = bundle?.get("INTENT_DESCRIPTION")
        profileStartDate = bundle?.get("INTENT_START_DATE")
        profileFormat = bundle?.get("INTENT_FORMAT")
        profileId = bundle?.get("INTENT_ID")
        profileParticipants = bundle?.get("INTENT_PARTICIPANTS")
        profileMatches = bundle?.get("INTENT_MATCHES")
        profileStatus = bundle?.get("INTENT_STATUS")
    }


    private fun bottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        bottomNavigationView.setSelectedItemId(R.id.tournament_results)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.tournament_profile -> {
                    val intent = Intent(applicationContext, ProfileTournament::class.java)
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    startActivity(intent)
                }
                R.id.tournament_matches ->{
                    val intent = Intent(applicationContext, TournamentNextMatches::class.java)
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    startActivity(intent)
                }
                R.id.tournament_results ->{
                    //current item

                }
                R.id.tournament_table ->{
                    val intent = Intent(applicationContext, ProfileTournamentTable::class.java)
                    intent.putExtra("INTENT_NAME", "$profileName")
                    intent.putExtra("INTENT_DESCRIPTION", "$profileDescription")
                    intent.putExtra("INTENT_START_DATE", "$profileStartDate")
                    intent.putExtra("INTENT_FORMAT", "$profileFormat")
                    intent.putExtra("INTENT_ID", "$profileId")
                    intent.putExtra("INTENT_PARTICIPANTS", "$profileParticipants")
                    intent.putExtra("INTENT_MATCHES", "$profileMatches")
                    intent.putExtra("INTENT_ICON", "$profileIcon")
                    intent.putExtra("INTENT_STATUS", "$profileStatus")
                    startActivity(intent)                }
            }
            true

        }
    }

}