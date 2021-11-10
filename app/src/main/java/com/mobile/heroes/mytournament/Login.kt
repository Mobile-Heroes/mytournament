package com.mobile.heroes.mytournament

import SessionManager
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.signup.SignUpOrganizer
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window


class Login : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    lateinit var txtLogin: TextInputEditText
    lateinit var txtPassword: TextInputEditText
    private var dialog: Dialog? = null //obj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //METODO PARA DESAROLLO RAPIDO



        //DECLARACION DE OBJETOS LAYOUT
        txtLogin = findViewById(R.id.txtLogin)
        txtPassword = findViewById(R.id.txtPassword)

        //MANEJO DE SESSION
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        if(sessionManager.development){
            val activityIntent= Intent(this, soccer_scoreboard::class.java)
            startActivity(activityIntent)
        }

        txtviewNewAccount.setOnClickListener{view ->val activityIntent= Intent(this, SignUpOrganizer::class.java)
            startActivity(activityIntent)}

        btnIniciarSesion.setOnClickListener(){
            starNewSession(txtLogin.text.toString(), txtPassword.text.toString() )

        }
    }

    fun starNewSession(username: String, password: String){
        println("Login in: " + username + " Password: " + password )
        LoadingScreen.displayLoadingWithText(this,"Please wait...",false)

        apiClient.getApiService().login(LoginRequest(username = username, password = password, rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    LoadingScreen.hideLoading()
                    
                    HandleLoginError()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                        getAccount()
                        LoadingScreen.hideLoading()
                        runOnUiThread() {
                            val activityIntent: Intent =
                                Intent(applicationContext, soccer_scoreboard::class.java)
                            startActivity(activityIntent)
                        }

                    } else {
                        LoadingScreen.hideLoading()
                        HandleLoginError()
                    }
                }
            })
    }

    private fun getAccount() {
      /*  apiClient.getApiService().login(AccountRequest(username = username, password = password, rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    LoadingScreen.hideLoading()
                    HandleLoginError()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                        getAccount()
                        LoadingScreen.hideLoading()
                        runOnUiThread() {
                            val activityIntent: Intent =
                                Intent(applicationContext, soccer_scoreboard::class.java)
                            startActivity(activityIntent)
                        }

                    } else {
                        LoadingScreen.hideLoading()
                        HandleLoginError()
                    }
                }
            }) */
    }


    fun HandleLoginError() {
        runOnUiThread(){
            Toast.makeText(applicationContext, "Error al iniciar sesion, porfavor verifique credenciales", Toast.LENGTH_SHORT).show()
            txtLogin.setText("")
            txtPassword.setText("")
        }
    }
}