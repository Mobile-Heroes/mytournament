package com.mobile.heroes.mytournament

import SessionManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResource
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

class soccer_scoreboard : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

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

    var token: String = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzNjQyMjIwNn0.WhtwjdIKXyMSGbsOMhyxwnbHLlC7l7YUL_R_gwSlbQ87JeDQdR-3iB4ZfV7OcrkuiLdrCxsXS5LBiOvGTdKQCA"



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soccer_scoreboard)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)


        apiClient.getApiService().login(LoginRequest(username = "admin", password = "admin", rememberMe = false))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("error")
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()

                    if (loginResponse?.id_token != null) {
                        sessionManager.saveAuthToken(loginResponse.id_token)
                    } else {
                println("Error")
                    }
                }
            })

        /**
         * Esta sección meramente es el manejor de botones de la aplicación (definición e inicialización), a este punto no están relevante,
         * es lo que nos permite sumar o restar goles y enviar data a la DB
         */

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

        /**
         * Este botón de aquí abajo es el de aceptar, cuando vos los presiones vas a triggerear los siguiente:
         * Primero va a construir un objeto POJO de tipo MatchResponse que yo construí, ahí le das a la referencia del mismo
         * Segundo, el correrá una función para revisar si hay internet
         */

        btnAccept.setOnClickListener {
            val bodyResponse: MatchRequest = MatchRequest(
                "2021-11-08T05:38:09.305Z",
                pointH,
                pointV,
                FieldResource(1),
                TournamentResource(1),
                "Canceled"
            )
            if(isNetworkConnected())
                sendScore(bodyResponse)
            else
                println("No internet")
        }
    }

    /**
     * Esta función de aquí será nuestra "corrutina" que es un concepto que kotlin agregó con el fin
     * de aligerar el trabajo asíncrono. Entonces, lo que pase dentro del CoroutineScope será en un hilo
     * asíncrono de la aplicación. Lo que pase dentro del runOnUiThread será en el hilo primario de la aplicación.
     *
     * Sigamos...
     *
     * Lo primero que vamos a pedirle es que me devuelva una entidad de tipo Retrofit (que es con la que estoy trabajando
     * para las peticiones HTTP) usando el método de más abajo de getRetrofit().
     *
     * Segundo, una vez obtenido el objeto Retrofit, le digo con el método create() que construya una entidad de tipó
     * APIServiceMatch (es una interface construida por mí persona, la podemos visualizar como una especie de
     * controlador o handler de archivos) y llamamos al método sendDataMacht() que no es más que un método de la
     * interface que se encarga de hacer peticiones de tipo, se le debe pasar 3 datos:
     *
     * El token de autorización
     * El endpoint
     * El POJO (que en este caso generamos más arriba)
     *
     * Por ultimo...
     *
     * Tendremos una variable que se llama result, su objetivo es almacenar el cuerpo o body() de nuestra
     * petición y de ahí en más es lo que queda en nuestro hilo primario.
     */

    private fun sendScore(bodyResponse: MatchRequest) {

        var gson = Gson()
        var jsonString = gson.toJson(bodyResponse)
        println(jsonString)
            apiClient.getApiService().postMatch(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
            .enqueue(object : Callback<MatchResponce> {
                override fun onFailure(call: Call<MatchResponce>, t: Throwable) {

                    //TODO: ESTA VARA FUNCIONA LA VARA ES QUE PORQUE LOS POJOS DE TOURNAMENT Y FIELD NO TIENEN LOS CAMPOS NESESARIOS CORREJIT PARA EVITAR EL ERROR
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(call: Call<MatchResponce>, response: Response<MatchResponce>) {
                    // Handle function to display posts
                    println(call)
                    println(response)
                    runOnUiThread {
                        println("All good")
                        tvCounterH.setText("0")
                        tvCounterH.setText("0")
                    }
                }
            })


        /*
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit()
                .create(APIServiceMatch::class.java)
                .sendDataMacht(
                    token,
                    "matches",
                    bodyResponse
                )

            val result = call.body()

            println(call)

            runOnUiThread {
                if (call.isSuccessful) {
                    tvCounterH.setText("0")
                    tvCounterH.setText("0")
                } else {
                    showError()
                }
            }

        } */
    }



    /**
     * Este método checkea sí hay red
     */
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }
}