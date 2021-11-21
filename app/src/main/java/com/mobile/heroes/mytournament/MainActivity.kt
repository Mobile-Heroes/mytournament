package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.databinding.ActivityMainBinding
import com.mobile.heroes.mytournament.feed.FeedAdapter
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import kotlinx.android.synthetic.main.fragment_feed_destination.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var feedTitleList = mutableListOf<String>()
    private var feedDescriptionList = mutableListOf<String>()
    private var feedUserList = mutableListOf<String>()
    private var feedImageList = mutableListOf<Int>()

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentFeedDestination, R.id.nav_mistorneos, R.id.nav_favoritos, R.id.nav_paypal
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        postToFeedList()

        rv_feed_card.layoutManager = LinearLayoutManager(this)
        rv_feed_card.adapter = FeedAdapter(feedTitleList, feedDescriptionList, feedUserList, feedImageList)
        checkTeamTournaments()
    }

    private fun addToList(title:String, description:String, user:String, image:Int){
        feedTitleList.add(title)
        feedDescriptionList.add(description)
        feedUserList.add(user)
        feedImageList.add(image)
    }

    private fun postToFeedList(){
        for(i:Int in 1..10){
            addToList("Título Torneo $i", "Descripción del Torneo $i", "organizador $i", R.drawable.ic_tournament_image)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkTeamTournaments(){
        val account=sessionManager.fetchAccount()
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getTeamTournamentsByIdUser(token = "Bearer ${sessionManager.fetchAuthToken()}",id= account!!.id).enqueue(object: Callback<List<TeamTournamentResponse>>
        {
            override fun onResponse(call: Call<List<TeamTournamentResponse>>, response: Response<List<TeamTournamentResponse>>) {
                println(response.body())
                if (response.body()!!.size >0){
                    sessionManager.saveTeamTournament(response.body()!!.get(0))
                }
                println(sessionManager.fetchTeamTournament())
            }
            override fun onFailure(call: Call<List<TeamTournamentResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
        val activity: Intent = Intent(applicationContext, Tournament::class.java)
        startActivity(activity)
        finish()

    }
}