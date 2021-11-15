package com.mobile.heroes.mytournament.ui.createTournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.R

class create_tournament : AppCompatActivity() {

    lateinit var tietTournamentName: TextInputEditText
    lateinit var actvSelectStrategy: AutoCompleteTextView
    lateinit var strategyTournament: Array<String>
    lateinit var itemAdapter: ArrayAdapter<String>
    lateinit var btnCancel: Button
    lateinit var btnAccept: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tournament)

        var groupQuantity: Int = 0
        var tournamentName: String = ""
        var selectStrategy: String = ""

        tietTournamentName = findViewById(R.id.tietTournamentName)
        actvSelectStrategy = findViewById(R.id.actvTournamentFormat)
        btnCancel = findViewById(R.id.btnCancel)
        btnAccept = findViewById(R.id.btnAccept)

        //Se genera el dropdown de formatos de torneos
        strategyTournament = arrayOf<String>(
            "Tabla General",
            "Grupos",
            "Eliminación"
        )
        itemAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_tournament_types,
            strategyTournament
        )
        actvSelectStrategy.setAdapter(itemAdapter)
        actvSelectStrategy.setOnItemClickListener { parent, view, position, id ->
            println(parent.getItemAtPosition(position))
        }

        btnAccept.setOnClickListener{}
    }
}