package com.mobile.heroes.mytournament.ui.createTournament

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mobile.heroes.mytournament.R
import java.time.*

@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.O)
class create_tournament : AppCompatActivity() {

    //Instance of inputs
    private lateinit var tietTournamentName: TextInputEditText
    private lateinit var tietTournamentDescription: TextInputEditText
    private lateinit var btnSelectStartDate: Button
    private lateinit var btnSelectEndDate: Button
    private lateinit var tietTournamentTeams: TextInputEditText
    private lateinit var tilTournamentName: TextInputLayout
    private lateinit var tilTournamentDescription: TextInputLayout
    private lateinit var tilTournamentTeams: TextInputLayout


    //Instance of dropdown
    private lateinit var actvTournamentFormat: AutoCompleteTextView
    private lateinit var formatTournament: Array<String>
    private lateinit var itemAdapter: ArrayAdapter<String>

    //Instances of buttons
    private lateinit var btnCancel: Button
    private lateinit var btnNext: Button

    //Tmp variables
    private var name: String = ""
    private var description: String = ""
    private var groupQuantity: Int = 0
    private var matchesQuantity: Int = 0
    private var strategy: String = ""
    private var startDate: ZonedDateTime = ZonedDateTime.now()
    private var endDate: ZonedDateTime = ZonedDateTime.now()
    private var todayDate: ZonedDateTime = ZonedDateTime.now()

    //Date Picker development
    private val datePickerStartDate =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione fecha de inicio")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

    private val datePickerEndDate =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione fecha de finalización")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tournament)

        //Instance of inputs
        tietTournamentName = findViewById(R.id.tietTournamentName)
        tietTournamentDescription = findViewById(R.id.tietTournamentDescription)
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate)
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate)
        tietTournamentTeams = findViewById(R.id.tietTournamentTeams)
        tilTournamentName = findViewById(R.id.tilTournamentName)
        tilTournamentDescription = findViewById(R.id.tilTournamentDescription)
        tilTournamentTeams = findViewById(R.id.tilTournamentTeams)

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
            "Tabla general"
        )
        itemAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_tournament_types,
            formatTournament
        )
        actvTournamentFormat.setAdapter(itemAdapter)
        actvTournamentFormat.showSoftInputOnFocus = false
        actvTournamentFormat.setOnItemClickListener { parent, view, position, id ->
            strategy = parent.getItemAtPosition(position).toString()
            when (strategy) {
                "Tabla general" -> {
                    strategy = "GeneralTable"
                }
                else -> {
                    Toast.makeText(
                        applicationContext,
                        "Por favor seleccione un formato de torneo valido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        datePickerStartDate.addOnPositiveButtonClickListener { selection: Long ->
            startDate =
                ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(selection),
                    ZoneId.systemDefault()
                ).plusHours(6)
            btnSelectStartDate.setText("Fecha Inicio: ${startDate.dayOfMonth}/${startDate.monthValue}/${startDate.year}")

        }

        datePickerEndDate.addOnPositiveButtonClickListener { selection: Long ->
            endDate =
                ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(selection),
                    ZoneId.systemDefault()
                ).plusHours(6)
            btnSelectEndDate.setText("Fecha Fin: ${endDate.dayOfMonth}/${endDate.monthValue}/${endDate.year}")
        }

        btnNext.setOnClickListener {
            checkName()
        }
    }

    private fun checkName() {
        name = tietTournamentName.text.toString()
        if (name.isEmpty())
           tilTournamentName.error = "Este campo debe ir lleno"
        else
            checkDescription()
    }

    private fun checkDescription() {
        description = tietTournamentDescription.text.toString()
        if (description.isEmpty())
            tilTournamentDescription.error = "Este campo debe ir lleno"
        else
            checkDates()
    }

    private fun checkQuantites() {
        groupQuantity = Integer.parseInt(tietTournamentTeams.text.toString())
        if (groupQuantity == 0) {
            tilTournamentTeams.error = "Este campo debe ir lleno"
        } else {
            matchesQuantity = (groupQuantity * 2) - 2
            passData()
        }
    }

    private fun passData() {
        val intent = Intent(applicationContext, upload_image_tournament::class.java)
        intent.putExtra("name", name)
        intent.putExtra("description", description)
        intent.putExtra("groupQuantity", groupQuantity)
        intent.putExtra("matchesQuantity", matchesQuantity)
        intent.putExtra("strategy", strategy)
        intent.putExtra("startDate", startDate.toString())
        intent.putExtra("endDate", endDate.toString())
        startActivity(intent)
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

    private fun checkDates() {

        lateinit var period: Period
        var totalDays = 1
        var totalMonths = 1

        if (startDate == endDate) {
            Toast.makeText(
                applicationContext,
                "Las fechas de inicio y fin no pueden ser iguales",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (startDate < todayDate)
                Toast.makeText(
                    applicationContext,
                    "Por favor digite una fecha de inicio que sea mayor a la de hoy",
                    Toast.LENGTH_SHORT
                ).show()
            else
                if (endDate <= todayDate)
                    Toast.makeText(
                        applicationContext,
                        "Por favor digite una fecha de fin mayor a la fecha de hoy",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    period = Period.between(startDate.toLocalDate(), endDate.toLocalDate())
                    totalDays = period.days
                    totalMonths = period.months

                    if (totalDays >= 7 || totalMonths >= 1) {
                        checkDropDownOption(strategy)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "La duración del torneo debe ser de por lo menos 7 días habiles",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}