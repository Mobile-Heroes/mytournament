package com.mobile.heroes.mytournament.networking.services.UserStatsResource

import com.google.gson.annotations.SerializedName

data class UserStatsRequest (

    @SerializedName("goals")
    var goals: Int,

    @SerializedName("id")
    var id: Int,

    @SerializedName("idUser")
    var idUser: Int,


    @SerializedName("titles")
    var titles: Int,

    @SerializedName("tournaments")
    var tournaments: Int,
    )