package com.mobile.heroes.mytournament.helpers

import android.graphics.Bitmap

data class MatchDTO(
        var infoDate: String,
        var home: String,
        var away: String,
        var location:String,
        var logoHome: String,
        var logoAway:String,
){
}
