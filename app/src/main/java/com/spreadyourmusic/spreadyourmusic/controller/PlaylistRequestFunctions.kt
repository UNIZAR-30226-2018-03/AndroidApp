package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.*
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton
import kotlin.collections.ArrayList

fun obtainPlaylistFromID(id: Long, activity: Activity, listener: (Playlist?) -> Unit) {
    Thread {
        val resultado = try {
            obtainPlaylistDataServer(id, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}


fun obtainSystemGeneratedPlaylist(id: Int, activity: Activity, listener: (Pair<String, List<Recommendation>>) -> Unit) {
    when (id) {
        0 -> {
            obtainFollowedPlaylists(activity, {
                if (it != null) {
                    listener(Pair<String, List<Recommendation>>(activity.resources.getString(R.string.playlist), it))
                } else {
                    Toast.makeText(activity, "ErrorPlaylist", Toast.LENGTH_SHORT).show()
                }
            })
        }
        1 -> {
            obtainFollowedArtists(activity, {
                if (it != null) {
                    listener(Pair<String, List<Recommendation>>(activity.resources.getString(R.string.artist), it))
                } else {
                    Toast.makeText(activity, "ErrorPlaylist", Toast.LENGTH_SHORT).show()
                }
            })

        }
        2 -> {
            obtainFavoriteSongsList(activity, {
                if (it != null) {
                    listener(Pair<String, List<Recommendation>>(activity.resources.getString(R.string.songs), it))
                } else {
                    Toast.makeText(activity, "ErrorPlaylist", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else -> {
            obtainDownloadSongsList(activity, {
                if (it != null) {
                    listener(Pair<String, List<Recommendation>>(activity.resources.getString(R.string.downloaded), it))
                } else {
                    Toast.makeText(activity, "ErrorPlaylist", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

fun obtainFavoriteSongsList(activity: Activity, listener: (List<Song>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainFavouriteSongsByUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun obtainFollowedArtists(activity: Activity, listener: (List<User>?) -> Unit) {
    Thread {
        val resultado = try {
            getFollowedUsersServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}

fun obtainFollowedPlaylists(activity: Activity, listener: (List<Playlist>?) -> Unit) {
    Thread {
        val resultado = try {
            getFollowedPlaylistsServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()

}

fun obtainDownloadSongsList(activity: Activity, listener: (List<Song>?) -> Unit) {
    Thread {
        val resultado = getDownloadedSongs(activity)
        activity.runOnUiThread {
            listener(resultado)
        }
    }.start()
}