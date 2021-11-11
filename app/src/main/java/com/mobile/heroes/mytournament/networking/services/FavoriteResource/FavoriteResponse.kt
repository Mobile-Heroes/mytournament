package com.mobile.heroes.mytournament.networking.services.FavoriteResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class FavoriteResponse(

    @SerializedName("id")
    var id: Int,

    @SerializedName("status")
    var status: String,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,

)
