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
            obtainPopularSongsServer( SessionSingleton.sessionToken!!,25)
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
            obtainTrendSongsServer(SessionSingleton.sessionToken!!,25)
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
            obtainTrendSongsInUserCountryServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25)
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
            obtainResultForQueryServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25,query)
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
            obtainPopularByGenreServer(SessionSingleton.currentUser!!.username!!, SessionSingleton.sessionToken!!,25)
        } catch (e: Exception) {
            null
        }
        activity.runOnUiThread {
            listener(resultado)
        }

    }.start()
}

// Datos hacer con room
/*
// TODO:
fun saveSongInternalStorage(songData:, songId:Int, context: Context){
    val filename = "song_" + songId.toString()
    val file = File(context.filesDir, filename)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(songData.toByteArray())
    }
}
 try {
        URL url = new URL("url of your .mp3 file");
        URLConnection conexion = url.openConnection();
        conexion.connect();
        // this will be useful so that you can show a tipical 0-100% progress bar
        int lenghtOfFile = conexion.getContentLength();

        // downlod the file
        InputStream input = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.mp3");

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
            total += count;
            // publishing the progress....
            publishProgress((int)(total*100/lenghtOfFile));
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
    } catch (Exception e) {}
*/

fun saveAlbumArtInternalStorage(albumPhoto: Bitmap, albumId:Int, context: Context){
    val filename = "photo_" + albumId.toString()
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        albumPhoto.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
}

fun deleteSongInternalStorage(path: String, context: Context){
    val file = File(context.filesDir, path)
    file.delete()
}

fun deleteAlbumArtInternalStorage(path: String, context: Context){
    val file = File(context.filesDir, path)
    file.delete()
}

fun openAlbumArtInternalStorage(){
    //TODO:
}