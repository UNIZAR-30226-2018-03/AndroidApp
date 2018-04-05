package com.spreadyourmusic.spreadyourmusic.controller

import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

/**
 * Created by abel
 * On 5/04/18.
 */

fun obtainUserFromID(id:Int): User {
    val autor2 = User("badbunny", "Box Bunny", "Partner", "http://storage.googleapis.com/automotive-media/album_art.jpg")
    return autor2
}

fun obtainPlaylistsFromUser (user:User): List<Playlist>{
    return obtainUpdatedPlaylists()
}

fun obtainSongsFromUser (user:User): List<Song>{
    return obtainNewsSongs()
}