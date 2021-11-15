package com.mobile.heroes.mytournament.ui.createTournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.R

class create_tournament : AppCompatActivity() {

    lateinit var tietTournamentName: TextInputEditText
    lateinit var tietGroupQuantity: TextInputEditText
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
        tietGroupQuantity = findViewById(R.id.tietGroupQuantity)
        actvSelectStrategy = findViewById(R.id.actvSelectStrategy)
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
        actvSelectStrategy.setOnItemClickListener { p0, p1, p2, p3 ->
            val selectedItem: String? = p0?.toString()
        }

        btnAccept.setOnClickListener{
            try {
                groupQuantity = tietGroupQuantity.text.toString().toInt()
            } catch (nfe: NumberFormatException) {
                Toast.makeText(
                    applicationContext,
                    "Por favor escribir un número valido, sin caracteres especiales",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}