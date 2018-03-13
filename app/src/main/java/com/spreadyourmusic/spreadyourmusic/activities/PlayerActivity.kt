package com.spreadyourmusic.spreadyourmusic.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.helpers.changeToNextSong
import com.spreadyourmusic.spreadyourmusic.helpers.changeToPreviousSong
import com.spreadyourmusic.spreadyourmusic.helpers.getCurrentSong
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.*


class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
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

        // Instanciacion media player
        mediaPlayer = MediaPlayer()
        prepareCurrentSong()

        playPauseImageButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseImageButton.setImageResource(R.drawable.ic_pause_white_24dp)
            } else {
                mediaPlayer.start()
                playPauseImageButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }
        }

        nextSongImageButton.setOnClickListener {
            val estadoAnterior = mediaPlayer.isPlaying
            changeToNextSong(this)
            prepareCurrentSong()

            if (estadoAnterior) {
                mediaPlayer.start()
                playPauseImageButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            } else {
                mediaPlayer.pause()
                playPauseImageButton.setImageResource(R.drawable.ic_pause_white_24dp)
            }
        }

        previousSongImageButton.setOnClickListener {
            val estadoAnterior = mediaPlayer.isPlaying
            changeToPreviousSong(this)
            prepareCurrentSong()

            if (estadoAnterior) {
                mediaPlayer.start()
                playPauseImageButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            } else {
                mediaPlayer.pause()
                playPauseImageButton.setImageResource(R.drawable.ic_pause_white_24dp)
            }
        }
    }

    fun prepareCurrentSong() {
        val cancionActual = getCurrentSong(this)
        songNameTextView.text = cancionActual.nombre
        val creatorplusalbum = cancionActual.creador.nombre + " | " + cancionActual.album.nombre + " , " + Integer.toString(cancionActual.fecha.get(Calendar.YEAR))
        songCreatorTextView.text = creatorplusalbum

        playerBackGroundImageView.setImageBitmap(cancionActual.album.getCover())
        albumArtCircularMusicProgressBar.setImageBitmap(cancionActual.album.getCover())
/*
        val url = "https://upload.wikimedia.org/wikipedia/commons/d/d5/Pop_Goes_the_Weasel.ogg"
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playPauseImageButton.isEnabled = true
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
