package com.spreadyourmusic.spreadyourmusic.controller

import com.spreadyourmusic.spreadyourmusic.models.*
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

    val cancion3 = Song(album = album2, id = 2, name = "Malabar", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 710000L, locationUri = "http://155.210.13.105:7480/TEST/malabar.mp3")

    val listaCanciones = ArrayList<Song>()
    listaCanciones.add(cancion1)
    listaCanciones.add(cancion2)

    val playlist = Playlist("Lista", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", listaCanciones)
    devolver.add(autor1)
    devolver.add(cancion2)
    devolver.add(cancion1)
    devolver.add(playlist)
    devolver.add(autor2)
    devolver.add(cancion3)
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

    val cancion3 = Song(album = album2, id = 2, name = "Malabar", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 710000L, locationUri = "http://155.210.13.105:7480/TEST/malabar.mp3")

    val listaCanciones = ArrayList<Song>()
    listaCanciones.add(cancion1)
    listaCanciones.add(cancion2)

    val playlist = Playlist("Lista", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", listaCanciones)

    devolver.add(playlist)
    return devolver
}