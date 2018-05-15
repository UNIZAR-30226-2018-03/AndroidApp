package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

/**
 * Created by Abel
 * On 27/02/18.
 */

/**
 * Comprueba si el usuario y contraseña existen, si lo hacen, almacena en el almacenamiento persistente
 * los datos relativos a la sesión y devuelve true, en caso contrario devuelve false
 * Warning: Esta función puede ser costosa en tiempo, ejecutar en thread diferente al principal
 */
fun doLogin(user: User, activity: Activity): Boolean {
    return try {
        val sessionToken = doLoginServer(user.username, user.password!!)

        // El login en el server ha sido correcto
        loginUserSharedPreferences(user.username, sessionToken, activity)

        // Se actualiza el objeto de sesión
        SessionSingleton.currentUser = user
        SessionSingleton.sessionToken = sessionToken
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Comprueba si hay un usuario logeado, en caso afirmativo guarda sus datos en el objeto de sesión
 * Warning: Esta función puede ser costosa en tiempo, ejecutar en thread diferente al principal
 */
fun isLogin(activity: Activity): Boolean {
    val mSharedPreferences = getUserSharedPreferences(activity)
    return if (mSharedPreferences != null) {
        SessionSingleton.currentUser = User(mSharedPreferences.first)
        SessionSingleton.sessionToken = mSharedPreferences.second
        true
    } else {
        false
    }
}

/**
 * Elimina al actual usuario logeado si existe
 * Warning: Esta función puede ser costosa en tiempo, ejecutar en thread diferente al principal
 */
fun doLogout(activity: Activity) {
    val mSharedPreferences = getUserSharedPreferences(activity)
    if (mSharedPreferences != null) {
        val user = mSharedPreferences.first
        val sessionToken = mSharedPreferences.second
        doLogoutServer(user, sessionToken)
        logoutUserSharedPreferences(activity)
    } else {
        SessionSingleton.currentUser = null
        SessionSingleton.sessionToken = null
        SessionSingleton.isUserDataLoaded = false
    }
}

fun doGoogleLogin(user: GoogleSignInAccount, activity: Activity): Boolean {
    val email = user.email
    val auth_code = user.getServerAuthCode();

    // TODO: Falta implementar
    // Si el login es correcto se sobreescribe session
    // Y preferences y demas
    Thread.sleep(1000)
    return false
}

/**
 * Llama a la función listener pasandole como parámetro el usuario actual.
 * En caso de error le pasa un objeto nulo
 */
fun obtainCurrentUserData(listener: (User?) -> Unit, activity: Activity) {
    if (SessionSingleton.isUserDataLoaded) {
        listener(SessionSingleton.currentUser)
    } else {
        Thread {
            val user = try {
                obtainUserDataServer(SessionSingleton.currentUser!!.username, SessionSingleton.sessionToken!!)
            } catch (e: Exception) {
                null
            }
            activity.runOnUiThread {
                if (user != null) {
                    SessionSingleton.currentUser = user
                    SessionSingleton.isUserDataLoaded = true
                    listener(user)
                } else {
                    listener(null)
                }
            }
        }.start()

    }
}

fun doSignUp(user: User, activity: Activity, listener: (String?) -> Unit) {
    Thread {
        var sessionToken = ""
        val resultado = try {
            sessionToken = doSignUpServer(user)
            null
        } catch (e: Exception) {
            e.message
        }
        activity.runOnUiThread {
            if (resultado.isNullOrEmpty()) {
                // El login en el server ha sido correcto
                loginUserSharedPreferences(user.username, sessionToken, activity)

                // Se actualiza el objeto de sesión
                SessionSingleton.currentUser = user
                SessionSingleton.sessionToken = sessionToken
            }
            listener(resultado)
        }
    }.start()
}

fun doDeleteAccount(activity: Activity): Boolean {
    val mSharedPreferences = getUserSharedPreferences(activity)
    return if (mSharedPreferences != null) {
        val user = mSharedPreferences.first
        val sessionToken = mSharedPreferences.second
        logoutUserSharedPreferences(activity)
        SessionSingleton.currentUser = null
        SessionSingleton.sessionToken = null
        SessionSingleton.isUserDataLoaded = false
        try {
            doDeleteAccountServer(user, sessionToken)
            true
        } catch (e: Exception) {
            false
        }
    } else false
}

fun isCurrentUserLoggedinOtherSession(activity: Activity, listener: (Song?) -> Unit) {
    Thread {
        val resultado = try {
            if (isOtherSessionOpenFromSameUserServer(SessionSingleton.currentUser!!.username, SessionSingleton.sessionToken!!)) {
                obtainLastSongListenedServer(SessionSingleton.currentUser!!.username, SessionSingleton.sessionToken!!)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}