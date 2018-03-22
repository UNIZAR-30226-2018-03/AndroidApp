package com.spreadyourmusic.spreadyourmusic.activities

import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ImageView

import android.widget.TextView
import com.spreadyourmusic.spreadyourmusic.media.AlbumArtCache
import com.spreadyourmusic.spreadyourmusic.services.MusicService


import com.spreadyourmusic.spreadyourmusic.R
import info.abdolahi.CircularMusicProgressBar
import info.abdolahi.OnCircularSeekBarChangeListener
import kotlinx.android.synthetic.main.app_bar_home.*

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class PlayerActivity : AppCompatActivity() {

    private val PROGRESS_UPDATE_INTERNAL: Long = 1000
    private val PROGRESS_UPDATE_INITIAL_INTERVAL: Long = 100

    private lateinit var playPauseImageButton: ImageButton
    private lateinit var nextSongImageButton: ImageButton
    private lateinit var previousSongImageButton: ImageButton
    private lateinit var randomReproductionImageButton: ImageButton
    private lateinit var downloadOrDeleteSongImageButton: ImageButton
    private lateinit var favoriteSongImageButton: ImageButton
    private lateinit var shareSongImageButton: ImageButton

    private lateinit var startTimeTextView: TextView
    private lateinit var finalTimeTextView: TextView

    private lateinit var albumArtCircularMusicProgressBar: info.abdolahi.CircularMusicProgressBar
    private lateinit var playerBackGroundImageView: ImageView
    private lateinit var songCreatorTextView: TextView
    private lateinit var songNameTextView: TextView

    private var mPauseDrawable: Drawable? = null
    private var mPlayDrawable: Drawable? = null


    private val mHandler = Handler()
    private var mMediaBrowser: MediaBrowserCompat? = null

    private val mUpdateProgressTask = Runnable { updateProgress() }

    private val mExecutorService = Executors.newSingleThreadScheduledExecutor()

    private var mScheduleFuture: ScheduledFuture<*>? = null
    private var mLastPlaybackState: PlaybackStateCompat? = null

    private var songDuration: Int = 100

    private val mCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            updatePlaybackState(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (metadata != null) {
                updateMediaDescription(metadata.description)
                updateDuration(metadata)
            }
        }
    }

    private val mConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                connectToSession(mMediaBrowser!!.getSessionToken())
            } catch (e: RemoteException) {
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbar)

        // Linkado de la interfaz a objetos
        songCreatorTextView = findViewById(R.id.songAuthor)
        songNameTextView = findViewById(R.id.songName)
        playerBackGroundImageView = findViewById(R.id.player_background)
        albumArtCircularMusicProgressBar = findViewById(R.id.album_art)

        playPauseImageButton = findViewById(R.id.playPauseSong)
        nextSongImageButton = findViewById(R.id.nextSong)
        previousSongImageButton = findViewById(R.id.previousSong)

        randomReproductionImageButton = findViewById(R.id.randomReproduction)
        downloadOrDeleteSongImageButton = findViewById(R.id.downloadOrDeleteSong)
        favoriteSongImageButton = findViewById(R.id.favoriteSong)
        shareSongImageButton = findViewById(R.id.shareSong)

        startTimeTextView = findViewById(R.id.startTime)
        finalTimeTextView = findViewById(R.id.finalTime)


        mPauseDrawable = ContextCompat.getDrawable(this, R.drawable.ic_pause_white_24dp)
        mPlayDrawable = ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_white_24dp)



        nextSongImageButton.setOnClickListener({
            val controls = MediaControllerCompat.getMediaController(this).transportControls
            controls.skipToNext()
        })

        previousSongImageButton.setOnClickListener({
            val controls = MediaControllerCompat.getMediaController(this).transportControls
            controls.skipToPrevious()
        })

        playPauseImageButton.setOnClickListener({
            val state = MediaControllerCompat.getMediaController(this).playbackState
            if (state != null) {
                val controls = MediaControllerCompat.getMediaController(this).transportControls
                when (state.state) {
                    PlaybackStateCompat.STATE_PLAYING // fall through
                        , PlaybackStateCompat.STATE_BUFFERING -> {
                        controls.pause()
                        stopSeekbarUpdate()
                    }
                    PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                        controls.play()
                        scheduleSeekbarUpdate()
                    }
                }
            }
        })

        albumArtCircularMusicProgressBar.setOnCircularBarChangeListener(object : OnCircularSeekBarChangeListener {
            override fun onProgressChanged(circularBar: CircularMusicProgressBar?, progress: Int, fromUser: Boolean) {
               // startTimeTextView.text = DateUtils.formatElapsedTime(((progress.toFloat() / 100) * songDuration.toFloat()).toLong())
            }

            override fun onClick(circularBar: CircularMusicProgressBar?) {
            }

            override fun onLongPress(circularBar: CircularMusicProgressBar?) {
            }
        })

        // Only update from the intent if we are not recreating from a config change:
        if (savedInstanceState == null) {
            updateFromParams(intent)
        }

       mMediaBrowser = MediaBrowserCompat(this,
                ComponentName(this, MusicService::class.java), mConnectionCallback, null)

    }


    @Throws(RemoteException::class)
    private fun connectToSession(token: MediaSessionCompat.Token) {
        val mediaController = MediaControllerCompat(
                this, token)
        if (mediaController.metadata == null) {
            finish()
            return
        }
        MediaControllerCompat.setMediaController(this, mediaController)
        mediaController.registerCallback(mCallback)
        val state = mediaController.playbackState
        updatePlaybackState(state)
        val metadata = mediaController.metadata
        if (metadata != null) {
            updateMediaDescription(metadata.description)
            updateDuration(metadata)
        }
        //updateProgress()
        if (state != null && (state.state == PlaybackStateCompat.STATE_PLAYING || state.state == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate()
        }
    }

    private fun updateFromParams(intent: Intent?) {
        /*if (intent != null) {
           val description = intent.getParcelableExtra<MediaDescriptionCompat>(
                  MusicPlayerActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION)
            if (description != null) {
                updateMediaDescription(description)
            }
        }*/
    }

    private fun scheduleSeekbarUpdate() {
        stopSeekbarUpdate()
        if (!mExecutorService.isShutdown) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    { mHandler.post(mUpdateProgressTask) }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS)
        }
    }

    private fun stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            (mScheduleFuture as ScheduledFuture<*>).cancel(false)
        }
    }

    public override fun onStart() {
        super.onStart()
        if (mMediaBrowser != null) {
            (mMediaBrowser as MediaBrowserCompat).connect()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (mMediaBrowser != null) {
            mMediaBrowser!!.disconnect()
        }
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mCallback)
    }

    public override fun onDestroy() {
        super.onDestroy()
        stopSeekbarUpdate()
        mExecutorService.shutdown()
    }

    private fun fetchImageAsync(description: MediaDescriptionCompat) {
        if (description.iconUri == null) {
            return
        }
        val artUrl = description.iconUri!!.toString()
        AlbumArtCache.instance.getBigImage(artUrl, {url, bitmap ->
            if (artUrl == url) {
                playerBackGroundImageView.setImageBitmap(bitmap)
                albumArtCircularMusicProgressBar.setImageBitmap(bitmap)
            }
        })
    }

    private fun updateMediaDescription(description: MediaDescriptionCompat?) {
        if (description == null) {
            return
        }
        songNameTextView.text = description.title
        songCreatorTextView.text = description.subtitle
        fetchImageAsync(description)
    }

    private fun updateDuration(metadata: MediaMetadataCompat?) {
        if (metadata == null) {
            return
        }
        val duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
        songDuration = duration
        finalTimeTextView.text = DateUtils.formatElapsedTime((duration / 1000).toLong())
    }

    private fun updatePlaybackState(state: PlaybackStateCompat?) {
        if (state == null) {
            return
        }
        mLastPlaybackState = state

        when (state.state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                playPauseImageButton.setVisibility(VISIBLE)
                playPauseImageButton.setImageDrawable(mPauseDrawable)
                scheduleSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                playPauseImageButton.setVisibility(VISIBLE)
                playPauseImageButton.setImageDrawable(mPlayDrawable)
                stopSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> {
                playPauseImageButton.setVisibility(VISIBLE)
                playPauseImageButton.setImageDrawable(mPlayDrawable)
                stopSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                playPauseImageButton.setVisibility(INVISIBLE)
                stopSeekbarUpdate()
            }
        }

        nextSongImageButton.setVisibility(if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT == 0L)
            INVISIBLE
        else
            VISIBLE)
        previousSongImageButton.setVisibility(if (state.actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS == 0L)
            INVISIBLE
        else
            VISIBLE)
    }

    private fun updateProgress() {
        if (mLastPlaybackState == null) {
            return
        }
        var currentPosition = (mLastPlaybackState as PlaybackStateCompat).getPosition()
        if ((mLastPlaybackState as PlaybackStateCompat).getState() == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            val timeDelta = SystemClock.elapsedRealtime() - (mLastPlaybackState as PlaybackStateCompat).getLastPositionUpdateTime()
            currentPosition += (timeDelta.toInt() * (mLastPlaybackState as PlaybackStateCompat).getPlaybackSpeed()).toLong()
        }
        startTimeTextView.text = DateUtils.formatElapsedTime((currentPosition.toFloat() / 1000).toLong())
        albumArtCircularMusicProgressBar.setValue((currentPosition.toFloat()/songDuration.toFloat()) * 100)
    }
}
