package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class MatchRequest(

    @SerializedName("date")
    var date: String,

    @SerializedName("goalsAway")
    var goalsAway: Int,

    @SerializedName("goalsHome")
    var goalsHome: Int,

    @SerializedName("idField")
    var idField: FieldResponse,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,

    @SerializedName("status")
    var status: String,


    )