package com.mobile.heroes.mytournament

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
import kotlinx.android.synthetic.main.fragment_feed_destination.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var feedTitleList = mutableListOf<String>()
    private var feedDescriptionList = mutableListOf<String>()
    private var feedUserList = mutableListOf<String>()
    private var feedImageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        postToFeedList()

        rv_feed_card.layoutManager = LinearLayoutManager(this)
        rv_feed_card.adapter = FeedAdapter(feedTitleList, feedDescriptionList, feedUserList, feedImageList)
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
}