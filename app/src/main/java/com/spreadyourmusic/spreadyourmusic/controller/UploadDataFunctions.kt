package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.addSongToUserServer
import com.spreadyourmusic.spreadyourmusic.apis.createAlbumsServer
import com.spreadyourmusic.spreadyourmusic.apis.createPlaylistServer
import com.spreadyourmusic.spreadyourmusic.apis.doUpdateAccountServer
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

// listener devuelve string nulo si la creacion es correcta, en caso de que string no sea
// nulo, se almacena en el el error
fun createAlbum(album: Album, activity: Activity, listener: (String?, Long) -> Unit) {
    Thread {
        var id: Long
        val resultado = try {
            id = createAlbumsServer(album.creator.username, SessionSingleton.sessionToken!!, album)
            null
        } catch (e: Exception) {
            id = 0L
            e.message
        }
        activity.runOnUiThread {
            listener(resultado, id)
        }

    }.start()
}

// listener devuelve string nulo si la creacion es correcta, en caso de que string no sea
// nulo, se almacena en el el error
fun createPlaylist(playlist: Playlist, activity: Activity, listener: (String?, Long) -> Unit) {
    Thread {
        var id: Long
        val resultado = try {
            id = createPlaylistServer(playlist.creator.username, SessionSingleton.sessionToken!!, playlist)
            null
        } catch (e: Exception) {
            id = 0L
            e.message
        }
        activity.runOnUiThread {
            listener(resultado, id)
        }

    }.start()
}

// listener devuelve string nulo si la creacion es correcta, en caso de que string no sea
// nulo, se almacena en el el error
fun createSong(user: User, song: Song, activity: Activity, listener: (String?) -> Unit) {
    Thread {
        val resultado = try {
            addSongToUserServer(user.username, song)
            null
        } catch (e: Exception) {
            e.message
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun updateUserData(user: User, activity: Activity, listener: (String?) -> Unit){
    Thread {
        val resultado = try {
            doUpdateAccountServer(user, SessionSingleton.sessionToken!!)
            null
        } catch (e: Exception) {
            e.message
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}