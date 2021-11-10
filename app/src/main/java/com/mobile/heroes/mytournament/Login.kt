package com.mobile.heroes.mytournament

import SessionManager
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

class Login : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    lateinit var txtLogin: TextInputEditText
    lateinit var txtPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //DECLARACION DE OBJETOS LAYOUT
        txtLogin = findViewById(R.id.txtLogin)
        txtPassword = findViewById(R.id.txtPassword)

        //MANEJO DE SESSION
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        txtviewNewAccount.setOnClickListener{view ->val activityIntent= Intent(this, SignUpOrganizer::class.java)
            startActivity(activityIntent)}

        btnIniciarSesion.setOnClickListener(){
            starNewSession(txtLogin.text.toString(), txtPassword.text.toString() )

        }
    }

    fun starNewSession(username: String, password: String){
        println("Login in: " + username + " Password: " + password )

        apiClient.getApiService().login(LoginRequest(username = username, password = password, rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    HandleLoginError()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                        val activityIntent : Intent = Intent(applicationContext, soccer_scoreboard::class.java)
                        startActivity(activityIntent)

                    } else {
                        HandleLoginError()
                    }
                }
            })
    }

    fun HandleLoginError() {
        Toast.makeText(applicationContext, "Error al iniciar sesion, porfavor verifique credenciales", Toast.LENGTH_SHORT).show()
        txtLogin.setText("")
        txtPassword.setText("")
    }
}