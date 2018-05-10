package com.spreadyourmusic.spreadyourmusic.session

import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import java.util.HashMap

/**
 * Created by abel on 27/02/18.
 */

object SessionSingleton {
    /**
     * Almacena si todos los datos de usuario han sido cargados en currentUser
     */
    var isUserDataLoaded = false

    /**
     * Almacena el token de la sesión
     */
    var sessionToken: String? = null

    /**
     * Almacena el usuario logeado
     */
    var currentUser: User? = null

    /**
     * Almacena el QueueManager actual
     */
    val musicQueueManager: MusicQueueManager = MusicQueueManager.getInstance()

    /**
     * Almacena la última canción escuchada por el usuario en otraq sesión
     */
    var lastSongListened: Song? = null
}
