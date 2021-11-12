package com.mobile.heroes.mytournament.networking.services.FieldResource

import com.google.gson.annotations.SerializedName

data class FieldResponse (

    @SerializedName("id")
    var id: Int,

    @SerializedName("lat")
    var lat: Double,

    @SerializedName("lon")
    var lon: Double,

    @SerializedName("name")
    var name: String?,

    @SerializedName("status")
    var status: String?,
) {
    constructor(id: Int) : this(id, 0.0, 0.0, null, null)
}