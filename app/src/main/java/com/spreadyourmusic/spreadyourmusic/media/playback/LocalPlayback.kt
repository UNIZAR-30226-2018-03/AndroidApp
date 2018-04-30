package com.spreadyourmusic.spreadyourmusic.media.playback

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.util.MimeTypes
import com.spreadyourmusic.spreadyourmusic.media.lyrics.LyricsManager
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.media.MusicProviderSource
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import android.support.v4.media.session.MediaSessionCompat.QueueItem
import android.util.Log
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA

/**
 * This class control com.google.android.exoplayer2.ExoPlayer
 */
class LocalPlayback(context: Context, private val mMusicQueueManager: MusicQueueManager) : Playback {

    private val mContext: Context?
    private val mWifiLock: WifiManager.WifiLock?
    private var mPlayOnFocusGain: Boolean = false
    private var mCallback: Playback.Callback? = null
    private var mCurrentMediaId: String? = null

    private var mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
    private val mAudioManager: AudioManager
    private var mExoPlayer: SimpleExoPlayer? = null
    private val mEventListener = PlayerEventListener()
    private val mTextOutputListener = TextOutputListener()
    private val mAudioSessionIdListener = AudioSessionIdListener()

    // Whether to return STATE_NONE or STATE_STOPPED when mExoPlayer is null;
    private var mExoPlayerNullIsStopped = false

    var songDurationListener: ((Long) -> Unit)? = null

    var songDuration = C.TIME_UNSET
    var audioSessionId = 0


    override fun getState(): Int? {
        if (mExoPlayer == null) {
            return if (mExoPlayerNullIsStopped)
                PlaybackStateCompat.STATE_STOPPED
            else
                PlaybackStateCompat.STATE_NONE
        }
        return when (mExoPlayer!!.playbackState) {
            Player.STATE_IDLE -> PlaybackStateCompat.STATE_PAUSED
            Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            Player.STATE_READY -> if (mExoPlayer!!.playWhenReady)
                PlaybackStateCompat.STATE_PLAYING
            else
                PlaybackStateCompat.STATE_PAUSED
            Player.STATE_ENDED -> PlaybackStateCompat.STATE_PAUSED
            else -> PlaybackStateCompat.STATE_NONE
        }
    }

    override fun isConnected(): Boolean = true

    override fun isPlaying(): Boolean? = mPlayOnFocusGain || mExoPlayer != null && mExoPlayer!!.playWhenReady

    override fun currentStreamPosition(): Long? = if (mExoPlayer != null) mExoPlayer!!.currentPosition else 0

