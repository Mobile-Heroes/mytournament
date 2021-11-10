package com.mobile.heroes.mytournament.networking.services.PublicUserResource

import com.google.gson.annotations.SerializedName

data class PublicUserRequest(

    @SerializedName("login")
    var login: String
)
