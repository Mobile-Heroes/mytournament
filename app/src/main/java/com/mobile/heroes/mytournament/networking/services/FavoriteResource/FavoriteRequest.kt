package com.mobile.heroes.mytournament.networking.services.FavoriteResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class FavoriteRequest(

    @SerializedName("status")
    var status: String,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,

    @SerializedName("idUser")
    var idUser: UserResponse,

    )
