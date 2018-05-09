package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.addSongToUserServer
import com.spreadyourmusic.spreadyourmusic.apis.createAlbumsServer
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

// listener devuelve string nulo si la creacion es correcta, en caso de que string no sea
// nulo, se almacena en el el error
fun createAlbum(album: Album, activity: Activity, listener: (String?) -> Unit) {
    Thread {
        try {
            createAlbumsServer(album.creator.username, SessionSingleton.sessionToken!!, album)
            listener(null)
        } catch (e: Exception) {
            listener(e.message)
        }
    }.start()
}

// listener devuelve string nulo si la creacion es correcta, en caso de que string no sea
// nulo, se almacena en el el error
fun createSong(user: User, song: Song, activity: Activity, listener: (String?) -> Unit) {
    Thread {
        try {
            addSongToUserServer(user.username, song)
            listener(null)
        } catch (e: Exception) {
            listener(e.message)
        }
    }.start()
}