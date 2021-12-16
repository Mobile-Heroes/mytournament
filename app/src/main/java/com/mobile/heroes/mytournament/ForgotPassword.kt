package com.mobile.heroes.mytournament

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobile.heroes.mytournament.networking.ApiClient
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {

    private lateinit var apiClient: ApiClient
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        apiClient = ApiClient()
        email = txtForgetPass.text.toString()

        btnSendData.setOnClickListener {
            LoadingScreen.displayLoadingWithText(this, "Por favor espere...", false)
            apiClient
                .getApiService()
                .resetPassword(
                    email.toString()
                ).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        println("Estoy bien")
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Por favor revise su correo electrónico asociado para que cambie su contraseña",
                                Toast.LENGTH_SHORT
                            ).show()
                            Thread.sleep(2000)
                            LoadingScreen.hideLoading()
                            val easyIntent: Intent = Intent(applicationContext, Login::class.java)
                            startActivity(easyIntent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        runOnUiThread {
                            println("Estoy mal")
                            Toast.makeText(
                                applicationContext,
                                "Ha ocurrido un error en la solicitud",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        }
    }
}