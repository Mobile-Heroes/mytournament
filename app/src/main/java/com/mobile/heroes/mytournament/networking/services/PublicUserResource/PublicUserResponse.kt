package com.mobile.heroes.mytournament.networking.services.PublicUserResource

import com.google.gson.annotations.SerializedName

data class PublicUserResponse(
    @SerializedName("id")
    var id: Int,

    @SerializedName("login")
    var login: String
)
