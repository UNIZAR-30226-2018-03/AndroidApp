package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.deletePlaylistServer
import com.spreadyourmusic.spreadyourmusic.apis.deleteSongServer
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

fun doDeleteSong(song: Song, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val result = try {
            deleteSongServer(SessionSingleton.currentUser!!.username, SessionSingleton.sessionToken!!, song, activity)
            true
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(result)
        }
    }.start()
}

fun doDeletePlaylist(playlist: Playlist, activity: Activity, listener: (Boolean) -> Unit) {
    Thread {
        val result = try {
            deletePlaylistServer(SessionSingleton.currentUser!!.username, SessionSingleton.sessionToken!!, playlist, activity)
            true
        } catch (e: Exception) {
            false
        }
        activity.runOnUiThread {
            listener(result)
        }
    }.start()
}