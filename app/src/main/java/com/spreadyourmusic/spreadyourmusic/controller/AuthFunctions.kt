package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.spreadyourmusic.spreadyourmusic.models.User

/**
 * Created by Abel
 * On 27/02/18.
 */

// Comprueba si el usuario y contraseña existen, si lo hacen, almacena en elmacenamiento persistente
// ambos datos y devuelve true
// En caso contrario devuelve false
fun doLogin(user: User): Boolean {
    // TODO: Falta implementar
    // Si el login es correcto se sobreescribe session
    // Y preferences y demas
    Thread.sleep(3000)
    return true
}

fun isLogin(): Boolean {
    // TODO: Falta implementar
    return false
}

fun doGoogleLogin(user: GoogleSignInAccount): Boolean {
    // TODO: Falta implementar
    // Si el login es correcto se sobreescribe session
    // Y preferences y demas
    Thread.sleep(1000)
    return false
}

fun obtainCurrentUser(): User {
    val autor2 = User("abelcht", "Abel ChT", "Lion", "http://storage.googleapis.com/automotive-media/album_art.jpg")
    return autor2
}

fun doLogout() {

}

fun loginUserSharedPreferences(username: String, sessionToken: String, activity: Activity?) {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putString("username", username)
        putString("sessionToken", sessionToken)
        putBoolean("logged", true)
        apply()
    }
}

fun logoutUserSharedPreferences(activity: Activity?) {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putBoolean("logged", false)
        apply()
    }
}

fun getUserSharedPreferences(activity: Activity?): Pair<String, String>? {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return null
    val isLogged = sharedPref.getBoolean("logged", false)
    return if (!isLogged)  null
    else {
        val username = sharedPref.getString("username", "")
        val sessionToken = sharedPref.getString("sessionToken", "")
        Pair(username, sessionToken)
    }
}
