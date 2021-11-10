package com.mobile.heroes.mytournament.networking.services.TournamentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.Tournament

data class TournamentResponse(

    @SerializedName("description")
    var description: String,

    @SerializedName("endDate")
    var endDate: String,

    @SerializedName("format")
    var format: String,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("iconContentType")
    var iconContentType: String,

    @SerializedName("id")
    var id: Int,

    @SerializedName("matches")
    var matches: Int,

    @SerializedName("participants")
    var participants: Int,

    @SerializedName("startDate")
    var startDate: String,

    @SerializedName("status")
    var status: String
)