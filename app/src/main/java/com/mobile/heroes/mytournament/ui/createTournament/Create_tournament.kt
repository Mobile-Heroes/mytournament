package com.mobile.heroes.mytournament.ui.createTournament

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.O)
class create_tournament : AppCompatActivity() {

    //Instance of inputs
    private lateinit var tietTournamentName: TextInputEditText
    private lateinit var btnSelectStartDate: Button
    private lateinit var btnSelectEndDate: Button
    private lateinit var tietTournamentTeams: TextInputEditText
    private lateinit var tietTournamentMatches: TextInputEditText

    //Instance of dropdown
    private lateinit var actvTournamentFormat: AutoCompleteTextView
    private lateinit var formatTournament: Array<String>
    private lateinit var itemAdapter: ArrayAdapter<String>

    //Instances of buttons
    private lateinit var btnCancel: Button
    private lateinit var btnNext: Button

    //Tmp variables
    private var description: String = ""
    private var groupQuantity: Int = 0
    private var matchesQuantity: Int = 0
    private var strategy: String = ""
    private var startDate: LocalDateTime = LocalDateTime.now()
    private var endDate: LocalDateTime = LocalDateTime.now()
    private var todayDate: LocalDateTime = LocalDateTime.now()

    //Date Picker development
    private val datePickerStartDate =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()

    private val datePickerEndDate =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tournament)

        //Instance of inputs
        tietTournamentName = findViewById(R.id.tietTournamentName)
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate)
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate)
        tietTournamentTeams = findViewById(R.id.tietTournamentTeams)
        tietTournamentMatches = findViewById(R.id.tietTournamentMatches)

        //Instances of buttons
        btnCancel = findViewById(R.id.btnCancel)
        btnNext = findViewById(R.id.btnNext)

        this.btnSelectStartDate.setOnClickListener {
            datePickerStartDate.show(supportFragmentManager, "tag")
        }

        this.btnSelectEndDate.setOnClickListener {
            datePickerEndDate.show(supportFragmentManager, "tag")
        }

        //Dropdown development
        actvTournamentFormat = findViewById(R.id.actvTournamentFormat)
        actvTournamentFormat.inputType = 0
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
            strategy = parent.getItemAtPosition(position).toString()
        }

        btnNext.setOnClickListener {
            checkDescription()
        }
    }

    private fun checkDescription() {
        description = tietTournamentName.text.toString()
        if (description.isEmpty())
            Toast.makeText(
                applicationContext,
                "Por favor digite una descripción o nombre para el torneo",
                Toast.LENGTH_SHORT
            ).show()
        else
            checkDates(datePickerStartDate, datePickerEndDate)
    }

    private fun checkQuantites() {
        try {
            groupQuantity = Integer. parseInt(tietTournamentTeams.text.toString())
            matchesQuantity  = Integer. parseInt(tietTournamentMatches.text.toString())
        }catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Por favor seleccione un número valido",
                Toast.LENGTH_SHORT
            ).show()
        }

        if(groupQuantity < 0 || matchesQuantity < 0)
            Toast.makeText(
                applicationContext,
                "Por favor seleccione un número mayor a 0",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun checkDropDownOption(strategy: String) {
        if (strategy.isEmpty())
            Toast.makeText(
                applicationContext,
                "Por favor seleccione una opción de formato para el torneo",
                Toast.LENGTH_SHORT
            ).show()
        else
            checkQuantites()
    }

    private fun checkDates(
        datePickerStartDate: MaterialDatePicker<Long>,
        datePickerEndDate: MaterialDatePicker<Long>
    ) {
        startDate =
            LocalDateTime.ofInstant(
                datePickerStartDate.selection?.let
                { it1 -> Instant.ofEpochMilli(it1) }
                , ZoneId.systemDefault())

        endDate =
            LocalDateTime.ofInstant(
                datePickerEndDate.selection?.let
                { it1 -> Instant.ofEpochMilli(it1) }
                , ZoneId.systemDefault())

        if(startDate < todayDate)
            Toast.makeText(
                applicationContext,
                "Por favor digite una fecha de inicio que sea igual o mayor a la de hoy",
                Toast.LENGTH_SHORT
            ).show()

        if(endDate < todayDate)
            Toast.makeText(
                applicationContext,
                "Por favor digite una fecha de fin superior a la fecha de hoy",
                Toast.LENGTH_SHORT
            ).show()
        else
            checkDropDownOption(strategy)
    }
}