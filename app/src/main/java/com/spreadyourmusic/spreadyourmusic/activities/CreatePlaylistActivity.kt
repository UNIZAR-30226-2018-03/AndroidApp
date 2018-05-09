package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.obtainSongsFromQuery
import com.spreadyourmusic.spreadyourmusic.models.Song
import kotlinx.android.synthetic.main.content_create_playlist.*
import kotlin.collections.ArrayList

class CreatePlaylistActivity : AppCompatActivity() {
    var pathPortada: String? = null
    val selectPortada: Int = 455

    private lateinit var recyclerViewAdapterSelected: RecomendationsVerticalRecyclerViewAdapter
    private lateinit var recyclerViewAdapterBrowsed: RecomendationsVerticalRecyclerViewAdapter

    private var selectedList: ArrayList<Song> = ArrayList()

    private var browsedList: ArrayList<Song> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setTitle(R.string.create_playlist)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerViewAdapterSelected = RecomendationsVerticalRecyclerViewAdapter(this)

        selectedRecyclerView.adapter = recyclerViewAdapterSelected
        selectedRecyclerView.setHasFixedSize(true)

        selectedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        selectedRecyclerView.itemAnimator = DefaultItemAnimator()


        recyclerViewAdapterBrowsed = RecomendationsVerticalRecyclerViewAdapter(this)

        browsedRecyclerView.adapter = recyclerViewAdapterBrowsed
        browsedRecyclerView.setHasFixedSize(true)

        browsedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        browsedRecyclerView.itemAnimator = DefaultItemAnimator()


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
}