    private val mOnAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> mCurrentAudioFocusState = AUDIO_FOCUSED
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Lost audio focus, but will gain it back (shortly), so note whether
                // playback should resume
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
                mPlayOnFocusGain = mExoPlayer != null && mExoPlayer!!.playWhenReady
            }
            AudioManager.AUDIOFOCUS_LOSS ->
                // Lost audio focus, probably "permanently"
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }

        if (mExoPlayer != null) {
            // Update the player state based on the change
            configurePlayerState()
        }
    }

    init {
        val applicationContext = context.applicationContext
        this.mContext = applicationContext

        this.mAudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        this.mWifiLock = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "sym_lock")
    }

    override fun start() {
        // Nothing to do
    }

    override fun stop(notifyListeners: Boolean) {
        giveUpAudioFocus()
        releaseResources(true)
    }

    override fun play(item: QueueItem, song: Song) {
        mPlayOnFocusGain = true
        tryToGetAudioFocus()
        val mediaId = item.description.mediaId
        val mediaHasChanged = !TextUtils.equals(mediaId, mCurrentMediaId)
        if (mediaHasChanged) {
            mCurrentMediaId = mediaId
        }

        if (mediaHasChanged || mExoPlayer == null) {
            songDuration = C.TIME_UNSET

            releaseResources(false) // release everything except the player
            val track = mMusicQueueManager.currentMusicMetadata

            var source: String? = track!!.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE)
            if (source != null) {
                source = source.replace(" ".toRegex(), "%20") // Escape spaces for URLs
            }

            if (mExoPlayer == null) {
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        DefaultRenderersFactory(mContext), DefaultTrackSelector(), DefaultLoadControl())
                mExoPlayer!!.addListener(mEventListener)
                mExoPlayer!!.addAudioDebugListener(mAudioSessionIdListener)
            }

            // Set music type
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build()
            mExoPlayer!!.audioAttributes = audioAttributes

            // Produces DataSource instances through which media data is loaded.
            val dataSourceFactory = DefaultDataSourceFactory(
                    mContext, Util.getUserAgent(mContext, "sym"))

            // The MediaSource represents the media to be played
            var mediaSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(source))

            if (song.lyricsPath != null) {
                val format = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en")

                val subtitleUri = Uri.parse(song.lyricsPath)
                val subtitleMediaSource = SingleSampleMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(subtitleUri, format, C.TIME_UNSET)

                mediaSource = MergingMediaSource(mediaSource, subtitleMediaSource)
                mExoPlayer!!.addTextOutput(mTextOutputListener)
            }

            // Prepares media to play (happens on background thread) and triggers
            // {@code onPlayerStateChanged} callback when the stream is ready to play.
            mExoPlayer!!.prepare(mediaSource)

            // If we are streaming from the internet, we want to hold a
            // Wifi lock, which prevents the Wifi radio from going to
            // sleep while the song is playing.
            mWifiLock!!.acquire()
        }

        configurePlayerState()
    }

    override fun replay() {
        seekTo(0)
    }

    override fun pause() {
        // Pause player and cancel the 'foreground service' state.
        if (mExoPlayer != null) {
            mExoPlayer!!.playWhenReady = false
        }
        // While paused, retain the player instance, but give up audio focus.
        releaseResources(false)
    }

    override fun seekTo(position: Long) {
        if (mExoPlayer != null) {
            mExoPlayer!!.seekTo(position)
        }
    }

    override fun setCallback(callback: Playback.Callback) {
        this.mCallback = callback
    }


    private fun tryToGetAudioFocus() {
        val result = mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_FOCUSED
        } else {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    private fun giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    /**
     * Reconfigures the player according to audio focus settings and starts/restarts it. This method
     * starts/restarts the ExoPlayer instance respecting the current audio focus state. So if we
     * have focus, it will play normally; if we don't have focus, it will either leave the player
     * paused or set it to a low volume, depending on what is permitted by the current focus
     * settings.
     */
    private fun configurePlayerState() {
        if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
            // We don't have audio focus and can't duck, so we have to pause
            pause()
        } else {

            if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
                // We're permitted to play, but only if we 'duck', ie: play softly
                mExoPlayer!!.volume = VOLUME_DUCK
            } else {
                mExoPlayer!!.volume = VOLUME_NORMAL
            }

            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                mExoPlayer!!.playWhenReady = true
                mPlayOnFocusGain = false
            }
        }
    }

    /**
     * Releases resources used by the service for playback, which is mostly just the WiFi lock for
     * local playback. If requested, the ExoPlayer instance is also released.
     *
     * @param releasePlayer Indicates whether the player should also be released
     */
    private fun releaseResources(releasePlayer: Boolean) {
        // Stops and releases player (if requested and available).
        if (releasePlayer && mExoPlayer != null) {
            mExoPlayer!!.release()
            mExoPlayer!!.removeListener(mEventListener)
            mExoPlayer = null
            mExoPlayerNullIsStopped = true
            mPlayOnFocusGain = false
        }

        if (mWifiLock!!.isHeld) {
            mWifiLock!!.release()
        }
    }

    override fun getAudioSessionId(l: (Int) -> Unit) {
        l(audioSessionId)
    }

    override fun getDuration(l: (Long) -> Unit) {
        // No funciona correctamente al cambiar de canción
        if (songDuration != C.TIME_UNSET) {
            l(songDuration)
        } else
            songDurationListener = l
    }

    private inner class TextOutputListener : TextOutput {

        override fun onCues(cues: List<Cue>?) {
            if (cues != null && cues.size == 1) {
                val letra = cues[0].text.toString()
                LyricsManager.onTextChange(letra)
            }

        }
    }

    private inner class AudioSessionIdListener : AudioRendererEventListener {

        override fun onAudioEnabled(counters: DecoderCounters) {

        }

        override fun onAudioSessionId(audioSessionId: Int) {
            this@LocalPlayback.audioSessionId = audioSessionId
        }

        override fun onAudioDecoderInitialized(decoderName: String, initializedTimestampMs: Long, initializationDurationMs: Long) {

        }

        override fun onAudioInputFormatChanged(format: Format) {

        }

        override fun onAudioSinkUnderrun(bufferSize: Int, bufferSizeMs: Long, elapsedSinceLastFeedMs: Long) {

        }

        override fun onAudioDisabled(counters: DecoderCounters) {

        }
    }

    private inner class PlayerEventListener : Player.EventListener {

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            if (Player.TIMELINE_CHANGE_REASON_DYNAMIC == reason) {
                songDuration = mExoPlayer!!.duration
                if (songDuration != C.TIME_UNSET && songDurationListener != null) {
                    songDurationListener!!.invoke(songDuration)
                    songDurationListener = null
                }
            }
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}

        override fun onLoadingChanged(isLoading: Boolean) {}

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE, Player.STATE_BUFFERING, Player.STATE_READY -> if (mCallback != null) {
                    mCallback!!.onPlaybackStatusChanged(getState()!!)
                }
                Player.STATE_ENDED ->
                    // The media player finished playing the current song.
                    if (mCallback != null) {
                        mCallback!!.onCompletion()
                    }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {

        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            if (error != null) {
                val what: String? = when (error.type) {
                    ExoPlaybackException.TYPE_SOURCE -> error.sourceException.message
                    ExoPlaybackException.TYPE_RENDERER -> error.rendererException.message
                    ExoPlaybackException.TYPE_UNEXPECTED -> error.unexpectedException.message
                    else -> "Unknown: $error"
                }

                if (mCallback != null) {
                    mCallback!!.onError("ExoPlayer error $what")
                }
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {

        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

        }

        override fun onSeekProcessed() {

        }
    }

    companion object {

        // The volume we set the media player to when we lose audio focus, but are
        // allowed to reduce the volume instead of stopping playback.
        private val VOLUME_DUCK = 0.2f
        // The volume we set the media player when we have audio focus.
        private val VOLUME_NORMAL = 1.0f

        // we don't have audio focus, and can't duck (play at a low volume)
        private val AUDIO_NO_FOCUS_NO_DUCK = 0
        // we don't have focus, but can duck (play at a low volume)
        private val AUDIO_NO_FOCUS_CAN_DUCK = 1
        // we have full audio focus
        private val AUDIO_FOCUSED = 2
    }
}
