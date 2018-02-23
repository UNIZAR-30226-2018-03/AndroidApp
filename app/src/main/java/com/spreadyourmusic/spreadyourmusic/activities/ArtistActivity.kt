package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.spreadyourmusic.spreadyourmusic.R
import kotlinx.android.synthetic.main.activity_artist.*

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)
        setSupportActionBar(toolbar)

    }
}
