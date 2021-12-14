package com.mobile.heroes.mytournament.helpers

import com.mobile.heroes.mytournament.networking.services.UserResource.UserResponse
import java.util.*

data class TournamentDTO(
    var id: Int?,
    var description: String?,
    var endDate: String?,
    var format: String?,
    var icon: String?,
    var iconContentType: String?,
    var idUser: UserResponse?,
    var matches: Int?,
    var name: String?,
    var participants: Int?,
    var startDate: String?,
    var status: String?
    )
