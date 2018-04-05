package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.obtainPlaylistFromID
import com.spreadyourmusic.spreadyourmusic.controller.onPlaylistSelected
import com.spreadyourmusic.spreadyourmusic.controller.onSongSelected
import com.spreadyourmusic.spreadyourmusic.controller.onUserSelected
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class PlaylistActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val playlist = obtainPlaylistFromID(23)

        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = playlist.name

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val lista = findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerViewAdapter = RecomendationsVerticalRecyclerViewAdapter(this)

        lista.adapter = recyclerViewAdapter
        lista.setHasFixedSize(true)

        lista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lista.itemAnimator = DefaultItemAnimator()

        recyclerViewAdapter.setOnClickListener(onRecomendationSelected)
        recyclerViewAdapter.changeData(playlist.content)

    }

    fun onDoFollow(view: View) {

    }

    private val onRecomendationSelected: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongSelected(it, this)
            is User -> onUserSelected(it)
            is Playlist -> onPlaylistSelected(it)
        }
    }

}
