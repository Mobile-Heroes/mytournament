package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.helpers.MatchDTO
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import java.util.*

data class MetaMatchResponse(

    @SerializedName("matchDTO")
    var matchDTO: MatchResponce?,

    @SerializedName("userStatsDTOHome")
    var userStatsHome: UserStatsResponse?,

    @SerializedName("userStatsDTOVisitor")
    var userStatsAway: UserStatsResponse?,

    )
