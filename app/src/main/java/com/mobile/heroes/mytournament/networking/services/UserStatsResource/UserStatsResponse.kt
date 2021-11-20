package com.mobile.heroes.mytournament.networking.services.UserStatsResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse

data class UserStatsResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("nickName")
    var nickName: String?,

    @SerializedName("goals")
    var goals: Int?,

    @SerializedName("titles")
    var titles: Int?,

    @SerializedName("tournaments")
    var tournaments: Int?,


    @SerializedName("icon")
    var icon: String?,

    @SerializedName("iconContentType")
    var iconContentType: String?,

    @SerializedName("nickName")
    var nickname: String?,

    @SerializedName("idUser")
    var idUser: UserResponse?,


    ){
}
