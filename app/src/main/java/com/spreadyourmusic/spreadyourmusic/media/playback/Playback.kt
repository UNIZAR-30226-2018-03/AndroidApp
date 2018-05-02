package com.spreadyourmusic.spreadyourmusic.media.playback

import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.services.MusicService

import android.support.v4.media.session.MediaSessionCompat.QueueItem

/**
 * Interface representing either Local or Remote Playback. The [MusicService] works
 * directly with an instance of the Playback object to make the various calls such as
 * play, pause etc.
 */
interface Playback {


    /**
     * Get the current [android.media.session.PlaybackState.getState]
     */
    fun getState(): Int?

    /**
     * @return boolean that indicates that this is ready to be used.
     */
    fun isConnected(): Boolean?

    /**
     * @return boolean indicating whether the player is playing or is supposed to be
     * playing when we gain audio focus.
     */
    fun isPlaying(): Boolean?

    /**
     * @return pos if currently playing an item
     */
    fun currentStreamPosition(): Long?

    fun getAudioSessionId(l: (Int)->Unit)

    fun getDuration(l: (Long)->Unit)

    /**
     * Start/setup the playback.
     * Resources/listeners would be allocated by implementations.
     */
    fun start()

    /**
     * Stop the playback. All resources can be de-allocated by implementations here.
     * @param notifyListeners if true and a callback has been set by setCallback,
     * callback.onPlaybackStatusChanged will be called after changing
     * the state.
     */
    fun stop(notifyListeners: Boolean)

    /**
     * Queries the underlying stream and update the internal last known stream position.
     */

    fun play(item: QueueItem, song: Song)

    fun pause()

    fun replay()

    fun seekTo(position: Long)

    interface Callback {
        /**
         * On current music completed.
         */
        fun onCompletion()

        /**
         * on Playback status changed
         * Implementations can use this callback to update
         * playback state on the media sessions.
         */
        fun onPlaybackStatusChanged(state: Int)

        /**
         * @param error to be added to the PlaybackState
         */
        fun onError(error: String)
    }

    fun setCallback(callback: Callback)
}
