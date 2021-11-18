package com.mobile.heroes.mytournament.networking.services.TournamentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class TournamentRequest(

    @SerializedName("description")
    var description: String,

    @SerializedName("endDate")
    var endDate: String,

    @SerializedName("format")
    var format: String,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("idUser")
    var idUser: UserResponse,

    @SerializedName("iconContentType")
    var iconContentType: String,

    @SerializedName("matches")
    var matches: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("participants")
    var participants: Int,

    @SerializedName("startDate")
    var startDate: String,

    @SerializedName("status")
    var status: String
)