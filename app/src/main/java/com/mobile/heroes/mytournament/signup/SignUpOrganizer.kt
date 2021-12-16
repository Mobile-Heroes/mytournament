package com.mobile.heroes.mytournament.signup

import SessionManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.mobile.heroes.mytournament.Login
import com.mobile.heroes.mytournament.MainActivity
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountRequest
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.NewUserResource.NewUserRequest
import com.mobile.heroes.mytournament.networking.services.NewUserResource.NewUserResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserRequest
import kotlinx.android.synthetic.main.activity_sign_up_organizer.*
import kotlinx.android.synthetic.main.activity_sign_up_organizer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpOrganizer : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var emailAddress: TextInputEditText
    private lateinit var user: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordConfirmation: TextInputEditText
    private var userlogin:String = ""
    private var userEmail:String = ""
    private var usernFirstName:String = ""
    private var userPassword:String = ""
    private var userPasswordConfirmation:String = ""
    private var formValidator:Boolean?=false
    private var passwordValidator:Boolean?=false
    private var organizer:Boolean?=false
    private var team:Boolean?=false
    private var rolValidator:Boolean?=false
    private lateinit var authorities: List<String>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_organizer)
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        initInfo()

        txtUser.doOnTextChanged{text,start,before,count ->
            if(text!!.isEmpty()){
                edtxrUser.error="Usuario no puede ser vacío"
            }
            else if(text!!.isNotEmpty()){
                edtxrUser.error=null
            }
        }

        txtEmail.doOnTextChanged{text,start,before,count ->
            if(text!!.isEmpty()){
                edtxtEmail.error="Correo no puede ser vacío"
            }
            else if(text!!.isNotEmpty()){
                edtxtEmail.error=null
            }
        }

        txtName.doOnTextChanged{text,start,before,count ->
            if(text!!.isEmpty()){
                edtxtName.error="Correo no puede ser vacío"
            }
            else if(text!!.isNotEmpty()){
                edtxtName.error=null
            }
        }
        txtPasswordConfirmation.doOnTextChanged{text,start,before,count ->
            var d=txtPassword.text.toString()
            if(text.toString()!=d){
                edtxRePassword.error="Las contraseñas no coinciden"
            }
            else if (text.toString()==d){
                edtxRePassword.error=null
            }
        }
        txtviewLogin.setOnClickListener{
                view ->val activityIntent= Intent(this, Login::class.java)
            startActivity(activityIntent)
        }

        btnCancelar.setOnClickListener{view ->val activityIntent= Intent(this, Login::class.java)
            startActivity(activityIntent)
        }

        btnRegister.setOnClickListener {
            checkInfo()
            if (formValidator == true || passwordValidator == true || rolValidator==true) {
            HandleLError()
            } else {

                val bodyResponse = NewUserRequest(
                    userEmail,
                    usernFirstName,
                    userlogin,
                    authorities,
                    true,
                    userPassword,
                    "es"
                )

                if (isNetworkConnected()){
                    sendUser(bodyResponse)
                }

                else {
                    Toast.makeText(
                        applicationContext,
                        "No hay conexión de internet en este momento, favor revisar su conexión o intente más tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }

    private fun initInfo (){
        emailAddress= findViewById(R.id.txtEmail)
        user= findViewById(R.id.txtUser)
        name= findViewById(R.id.txtName)
        password= findViewById(R.id.txtPassword)
        passwordConfirmation= findViewById(R.id.txtPasswordConfirmation)

    }

    private fun checkInfo (){
        formValidator=false
        passwordValidator=false
        rolValidator=false
        userEmail= emailAddress.text.toString()
        userlogin=  user.text.toString()
        usernFirstName =name.text.toString()
        userPassword = password.text.toString()
        userPasswordConfirmation = passwordConfirmation.text.toString()
        organizer= radioOrganizador.isChecked
        team= radioEquipo.isChecked
        if (userEmail.equals(""))
            formValidator=true
        if (userlogin.equals(""))
            formValidator=true
        if (usernFirstName.equals(""))
            formValidator=true
        if (userPassword.equals(""))
            formValidator=true
        if (userPasswordConfirmation.equals(""))
            formValidator=true
        if (userPassword!=userPasswordConfirmation)
            passwordValidator=true
        if(team==false &&organizer==false)
            rolValidator=true
        if(radioOrganizador.isChecked)
            authorities= mutableListOf("ROLE_TOURNAMENT")
        else if(radioEquipo.isChecked)
            authorities=mutableListOf("ROLE_USER")
    }

    private fun sendUser(bodyResponse: NewUserRequest){

        apiClient.getApiService().postNewUser(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                )
                {
                    println(response.code())

                    if(response.code() == 201){
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Por favor revise su correo", Toast.LENGTH_SHORT).show()
                            Thread.sleep(2000)
                            val easyIntent: Intent = Intent(applicationContext, Login::class.java)
                            startActivity(easyIntent)
                            finish()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Ocurrió un problema en el servidor", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })


    }

    fun HandleLError() {
        runOnUiThread(){
            Toast.makeText(applicationContext, "Verifique la información ingresada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}