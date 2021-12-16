package com.mobile.heroes.mytournament.ui.checkfields

import SessionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import kotlinx.android.synthetic.main.activity_check_soccer_fields.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Check_Soccer_Fields : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var adapter: SoccerFieldsAdapter
    private lateinit var fieldList: List<FieldResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_soccer_fields)

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        loadFields()

    }

    private fun loadFields() {
        apiClient.getApiService()
            .getField(token = "Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<List<FieldResponse>> {
                override fun onResponse(
                    call: Call<List<FieldResponse>>,
                    response: Response<List<FieldResponse>>
                ) {
                    fieldList = response.body()!!
                    adapter = SoccerFieldsAdapter(fieldList)
                    rvSocerFields.layoutManager = LinearLayoutManager(applicationContext)
                    rvSocerFields.adapter = adapter
                }

                override fun onFailure(call: Call<List<FieldResponse>>, t: Throwable) {
                    println(call)
                    println(t)
                    println("error")
                    runOnUiThread() {
                        Toast.makeText(
                            applicationContext,
                            "Error 2",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
    }
}