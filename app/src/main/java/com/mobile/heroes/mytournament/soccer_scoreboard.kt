package com.mobile.heroes.mytournament

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class soccer_scoreboard : AppCompatActivity() {

    lateinit var tvDateTime: TextView
    lateinit var tvUbication: TextView
    lateinit var etHomeTeam: EditText
    lateinit var etVisitorTeam: EditText
    lateinit var ivHomeTeam: ImageView
    lateinit var ivVisitorTeam: ImageView
    lateinit var tvHomeTeam: TextView
    lateinit var tvVisitorTeam: TextView
    lateinit var ivIconLessPointH: ImageView
    lateinit var ivIconPlusPointH: ImageView
    lateinit var ivIconLessPointV: ImageView
    lateinit var ivIconPlusPointV: ImageView
    lateinit var tvCounterH: TextView
    lateinit var tvCounterV: TextView
    lateinit var btnCancel: Button
    lateinit var btnAccept: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soccer_scoreboard)

        var pointH: Int = 0
        var pointV: Int = 0

        tvDateTime = findViewById(R.id.tvDateTime)
        tvUbication = findViewById(R.id.tvUbication)
        etHomeTeam = findViewById(R.id.etHomeTeam)
        etVisitorTeam = findViewById(R.id.etVisitorTeam)
        ivHomeTeam = findViewById(R.id.ivHomeTeam)
        ivVisitorTeam = findViewById(R.id.ivVisitorTeam)
        tvHomeTeam = findViewById(R.id.tvHomeTeam)
        tvVisitorTeam = findViewById(R.id.tvVisitorTeam)
        ivIconLessPointH = findViewById(R.id.ivIconLessPointH)
        ivIconPlusPointH = findViewById(R.id.ivIconPlusPointH)
        ivIconLessPointV = findViewById(R.id.ivIconLessPointV)
        ivIconPlusPointV = findViewById(R.id.ivIconPlusPointV)
        tvCounterH = findViewById(R.id.tvCounterH)
        tvCounterV = findViewById(R.id.tvCounterV)
        btnCancel = findViewById(R.id.btnCancel)
        btnAccept = findViewById(R.id.btnAccept)


        etHomeTeam.isEnabled = false
        etVisitorTeam.isEnabled = false
        tvCounterH.setText("0")
        tvCounterV.setText("0")

        ivIconLessPointH.setOnClickListener {
            if (pointH == 0) {
                pointH = 0
            } else {
                pointH--
            }
            tvCounterH.setText(pointH.toString())
        }

        ivIconLessPointV.setOnClickListener {
            if (pointV == 0) {
                pointV = 0
            } else {
                pointV--
            }
            tvCounterV.setText(pointV.toString())
        }

        ivIconPlusPointH.setOnClickListener {
            pointH++
            tvCounterH.setText(pointH.toString())
        }

        ivIconPlusPointV.setOnClickListener {
            pointV++
            tvCounterV.setText(pointV.toString())
        }



        btnCancel.setOnClickListener {
            View.OnClickListener {
                TODO("RETURN TO THE MAIN MENU WITHOUT SAVING THE SCORE")
            }
        }


        btnAccept.setOnClickListener {
            View.OnClickListener {
                TODO("RETURN TO THE MAIN MENU AND SENDING THE INFO TO THE DB")
            }
        }


    }
}