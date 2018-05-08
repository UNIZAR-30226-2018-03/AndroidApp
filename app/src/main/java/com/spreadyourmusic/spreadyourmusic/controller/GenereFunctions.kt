package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import com.spreadyourmusic.spreadyourmusic.apis.isPlaylistFollowedByUserServer
import com.spreadyourmusic.spreadyourmusic.apis.obtainGeneresServer
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton

fun obtainGeneres(activity: Activity, listener: (List<String>) -> Unit) {
    Thread {
        val result = try {
            obtainGeneresServer()
        } catch (e: Exception) {
            ArrayList<String>()
        }
        activity.runOnUiThread {
            listener(result)
        }

    }.start()
}
