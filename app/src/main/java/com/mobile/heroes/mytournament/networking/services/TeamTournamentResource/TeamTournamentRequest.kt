package com.mobile.heroes.mytournament.networking.services.TeamTournamentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class TeamTournamentRequest (

    @SerializedName("goalsDone")
    var goalsDone: Int,

    @SerializedName("goalsReceived")
    var goalsReceived: Int,

    @SerializedName("points")
    var points: Int,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,


    @SerializedName("idUser")
    var idUser: Int,

    )