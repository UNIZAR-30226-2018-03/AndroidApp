package com.spreadyourmusic.spreadyourmusic.activities

import android.content.ComponentName
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
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

import android.widget.TextView
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.services.MusicService


import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.circularprogressbar.CircularMusicProgressBar
import com.spreadyourmusic.spreadyourmusic.circularprogressbar.OnCircularSeekBarChangeListener
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager

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

    private lateinit var albumArtCircularMusicProgressBar: CircularMusicProgressBar
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

    // Control seek bar
    private var onProgessChangedControl = false
    private var onLastProgessChanged = 0

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
                connectToSession(mMediaBrowser!!.sessionToken)
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


        mPauseDrawable = ContextCompat.getDrawable(this, R.drawable.ic_pause_circle_filled_white_24dp)
        mPlayDrawable = ContextCompat.getDrawable(this, R.drawable.ic_play_circle_filled_white_24dp)



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
            override fun onStartTrackingTouch(seekBar: CircularMusicProgressBar?) {
                onProgessChangedControl = false

                // Mientras se modifica el seekbar, este no es actualizado mediante programa
                val state = MediaControllerCompat.getMediaController(this@PlayerActivity).playbackState
                if (state != null) {
                    if(state.state == PlaybackStateCompat.STATE_PLAYING ||
                            state.state == PlaybackStateCompat.STATE_BUFFERING ){
                        stopSeekbarUpdate()
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: CircularMusicProgressBar?) {
                if(onProgessChangedControl){
                    MediaControllerCompat.getMediaController(this@PlayerActivity).transportControls.seekTo((songDuration.toFloat() * (onLastProgessChanged.toFloat() / 100)).toLong())
                }

                // Tras modificarse se permite que se siga actualizando
                val state = MediaControllerCompat.getMediaController(this@PlayerActivity).playbackState
                if (state != null) {
                    if(state.state == PlaybackStateCompat.STATE_PLAYING ||
                            state.state == PlaybackStateCompat.STATE_BUFFERING ){
                        scheduleSeekbarUpdate()
                    }
                }
            }

            override fun onProgressChanged(circularBar: CircularMusicProgressBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    onProgessChangedControl = true
                    onLastProgessChanged = progress
                    val currentPosition = (songDuration * progress).toFloat() / 100f
                    startTimeTextView.text = DateUtils.formatElapsedTime((currentPosition / 1000f).toLong())
                }
            }
        })

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

        updateProgress()
        if (state != null && (state.state == PlaybackStateCompat.STATE_PLAYING || state.state == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate()
        }
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

    private fun updateMediaDescription(description: MediaDescriptionCompat?) {
        if (description == null) {
            return
        }
        val currentSong = getCurrentSong()

        songNameTextView.text = currentSong.name
        val songCreatorS = currentSong.album.name + " | " + currentSong.album.creator.username
        songCreatorTextView.text = songCreatorS

        val artUrl = description.iconUri!!.toString()
        Glide.with(this).load(artUrl).into(playerBackGroundImageView)
        Glide.with(this).load(artUrl).into(albumArtCircularMusicProgressBar)

        val randomReproductionImageButton :ImageButton = findViewById(R.id.randomReproduction)
        randomReproductionImageButton.alpha = if(isRandomReproductionEnabled()) 1f else 0.5f
        val favoriteSongImageButton:ImageButton = findViewById(R.id.favoriteSong)
        favoriteSongImageButton.alpha = if(isCurrentSongFavorite()) 1f else 0.5f
        val downloadOrDeleteSongImageButton:ImageButton = findViewById(R.id.downloadOrDeleteSong)
        downloadOrDeleteSongImageButton.alpha = if(isCurrentSongDownloaded()) 1f else 0.5f
    }

    private fun updateDuration(metadata: MediaMetadataCompat?) {
        if (metadata == null) {
            return
        }
        val duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
        songDuration = duration
        finalTimeTextView.text = DateUtils.formatElapsedTime((duration / 1000f).toLong())
    }

    private fun updatePlaybackState(state: PlaybackStateCompat?) {
        if (state == null) {
            return
        }
        mLastPlaybackState = state

        when (state.state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                playPauseImageButton.alpha = 1F
                playPauseImageButton.setImageDrawable(mPauseDrawable)
                scheduleSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                playPauseImageButton.alpha = 1F
                playPauseImageButton.setImageDrawable(mPlayDrawable)
                stopSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> {
                playPauseImageButton.alpha = 1F
                playPauseImageButton.setImageDrawable(mPlayDrawable)
                stopSeekbarUpdate()
            }
            PlaybackStateCompat.STATE_BUFFERING -> {
                playPauseImageButton.alpha = 0.5F
                stopSeekbarUpdate()
            }
        }

        nextSongImageButton.visibility = if (MusicQueueManager.getInstance().currentQueueSize == 1)
            View.INVISIBLE
        else
            View.VISIBLE

        previousSongImageButton.visibility = nextSongImageButton.visibility
    }

    private var lastProgress = 0

    private fun updateProgress() {
        if (mLastPlaybackState == null) {
            return
        }
        var currentPosition = (mLastPlaybackState as PlaybackStateCompat).position
        if ((mLastPlaybackState as PlaybackStateCompat).state == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            val timeDelta = SystemClock.elapsedRealtime() - (mLastPlaybackState as PlaybackStateCompat).lastPositionUpdateTime
            currentPosition += (timeDelta.toInt() * (mLastPlaybackState as PlaybackStateCompat).playbackSpeed).toLong()
        }
        startTimeTextView.text = DateUtils.formatElapsedTime((currentPosition.toFloat() / 1000).toLong())
        val progress = (currentPosition.toFloat() / songDuration.toFloat()) * 100
        if (progress.toInt() != lastProgress) {
            lastProgress = progress.toInt()
            albumArtCircularMusicProgressBar.setValue(progress)
        }
    }

    // handle the share songs button's click
    fun shareSong(v:View){
        shareCurrentSong(this)
    }

    // handle the add song to favourite button's click
    fun addSongToFavourite(v:View){
        setFavoriteCurrentSong(!isCurrentSongFavorite())
        val favoriteSongImageButton:ImageButton = findViewById(R.id.favoriteSong)
        favoriteSongImageButton.alpha = if(isCurrentSongFavorite()) 1f else 0.5f
    }

    // handle the random download button's click
    fun downloadSong(v:View){
        downloadCurrentSong(!isCurrentSongDownloaded())
        val downloadOrDeleteSongImageButton:ImageButton = findViewById(R.id.downloadOrDeleteSong)
        downloadOrDeleteSongImageButton.alpha = if(isCurrentSongDownloaded()) 1f else 0.5f
    }

    // handle the random reproduction button's click
    fun randReproduction(v:View){
        randomReproduction(!isRandomReproductionEnabled())
        val randomReproductionImageButton :ImageButton = findViewById(R.id.randomReproduction)
        randomReproductionImageButton.alpha = if(isRandomReproductionEnabled()) 1f else 0.5f
    }
}
