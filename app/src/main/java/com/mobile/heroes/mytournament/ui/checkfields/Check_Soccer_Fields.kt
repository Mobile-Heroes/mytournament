package com.mobile.heroes.mytournament.ui.checkfields

import SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.heroes.mytournament.MainActivity
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.ApiClient
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import kotlinx.android.synthetic.main.activity_check_soccer_fields.*
import kotlinx.android.synthetic.main.fragment_feed_destination.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Check_Soccer_Fields : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var adapter: SoccerFieldsAdapter
    //private lateinit var fieldList: List<FieldResponse>
    private lateinit var fieldList: ArrayList<FieldResponse>
    private lateinit var filteredFieldList: ArrayList<FieldResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_soccer_fields)
        fieldList = arrayListOf()
        filteredFieldList = arrayListOf()

        apiClient = ApiClient() //NEW CALL TO API
        sessionManager = SessionManager(this)

        loadFields()
        backbutton()

    }

    private fun searchViewBar() {

        val searchViewField = findViewById<View>(R.id.sv_buscar_cancha) as SearchView

        searchViewField.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                filterField(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filterField(newText!!)
                return false
            }
        })
    }

    private fun filterField(text: String){
        filteredFieldList.clear()
        val searchText = text!!.toLowerCase(Locale.getDefault())
        if(searchText.isNotEmpty()){
            fieldList.forEach {

                if(it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)){

                    filteredFieldList.add(it)
                }
            }
            rvSocerFields.adapter!!.notifyDataSetChanged()

        }else{

            filteredFieldList.clear()
            filteredFieldList.addAll(fieldList)
            rvSocerFields.adapter!!.notifyDataSetChanged()
        }
    }

    private fun loadFields() {
        apiClient.getApiService()
            .getField(token = "Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<List<FieldResponse>> {
                override fun onResponse(
                    call: Call<List<FieldResponse>>,
                    response: Response<List<FieldResponse>>
                ) {
                    fieldList = (response.body() as ArrayList<FieldResponse>?)!!
                    filteredFieldList.addAll(fieldList)

                    adapter = SoccerFieldsAdapter(filteredFieldList)
                    rvSocerFields.layoutManager = LinearLayoutManager(applicationContext)
                    rvSocerFields.adapter = adapter

                    searchViewBar()
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

    private fun backbutton() {
        val backButton = findViewById<ImageButton>(R.id.bt_soccer_field_back)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            //onBackPressed()
        }
    }
}