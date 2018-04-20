package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Intent
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

/**
 * Created by abel on 8/03/18.
 */
/**
 * La función listener es llamada con el resultado
 */
fun isFollowing(playlist: Playlist, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val result = try {
            isPlaylistFollowedByUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, playlist.id.toLong())
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(result)
        }

    }.start()
}

/**
 * Si estado == true, el usuario actual empieza a seguir a la playlist @playlist
 * en caso contrario la deja de seguir
 * La funcion listener es llamada con true, si la operacion es realizada con exito
 */
fun changeFollowState(playlist: Playlist, estado: Boolean, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val resultado = try {
            if (estado) addFollowerToPlaylistServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, playlist.id.toLong())
            else deleteFollowerToPlaylistServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, playlist.id.toLong())
            true
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

/**
 * La función listener es llamada con el resultado
 */
fun isFollowing(user: User, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val resultado = try {
            isUserFollowedByUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, user.username!!)
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

/**
 * Si estado == true, el usuario actual empieza a seguir al usuario @user
 * en caso contrario la deja de seguir
 * La funcion listener es llamada con true, si la operacion es realizada con exito
 */
fun changeFollowState(user: User, estado: Boolean, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val resultado = try {
            if (estado) addFollowerToUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, user.username!!)
            else deleteFollowerToUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, user.username!!)
            true
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

/**
 * La función listener es llamada con el resultado
 */
fun obtainNumberOfFollowers(user: User, activity: Activity, listener: (Long) -> Unit) {
    Thread {
        val resultado = try {
            getNumberOfFollowersOfUserServer(user.username!!, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            0L
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

/**
 * La función listener es llamada con el resultado
 */
fun obtainNumberOfFollowers(playlist: Playlist, activity: Activity, listener: (Long) -> Unit) {
    Thread {
        val resultado = try {
            getNumberOfFollowersOfPlaylistServer(playlist.id.toLong(), SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            0L
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

/**
 * Abre el asistente para compartir la url pasada
 */
fun shareElement(url: String, activity: Activity) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT, url)
    sendIntent.type = "text/plain"
    activity.startActivity(sendIntent)
}