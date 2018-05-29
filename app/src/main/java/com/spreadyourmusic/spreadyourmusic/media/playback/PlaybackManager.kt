package com.spreadyourmusic.spreadyourmusic.media.playback

import android.os.Bundle
import android.os.ResultReceiver
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.spreadyourmusic.spreadyourmusic.media.queue.MusicQueueManager

import com.spreadyourmusic.spreadyourmusic.services.MusicService

/**
 * Manage the interactions among the container service, the queue manager and the actual playback.
 */
class PlaybackManager(private val mServiceCallback: PlaybackServiceCallback,
        // Action to thumbs up a media item
                      private val mMusicQueueManager: MusicQueueManager,
                      val playback: Playback?) : Playback.Callback {
    private val mMediaSessionCallback: MediaSessionCallback

    val mediaSessionCallback: MediaSessionCompat.Callback
        get() = mMediaSessionCallback

    private val availableActions: Long
        get() {
            var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            actions = if (playback!!.isPlaying()!!) {
                actions or PlaybackStateCompat.ACTION_PAUSE
            } else {
                actions or PlaybackStateCompat.ACTION_PLAY
            }
            return actions
        }

    init {
        mMediaSessionCallback = MediaSessionCallback()
        this.playback!!.setCallback(this)
    }

    /**
     * Handle a request to play music
     */
    fun handlePlayRequest() {
        val currentMusic = mMusicQueueManager.currentMusic
        if (currentMusic != null) {
            mServiceCallback.onPlaybackStart()
            playback!!.play(currentMusic, mMusicQueueManager.currentSong!!)
        }
    }

    /**
     * Handle a request to pause music
     */
    fun handlePauseRequest() {
        if (playback!!.isPlaying()!!) {
            playback.pause()
            mServiceCallback.onPlaybackStop()
        }
    }

    /**
     * Handle a request to stop music
     *
     * @param withError Error message in case the stop has an unexpected cause. The error
     * message will be set in the PlaybackState and will be visible to
     * MediaController clients.
     */
    fun handleStopRequest(withError: String?) {
        playback!!.stop(true)
        mServiceCallback.onPlaybackStop()
        updatePlaybackState(withError)
    }

    /**
     * Handle a request to play music
     */
    private fun handleReplayRequest() {
        playback!!.replay()
    }




    /**
     * Update the current media player state, optionally showing an error message.
     *
     * @param error if not null, error message to present to the user.
     */
    fun updatePlaybackState(error: String?) {
        var position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN
        if (playback != null && playback.isConnected()!!) {
            position = playback.currentStreamPosition()!!
        }


        val stateBuilder = PlaybackStateCompat.Builder()
                .setActions(availableActions)

        var state = playback!!.getState()

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error)
            state = PlaybackStateCompat.STATE_ERROR
        }

        stateBuilder.setState(state!!, position, 1.0f, SystemClock.elapsedRealtime())

        // Set the activeQueueItemId if the current index is valid.
        val currentMusic = mMusicQueueManager.currentMusic
        if (currentMusic != null) {
            stateBuilder.setActiveQueueItemId(currentMusic.queueId)
        }

        mServiceCallback.onPlaybackStateUpdated(stateBuilder.build())

        if (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_PAUSED) {
            mServiceCallback.onNotificationRequired()
        }
    }

    /**
     * Implementation of the Playback.Callback interface
     */
    override fun onCompletion() {
        // The media player finished playing the current song, so we go ahead
        // and start the next.
        if (mMusicQueueManager.skipQueuePosition(1) && mMusicQueueManager.musicQueueSize() == 1L) {
            handleReplayRequest()
        } else if (mMusicQueueManager.skipQueuePosition(1)) {
            handlePlayRequest()
            mMusicQueueManager.updateMetadata()
        } else {
            // If skipping was not possible, we stop and release the resources:
            handleStopRequest(null)
        }
    }

    override fun onPlaybackStatusChanged(state: Int) {
        updatePlaybackState(null)
    }

    override fun onError(error: String) {
        updatePlaybackState(error)
    }

    private inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            handlePlayRequest()
        }

        override fun onSkipToQueueItem(queueId: Long) {
            mMusicQueueManager.setCurrentQueueItem(queueId)
            mMusicQueueManager.updateMetadata()
        }

        override fun onSeekTo(position: Long) {
            playback!!.seekTo(position.toInt().toLong())
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            handlePlayRequest()
        }

        override fun onPause() {
            handlePauseRequest()
        }

        override fun onStop() {
            handleStopRequest(null)
        }

        override fun onSkipToNext() {
            if (mMusicQueueManager.skipQueuePosition(1)) {
                handlePlayRequest()
            } else {
                handleStopRequest("Cannot skip")
            }
            mMusicQueueManager.updateMetadata()
        }

        override fun onSkipToPrevious() {
            if (mMusicQueueManager.skipQueuePosition(-1)) {
                handlePlayRequest()
            } else {
                handleStopRequest("Cannot skip")
            }
            mMusicQueueManager.updateMetadata()
        }

        override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {
            super.onCommand(command, extras, cb)
            val dataToSend = Bundle()
            if (command == MusicService.CMD_AUDIO_SESSION) {
                playback!!.getAudioSessionId {
                    dataToSend.putInt(MusicService.CMD_AUDIO_SESSION, it)
                    cb?.send(0, dataToSend)
                }
            } else if (command == MusicService.CMD_SONG_DURATION) {
                playback!!.getDuration {
                    dataToSend.putLong(MusicService.CMD_SONG_DURATION, it)
                    cb?.send(0, dataToSend)
                }
            }

        }
    }


    interface PlaybackServiceCallback {
        fun onPlaybackStart()

        fun onNotificationRequired()

        fun onPlaybackStop()

        fun onPlaybackStateUpdated(newState: PlaybackStateCompat)
    }
}
