package com.mobile.heroes.mytournament.networking.services.FieldResource

import com.google.gson.annotations.SerializedName

data class FieldRequest(
    @SerializedName("lat")
    var lat: Double,

    @SerializedName("lon")
    var lon: Double,

    @SerializedName("name")
    var name: String,

    @SerializedName("status")
    var status: String,
)