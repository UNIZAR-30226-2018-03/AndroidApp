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
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class TrendsFragment : Fragment() {
    private var mSongSelectedListener: (Song) -> Unit = {}
    private var mUserSelectedListener: (User) -> Unit = {}
    private var mPlaylistSelectedListener: (Playlist) -> Unit = {}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_trends, container, false)
        val listaTrends = view.findViewById<RecyclerView>(R.id.popularNowRecyclerView)
        val trendsRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)

        val listaPopularInMyCountry = view.findViewById<RecyclerView>(R.id.popularInMyCountryRecyclerView)
        val popularInMyCountryRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)

        val listaPopulares = view.findViewById<RecyclerView>(R.id.popularInTheWorldRecyclerView)
        val popularesRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)

        listaTrends.adapter = trendsRecyclerViewAdapter
        listaTrends.setHasFixedSize(true)

        listaPopularInMyCountry.adapter = popularInMyCountryRecyclerViewAdapter
        listaPopularInMyCountry.setHasFixedSize(true)

        listaPopulares.adapter = popularesRecyclerViewAdapter
        listaPopulares.setHasFixedSize(true)

        listaTrends.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaTrends.itemAnimator = DefaultItemAnimator()

        listaPopularInMyCountry.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaPopularInMyCountry.itemAnimator = DefaultItemAnimator()

        listaPopulares.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaPopulares.itemAnimator = DefaultItemAnimator()

        trendsRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        trendsRecyclerViewAdapter.changeData(obtainTrendSongs())

        popularesRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        popularesRecyclerViewAdapter.changeData(obtainPopularSongs())

        popularInMyCountryRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        popularInMyCountryRecyclerViewAdapter.changeData(obtainTrendInMyCountry())

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(mmSongSelectedListener: (Song) -> Unit, mmUserSelectedListener: (User) -> Unit,
                        mmPlaylistSelectedListener: (Playlist) -> Unit): TrendsFragment {

            val fragment = TrendsFragment()
            fragment.mSongSelectedListener = mmSongSelectedListener
            fragment.mUserSelectedListener = mmUserSelectedListener
            fragment.mPlaylistSelectedListener = mmPlaylistSelectedListener
            return fragment
        }
    }
}// Required empty public constructor
