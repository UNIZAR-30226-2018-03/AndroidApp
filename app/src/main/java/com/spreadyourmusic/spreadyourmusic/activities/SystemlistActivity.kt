package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

/**
 * Descripción: La siguiente clase se encarga de visualizar los artistas seguidos, las canciones añadidas
 * a favoritos y las playlist seguidas
 * */
class SystemlistActivity : BaseActivity() {
    var values: Pair<String, List<Recommendation>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_list)

        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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

        val systemPlaylistId = intent.getIntExtra(resources.getString(R.string.system_list_id), 0)
        obtainSystemGeneratedPlaylist(systemPlaylistId, this, {
            values = it
            supportActionBar!!.title = values!!.first
            recyclerViewAdapter.changeData(values!!.second)
        })

        val image = findViewById<ImageView>(R.id.coverImageView)

        obtainCurrentUserData({
            if (it != null)
                Glide.with(this).load(it.pictureLocationUri).into(image)
        }, this)
    }

    private val onRecomendationSelected: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongFromPlaylistSelected(it, Playlist(1, "", User(""), "", values!!.second as List<Song>), this)
            is User -> onUserSelected(it, this)
            is Playlist -> onPlaylistSelected(it, this)
        }
    }

}
