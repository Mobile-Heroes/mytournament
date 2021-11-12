import android.accounts.Account
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mobile.heroes.mytournament.R
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    public val development = true;

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ACCOUNT = "user_account"
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
     * Function to save account
     */
    fun saveAccount(account: AccountResponce) {
        val gson = Gson()
        val editor = prefs.edit()
        editor.putString(USER_ACCOUNT, gson.toJson(account).toString())
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