package com.spreadyourmusic.spreadyourmusic.controller

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */

// TODO: La llamada en la aplicacion final no llevara el contexto
fun obtainRecommendations(context: Context): List<Recommendation> {
    val devolver = ArrayList<Recommendation>()
    val autor1 = User("Autorrrrr")
    val album = Album("PRUEBA", context)
    val cancion1 = Song(album = album, id = 1, nombre = "Cancion1", fecha = GregorianCalendar(), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    val cancion2 = Song(album = album, id = 2, nombre = "Cancion2", fecha = GregorianCalendar(), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    devolver.add(autor1)
    devolver.add(cancion1)
    devolver.add(cancion2)
    return devolver
}

fun obtainPopularSongs(context: Context): List<Recommendation>{
    val devolver = ArrayList<Recommendation>()
    val autor1 = User("Autorrrrr")
    val album = Album("PRUEBA", context)
    val cancion1 = Song(album = album, id = 1, nombre = "Cancion1", fecha = GregorianCalendar(), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    val cancion2 = Song(album = album, id = 2, nombre = "Cancion2", fecha = GregorianCalendar(), numeroDeFavoritos = 15, numeroDeVisualizaciones = 16, creador = autor1)
    devolver.add(cancion1)
    devolver.add(cancion2)
    return devolver
}

fun obtainNewsSongs(context: Context): List<Recommendation>{
    return obtainPopularSongs(context)
}
