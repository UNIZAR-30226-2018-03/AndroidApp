package com.spreadyourmusic.spreadyourmusic.controller

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import java.util.*

/**
 * Created by abel
 * On 8/03/18.
 */
/*
fun getCurrentSong(context: Context):Song{
    val autor1 = User("Trey Songz")
    val album = Album("TREMAINE", context)
    val cancion1 = Song(album = album, id = 1, name = "Animal", releaseDate = GregorianCalendar(2014,12,12), numOfLikes = 15, numOfViews = 16, creator = autor1)
    return cancion1
}

fun getCurrentSongList(context: Context):List<Song>{
    val devolver = ArrayList<Song>()
    val autor1 = User("Autorrrrr")
    val album = Album("PRUEBA", context)
    val cancion1 = Song(album = album, id = 1, name = "Cancion1", releaseDate = GregorianCalendar(2014,12,12), numOfLikes = 15, numOfViews = 16, creator = autor1)
    val cancion2 = Song(album = album, id = 2, name = "Cancion2", releaseDate = GregorianCalendar(2017,12,12), numOfLikes = 15, numOfViews = 16, creator = autor1)
    devolver.add(cancion1)
    devolver.add(cancion2)
    return devolver
}*/

fun changeToNextSong(context: Context){

}

fun changeToPreviousSong(context: Context){

}

fun setFavoriteCurrentSong(context: Context, estado: Boolean){

}

// Si es true descarga la cancion, sino la elimina
fun downloadCurrentSong(context: Context, estado: Boolean){

}

// Si es true crea reproduccion aleatoria
fun randomReproduction(context: Context, estado: Boolean){

}
