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
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsHorizontalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.obtainNewsSongs
import com.spreadyourmusic.spreadyourmusic.controller.obtainUpdatedPlaylists
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class NewsFragment : Fragment() {
    private var mSongSelectedListener: (Song) -> Unit = {}
    private var mUserSelectedListener: (User) -> Unit = {}
    private var mPlaylistSelectedListener: (Playlist) -> Unit = {}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_novedades, container, false)
        val listaPlaylistUpdatesRecomendaciones = view.findViewById<RecyclerView>(R.id.playlistUpdatesRecyclerView)
        val playlistUpdatesRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)

        val listaNewSongsNovedades = view.findViewById<RecyclerView>(R.id.newSongsRecyclerView)
        val newSongsRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)


        listaPlaylistUpdatesRecomendaciones.adapter = playlistUpdatesRecyclerViewAdapter
        listaPlaylistUpdatesRecomendaciones.setHasFixedSize(true)

        listaNewSongsNovedades.adapter = newSongsRecyclerViewAdapter
        listaNewSongsNovedades.setHasFixedSize(true)

        listaPlaylistUpdatesRecomendaciones.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaPlaylistUpdatesRecomendaciones.itemAnimator = DefaultItemAnimator()

        listaNewSongsNovedades.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaNewSongsNovedades.itemAnimator = DefaultItemAnimator()


        playlistUpdatesRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        playlistUpdatesRecyclerViewAdapter.changeData(obtainUpdatedPlaylists())

        newSongsRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        newSongsRecyclerViewAdapter.changeData(obtainNewsSongs())

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(mmSongSelectedListener: (Song) -> Unit, mmUserSelectedListener: (User) -> Unit,
                        mmPlaylistSelectedListener: (Playlist) -> Unit): NewsFragment {

            val fragment = NewsFragment()
            fragment.mSongSelectedListener = mmSongSelectedListener
            fragment.mUserSelectedListener = mmUserSelectedListener
            fragment.mPlaylistSelectedListener = mmPlaylistSelectedListener
            return fragment
        }
    }
}// Required empty public constructor
