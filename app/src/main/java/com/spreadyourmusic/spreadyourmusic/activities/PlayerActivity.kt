package com.spreadyourmusic.spreadyourmusic.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import android.widget.Button
import com.spreadyourmusic.spreadyourmusic.R


class PlayerActivity : AppCompatActivity() {
   // private lateinit var mediaPlayer:MediaPlayer
   // private lateinit var controlButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
/*
        mediaPlayer = MediaPlayer()
        controlButton = findViewById<View>(R.id.button) as Button

        val url = "https://upload.wikimedia.org/wikipedia/commons/d/d5/Pop_Goes_the_Weasel.ogg"
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            controlButton.isEnabled = true
        }

        controlButton.setOnClickListener{
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
                controlButton.text = getString(R.string.play)
            }else{
                mediaPlayer.start()
                controlButton.text = getString(R.string.pause)
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        //mediaPlayer.release()
    }
}
