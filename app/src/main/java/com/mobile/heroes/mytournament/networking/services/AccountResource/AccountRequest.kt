package com.mobile.heroes.mytournament.networking.services.AccountResource

import android.icu.util.TimeZone
import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import java.sql.Time

data class AccountRequest(

    @SerializedName("id")
    var id: Int,

    @SerializedName("email")
    var email: String,

    @SerializedName("firstName")
    var firstName: String,

    @SerializedName("lastName")
    var lastName: String,

    @SerializedName("login")
    var login: String,

    @SerializedName("imageUrl")
    var imageUrl: String,

    @SerializedName("authorities")
    var authorities: List<String>,

    @SerializedName("activated")
    var activated: Boolean,

    @SerializedName("createdBy")
    var createdBy: String,

    @SerializedName("createdDate")
    var createdDate: String,

    @SerializedName("lastModifiedBy")
    var lastModifiedBy: String,

    @SerializedName("lastModifiedDate")
    var lastModifiedDate: String,

    @SerializedName("langKey")
    var langKey: String

    )
