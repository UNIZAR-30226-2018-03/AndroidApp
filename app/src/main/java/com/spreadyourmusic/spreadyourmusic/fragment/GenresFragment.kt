package com.spreadyourmusic.spreadyourmusic.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.NamedListRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class GenresFragment : Fragment() {
    private var mSongSelectedListener: (Song) -> Unit = {}
    private var mUserSelectedListener: (User) -> Unit = {}
    private var mPlaylistSelectedListener: (Playlist) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_recyclerview, container, false)

        val lista = view.findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerViewAdapter = NamedListRecyclerViewAdapter(context)

        lista.adapter = recyclerViewAdapter
        lista.setHasFixedSize(true)

        lista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lista.itemAnimator = DefaultItemAnimator()

        recyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        recyclerViewAdapter.changeData(obtainPopularByGenre())

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(mmSongSelectedListener: (Song) -> Unit, mmUserSelectedListener: (User) -> Unit,
                        mmPlaylistSelectedListener: (Playlist) -> Unit): GenresFragment {

            val fragment = GenresFragment()
            fragment.mSongSelectedListener = mmSongSelectedListener
            fragment.mUserSelectedListener = mmUserSelectedListener
            fragment.mPlaylistSelectedListener = mmPlaylistSelectedListener
            return fragment
        }
    }
}// Required empty public constructor
