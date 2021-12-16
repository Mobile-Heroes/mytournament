package com.mobile.heroes.mytournament

import SessionManager
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import com.mobile.heroes.mytournament.signup.CompleteRegistration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppLauncher : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var validator = false
    var txtLogin: TextInputEditText? = null
    lateinit var txtPassword: TextInputEditText
    private var dialog: Dialog? = null //obj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_launcher)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        if (sessionManager.development) {
            starNewSession("user", "user")
        }
    }

    fun starNewSession(username: String, password: String) {
        println("Login in: " + username + " Password: " + password)
        LoadingScreen.displayLoadingWithText(this, "", false)

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
            txtLogin?.setText("")
            txtPassword.setText("")
        }
    }

    private fun checkUserStats(){
        val account=sessionManager.fetchAccount()
        val barrear: String = sessionManager.fetchAuthToken()!!;
        apiClient.getApiService().getUserStatsByUserId(token = "Bearer ${sessionManager.fetchAuthToken()}",id= account!!.id).enqueue(object: Callback<List<UserStatsResponse>>
        {
            override fun onResponse(call: Call<List<UserStatsResponse>>, response: Response<List<UserStatsResponse>>) {
                if (response.body()!!.size >0){
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