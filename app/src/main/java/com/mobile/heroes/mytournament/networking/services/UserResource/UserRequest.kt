package com.mobile.heroes.mytournament.networking.services.UserResource

import com.google.gson.annotations.SerializedName

data class UserRequest(

    @SerializedName("login")
    var login: String
)
