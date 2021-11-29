package com.mobile.heroes.mytournament.networking.services.FavoriteResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class FavoriteResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("tournaments")
    var idTournament: TournamentResponse?,

    @SerializedName("idUser")
    var idUser: UserResponse?,
    )
{
    constructor(id: Int) : this(id, null, null, null)
}
