package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResource
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResource
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

data class MatchRequest (

    @SerializedName("date")
    var date: String,

    @SerializedName("goalsAway")
    var goalsAway: Int,

    @SerializedName("goalsHome")
    var goalsHome: Int,

    @SerializedName("idField")
    var idField: FieldResource,

    @SerializedName("idTournament")
    var idTournament: TournamentResource,

    @SerializedName("status")
    var status: String,


)