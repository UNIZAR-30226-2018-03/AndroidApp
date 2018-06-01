/**
 * Created by abel
 * On 7/03/18.
 */
package com.spreadyourmusic.spreadyourmusic.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.spreadyourmusic.spreadyourmusic.media.common.MusicProviderSource
import com.spreadyourmusic.spreadyourmusic.helpers.media.MediaIDHelper

/**
 * Constructor usado cuando se generan datos desde el dispositivo
 */
class Song(val name: String, var locationUri: String, val album: Album, val genere: String?, val lyricsPath: String?) : Recommendation {
    var id: Long = 0
    var isDownloaded = false

    private val shareLinkPath = "http://155.210.13.105:8006/song?id="
    override var shareLink: String = shareLinkPath

    /**
     * Constructor usado cuando se obtienen datos desde el back-end
     */
    constructor(id: Long, name: String, locationUri: String, album: Album, genere: String?, lyricsPath: String?)
            : this(name, locationUri, album, genere, lyricsPath) {
        this.id = id
        this.shareLink = shareLinkPath + id.toString() + "/"
    }

    fun getMediaItem(): MediaBrowserCompat.MediaItem {
        return createMediaItem(getMetadata())
    }

    fun getMetadata(): MediaMetadataCompat {
        return MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id.toString())
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, locationUri)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album.name)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, album.creator.username)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, album.artLocationUri)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .build()
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
}