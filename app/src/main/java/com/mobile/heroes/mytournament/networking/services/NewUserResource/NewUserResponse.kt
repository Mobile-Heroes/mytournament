package com.mobile.heroes.mytournament.networking.services.NewUserResource

import com.google.gson.annotations.SerializedName

data class NewUserResponse(

    @SerializedName("activated")
    var activated: Boolean?,

    @SerializedName("authorities")
    var authorities: List<String>?,

    @SerializedName("createdBy")
    var createdBy: String?,

    @SerializedName("createdDate")
    var createdDate: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("firstName")
    var firstName: String?,

    @SerializedName("id")
    var id: Int,

    @SerializedName("imageUrl")
    var imageUrl: String?,

    @SerializedName("langKey")
    var langKey: String?,

    @SerializedName("lastModifiedBy")
    var lastModifiedBy: String?,

    @SerializedName("lastModifiedDate")
    var lastModifiedDate: String?,

    @SerializedName("lastName")
    var lastName: String?,

    @SerializedName("login")
    var login: String?,

    @SerializedName("password")
    var password: String?,

)
