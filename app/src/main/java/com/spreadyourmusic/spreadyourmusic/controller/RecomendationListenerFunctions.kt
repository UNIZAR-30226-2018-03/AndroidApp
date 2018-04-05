package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.support.v4.media.session.MediaControllerCompat
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

fun onSongSelected(song: Song, activity: Activity) {
    val mediaController = MediaControllerCompat.getMediaController(activity)
    MusicQueueManager.getInstance().setCurrentQueue(song.name, song)
    mediaController.transportControls
            .playFromMediaId(song.getMediaItem().mediaId, null)
}

fun onUserSelected(user: User) {
    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

fun onPlaylistSelected(playlist: Playlist) {
    /*
    No se ha de reproducir ahora la musica, este codigo se ha de copiar en el playlist
    val mediaController = MediaControllerCompat.getMediaController(this)
     MusicQueueManager.getInstance().setCurrentQueue(it.name,it.content)
     mediaController.transportControls
             .playFromMediaId(MusicQueueManager.getInstance().currentSong.getMediaItem().mediaId, null)*/

    //TODO: Abrir playlist
    /* val intent = Intent(this, PlaylistActivity::class.java)
     startActivity(intent)*/
}