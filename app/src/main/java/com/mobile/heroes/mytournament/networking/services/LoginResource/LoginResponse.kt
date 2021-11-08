package com.mobile.heroes.mytournament.networking.services.LoginResource

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("id_token")
    var id_token: String,

)