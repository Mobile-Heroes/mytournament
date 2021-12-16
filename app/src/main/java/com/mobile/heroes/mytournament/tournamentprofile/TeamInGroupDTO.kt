package com.mobile.heroes.mytournament.tournamentprofile

import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse

class TeamInGroupDTO(
    var groupDTO: GroupResponse,
    var teamTournamentDTOList: List<TeamTournamentResponse>?
){
    constructor(groupDTO: GroupResponse) : this(groupDTO, null)
}