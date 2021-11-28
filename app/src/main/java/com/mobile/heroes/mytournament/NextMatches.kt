package com.mobile.heroes.mytournament

import android.graphics.Bitmap

data class NextMatches(
    val infoDate: String,
    val location: String,
    var score: String,
    val home: String,
    val away: String,
    var logoHome: Bitmap,
    var logoAway: Bitmap,
    var idMatch: Int
)