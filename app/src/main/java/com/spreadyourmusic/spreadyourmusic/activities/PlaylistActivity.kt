package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class PlaylistActivity : BaseActivity() {
    var playlist: Playlist? = null
    var followButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        val playlistId = intent.getIntExtra(resources.getString(R.string.playlist_id), 0)

        playlist = obtainPlaylistFromID(playlistId)


        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = playlist!!.name

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
        recyclerViewAdapter.changeData(playlist!!.content)

        val image = findViewById<ImageView>(R.id.image)

        Glide.with(this).load(playlist!!.artLocationUri).into(image)

        val creatorName = findViewById<TextView>(R.id.creatorUsername)

        val followers = findViewById<TextView>(R.id.numOfFollowersTextView)

        followButton = findViewById(R.id.followButton)

        // Lista de reproducción creada por usuario
        val sCreatorName = resources.getString(R.string.creator) + ":@" + playlist!!.creator.username

        creatorName.text = sCreatorName



      /*  val sNumFollowers = obtainNumberOfFollowers(playlist!!).toString() + " " + resources.getString(R.string.followers)

        followers.text = sNumFollowers

        if (!playlist!!.creator.username.equals(obtainCurrentUser().username)) {
            followButton!!.text = if (!isFollowing(playlist!!)) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
        } else {
            followButton!!.text = resources.getString(R.string.edit)
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater

        mInflater.inflate(R.menu.menu_playlist, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // TODO: Hacer
        return super.onOptionsItemSelected(item)
    }

    private val onRecomendationSelected: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongFromPlaylistSelected(it, playlist!!, this)
            is User -> onUserSelected(it, this)
            is Playlist -> onPlaylistSelected(it, this)
        }
    }

    fun onDoFollow(view: View) {
      /*  if (!playlist!!.creator.username.equals(obtainCurrentUser().username)) {
            changeFollowState(playlist!!, !isFollowing(playlist!!))
            followButton!!.text = if (!isFollowing(playlist!!)) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
        } else {
            // TODO: Hacer
        }*/
    }

}
