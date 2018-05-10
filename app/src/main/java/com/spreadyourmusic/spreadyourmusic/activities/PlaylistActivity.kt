package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
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
import android.widget.Toast
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
    var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        val playlistId = intent.getLongExtra(resources.getString(R.string.playlist_id), 0)

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

        val image = findViewById<ImageView>(R.id.image)

        val creatorName = findViewById<TextView>(R.id.creatorUsername)

        val followers = findViewById<TextView>(R.id.numOfFollowersTextView)

        followButton = findViewById(R.id.followButton)

        obtainPlaylistFromID(playlistId,this,{
            if(it!= null){
                playlist = it
                supportActionBar!!.title = playlist!!.name

                recyclerViewAdapter.changeData(playlist!!.content)
                Glide.with(this).load(playlist!!.artLocationUri).into(image)

                // Lista de reproducción creada por usuario
                val sCreatorName = resources.getString(R.string.creator) + ":@" + playlist!!.creator.username

                creatorName.text = sCreatorName

                obtainNumberOfFollowers(it,this,{
                    val sNumFollowers = it.toString() + " " + resources.getString(R.string.followers)
                    followers.text = sNumFollowers
                })

                obtainCurrentUserData({
                    if (!playlist!!.creator.username.equals(it!!.username)) {
                        isFollowing(it,this,{
                            followButton!!.text = if (!it) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
                        })
                    } else {
                        followButton!!.text = resources.getString(R.string.edit)
                    }
                },this)

            }else{
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                finish()
            }

            if(mMenu != null){
                // Los datos del usuario no habian llegado aun cuando se creo el menu
                onCreateOptionsMenu(mMenu)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        obtainCurrentUserData({
            if(playlist != null){
                if (!playlist!!.creator.username.equals(it!!.username)) {
                    mInflater.inflate(R.menu.menu_playlist, menu)
                }
            }else{
                mMenu = menu
            }
        }, this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!= null && item.itemId == R.id.share){
            shareElement(playlist!!.shareLink, this)
           return true
        }
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
        obtainCurrentUserData({
            if (!playlist!!.creator.username.equals(it!!.username)) {
                isFollowing(playlist!!, this, {
                    val anterior = it
                    changeFollowState(playlist!!, !it, this, {
                        if (it)
                            followButton!!.text = if (anterior) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
                    })
                })
            } else {
                val intent = Intent(this, CreatePlaylistActivity::class.java)
                intent.putExtra(resources.getString(R.string.playlist_id), playlist!!.id)
                startActivity(intent)
            }
        }, this)
    }

}
