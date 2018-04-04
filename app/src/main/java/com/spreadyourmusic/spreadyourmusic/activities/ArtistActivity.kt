package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spreadyourmusic.spreadyourmusic.R
import android.support.v7.widget.*


class ArtistActivity : AppCompatActivity() {
    private val lstLista: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        //App bar
        val toolbar = findViewById(R.id.main_appbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Mi Aplicación")

        //RecyclerView
    }

}
