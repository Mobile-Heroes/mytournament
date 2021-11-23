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
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlinx.android.synthetic.main.activity_tournament.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


private lateinit var sessionManager: SessionManager
private lateinit var apiClient: ApiClient
private lateinit var matchesList: MutableList<MatchDTO>
private lateinit var matchesListFinal: MutableList<MatchDTO>
private lateinit var teamTournamentIdAway: MutableList<Int>
private lateinit var idUserToBring: MutableList<Int>

private lateinit var teamTournamentIdHome: MutableList<Int>
private lateinit var matchesList2: MutableList<MatchDTO>
private lateinit var idUserToBringHome: MutableList<Int>
private lateinit var matchListId: MutableList<Int>


class Tournament : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        matchesList= mutableListOf()
        matchesListFinal= mutableListOf()
        teamTournamentIdAway= mutableListOf()
        idUserToBring= mutableListOf()

        matchesList2= mutableListOf()
        teamTournamentIdHome= mutableListOf()
        idUserToBringHome= mutableListOf()
        matchListId= mutableListOf()


        checkMatches()




    }

    //Paso 1 traer matches Home del User loggeado
    private fun checkMatches(){
        val teamTournamentId=sessionManager.fetchTeamTournament()!!
        val userStats=sessionManager.fetchUserStats()!!

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getMatchesByTeamTournamentHome(token = "Bearer ${sessionManager.fetchAuthToken()}",id= teamTournamentId.id!!).enqueue(object: Callback<List<MatchResponce>>
        {
            override fun onResponse(call: Call<List<MatchResponce>>, response: Response<List<MatchResponce>>) {
                if (response.body()!!.size >0){

                    for (i in response.body()!!.indices){
                        var match= MatchDTO(response.body()!!.get(i).date!!.dateToString("EE dd MMM yyyy"),userStats.nickName!!,"","Ricardo Sapprissa",userStats.icon!!,"")
                        matchesList.add(match)
                        matchListId.add(response.body()!!.get(i).id!!)
                        teamTournamentIdAway.add(response.body()!!.get(i).idTeamTournamentVisitor?.id!!)
                    }
                    bringIdUsers(teamTournamentIdAway)
                }
                
            }
            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }



    //Paso 2 Traer los UserId del Away

    private fun bringIdUsers(listIdAway: List<Int>){
        val userStats=sessionManager.fetchUserStats()

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                if (response.body()!!.size >0){

                    for (i in response.body()!!.indices){
                        idUserToBring.add(response.body()!!.get(i).idUser!!.id!!)
                    }
                    brinUserStats(idUserToBring)
                }
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }

    //Paso 3 Traer los userStats de los away


    private fun brinUserStats(listIdAway: List<Int>){

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0){

                        for (i in response.body()!!.indices) {
                            var match= MatchDTO( matchesList.get(i).infoDate, matchesList.get(i).home,response.body()!!.get(i).nickName!!, matchesList.get(i).location,matchesList.get(i).logoHome,response.body()!!.get(i).icon!!)
//                            println(match)
                            matchesListFinal.add(match)
                        }
                    checkMatchesAway()
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





    private fun checkMatchesAway(){
        val teamTournamentId=sessionManager.fetchTeamTournament()!!
        val userStats=sessionManager.fetchUserStats()!!
        val barrear: String = sessionManager.fetchAuthToken()!!;

        apiClient.getApiService().getMatchesByTeamTournamentAway(token = "Bearer ${sessionManager.fetchAuthToken()}",id= teamTournamentId.id!!).enqueue(object: Callback<List<MatchResponce>>
        {
            override fun onResponse(call: Call<List<MatchResponce>>, response: Response<List<MatchResponce>>) {
                if (response.body()!!.size >0){

                    for (i in response.body()!!.indices){
                        var match= MatchDTO(response.body()!!.get(i).date!!.dateToString("EEEE dd MMM  yyyy"),"",userStats.nickName!!,"Ricardo Sapprissa","",userStats.icon!!)
                        matchesList2.add(match)
                        matchListId.add(response.body()!!.get(i).id!!)
                        teamTournamentIdHome.add(response.body()!!.get(i).idTeamTournamentHome?.id!!)
                    }
                    bringIdUsersHome(teamTournamentIdHome)
                }

            }
            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }



    private fun bringIdUsersHome(listIdAway: List<Int>){
        val userStats=sessionManager.fetchUserStats()

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                if (response.body()!!.size >0){

                    for (i in response.body()!!.indices){
                        idUserToBringHome.add(response.body()!!.get(i).idUser!!.id!!)
                    }
                    brinUserStatsHome(idUserToBringHome)
                }
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }



    private fun brinUserStatsHome(listIdAway: List<Int>){

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0){

                    for (i in response.body()!!.indices) {
                        var match= MatchDTO( matchesList2.get(i).infoDate,response.body()!!.get(i).nickName!!,matchesList2.get(i).away, matchesList2.get(i).location,response.body()!!.get(i).icon!!,matchesList2.get(i).logoAway)
//                            println(match)
                        matchesListFinal.add(match)
                    }
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
        matchesListFinal.sortBy { it.infoDate }
        for (i in matchesListFinal.indices){

                var decodedBitmapAway: Bitmap? = matchesListFinal.get(i).logoAway.toBitmap()
                var decodedBitmapHome: Bitmap? = matchesListFinal.get(i).logoHome.toBitmap()
                if(decodedBitmapAway!=null && decodedBitmapHome!=null){
                    val next= NextMatches(matchesListFinal.get(i).infoDate,matchesListFinal.get(i).location,"",matchesListFinal.get(i).home,matchesListFinal.get(i).away,decodedBitmapHome,decodedBitmapAway)
                    nextMatches.add(next)
                }
            }
        val adapter =NextMatchesAdapter(nextMatches, matchListId)
        rvTournament.adapter=adapter
        rvTournament.layoutManager= LinearLayoutManager(this)

    }

    fun String.toBitmap():Bitmap?{
        Base64.decode(this,Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }

    private fun Date.dateToString(format: String):String{
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }


}

