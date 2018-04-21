package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

/**
 * Created by abel
 * On 8/03/18.
 */
fun getCurrentSong():Song{
   return MusicQueueManager.getInstance().currentSong
}

fun setFavoriteCurrentSong(state: Boolean, activity: Activity, listener: (Boolean) -> Unit){
    Thread {
        val resultado = try {
            if (state) setSongFavoutireServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, getCurrentSong().id)
            else unSetSongFavoutireServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, getCurrentSong().id)
            true
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun isCurrentSongFavorite(activity: Activity, listener: (Boolean) -> Unit){
    Thread {
        val resultado = try {
            isSongFavoutireByUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!, getCurrentSong().id)
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

// Si es true descarga la cancion, sino la elimina
fun downloadCurrentSong(state: Boolean, activity: Activity, listener: (Boolean) -> Unit){
// TODO: Hacer
    listener(true)
}


fun isCurrentSongDownloaded(activity: Activity, listener: (Boolean) -> Unit){
    // TODO: Hacer
    listener(false)
}

// Si es true crea reproduccion aleatoria
fun randomReproduction(nextState: Boolean) {
// TODO: Hacer
}

fun isRandomReproductionEnabled():Boolean{
    // TODO: Hacer
    return false
}