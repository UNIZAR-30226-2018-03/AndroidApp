package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Context


/**
 * Guarda los datos del usuario en shared preferences
 */
fun loginUserSharedPreferences(username: String, sessionToken: String, activity: Activity?) {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putString("username", username)
        putString("sessionToken", sessionToken)
        putBoolean("logged", true)
        apply()
    }
}

/**
 *  Elimina los datos almacenados en shared preferences
*/
fun logoutUserSharedPreferences(activity: Activity?) {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    with(sharedPref.edit()) {
        putBoolean("logged", false)
        apply()
    }
}

/**
 * Obtiene los datos almacenados en shared preferences
 * Devuelve un par del tipo (usuario, token de sesión) en el caso de que exista null en caso contrario
 * */
fun getUserSharedPreferences(activity: Activity?): Pair<String, String>? {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return null
    val isLogged = sharedPref.getBoolean("logged", false)
    return if (!isLogged) null
    else {
        val username = sharedPref.getString("username", "")
        val sessionToken = sharedPref.getString("sessionToken", "")
        Pair(username, sessionToken)
    }
}