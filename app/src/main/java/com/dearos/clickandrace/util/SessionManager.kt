import android.content.Context

object SessionManager {
    private const val PREFS_NAME = "session_prefs"

    fun saveSession(context: Context, isAuthenticated: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("isAuthenticated", isAuthenticated)
            .apply()
    }

    fun getSession(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean("isAuthenticated", false)
    }
}