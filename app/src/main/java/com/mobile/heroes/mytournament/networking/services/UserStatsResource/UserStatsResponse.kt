package com.mobile.heroes.mytournament.networking.services.UserStatsResource

import com.google.gson.annotations.SerializedName

data class UserStatsResponse(


    @SerializedName("goals")
    var goals: Int,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("iconContentType")
    var iconContentType: String,

    @SerializedName("id")
    var id: Int,

    @SerializedName("idUser")
    var idUser: Int,


    @SerializedName("titles")
    var titles: Int,

    @SerializedName("tournaments")
    var tournaments: Int,
    )
