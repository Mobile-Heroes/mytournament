package com.mobile.heroes.mytournament.networking.services.UserStatsResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class UserStatsRequest (

    @SerializedName("goals")
    var goals: Int,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("iconContentType")
    var iconContentType: String,

    @SerializedName("idUser")
    var idUser: UserResponse,

    @SerializedName("nickName")
    var nickname: String?,

    @SerializedName("titles")
    var titles: Int,

    @SerializedName("tournaments")
    var tournaments: Int,
    )