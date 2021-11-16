package com.mobile.heroes.mytournament.ui.createTournament

import SessionManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentRequest
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsRequest
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import kotlinx.android.synthetic.main.activity_upload_image_tournament.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class upload_image_tournament : AppCompatActivity() {

    //Connection to back end
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    //Tmp variables
    private var description: String? = ""
    private var groupQuantity: Int? = 0
    private var matchesQuantity: Int? = 0
    private var strategy: String? = ""
    private var startDate: String? = ""
    private var endDate: String? = ""

    //Casting variables
    private var startDateCast: LocalDateTime = LocalDateTime.now()
    private var endDateCast: LocalDateTime = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image_tournament)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        description = getIntent().getStringExtra("description")
        groupQuantity = getIntent().getIntExtra("groupQuantity", 0)
        matchesQuantity = getIntent().getIntExtra("matchesQuantity", 0)
        strategy = getIntent().getStringExtra("strategy")
        startDate = getIntent().getStringExtra("startDate")
        endDate = getIntent().getStringExtra("endDate")

        btnSelect.setOnClickListener {
            selectImage()
        }

        btnSubmitPhoto.setOnClickListener {
            var logo = (ivLogo.drawable as BitmapDrawable).bitmap
            var logoApp: String? = logo.toBase64String()

            val userResponse = UserResponse(
                sessionManager.fetchAccount()!!.id,
                sessionManager.fetchAccount()!!.login
            )

            val bodyResponse = TournamentRequest(
                description.toString(),
                endDate.toString(),
                strategy.toString(),
                logoApp.toString(),
                userResponse,
                "image/png",
                matchesQuantity!!.toInt(),
                groupQuantity!!.toInt(),
                startDate.toString(),
                "Activated"
            )

            if (isNetworkConnected()) {
                sendNewTournament(bodyResponse)
            } else {
                Toast.makeText(
                    applicationContext,
                    "No hay conexión de internet en este momento, favor revisar su conexión o intente más tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ivLogo.setImageURI(data?.data) // handle chosen image

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun sendNewTournament(bodyResponse: TournamentRequest) {

        apiClient.getApiService()
            .postTournament(token = "Bearer ${sessionManager.fetchAuthToken()}", bodyResponse)
            .enqueue(object : Callback<TournamentResponse> {
                override fun onFailure(call: Call<TournamentResponse>, t: Throwable) {
                    println(call)
                    println(t)
                    println("Error")
                }

                override fun onResponse(
                    call: Call<TournamentResponse>,
                    response: Response<TournamentResponse>

                ) {
                    if (response.code() == 201) {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Dato enviado exitosamente !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        print(response.code())
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Hubo un error en el envío de datos, vuelta a intentar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
            })
    }

    fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 10, this)
            return Base64.encodeToString(toByteArray(), Base64.DEFAULT)
        }
    }
}