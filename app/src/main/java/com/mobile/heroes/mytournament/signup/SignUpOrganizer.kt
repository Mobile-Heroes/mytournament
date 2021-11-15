package com.mobile.heroes.mytournament.signup

import SessionManager
import android.content.Intent
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
    private var userlogin:String? = null
    private var userEmail:String? = null
    private var usernFirstName:String? = null
    private var userPassword:String? = null
    private var userPasswordConfirmation:String? = null
    private var formValidator:Boolean?=false
    private var passwordValidator:Boolean?=false


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
                btnRegister.isClickable=false
            }
            else if (text.toString()==d){
                edtxRePassword.error=null
                btnRegister.isClickable=true

            }

        }

        txtviewLogin.setOnClickListener{view ->val activityIntent= Intent(this, Login::class.java)
            startActivity(activityIntent)
        }

        btnCancelar.setOnClickListener{view ->val activityIntent= Intent(this, CompleteRegistration::class.java)
            startActivity(activityIntent)
        }


        btnRegister.setOnClickListener {
            checkInfo()
            if (formValidator == true || passwordValidator == true) {
            HandleLError()

            } else {

                    Toast.makeText(applicationContext, "Funca",Toast.LENGTH_SHORT).show()
//                try {
//
//                } catch {
//
//                }


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
        userEmail= emailAddress.text.toString()
        userlogin=  user.text.toString()
        usernFirstName =name.text.toString()
        userPassword = password.text.toString()
        userPasswordConfirmation = passwordConfirmation.text.toString()

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

    }

    private fun sendUser(bodyResponse: AccountRequest){

        val gson = Gson()
        val jsonString = gson.toJson(bodyResponse)


        apiClient.getApiService().postAccount(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
            .enqueue(object : Callback<AccountResponce> {
                override fun onFailure(call: Call<AccountResponce>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(
                    call: Call<AccountResponce>,
                    response: Response<AccountResponce>
                ) {
                    if(response.code() == 201){
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Dato enviado exitosamente !", Toast.LENGTH_SHORT).show()
//                            tieScoreH.refreshDrawableState()
//                            tieScoreV.refreshDrawableState()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Why you are so stupid ?!", Toast.LENGTH_SHORT).show()
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
}