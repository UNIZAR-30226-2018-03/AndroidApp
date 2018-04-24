package com.spreadyourmusic.spreadyourmusic.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.spreadyourmusic.spreadyourmusic.media.MusicProviderSource
import com.spreadyourmusic.spreadyourmusic.helpers.media.MediaIDHelper

/**
 * Created by abel
 * On 7/03/18.
 */
// TODO:
class Song(val id: Long, val name: String, var locationUri: String, val duration: Long, val album: Album, val genere:String?, val lyricsPath:String?) : Recommendation {

    var isDownloaded = false

    private var mMediaMetadataCompat: MediaMetadataCompat = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id.toString())
            .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, locationUri)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album.name)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, album.creator.username)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, album.artLocationUri)
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
            .build()


    fun getMediaItem(): MediaBrowserCompat.MediaItem {
        return createMediaItem(getMetadata())
    }

    fun getMetadata(): MediaMetadataCompat {
        return mMediaMetadataCompat
    }

    fun setMetadata(m: MediaMetadataCompat) {
        mMediaMetadataCompat = m
    }

    private fun createMediaItem(metadata: MediaMetadataCompat): MediaBrowserCompat.MediaItem {
        // Since mediaMetadata fields are immutable, we need to create a copy, so we
        // can set a hierarchy-aware mediaID. We will need to know the media hierarchy
        // when we get a onPlayFromMusicID call, so we can create the proper queue based
        // on where the music was selected from (by artist, by genre, random, etc)
        val hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                metadata.description.mediaId)
        val copy = MediaMetadataCompat.Builder(metadata)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                .build()
        return MediaBrowserCompat.MediaItem(copy.description,
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)

    }

    fun getShareLink(): String{
        //TODO: El link devuelto ha de ser el que apunta a la misma cancion desde la interfaz web
        return "https://www.google.es/"
    }
}