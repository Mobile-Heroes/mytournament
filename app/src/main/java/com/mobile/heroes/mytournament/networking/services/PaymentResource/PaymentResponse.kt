package com.mobile.heroes.mytournament.networking.services.PaymentResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class PaymentResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("amount")
    var amount: Int?,

    @SerializedName("date")
    var date: String?,

    @SerializedName("idMatch")
    var matches: MatchResponce?,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse?,
    )
{
    constructor(id: Int) : this(id, null, null, null, null)
}
