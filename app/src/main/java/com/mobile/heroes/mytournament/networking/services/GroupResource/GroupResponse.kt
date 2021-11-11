package com.mobile.heroes.mytournament.networking.services.GroupResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class GroupResponse(

    @SerializedName("id")
    var id: Int,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse,

    @SerializedName("name")
    var name: String,

    @SerializedName("type")
    var type: String,

    )
