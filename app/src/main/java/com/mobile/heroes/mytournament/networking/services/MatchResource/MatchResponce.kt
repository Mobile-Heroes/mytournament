package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

data class MatchResponce (

    @SerializedName("date")
    var date: Date,

    @SerializedName("goalsAway")
    var goalsAway: Int,

    @SerializedName("goalsHome")
    var goalsHome: Int,

    @SerializedName("id")
    var id: Int,

    @SerializedName("idField")
    var idField: Int,

    @SerializedName("idTournament")
    var idTournament: Int,

    @SerializedName("status")
    var status: String,

    )