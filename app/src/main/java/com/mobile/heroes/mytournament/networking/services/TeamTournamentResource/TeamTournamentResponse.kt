package com.mobile.heroes.mytournament.networking.services.TeamTournamentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class TeamTournamentResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("goalsDone")
    var goalsDone: Int?,

    @SerializedName("goalsReceived")
    var goalsReceived: Int?,

    @SerializedName("points")
    var points: Int?,

        @SerializedName("countMatches")
    var countMatches: Int?,

        @SerializedName("idTournament")
    var idTournament: TournamentResponse?,

    @SerializedName("idUser")
    var idUser: UserResponse?,
    ){
    constructor(id: Int) : this(id,null, null, null, null, null, null)
}
