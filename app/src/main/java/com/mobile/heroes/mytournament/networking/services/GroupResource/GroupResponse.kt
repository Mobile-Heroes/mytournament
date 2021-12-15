package com.mobile.heroes.mytournament.networking.services.GroupResource

import com.google.gson.annotations.SerializedName
import com.mobile.heroes.mytournament.networking.services.TournamentResource.TournamentResponse

data class GroupResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("idTournament")
    var idTournament: TournamentResponse?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("grade")
    var grade: Int?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("status")
    var status: String?,
    )

{
    constructor(id: Int) : this(id, null, null, null, null, null)
}
