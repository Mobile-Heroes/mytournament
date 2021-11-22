package com.mobile.heroes.mytournament.networking.services.TournamentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import java.util.*

data class TournamentResponse(

    @SerializedName("id")
    var id: Int,

    @SerializedName("description")
    var description: String?,

    @SerializedName("endDate")
    var endDate: String?,

    @SerializedName("format")
    var format: String?,

    @SerializedName("icon")
    var icon: String?,

    @SerializedName("iconContentType")
    var iconContentType: String?,

    @SerializedName("idUser")
    var idUser: UserResponse?,

    @SerializedName("matches")
    var matches: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("participants")
    var participants: Int?,

    @SerializedName("startDate")
    var startDate: Date?,

    @SerializedName("status")
    var status: String?
) {
    constructor(id: Int) : this(id, null, null, null, null, null, null, null, null, null, null, null)
}