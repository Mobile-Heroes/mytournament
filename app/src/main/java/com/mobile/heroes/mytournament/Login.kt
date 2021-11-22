package com.mobile.heroes.mytournament

import SessionManager
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.signup.SignUpOrganizer
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.signup.CompleteRegistration
import kotlinx.android.synthetic.main.activity_sign_up_organizer.*


class Login : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var validator = false
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


        txtLogin.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                edtxtEmailLogin.error = "Correo no puede ser vacío"
            } else if (text!!.isNotEmpty()) {
                edtxtEmailLogin.error = null
            }
        }

        txtPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                edtxtPasswordLogin.error = "Clave no puede ser vacía"
            }
            else if (text!!.isNotEmpty()) {
                edtxtPasswordLogin.error = null
            }
        }

        //MANEJO DE SESSION
        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        /*if (sessionManager.development) {
            starNewSession("admin", "admin")
        }*/


        txtviewNewAccount.setOnClickListener { view ->
            val activityIntent = Intent(this, SignUpOrganizer::class.java)
            startActivity(activityIntent)
        }

        btnIniciarSesion.setOnClickListener() {
            starNewSession(txtLogin.text.toString(), txtPassword.text.toString())
        }
    }

    fun starNewSession(username: String, password: String) {
        println("Login in: " + username + " Password: " + password)
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

        apiClient.getApiService()
            .login(LoginRequest(username = username, password = password, rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    LoadingScreen.hideLoading()

                    HandleLoginError()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                        getAccount()
                    } else {
                        LoadingScreen.hideLoading()
                        HandleLoginError()
                    }
                }
            })
    }

    private fun getAccount() {
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getAccount(token = "Bearer ${barrear}")
            .enqueue(object : Callback<AccountResponce> {
                override fun onFailure(call: Call<AccountResponce>, t: Throwable) {
                    HandleLoginError()
                }

                override fun onResponse(
                    call: Call<AccountResponce>,
                    response: Response<AccountResponce>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val accunt: AccountResponce = response.body()!!
                        sessionManager.saveAccount(accunt)
                        LoadingScreen.hideLoading()
                        checkUserStats()
                    }
                }
            })
    }


    fun HandleLoginError() {
        runOnUiThread() {
            Toast.makeText(
                applicationContext,
                "Error al iniciar sesion, porfavor verifique credenciales",
                Toast.LENGTH_SHORT
            ).show()
            txtLogin.setText("")
            txtPassword.setText("")
        }
    }

    private fun checkUserStats(){
        val account=sessionManager.fetchAccount()
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getOneUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= account!!.id).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0){
                    println(response.body())
                    sessionManager.saveUserStats(response.body()!!.get(0))
                    val activity: Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(activity)
                    finish()
                }

                else{
                    Toast.makeText(applicationContext, "Debe completar el registro", Toast.LENGTH_SHORT).show()
                    val activity: Intent = Intent(applicationContext, CompleteRegistration::class.java)
                    Thread.sleep(1000)
                    startActivity(activity)
                    finish()
                }
            }
            override fun onFailure(call: Call<List<UserStatsResponse>>, t: Throwable) {
                println(call)
                println(t)
                println("error")
            }
        }
        )
    }



}