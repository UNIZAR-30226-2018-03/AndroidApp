package com.spreadyourmusic.spreadyourmusic.controller

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
fun obtainRecommendations(): List<Recommendation> {
    val devolver = ArrayList<Recommendation>()

    val autor1 = User("Media Right Productions", "Media", "Right")
    val autor2 = User("Silent Partner","Silent", "Partner")

    val album1 = Album("Jazz", autor1, GregorianCalendar(2018,3,22), "http://storage.googleapis.com/automotive-media/album_art.jpg")
    val album2= Album("Blues", autor2, GregorianCalendar(2017,6,27), "http://storage.googleapis.com/automotive-media/album_art_2.jpg")

    val cancion1 = Song(album = album1, id = 1, name = "Jazz in Paris", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 103000L, locationUri = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3" )
    val cancion2 = Song(album = album2, id = 2, name = "The Messenger", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 132000L, locationUri = "http://storage.googleapis.com/automotive-media/The_Messenger.mp3")
    devolver.add(autor1)
    devolver.add(cancion1)
    devolver.add(cancion2)
    return devolver
}

fun obtainPopularSongs(): List<Recommendation>{
    return obtainRecommendations()
}

fun obtainNewsSongs(): List<Recommendation>{
    return obtainPopularSongs()
}
