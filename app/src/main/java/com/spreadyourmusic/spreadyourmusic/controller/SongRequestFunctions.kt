package com.spreadyourmusic.spreadyourmusic.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.spreadyourmusic.spreadyourmusic.models.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by abel
 * On 7/03/18.
 */

// Recomendations for me
fun obtainRecommendations(): List<Recommendation> {
    val devolver = ArrayList<Recommendation>()

    val autor1 = User("Media", "Media", "Right", "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val autor2 = User("Silent", "Silent", "Partner", "http://storage.googleapis.com/automotive-media/album_art.jpg")

    val album1 = Album("Jazz", autor1, GregorianCalendar(2018, 3, 22), "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val album2 = Album("Blues", autor2, GregorianCalendar(2017, 6, 27), "http://storage.googleapis.com/automotive-media/album_art_2.jpg")

    val cancion1 = Song(album = album1, id = 1, name = "Jazz in Paris", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 103000L, locationUri = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3")
    val cancion2 = Song(album = album2, id = 2, name = "The Messenger", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 132000L, locationUri = "http://storage.googleapis.com/automotive-media/The_Messenger.mp3")

    val cancion3 = Song(album = album2, id = 3, name = "Malabar", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 710000L, locationUri = "http://155.210.13.105:7480/TEST/malabar.mp3")
    val cancion4 = Song(album = album2, id = 4, name = "KidKudi", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 313000L, locationUri =  Environment.getExternalStorageDirectory().getPath()+ "/Music/Prueba/Prueba.mp3")

    val listaCanciones = ArrayList<Song>()
    listaCanciones.add(cancion1)
    listaCanciones.add(cancion2)

    val playlist = Playlist(2,"Lista", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", listaCanciones)
    devolver.add(autor1)
    devolver.add(cancion2)
    devolver.add(cancion1)
    devolver.add(playlist)
    devolver.add(autor2)
    devolver.add(cancion3)
    devolver.add(cancion4)
    return devolver
}


// Popular in the word
fun obtainPopularSongs(): List<Song> {
    val devolver = ArrayList<Song>()

    val autor1 = User("Media", "Media", "Right", "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val autor2 = User("Silent", "Silent", "Partner", "http://storage.googleapis.com/automotive-media/album_art.jpg")

    val album1 = Album("Jazz", autor1, GregorianCalendar(2018, 3, 22), "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val album2 = Album("Blues", autor2, GregorianCalendar(2017, 6, 27), "http://storage.googleapis.com/automotive-media/album_art_2.jpg")

    val cancion1 = Song(album = album1, id = 1, name = "Jazz in Paris", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 103000L, locationUri = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3")
    val cancion2 = Song(album = album2, id = 2, name = "The Messenger", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 132000L, locationUri = "http://storage.googleapis.com/automotive-media/The_Messenger.mp3")

    val cancion3 = Song(album = album2, id = 2, name = "Malabar", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 710000L, locationUri = "http://155.210.13.105:7480/TEST/malabar.mp3")

    val listaCanciones = ArrayList<Song>()
    listaCanciones.add(cancion1)
    listaCanciones.add(cancion2)

    devolver.add(cancion2)
    devolver.add(cancion1)
    devolver.add(cancion3)
    return devolver
}

// New songs from followed Artists
fun obtainNewsSongs(): List<Song> {
    return obtainPopularSongs()
}

// Popular Now
fun obtainTrendSongs(): List<Song> {
    return obtainPopularSongs()
}

// Popular in my country
fun obtainTrendInMyCountry(): List<Song> {
    return obtainPopularSongs()
}

// Updated playlists
fun obtainUpdatedPlaylists(): List<Playlist> {
    val devolver = ArrayList<Playlist>()

    val autor1 = User("Media", "Media", "Right", "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val autor2 = User("Silent", "Silent", "Partner", "http://storage.googleapis.com/automotive-media/album_art.jpg")

    val album1 = Album("Jazz", autor1, GregorianCalendar(2018, 3, 22), "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val album2 = Album("Blues", autor2, GregorianCalendar(2017, 6, 27), "http://storage.googleapis.com/automotive-media/album_art_2.jpg")

    val cancion1 = Song(album = album1, id = 1, name = "Jazz in Paris", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 103000L, locationUri = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3")
    val cancion2 = Song(album = album2, id = 2, name = "The Messenger", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 132000L, locationUri = "http://storage.googleapis.com/automotive-media/The_Messenger.mp3")

    val listaCanciones = ArrayList<Song>()
    listaCanciones.add(cancion1)
    listaCanciones.add(cancion2)

    val playlist = Playlist(1,"Lista", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", listaCanciones)

    devolver.add(playlist)
    return devolver
}

fun obtainResultFromQuery(query: String): List<Recommendation> {
    return obtainRecommendations()
}

// Obtain Songs to show in Popular by genre screen
fun obtainPopularByGenre(): List<Pair<String, List<Recommendation>>> {
    val devolver = ArrayList<Pair<String, List<Recommendation>>>()
    devolver.add(Pair("Rock", obtainPopularSongs()))
    devolver.add(Pair("Pop", obtainPopularSongs()))
    devolver.add(Pair("Rap", obtainPopularSongs()))
    devolver.add(Pair("Trap", obtainPopularSongs()))
    return devolver
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