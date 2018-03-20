package com.spreadyourmusic.spreadyourmusic.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.spreadyourmusic.spreadyourmusic.media.MusicProviderSource
import com.spreadyourmusic.spreadyourmusic.helpers.media.MediaIDHelper
import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */
class Song(public val id: Long, public val nombre: String, public val album: Album, public val fecha: Calendar, public val numeroDeVisualizaciones: Long, public val numeroDeFavoritos: Long, public val creador: User) : Recommendation {

    var mMediaMetadataCompat : MediaMetadataCompat

    init {
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

        mMediaMetadataCompat =
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
                        .build()
    }

    fun isDownloaded() : Boolean{
        return false
    }
    fun getMediaItem() : MediaBrowserCompat.MediaItem{
        return createMediaItem(getMetadata())
    }

    fun getMetadata() : MediaMetadataCompat{

        return mMediaMetadataCompat
    }

    fun setMetadata(m : MediaMetadataCompat){
        mMediaMetadataCompat = m
    }

    private fun createMediaItem(metadata: MediaMetadataCompat): MediaBrowserCompat.MediaItem {
        // Since mediaMetadata fields are immutable, we need to create a copy, so we
        // can set a hierarchy-aware mediaID. We will need to know the media hierarchy
        // when we get a onPlayFromMusicID call, so we can create the proper queue based
        // on where the music was selected from (by artist, by genre, random, etc)
        val genre = metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE)
        val hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                metadata.description.mediaId)
        val copy = MediaMetadataCompat.Builder(metadata)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                .build()
        return MediaBrowserCompat.MediaItem(copy.description,
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)

    }
}