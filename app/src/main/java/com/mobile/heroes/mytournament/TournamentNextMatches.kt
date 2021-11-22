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


class TournamentNextMatches : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_next_matches)
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        listOfMatches= mutableListOf<Int>()
        checkTournamentMatches()
    }


    //Paso 1 Traer una lista de Matches

    private fun checkTournamentMatches(){
        val bundle = intent.extras
        val profileId = bundle?.get("INTENT_ID")
        println(profileId)
        lateinit var statsHome: TTHelper
        lateinit var statsAway: TTHelper

        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getMatchesByTournament(token = "Bearer ${sessionManager.fetchAuthToken()}",id= "$profileId".toInt()).enqueue(object: Callback<List<MatchResponce>>
        {
            override fun onResponse(call: Call<List<MatchResponce>>, response: Response<List<MatchResponce>>) {
                if (response.body()!!.size >0){
                    for (i in response.body()!!.indices){
                        statsHome= bringUserStats(bringIdUsers(response.body()!!.get(i).idTeamTournamentHome.id!!))
                        statsAway= bringUserStats(bringIdUsers(response.body()!!.get(i).idTeamTournamentVisitor.id!!))
                        var match= MatchDTO(response.body()!!.get(i).date.dateToString("EE dd MMM yyyy"),statsHome.nickName,statsAway.nickName,response.body()!!.get(i).idField.name!!,statsHome.logo, statsAway.logo)
                        matchesList.add(match)
                    }
                }
//                loadMatches()
            }
            override fun onFailure(call: Call<List<MatchResponce>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }




    private fun bringUserStats(listIdAway: Int): TTHelper {
        var stats= TTHelper("","")
        apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= listIdAway).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0){
                    stats= TTHelper( response.body()!!.get(0).nickName!!,response.body()!!.get(0).icon!!)
                }
            }
            override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
        return stats
    }


    private fun bringIdUsers(teamTournamenteId: Int): Int{
        var idUser= 3
        apiClient.getApiService().getTeamTournamentsById(token = "Bearer ${sessionManager.fetchAuthToken()}",id= teamTournamenteId).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                if (response.body()!!.size >0){
                    idUser= response.body()!!.get(0).idUser!!.id!!
                }
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
        return idUser
    }



    private fun loadMatches(){
        var nextMatches:  MutableList<NextMatches>
        nextMatches= mutableListOf()
        matchesList.sortBy { it.infoDate }
        for (i in matchesList.indices){

            var decodedBitmapAway: Bitmap? = matchesList.get(i).logoAway.toBitmap()
            var decodedBitmapHome: Bitmap? = matchesList.get(i).logoHome.toBitmap()
            if(decodedBitmapAway!=null && decodedBitmapHome!=null){
                val next= NextMatches(matchesList.get(i).infoDate,matchesList.get(i).location,"",matchesList.get(i).home,matchesList.get(i).away,decodedBitmapHome,decodedBitmapAway)
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
