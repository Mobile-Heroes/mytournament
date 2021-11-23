import android.accounts.Account
import android.content.Context
import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear
import com.google.gson.Gson
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    public val development = true;

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ACCOUNT = "user_account"
        const val USER_STATS ="user_stats"
        const val TEAM_TOURNAMENTS_ID ="team_tournaments"

    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Clear all preferences
     */
    fun clearAll() {
        val editor = prefs.edit()
        editor.clear()
        editor.commit()
        editor.apply()
    }

    /**
     * Function to save account
     */
    fun saveAccount(account: AccountResponce) {
        val gson = Gson()
        val editor = prefs.edit()
        editor.putString(USER_ACCOUNT, gson.toJson(account).toString())
        editor.apply()
    }

    fun saveUserStats(userStat: UserStatsResponse) {
        val gson = Gson()
        val editor = prefs.edit()
        editor.putString(USER_STATS, gson.toJson(userStat).toString())
        editor.apply()
    }

    fun saveTeamTournament(userStat: TeamTournamentResponse) {
        val gson = Gson()
        val editor = prefs.edit()
        editor.putString(TEAM_TOURNAMENTS_ID, gson.toJson(userStat).toString())
        editor.apply()
    }

    /**
     * Function to save account
     */
    fun fetchAccount() : AccountResponce? {
        val gson = Gson()
        val userAccount: AccountResponce = gson.fromJson(prefs.getString(USER_ACCOUNT, null), AccountResponce::class.java)
        return userAccount
    }

    fun fetchUserStats() : UserStatsResponse? {
        val gson = Gson()
        val userStats: UserStatsResponse = gson.fromJson(prefs.getString(USER_STATS, null), UserStatsResponse::class.java)
        return userStats
    }
    fun fetchTeamTournament() : TeamTournamentResponse? {
        val gson = Gson()
        val userStats: TeamTournamentResponse = gson.fromJson(prefs.getString(TEAM_TOURNAMENTS_ID, null), TeamTournamentResponse::class.java)
        return userStats
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {

        if(prefs.getString(USER_TOKEN, null) == null){
            if(development)
            {
                getDevelpomentToken()
            }
        }

        return prefs.getString(USER_TOKEN, null)
    }


    private fun getDevelpomentToken() {
        saveAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTYzOTEwNDQ3MH0.bcogGaMyxHFCQKoT6QGZQyootOKOHMm3rj6rl-v0HS6mop_-QNv7PA-vgt2tnSEYsTz6fzHier_hdBrtDtKhEA")
    }
}