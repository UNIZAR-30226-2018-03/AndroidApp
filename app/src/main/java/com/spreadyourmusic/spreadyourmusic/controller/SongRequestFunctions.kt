package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.*
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton
import java.io.File

/**
 * Created by abel
 * On 7/03/18.
 */

// Recomendations for me
fun obtainRecommendations(activity: Activity, listener: (List<Recommendation>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainRecomendationsForUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}


// Popular in the word
fun obtainPopularSongs(activity: Activity, listener: ( List<Song>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainPopularSongsServer(25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// New songs from followed Artists
fun obtainNewsSongs(activity: Activity, listener: (List<Song> ?) -> Unit) {
    Thread {
        val resultado = try {
            obtainNewSongsFromFollowedArtistOfUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// Popular Now
fun obtainTrendSongs(activity: Activity, listener: (List<Song> ?) -> Unit) {
    Thread {
        val resultado = try {
            obtainTrendSongsServer(25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// Popular in my country
fun obtainTrendInMyCountry(activity: Activity, listener: ( List<Song>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainTrendSongsInUserCountryServer(SessionSingleton.currentUser!!.username!!,25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// Updated playlists
fun obtainUpdatedPlaylists(activity: Activity, listener: ( List<Playlist>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainUpdatedPlaylistsFollowedByUserServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

fun obtainResultFromQuery(query: String,activity: Activity, listener: (List<Recommendation>?) -> Unit) {
    Thread {
        val resultado = try {
            obtainResultForQueryServer(25,query, null)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// Obtain Songs to show in Popular by genre screen
fun obtainPopularByGenre(activity: Activity, listener: (List<Pair<String, List<Recommendation>>>?) -> Unit){
    Thread {
        val resultado = try {
            obtainPopularByGenreServer(25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}