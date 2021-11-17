package com.mobile.heroes.mytournament

import SessionManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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
import com.mobile.heroes.mytournament.networking.services.TournamentResource.ApiServiceTournament
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import kotlinx.android.synthetic.main.fragment_feed_destination.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var feedTitleList = mutableListOf<String?>()
    private var feedDescriptionList = mutableListOf<String?>()
    private var feedUserList = mutableListOf<String>()
    private var feedImageList = mutableListOf<Int>()
    private var tournamentList = mutableListOf<TournamentResponse>()
    private lateinit var apiServiceTournament: ApiServiceTournament
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //MANEJO DE SESSION
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

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

        getTournaments()

        rv_feed_card.layoutManager = LinearLayoutManager(this)
        rv_feed_card.adapter = FeedAdapter(feedTitleList, feedDescriptionList, feedUserList, feedImageList)
    }

    private fun addToList(title:String?, description:String?, user:String, image:Int){

        feedTitleList.add(title)
        feedDescriptionList.add(description)
        feedUserList.add(user)
        feedImageList.add(image)

    }

    private fun postToFeedList(){
        for(i:Int in 1..2){
            addToList("Titulo $i", "Descripcion $i", "organizador $i", R.drawable.ic_tournament_image)
        }
    }

    private fun postToFeedTournamentList(){
        for(i:Int in 1..tournamentList.size){
            addToList(tournamentList[i].name, tournamentList[i].description, "organizador $i", R.drawable.ic_tournament_image)
        }
    }

    private fun getTournaments() {


        LoadingScreen.displayLoadingWithText(this,"Please wait...",false)


        apiClient.getApiService().getTournament()
            .enqueue(object : Callback<List<TournamentResponse>> {
                override fun onFailure(call: Call<List<TournamentResponse>>, t: Throwable) {
                    HandleTournamentError()
                }

                override fun onResponse(
                    call: Call<List<TournamentResponse>>,
                    response: Response<List<TournamentResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null){

                        val tournaments : List<TournamentResponse> = response.body()!!
                        tournamentList = tournaments as MutableList<TournamentResponse>

                        var textName = tournaments[0].name
                        System.out.println("mensaje name:" + textName)

                        feedTitleList.add(textName)
                        feedDescriptionList.add(textName)
                        feedUserList.add("user")
                        feedImageList.add(R.drawable.ic_tournament_image)
                    }
                    LoadingScreen.hideLoading()
                }
            })

        postToFeedList()
        //postToFeedTournamentList()
    }

    fun HandleTournamentError() {
        LoadingScreen.hideLoading()

        runOnUiThread(){
            Toast.makeText(applicationContext, "Error al cargar torneos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}