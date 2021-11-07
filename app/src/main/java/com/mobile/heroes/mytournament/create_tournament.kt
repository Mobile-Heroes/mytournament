package com.mobile.heroes.mytournament

import android.icu.number.NumberFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class create_tournament : AppCompatActivity() {

    lateinit var tilTournamentType: TextInputLayout
    lateinit var actvSelectStrategy: AutoCompleteTextView
    lateinit var strategyTournament: Array<String>
    lateinit var itemAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tournament)

        tilTournamentType = findViewById(R.id.tilTournamentType)
        actvSelectStrategy = findViewById(R.id.actvSelectStrategy)

        strategyTournament = arrayOf<String>("item1", "item2", "item3")
        itemAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_tournament_types,
            strategyTournament
        )
        actvSelectStrategy.setAdapter(itemAdapter)
        actvSelectStrategy.setOnItemClickListener { p0, p1, p2, p3 ->
            val selectedItem: String? = p0?.toString()
        }
    }
}