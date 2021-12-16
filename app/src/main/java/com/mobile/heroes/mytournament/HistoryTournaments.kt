package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mobile.heroes.mytournament.databinding.ActivityMainBinding
import com.mobile.heroes.mytournament.feed.FeedAdapter
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.signup.SignUpOrganizer
import com.mobile.heroes.mytournament.ui.createTournament.create_tournament
import kotlinx.android.synthetic.main.fragment_feed_destination.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HistoryTournaments : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var feedAdapter: FeedAdapter
    private var tournamentList = mutableListOf<TournamentResponse>()
    private lateinit var tournamentFeedList: ArrayList<TournamentResponse>
    private lateinit var filteredTournamentFeedList: ArrayList<TournamentResponse>
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //MANEJO DE SESSION
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)
        tournamentFeedList = arrayListOf()
        filteredTournamentFeedList = arrayListOf()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentFeedDestination
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        changeProfileInfo()
        bottomNavigationMenu()
        getTournaments()

        feedAdapter = FeedAdapter(filteredTournamentFeedList)

        rv_feed_card.layoutManager = LinearLayoutManager(this)
        rv_feed_card.adapter = feedAdapter

        searchViewBar()
    }

    private fun searchViewBar() {

        val searchViewTournament = findViewById<View>(R.id.sv_buscar_torneo) as SearchView

        searchViewTournament.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                filterTournament(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filterTournament(newText!!)
                return false
            }
        })
    }

    private fun filterTournament(text: String){
        filteredTournamentFeedList.clear()
        val searchText = text!!.toLowerCase(Locale.getDefault())
        if(searchText.isNotEmpty()){
            tournamentFeedList.forEach {

                if(it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)){

                    filteredTournamentFeedList.add(it)
                }
            }
            rv_feed_card.adapter!!.notifyDataSetChanged()

        }else{

            filteredTournamentFeedList.clear()
            filteredTournamentFeedList.addAll(tournamentFeedList)
            rv_feed_card.adapter!!.notifyDataSetChanged()
        }
    }

    private fun changeProfileInfo() {
        if(sessionManager != null){

            val account = sessionManager.fetchAccount()

            val navigationView : NavigationView = findViewById(R.id.nav_view)
            val headerView : View = navigationView.getHeaderView(0)
            val navUsername : TextView = headerView.findViewById(R.id.tv_user_name)
            val navUserEmail : TextView = headerView.findViewById(R.id.tv_user_email)
            val navImage : ImageView = headerView.findViewById(R.id.iv_user_image)

            val userImage = sessionManager.fetchUserStats()?.icon
            val imageBytes = Base64.decode(userImage,0)
            val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)

            navUsername.setText(sessionManager.fetchUserStats()!!.nickName)
            navUserEmail.setText(account!!.email)

            val accountRole = sessionManager.fetchAccount()?.authorities?.get(0)
            if(image!=null && accountRole != "ROLE_ANONYMOUS"){
                navImage.setImageBitmap(image)
            }

        }
    }

    private fun getTournaments() {
        LoadingScreen.displayLoadingWithText(this, "", false)
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
                            if(tournamentList[i].status == "Inactive"){
                                tournamentFeedList.add(tournamentList[i])
                            }
                        }

                        showNavMenuByUser()

                        tournamentFeedList.sortByDescending{it.id}

                        filteredTournamentFeedList.addAll(tournamentFeedList)
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

    private fun bottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bn_bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.it_history_tournament)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.it_feed_tournament ->{
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                }
                R.id.it_join_tournament ->{
                    val intent = Intent(applicationContext, JoinTournaments::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                }
                R.id.it_history_tournament ->{
                    val intent = Intent(applicationContext, HistoryTournaments::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                }
            }
            true

        }
    }

    private fun showNavMenuByUser() {
        val accountRole = sessionManager.fetchAccount()?.authorities?.get(0)

        when (accountRole) {
            "ROLE_ADMIN" -> {
                removeVisibilityNavLogin()
            }

            "ROLE_TOURNAMENT" -> {
                removeVisibilityNavLogin()
            }
            "ROLE_USER" -> {
                removeVisibilityNavLogin()

                var itemMenuCrearTorneo : View = findViewById(R.id.it_crear_torneo)
                itemMenuCrearTorneo.setVisibility(View.GONE)

            }
            "ROLE_ANONYMOUS" -> {
                var itemMenuFavoritos : View = findViewById(R.id.it_favoritos)
                itemMenuFavoritos.setVisibility(View.GONE)

                var itemMenuCanchas : View = findViewById(R.id.it_canchas)
                itemMenuCanchas.setVisibility(View.GONE)

                var itemMenuCrearTorneo : View = findViewById(R.id.it_crear_torneo)
                itemMenuCrearTorneo.setVisibility(View.GONE)

                var itemMenuCerrarCesion : View = findViewById(R.id.nav_logout)
                itemMenuCerrarCesion.setVisibility(View.GONE)

                val navigationView : NavigationView  = findViewById(R.id.nav_view)
                val headerView : View = navigationView.getHeaderView(0)
                val navUsername : TextView = headerView.findViewById(R.id.tv_user_name)
                val navUserEmail : TextView = headerView.findViewById(R.id.tv_user_email)
                //val navImage : ImageView = headerView.findViewById(R.id.iv_user_image)

                navUsername.setVisibility(View.GONE)
                navUserEmail.setVisibility(View.GONE)
                //navImage.setVisibility(View.GONE)

                tv_nav_registrarse.setOnClickListener { view ->
                    val activityIntent = Intent(this, SignUpOrganizer::class.java)
                    startActivity(activityIntent)
                }

                bt_nav_iniciar_sesion.setOnClickListener { view ->
                    val activityIntent = Intent(this, Login::class.java)
                    startActivity(activityIntent)
                }
            }
        }
    }

    private fun removeVisibilityNavLogin() {
        var itemMenuLinkIniciarSesion : View = findViewById(R.id.bt_nav_iniciar_sesion)
        itemMenuLinkIniciarSesion.setVisibility(View.GONE)

        var itemMenuLinkRegistrarse : View = findViewById(R.id.tv_nav_registrarse)
        itemMenuLinkRegistrarse.setVisibility(View.GONE)
    }


    fun navBtnFavoritos(item: android.view.MenuItem) {
        val activity: Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(activity)
    }

    fun navBtnCanchas(item: android.view.MenuItem) {
        val activity: Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(activity)
    }

    fun navBtnCreateTournament(item: android.view.MenuItem) {

        val activity: Intent = Intent(applicationContext, create_tournament::class.java)
        startActivity(activity)
        finish()

    }

    fun logOut(item: android.view.MenuItem) {
        sessionManager.clearAll()
        val activity: Intent = Intent(applicationContext, AppLauncher::class.java)
        startActivity(activity)
        finish()
    }
}