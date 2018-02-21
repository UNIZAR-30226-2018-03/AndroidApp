package com.spreadyourmusic.spreadyourmusic.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.widget.Button
import com.spreadyourmusic.spreadyourmusic.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import com.spreadyourmusic.spreadyourmusic.R.id.imageView
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.item_player_bg.*
import java.net.URL


class PlayerActivity : AppCompatActivity() {
   // private lateinit var mediaPlayer:MediaPlayer
   // private lateinit var controlButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
       /* Thread{
            val url = URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464")
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            runOnUiThread ({
                player_background.setImageBitmap(bmp)
            })
        }.start()*/


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
