package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tournament.*

class Tournament : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournament)

        var nextMatches = mutableListOf(
        NextMatches("30 de noviembre de 2021","Ricardo Saprissa", "0-:-0","Saprissa","Alajuela"),
        NextMatches("30 de noviembre de 2021","Morera Soto", "0-:-0","Alajuela","Saprissa"),
        NextMatches("30 de noviembre de 2021","Fello Mesa", "0-:-0","Cartago","Heredia"),
        NextMatches("30 de noviembre de 2021","Rosabal Cordero", "0-:-0","Heredia","Cartago"),
        NextMatches("30 de noviembre de 2021","Ricardo Saprissa", "0-:-0","Saprissa","Santos")
        )
        val adapter =NextMatchesAdapter(nextMatches)
        rvTournament.adapter=adapter
        rvTournament.layoutManager= LinearLayoutManager(this)
    }
}