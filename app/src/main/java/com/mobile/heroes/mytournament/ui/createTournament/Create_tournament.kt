package com.mobile.heroes.mytournament.ui.createTournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.R

class create_tournament : AppCompatActivity() {

    //Instance of inputs
    lateinit var tietTournamentName: TextInputEditText
    lateinit var tietTournamentStartDate: TextInputEditText
    lateinit var tietTournamentEndDate: TextInputEditText
    lateinit var tietTournamentTeams: TextInputEditText
    lateinit var tietTournamentMatches: TextInputEditText

    //Instance of dropdown
    lateinit var actvTournamentFormat: AutoCompleteTextView
    lateinit var formatTournament: Array<String>
    lateinit var itemAdapter: ArrayAdapter<String>

    //Instances of buttons
    lateinit var btnCancel: Button
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tournament)

        //Tmp variables
        var groupQuantity: Int = 0
        var tournamentName: String = ""
        var selectStrategy: String = ""

        //Instance of inputs
        tietTournamentName = findViewById(R.id.tietTournamentName)
        tietTournamentStartDate = findViewById(R.id.tietTournamentStartDate)
        tietTournamentEndDate = findViewById(R.id.tietTournamentEndDate)
        tietTournamentTeams = findViewById(R.id.tietTournamentTeams)
        tietTournamentMatches = findViewById(R.id.tietTournamentMatches)
        
        //Instances of buttons
        btnCancel = findViewById(R.id.btnCancel)
        btnNext = findViewById(R.id.btnNext)

        //Date Picker development


        //Dropdown development
        actvTournamentFormat = findViewById(R.id.actvTournamentFormat)
        formatTournament = arrayOf<String>(
            "Tabla General",
            "Grupos",
            "Eliminación"
        )
        itemAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_tournament_types,
            formatTournament
        )
        actvTournamentFormat.setAdapter(itemAdapter)
        actvTournamentFormat.setOnItemClickListener { parent, view, position, id ->
            println(parent.getItemAtPosition(position))
        }

        btnNext.setOnClickListener{}
    }
}