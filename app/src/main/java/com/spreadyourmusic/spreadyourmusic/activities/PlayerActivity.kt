package com.spreadyourmusic.spreadyourmusic.activities

import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.services.MusicService


import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.view.circularprogressbar.CircularMusicProgressBar
import com.spreadyourmusic.spreadyourmusic.view.circularprogressbar.OnCircularSeekBarChangeListener
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.media.lyrics.LyricsManager
import com.spreadyourmusic.spreadyourmusic.media.queue.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.view.soundvisualizer.CircleSoundVisualizer

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {

    private val progressUpdateInterval: Long = 1000
    private val progressUpdateInitialInterval: Long = 100

    private lateinit var playPauseImageButton: ImageButton
    private lateinit var nextSongImageButton: ImageButton
    private lateinit var previousSongImageButton: ImageButton
    private lateinit var randomReproductionImageButton: ImageButton
    private lateinit var downloadOrDeleteSongImageButton: ImageButton
    private lateinit var favoriteSongImageButton: ImageButton
    private lateinit var lyricsImageButton: ImageButton

    private lateinit var startTimeTextView: TextView
    private lateinit var finalTimeTextView: TextView

    private lateinit var albumArtCircularMusicProgressBar: CircularMusicProgressBar
    private lateinit var circleSoundVisualizer: CircleSoundVisualizer
    private lateinit var playerBackGroundImageView: ImageView
    private lateinit var songCreatorTextView: TextView
    private lateinit var songNameTextView: TextView
    private lateinit var lyricsTextView: TextView


    private var isLyricsShowed = false

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
                // updateDuration(metadata)
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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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

        lyricsImageButton = findViewById(R.id.viewLyrics)

        startTimeTextView = findViewById(R.id.startTime)
        finalTimeTextView = findViewById(R.id.finalTime)

        lyricsTextView = findViewById(R.id.lyricsTextView)


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
                    if (state.state == PlaybackStateCompat.STATE_PLAYING ||
                            state.state == PlaybackStateCompat.STATE_BUFFERING) {
                        stopSeekbarUpdate()
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: CircularMusicProgressBar?) {
                if (onProgessChangedControl) {
                    MediaControllerCompat.getMediaController(this@PlayerActivity).transportControls.seekTo((songDuration.toFloat() * (onLastProgessChanged.toFloat() / 100)).toLong())
                }

                // Tras modificarse se permite que se siga actualizando
                val state = MediaControllerCompat.getMediaController(this@PlayerActivity).playbackState
                if (state != null) {
                    if (state.state == PlaybackStateCompat.STATE_PLAYING ||
                            state.state == PlaybackStateCompat.STATE_BUFFERING) {
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

        isLyricsShowed = false

        circleSoundVisualizer = findViewById(R.id.visualizer)

        // set custom color to the line of the visualizer
        circleSoundVisualizer.setColor(Color.WHITE)

        randomReproductionImageButton.setOnClickListener {
            randReproduction()
        }

        downloadOrDeleteSongImageButton.setOnClickListener {
            downloadSong()
        }

        favoriteSongImageButton.setOnClickListener {
            addSongToFavourite()
        }

        lyricsImageButton.setOnClickListener {
            showLyrics()
        }

        mMediaBrowser = MediaBrowserCompat(this,
                ComponentName(this, MusicService::class.java), mConnectionCallback, null)

        if (!isRandomReproductionEnabled()) {
            randomReproductionImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_trending_flat_white_24dp))
        } else randomReproductionImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_shuffle_white_24dp))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        mInflater.inflate(R.menu.menu_player, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null) {
            when (item.itemId) {
                R.id.share -> {
                    shareSong()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else super.onOptionsItemSelected(item)
    }

    private val commandHandler = object : ResultReceiver(
            null) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            if (resultData != null) {
                val ausioSessionID = resultData.getInt(MusicService.CMD_AUDIO_SESSION, -1)
                val songDurationLocal = resultData.getLong(MusicService.CMD_SONG_DURATION, -1)
                if (ausioSessionID != -1) {
                    circleSoundVisualizer.setPlayer(ausioSessionID)
                }

                if (songDurationLocal != -1L) {
                    songDuration = songDurationLocal.toInt()
                    finalTimeTextView.text = DateUtils.formatElapsedTime((songDurationLocal / 1000f).toLong())
                }
            }
        }
    }

    @Throws(RemoteException::class)
    private fun connectToSession(token: MediaSessionCompat.Token) {
        val mMediaController = MediaControllerCompat(
                this, token)
        if (mMediaController.metadata == null) {
            finish()
            return
        }
        MediaControllerCompat.setMediaController(this, mMediaController)
        mMediaController.registerCallback(mCallback)
        val state = mMediaController.playbackState
        updatePlaybackState(state)
        val metadata = mMediaController.metadata
        if (metadata != null) {
            updateMediaDescription(metadata.description)
            //  updateDuration(metadata)
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
                    { mHandler.post(mUpdateProgressTask) }, progressUpdateInitialInterval,
                    progressUpdateInterval, TimeUnit.MILLISECONDS)
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
        LyricsManager.changeListener(lyricsListener)
    }

    public override fun onStop() {
        super.onStop()
        if (mMediaBrowser != null) {
            mMediaBrowser!!.disconnect()
        }
        circleSoundVisualizer.releasePlayer()
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mCallback)
        LyricsManager.changeListener(null)
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

        val artUrl = description.iconUri
        if (artUrl != null) {
            Glide.with(this).load(artUrl.toString()).into(playerBackGroundImageView)
            Glide.with(this).load(artUrl.toString()).into(albumArtCircularMusicProgressBar)
        }

        val favoriteSongImageButton: ImageButton = findViewById(R.id.favoriteSong)
        isCurrentSongFavorite(this, {
            if (it) {
                favoriteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp))
            } else favoriteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp))
        })

        if (currentSong.isDownloaded) {
            downloadOrDeleteSongImageButton.visibility = View.INVISIBLE
        } else {
            downloadOrDeleteSongImageButton.visibility = View.VISIBLE
            isCurrentSongDownloaded(this, {
                if (!it) {
                    downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_download_white_24dp))
                } else downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete_forever_white_24dp))
            })
        }

        if (currentSong.lyricsPath == null) {
            lyricsTextView.text = resources.getString(R.string.no_lyrics)
        } else lyricsTextView.text = ""

        MediaControllerCompat.getMediaController(this)!!.sendCommand(MusicService.CMD_AUDIO_SESSION, null, commandHandler)
        MediaControllerCompat.getMediaController(this)!!.sendCommand(MusicService.CMD_SONG_DURATION, null, commandHandler)

    }

    /* private fun updateDuration(metadata: MediaMetadataCompat?) {
         if (metadata == null) {
             return
         }
         val currentSong = getCurrentSong()
         val duration = currentSong.duration.toInt()
         songDuration = duration
         finalTimeTextView.text = DateUtils.formatElapsedTime((duration / 1000f).toLong())
     }*/

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
    private fun shareSong() {
        shareElement(getCurrentSong().shareLink, this)
    }

    private var lyricsListener: ((String) -> Unit) = {
        lyricsTextView.text = it
    }

    // handle the add song to favourite button's click
    private fun addSongToFavourite() {
        isCurrentSongFavorite(this, {
            val estado = it
            setFavoriteCurrentSong(!estado, this, {
                if (it) {
                    val actualState = !estado
                    if (actualState) {
                        favoriteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp))
                    } else favoriteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp))
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        })
    }

    // handle the random download button's click
    private fun downloadSong() {
        isCurrentSongDownloaded(this, {
            val estado = it
            downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_queue_white_24dp))
            downloadCurrentSong(!it, this, {
                if (it) {
                    if (estado) {
                        downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_download_white_24dp))
                    } else downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete_forever_white_24dp))

                } else {
                    if (!estado) {
                        downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_download_white_24dp))
                    } else downloadOrDeleteSongImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete_forever_white_24dp))

                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        })
    }

    // handle the random reproduction button's click
    private fun randReproduction() {
        randomReproduction(!isRandomReproductionEnabled())
        if (isRandomReproductionEnabled()) {
            randomReproductionImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_shuffle_white_24dp))
        } else randomReproductionImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_trending_flat_white_24dp))
    }

    private fun showLyrics() {
        if (isLyricsShowed) {
            albumArtCircularMusicProgressBar.visibility = View.VISIBLE
            startTimeTextView.visibility = View.VISIBLE
            finalTimeTextView.visibility = View.VISIBLE
            circleSoundVisualizer.visibility = View.VISIBLE
            lyricsTextView.visibility = View.INVISIBLE
            lyricsImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.outline_closed_caption_white_24))

        } else {
            albumArtCircularMusicProgressBar.visibility = View.INVISIBLE
            circleSoundVisualizer.visibility = View.INVISIBLE
            startTimeTextView.visibility = View.INVISIBLE
            finalTimeTextView.visibility = View.INVISIBLE
            lyricsTextView.visibility = View.VISIBLE
            lyricsImageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.round_closed_caption_white_24))

        }
        isLyricsShowed = !isLyricsShowed
    }
}
