package com.mobile.heroes.mytournament.networking.services.UserResource

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    var id: Int,

    @SerializedName("login")
    var login: String
)
