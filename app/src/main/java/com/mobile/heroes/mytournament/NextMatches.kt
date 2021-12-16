package com.mobile.heroes.mytournament

import android.graphics.Bitmap
import java.util.*

data class NextMatches(
    val infoDate: Date,
    val location: String,
    var score: String,
    val home: String,
    val away: String,
    var logoHome: Bitmap,
    var logoAway: Bitmap,
    var idMatch: Int
)