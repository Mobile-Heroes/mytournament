package com.mobile.heroes.mytournament.networking.pojos

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

/**
 * POJO
 */

data class MatchResponse(
    @SerializedName("goals_home") var  goalsHome: Int,
    @SerializedName("goals_away") var  goalsVisitor: Int,
    @SerializedName("status") var status: String,
    @SerializedName("date") var date: LocalDateTime,
    @SerializedName("tournament_id") var tournament_id: Int,
    @SerializedName("field_id") var field_id: Int
    )