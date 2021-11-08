package com.mobile.heroes.mytournament.feed

import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("status") var status: String,
    @SerializedName("message") var images: List<String>
)