package com.mobile.heroes.mytournament

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.mobile.heroes.mytournament.networking.pojos.MatchResponse
import com.mobile.heroes.mytournament.networking.services.APIServiceMatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class soccer_scoreboard : AppCompatActivity() {


    lateinit var btnCancel: Button
    lateinit var btnAccept: Button
    var token: String = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzNjQwNzgwNX0.J8z_lch5O9P9fv3DYBaUWhu5uuEyJ3siUKpAWGRYUcWhRzQo8soPZfXk4xd9CSV5s1LdonAqtrvUvst0JdXDbg"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soccer_scoreboard)

        /**
         * Esta sección meramente es el manejor de botones de la aplicación (definición e inicialización), a este punto no están relevante,
         * es lo que nos permite sumar o restar goles y enviar data a la DB
         */

        var pointH: Int = 0
        var pointV: Int = 0

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
            val bodyResponse: MatchResponse = MatchResponse(
                pointH,
                pointV,
                "finalizado",
                LocalDateTime.now(),
                1,
                1
            )
            if(isNetworkConnected())
                sendScore(bodyResponse)
            else
                println("Pos no estamos conectados")
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

    private fun sendScore(bodyResponse: MatchResponse) {
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

                } else {
                    showError()
                }
            }

        }
    }

    /**
     * Este método me devuelve una instancia de tipo Retrofit con mí base url
     */

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mytournament-beta.herokuapp.com:443/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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