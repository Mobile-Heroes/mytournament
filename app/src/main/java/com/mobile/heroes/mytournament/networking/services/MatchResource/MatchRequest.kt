package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import java.util.*

data class MatchRequest(

        @SerializedName("date")
    var date: Date,

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

        @SerializedName("idTeamTournamentHome")
    var idTeamTournamentHome: TeamTournamentResponse,

        @SerializedName("idTeamTournamentVisitor")
    var idTeamTournamentVisitor: TeamTournamentResponse,

        )