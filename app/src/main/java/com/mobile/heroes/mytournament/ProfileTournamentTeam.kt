package com.mobile.heroes.mytournament

import SessionManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.MatchResource.MetaMatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MetaMatchResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import kotlinx.android.synthetic.main.activity_profile_tournament_team.*
import kotlinx.android.synthetic.main.activity_tournament_next_matches.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
private lateinit var sessionManager: SessionManager
private lateinit var apiClient: ApiClient
private lateinit var nextMatches:  MutableList<NextMatches>
private lateinit var adapter: NextMatchesAdapter


class ProfileTournamentTeam : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_tournament_team)
        backbutton()
        nextMatches= mutableListOf()
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        changeTeamProfileInfo()
        loadCompletedMatches()
        val auth= sessionManager.fetchAccount()?.authorities?.get(0)!!
        rvTeamCompletedMatches.layoutManager= LinearLayoutManager(this)
        adapter =NextMatchesAdapter(nextMatches, auth)
        rvTeamCompletedMatches.adapter=adapter

    }

    fun changeTeamProfileInfo(){
        val bundle = intent.extras
        val profileNickName = bundle?.get("INTENT_NICK_NAME")
        val profileTournaments = bundle?.get("INTENT_TOURNAMENTS")
        val profileTitles = bundle?.get("INTENT_TITLES")
        val profileGoals = bundle?.get("INTENT_GOALS")
        val profilePicture = bundle?.get("INTENT_TEAM_PICTURE")
        val profileIdUser = bundle?.get("INTENT_ID_USER")
        
        var profileNameTextView : TextView = findViewById(R.id.tv_tournament_team_profile_name)
        profileNameTextView.setText("$profileNickName")

        var profilePointsTextView : TextView = findViewById(R.id.tv_tournament_team_profile_tournaments_value)
        profilePointsTextView.setText("$profileTournaments")

        var profileGoalsDoneTextView : TextView = findViewById(R.id.tv_tournament_team_profile_goals_value)
        profileGoalsDoneTextView.setText("$profileGoals")

        var profileTitlesDoneTextView : TextView = findViewById(R.id.tv_tournament_team_profile_titulos_value)
        profileTitlesDoneTextView.setText("$profileTitles")

        val imageBytes = Base64.decode("$profilePicture",0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        var profileIconImageView : ImageView = findViewById(R.id.iv_tournament_team_profile_head_image)
        profileIconImageView.setImageBitmap(image)

    }

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_tournament_team_back)
        backButton.setOnClickListener {
            //startActivity(Intent(this,ProfileTournament::class.java))
            onBackPressed()
        }
    }

    private fun loadCompletedMatches(){
        val bundle = intent.extras
        var profileId= bundle?.get("INTENT_ID")
        var t= TournamentResponse(profileId.toString().toInt())
        val profileNickName = bundle?.get("INTENT_NICK_NAME")
        var score=""
        var metaMatch= MetaMatchRequest(idTournament = TournamentResponse(profileId.toString().toInt()), "Complete")
        LoadingScreen.displayLoadingWithText(this, "", false)
        apiClient.getApiService().getMatchesByTournamentAndStatus( token = "Bearer ${sessionManager.fetchAuthToken()}",metaMatch).enqueue(object:
            Callback<List<MetaMatchResponse>> {
            override fun onResponse(
                call: Call<List<MetaMatchResponse>>,
                response: Response<List<MetaMatchResponse>>
            ) {
                if (response.code()== 200){
                    teamTextViewError.visibility = View.INVISIBLE
                    for (i in response.body()!!.indices){
                        if(response.body()!!.get(i).userStatsHome!!.nickName!! ==profileNickName ||response.body()!!.get(i).userStatsAway!!.nickName!! ==profileNickName){
                            var decodedBitmapAway: Bitmap = response.body()!!.get(i).userStatsAway!!.icon!!.toBitmap()!!
                            var decodedBitmapHome: Bitmap = response.body()!!.get(i).userStatsHome!!.icon!!.toBitmap()!!
                            score= response.body()!!.get(i).matchDTO!!.goalsHome.toString() +" - "+ response.body()!!.get(i).matchDTO!!.goalsAway.toString()
                            var match=NextMatches(response.body()!!.get(i).matchDTO!!.date,"Por definir",score,
                                response.body()!!.get(i).userStatsHome!!.nickName!!,response.body()!!.get(i).userStatsAway!!.nickName!!,decodedBitmapHome,decodedBitmapAway,response.body()!!.get(i).matchDTO!!.id!!)
                            nextMatches.add(match)
                            adapter.notifyDataSetChanged()
                        }

                    }
                    nextMatches.sortBy { it.infoDate}
                    LoadingScreen.hideLoading()
                }
                else{
                    LoadingScreen.hideLoading()
                    teamTextViewError.text = "No han habido partidos completados"
                    teamTextViewError.visibility = View.VISIBLE
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



}
