package com.spreadyourmusic.spreadyourmusic.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.android.uamp.model.MusicProviderSource
import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */
class Song(public val id: Long, public val nombre: String, public val album: Album, public val fecha: Calendar, public val numeroDeVisualizaciones: Long, public val numeroDeFavoritos: Long, public val creador: User) : Recommendation {
    fun isDownloaded() : Boolean{
        return false
    }
    fun getMediaItem() : MediaBrowserCompat.MediaItem{
        val title1 = "Jazz in Paris"
        val album1 = "Jazz & Blues"
        val artist1 = "Media Right Productions"
        val genre1 = "Jazz & Blues"
        var source1 = "http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3"
        var iconUrl1 = "http://storage.googleapis.com/automotive-media/album_art.jpg"
        val id1 = source1.hashCode().toString()
        val duration1 = 103000L
        val trackNumber1 = 1L
        val totalTrackCount1 = 1L


        val metadata =
        MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id1)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source1)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album1)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist1)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration1)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre1)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl1)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title1)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber1)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount1)
                .build();


        val  mediaItem : MediaBrowserCompat.MediaItem
        return "https://upload.wikimedia.org/wikipedia/commons/d/d5/Pop_Goes_the_Weasel.ogg"
    }
}