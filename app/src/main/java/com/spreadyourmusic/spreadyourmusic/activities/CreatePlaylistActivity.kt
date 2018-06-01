package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.helpers.getPathFromUri
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import kotlinx.android.synthetic.main.content_create_playlist.*
import kotlinx.android.synthetic.main.activity_create_playlist.*
import kotlin.collections.ArrayList

class CreatePlaylistActivity : AppCompatActivity() {
    private var pathCover: String? = null
    private val selectCover: Int = 455

    private lateinit var recyclerViewAdapterSelected: RecomendationsVerticalRecyclerViewAdapter
    private lateinit var recyclerViewAdapterBrowsed: RecomendationsVerticalRecyclerViewAdapter

    private var selectedList: ArrayList<Song> = ArrayList()

    private var browsedList: ArrayList<Song> = ArrayList()

    private var playlistId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        playlistId = intent.getLongExtra(resources.getString(R.string.playlist_id), 0)

        if (playlistId != 0L) {
            toolbar.setTitle(R.string.update_playlist)
            createButton.setText(R.string.update)
        } else
            toolbar.setTitle(R.string.create_playlist)

        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerViewAdapterSelected = RecomendationsVerticalRecyclerViewAdapter(this)

        selectedSongsRecyclerView.adapter = recyclerViewAdapterSelected
        selectedSongsRecyclerView.setHasFixedSize(true)

        selectedSongsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        selectedSongsRecyclerView.itemAnimator = DefaultItemAnimator()


        recyclerViewAdapterBrowsed = RecomendationsVerticalRecyclerViewAdapter(this)

        browsedSongsRecyclerView.adapter = recyclerViewAdapterBrowsed
        browsedSongsRecyclerView.setHasFixedSize(true)

        browsedSongsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        browsedSongsRecyclerView.itemAnimator = DefaultItemAnimator()


        recyclerViewAdapterSelected.setOnClickListener({ orgSong ->
            if (orgSong is Song) {
                selectedList.removeAll({
                    orgSong.id == it.id
                })
                recyclerViewAdapterSelected.changeData(selectedList)
            }
        })

        recyclerViewAdapterBrowsed.setOnClickListener({ orgSong ->
            if (orgSong is Song) {
                val notContains = selectedList.all {
                    it.id != orgSong.id
                }
                if (notContains) {
                    selectedList.add(orgSong)
                    recyclerViewAdapterSelected.changeData(selectedList)
                }
            }
        })

        if (playlistId != 0L) {
            obtainPlaylistFromID(playlistId, this, {
                if (it != null) {
                    pathCover = it.artLocationUri
                    if (pathCover != null)
                        Glide.with(this).load(pathCover).into(coverCircleImageView)
                    nameEditText.setText(it.name)
                    for (i in it.content) {
                        selectedList.add(i)
                    }
                    recyclerViewAdapterSelected.changeData(selectedList)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        mInflater.inflate(R.menu.menu_home, menu)

        if (menu != null) {
            val searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        obtainSongsFromQuery(query, this@CreatePlaylistActivity, {
                            browsedList.clear()
                            if (it != null) {
                                for (i in it) {
                                    if (i is Song) {
                                        browsedList.add(i)
                                    }
                                }
                                recyclerViewAdapterBrowsed.changeData(browsedList)
                            }
                        })
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectCover) {
            pathCover = getPathFromUri(this, data!!.data)

            if (pathCover != null) {
                Glide.with(this).load(pathCover).into(coverCircleImageView)
            }
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.select_valid_file, Toast.LENGTH_SHORT).show()
    }

    fun onPictureClick(v: View) {
        val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file)), selectCover)
    }

    fun onCreateClick(v: View) {
        val nombre = nameEditText.text.toString().trim()
        if (pathCover.isNullOrEmpty() || nombre.isEmpty() || selectedList.size == 0) {
            Toast.makeText(applicationContext, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, R.string.empty_fields_4, Toast.LENGTH_SHORT).show()
        } else {
            obtainCurrentUserData({
                if (playlistId != 0L) {
                    Toast.makeText(this, R.string.updating, Toast.LENGTH_SHORT).show()
                    val newPlaylist = Playlist(playlistId, nombre, it!!, pathCover!!, selectedList)
                    updatePlaylist(newPlaylist, this, { error ->
                        if (error != null) {
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        } else {
                            finish()
                        }
                    })
                } else {
                    Toast.makeText(this, R.string.creating, Toast.LENGTH_SHORT).show()
                    val newPlaylist = Playlist(nombre, it!!, pathCover!!, selectedList)
                    createPlaylist(newPlaylist, this, { error, _ ->
                        if (error != null) {
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                        } else {
                            finish()
                        }
                    })
                }
            }, this)

        }
    }
}

