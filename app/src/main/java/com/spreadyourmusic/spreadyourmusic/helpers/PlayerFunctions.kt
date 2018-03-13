package com.spreadyourmusic.spreadyourmusic.helpers

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import java.util.*

/**
 * Created by abel
 * On 8/03/18.
 */

fun getCurrentSong(context: Context):Song{
    val autor1 = User("Trey Songz")
    val album = Album("TREMAINE", context)
    val cancion1 = Song(album = album, id = 1, nombre = "Animal", fecha = GregorianCalendar(2014,12,12), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    return cancion1
}

fun getCurrentSongList(context: Context):List<Song>{
    val devolver = ArrayList<Song>()
    val autor1 = User("Autorrrrr")
    val album = Album("PRUEBA", context)
    val cancion1 = Song(album = album, id = 1, nombre = "Cancion1", fecha = GregorianCalendar(2014,12,12), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    val cancion2 = Song(album = album, id = 2, nombre = "Cancion2", fecha = GregorianCalendar(2017,12,12), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    devolver.add(cancion1)
    devolver.add(cancion2)
    return devolver
}

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
