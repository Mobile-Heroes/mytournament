package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
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
    var idField: FieldResponse,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,

    @SerializedName("status")
    var status: String,

    )