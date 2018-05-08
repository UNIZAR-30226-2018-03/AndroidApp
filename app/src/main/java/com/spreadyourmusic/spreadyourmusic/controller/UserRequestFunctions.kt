package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

/**
 * Created by abel
 * On 5/04/18.
 */

fun obtainUserFromID(id: String, activity: Activity, listener: (User?) -> Unit) {
    Thread {
        val resultado = try {
            obtainUserDataServer(id, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun obtainPlaylistsFromUser(user: User, activity: Activity, listener: (List<Playlist>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainPlaylistsFromUserServer(user.username!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun obtainSongsFromUser(user: User, activity: Activity, listener: (List<Song>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainSongsFromUserServer(user.username!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun obtainAlbumsFromUser(user: User, activity: Activity, listener: (List<Album>?) -> Unit){
    Thread {
        val resultado = try {
            obtainAlbumsFromUserServer(user.username!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun addAlbumToUser(user: User, activity: Activity, album:Album){
    Thread {
        try {
            createAlbumsServer(user.username!!,SessionSingleton.sessionToken!!,album)
            } catch (e: Exception) {
        }
    }.start()
}

fun addSongToUser(user: User, activity: Activity, song: Song){
    Thread {
        try {
            addSongToUserServer(user.username!!,song)
        } catch (e: Exception) {
        }
    }.start()
}