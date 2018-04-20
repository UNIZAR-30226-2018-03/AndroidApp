package com.spreadyourmusic.spreadyourmusic.test

import com.spreadyourmusic.spreadyourmusic.models.*
import java.util.*
import kotlin.collections.ArrayList

object ServerEmulator {
    val songList = HashMap<Long, Song>()
    val userList = HashMap<String, User>()
    val playlistList = HashMap<Long, Playlist>()

    val cancionesFavoritas = HashMap<String, ArrayList<Song>>()
    val artistasSeguidos = HashMap<String, ArrayList<User>>()
    val playlistSeguidos = HashMap<String, ArrayList<Playlist>>()

    val recomendaciones = HashMap<String, ArrayList<Recommendation>>()
    val trends = ArrayList<Song>()

    val generos = ArrayList<Pair<String, List<Recommendation>>>()

    init {
        // TODO: HACER
        val autor1 = User("Media", "Media", "Right", "http://storage.googleapis.com/automotive-media/album_art.jpg")
        val autor2 = User("Silent", "Silent", "Partner", "http://storage.googleapis.com/automotive-media/album_art.jpg")
        val autor3 = User("abelcht", "Abel ChT", "Lion", "http://storage.googleapis.com/automotive-media/album_art.jpg")

        val album1 = Album("Jazz", autor1, GregorianCalendar(2018, 3, 22), "http://storage.googleapis.com/automotive-media/album_art.jpg")
        val album2 = Album("Blues", autor2, GregorianCalendar(2017, 6, 27), "http://storage.googleapis.com/automotive-media/album_art_2.jpg")

        val cancion1 = Song(album = album1, id = 1, name = "Jazz in Paris", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 103000L, locationUri = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3")
        val cancion2 = Song(album = album2, id = 2, name = "The Messenger", numOfLikes = 15, numOfViews = 16, collaborators = null, duration = 132000L, locationUri = "http://storage.googleapis.com/automotive-media/The_Messenger.mp3")

        val list1 = ArrayList<Song>()
        list1.add(cancion1)
        list1.add(cancion2)

        val list2 = ArrayList<Song>()
        list2.add(cancion1)

        val playlist1 = Playlist(1, "dfsdfdsfd", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", list1)
        val playlist2 = Playlist(2, "dfsdfdsfd", autor1, "http://storage.googleapis.com/automotive-media/album_art_2.jpg", list2)

        userList[autor1.username!!] = autor1
        userList[autor2.username!!] = autor2
        userList[autor3.username!!] = autor3

        songList[cancion1.id] = cancion1
        songList[cancion2.id] = cancion2

        playlistList[playlist1.id] = playlist1
        playlistList[playlist2.id] = playlist2


        artistasSeguidos[autor3.username!!] = ArrayList()
        artistasSeguidos[autor3.username!!]!!.add(autor1)
        artistasSeguidos[autor3.username!!]!!.add(autor2)
        artistasSeguidos[autor3.username!!]!!.add(autor3)

        cancionesFavoritas[autor3.username!!] = ArrayList()
        cancionesFavoritas[autor3.username!!]!!.add(cancion1)
        cancionesFavoritas[autor3.username!!]!!.add(cancion2)


        playlistSeguidos[autor3.username!!] = ArrayList()
        playlistSeguidos[autor3.username!!]!!.add(playlist1)
        playlistSeguidos[autor3.username!!]!!.add(playlist2)

        recomendaciones[autor3.username!!] = ArrayList()
        recomendaciones[autor3.username!!]!!.add(playlist1)
        recomendaciones[autor3.username!!]!!.add(playlist2)
        recomendaciones[autor3.username!!]!!.add(autor2)
        recomendaciones[autor3.username!!]!!.add(autor1)
        recomendaciones[autor3.username!!]!!.add(cancion1)
        recomendaciones[autor3.username!!]!!.add(cancion2)

        trends.add(cancion1)
        trends.add(cancion2)
        generos.add(Pair("Rock", trends))


    }
}