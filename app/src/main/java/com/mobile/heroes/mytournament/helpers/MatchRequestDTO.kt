package com.mobile.heroes.mytournament.helpers

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import java.util.*

data class MatchRequestDTO(
    @SerializedName("id")
    var id: Int?,

    @SerializedName("date")
    var date: String?,

    @SerializedName("goalsHome")
    var goalsHome: Int?,

    @SerializedName("goalsAway")
    var goalsAway: Int?,

    @SerializedName("idField")
    var idField: FieldResponse?,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("idTeamTournamentHome")
    var idTeamTournamentHome: TeamTournamentResponse?,

    @SerializedName("idTeamTournamentVisitor")
    var idTeamTournamentVisitor: TeamTournamentResponse?,
)
