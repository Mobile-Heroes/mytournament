package com.mobile.heroes.mytournament.networking

import android.accounts.Account

object Constants {

    //BASE URL
    const val BASE_URL = "https://mytournament-beta.herokuapp.com:443/api/"

    //Login
    const val LOGIN_URL = "authenticate"

    //Post-Login
    const val POSTS_URL = "posts"

    //Endpoints
    const val MATCH_URL = "matches"
    const val TOURNAMENT_URL = "tournaments"
    const val FIELD_URL = "fields"
    const val FAVORITE_URL = "favorites"
    const val GROUP_URL = "groups"
    const val PAYMENT_URL = "payments"
    const val TEAM_TOURNAMENT_URL = "team-tournaments"
    const val TEAM_TOURNAMENT_BY_ID_TOURNAMENT = "team-tournaments?"
    const val USER_STATS_URL = "user-stats"
    const val USER_ACCOUNT = "account"
    const val USER = "register"
    const val USER_STATS_BY_ID_USER="user-stats?"
    const val TEAM_TOURNAMENT_BY="team-tournaments?"
    const val MATCH_BY = "matches?"




    //PARA QUE USAN ESTO ? ESTE ENDPOINT NO FUNCIONA A NADA.
    //const val PUBLIC_USER_URL = "users"
}