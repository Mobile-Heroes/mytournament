package com.mobile.heroes.mytournament.networking.services.MatchResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentRequest
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class MetaMatchRequest(

    @SerializedName("idTournament")
    var idTournament: TournamentResponse?,

    @SerializedName("status")
    var status: String?,

    )
