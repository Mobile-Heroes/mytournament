package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName

data class MatchRequest(

    @SerializedName("date")
    var date: String,

    @SerializedName("goalsAway")
    var goalsAway: Int,

    @SerializedName("goalsHome")
    var goalsHome: Int,

    @SerializedName("idField")
    var idField: Int,

    @SerializedName("idTournament")
    var idTournament: Int,

    @SerializedName("status")
    var status: String,


    )