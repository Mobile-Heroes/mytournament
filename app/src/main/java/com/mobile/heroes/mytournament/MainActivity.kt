package com.mobile.heroes.mytournament

import SessionManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
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
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import kotlinx.android.synthetic.main.fragment_feed_destination.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private var tournamentList = mutableListOf<TournamentResponse>()
    private var tournamentFeedList = mutableListOf<TournamentResponse>()
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

        changeProfileInfo()
        getTournaments()

        feedAdapter = FeedAdapter(tournamentFeedList)

        rv_feed_card.layoutManager = LinearLayoutManager(this)
        rv_feed_card.adapter = feedAdapter
    }

    private fun changeProfileInfo() {
        if(sessionManager != null){

            val account = sessionManager.fetchAccount()

            val navigationView : NavigationView  = findViewById(R.id.nav_view)
            val headerView : View = navigationView.getHeaderView(0)
            val navUsername : TextView = headerView.findViewById(R.id.tv_user_name)
            val navUserEmail : TextView = headerView.findViewById(R.id.tv_user_email)
            val navImage : ImageView = headerView.findViewById(R.id.iv_user_image)

            val userImage = sessionManager.fetchUserStats()?.icon
            val imageBytes = Base64.decode(userImage,0)
            val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

            navUsername.setText(account!!.firstName)
            navUserEmail.setText(account!!.email)

            if(image!=null){
                navImage.setImageBitmap(image)
            }else{
                navImage.setImageResource(R.drawable.ic_user_image)
            }


        }


    }

    private fun getTournaments() {
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        apiClient.getApiService().getTournamentInList()
            .enqueue(object : Callback<List<TournamentResponse>> {
                override fun onFailure(call: Call<List<TournamentResponse>>, t: Throwable) {
                    LoadingScreen.hideLoading()
                    HandleTournamentError()
                }

                override fun onResponse(
                    call: Call<List<TournamentResponse>>,
                    response: Response<List<TournamentResponse>>
                ) {
                    LoadingScreen.hideLoading()
                    if(response.isSuccessful && response.body() != null){
                        val tournaments : List<TournamentResponse> = response.body()!!
                        tournamentList = tournaments as MutableList<TournamentResponse>

                        tournamentFeedList.clear()
                        feedAdapter.notifyDataSetChanged()

                        for(i:Int in 0..tournamentList.size-1){
                            if(tournamentList[i].status == "Active"){
                                tournamentFeedList.add(tournamentList[i])
                            }
                            if(tournamentList[i].status == "InProgress"){
                                tournamentFeedList.add(tournamentList[i])
                            }

                        }

                    }

                }
            })
    }

    fun HandleTournamentError() {


        runOnUiThread(){
            Toast.makeText(applicationContext, "Error al cargar torneos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}