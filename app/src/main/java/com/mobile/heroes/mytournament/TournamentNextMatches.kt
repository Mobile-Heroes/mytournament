package com.mobile.heroes.mytournament

import SessionManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.heroes.mytournament.feed.FeedAdapter
import com.mobile.heroes.mytournament.helpers.MatchDTO
import com.mobile.heroes.mytournament.helpers.MatchRequestDTO
import com.mobile.heroes.mytournament.helpers.TTHelper
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.MatchResource.MetaMatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MetaMatchResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlinx.android.synthetic.main.activity_tournament_next_matches.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


private lateinit var sessionManager: SessionManager
private lateinit var apiClient: ApiClient

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
private var status: Any? = ""
private lateinit var adapter: NextMatchesAdapter


class TournamentNextMatches : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament_next_matches)
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        nextMatches= mutableListOf()

        loadIntentExtras()
        bottomNavigationMenu()
        loadMatches()

        val auth= sessionManager.fetchAccount()?.authorities?.get(0)!!
        rvTournamentMatches.layoutManager= LinearLayoutManager(this)
        adapter =NextMatchesAdapter(nextMatches, auth)
        rvTournamentMatches.adapter=adapter
    }

    private fun loadMatches(){
        var t= TournamentResponse(profileId.toString().toInt())
        var score="VS"
        var metaMatch= MetaMatchRequest(idTournament = TournamentResponse(profileId.toString().toInt()), status.toString())
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        apiClient.getApiService().getMatchesByTournamentAndStatus( token = "Bearer ${sessionManager.fetchAuthToken()}",metaMatch).enqueue(object: Callback<List<MetaMatchResponse>>{
            override fun onResponse(
                call: Call<List<MetaMatchResponse>>,
                response: Response<List<MetaMatchResponse>>
            ) {
                if (response.code()== 200){
                    textViewError.visibility = INVISIBLE
                    for (i in response.body()!!.indices){
                        var decodedBitmapAway: Bitmap = response.body()!!.get(i).userStatsAway!!.icon!!.toBitmap()!!
                        var decodedBitmapHome: Bitmap = response.body()!!.get(i).userStatsHome!!.icon!!.toBitmap()!!
                        if(status=="Complete")
                            score= response.body()!!.get(i).matchDTO!!.goalsHome.toString() +"-"+ response.body()!!.get(i).matchDTO!!.goalsAway.toString()
                        var match=NextMatches(response.body()!!.get(i).matchDTO!!.date.dateToString("dd MMM yyyy"),"Por definir",score,
                            response.body()!!.get(i).userStatsHome!!.nickName!!,response.body()!!.get(i).userStatsAway!!.nickName!!,decodedBitmapHome,decodedBitmapAway,response.body()!!.get(i).matchDTO!!.id!!)
                        nextMatches.add(match)
                        adapter.notifyDataSetChanged()
                    }
                    nextMatches.sortedBy { it.infoDate}
                    LoadingScreen.hideLoading()
                }
                else{
                    LoadingScreen.hideLoading()
                    if(status=="Complete")
                        textViewError.text = "No han habido partidos completados"
                    textViewError.visibility = VISIBLE
                }
            }
            override fun onFailure(call: Call<List<MetaMatchResponse>>, t: Throwable) {
                LoadingScreen.hideLoading()
            }

        })

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
        status = bundle?.get("MATCH_STATUS")
        val tv1: TextView = findViewById(R.id.txtViewMatches)
        if (status == "Scheduled")
            tv1.text = "Próximos partidos"
        else
            tv1.text = "Partidos finalizados"

    }

    private fun bottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_tournament)
        if(status =="Scheduled")
            bottomNavigationView.setSelectedItemId(R.id.tournament_matches)
        else
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
                    intent.putExtra("MATCH_STATUS", "Scheduled")
                    startActivity(intent)
                    finish()

                }
                R.id.tournament_results ->{
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
                    intent.putExtra("MATCH_STATUS", "Complete")
                    startActivity(intent)
                    finish()
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
