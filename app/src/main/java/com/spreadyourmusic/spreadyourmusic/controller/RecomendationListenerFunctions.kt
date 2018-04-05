package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.media.session.MediaControllerCompat
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.activities.PlayerActivity
import com.spreadyourmusic.spreadyourmusic.activities.PlaylistActivity
import com.spreadyourmusic.spreadyourmusic.activities.SystemlistActivity
import com.spreadyourmusic.spreadyourmusic.activities.UserActivity
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

fun onSongFromPlaylistSelected(song: Song,playlist: Playlist, activity: Activity) {
    val mediaController = MediaControllerCompat.getMediaController(activity)
    MusicQueueManager.getInstance().setCurrentQueue(song.name, playlist.content,  playlist.content.indexOf(song))
    mediaController.transportControls
            .playFromMediaId(song.getMediaItem().mediaId, null)
}

fun onUserSelected(user: User, activity: Activity) {
    val intent = Intent(activity, UserActivity::class.java)
    intent.putExtra(activity.resources.getString(R.string.user_id), user.username)
    activity.startActivity(intent)
}

fun onPlaylistSelected(playlist: Playlist, activity: Activity) {
    val intent = Intent(activity, PlaylistActivity::class.java)
    intent.putExtra(activity.resources.getString(R.string.playlist_id), playlist.id)
    activity.startActivity(intent)
}

fun onSystemListSelected(id: Int, activity: Activity){
    val intent = Intent(activity, SystemlistActivity::class.java)
    intent.putExtra(activity.resources.getString(R.string.system_list_id), id)
    activity.startActivity(intent)
}