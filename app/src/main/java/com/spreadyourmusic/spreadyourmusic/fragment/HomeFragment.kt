package com.spreadyourmusic.spreadyourmusic.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsHomeRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.controller.obtainNewsSongs
import com.spreadyourmusic.spreadyourmusic.controller.obtainPopularSongs
import com.spreadyourmusic.spreadyourmusic.controller.obtainRecommendations
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class HomeFragment : Fragment() {
    private var mSongSelectedListener: (Song) -> Unit = {}
    private var mUserSelectedListener: (User) -> Unit = {}
    private var mPlaylistSelectedListener: (Playlist) -> Unit = {}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflaterD = inflater.inflate(R.layout.content_home, container, false)
        val listaRecomendaciones = inflaterD.findViewById<RecyclerView>(R.id.recommendationsRecyclerView)
        val recomendacionesRecyclerViewAdapter = RecomendationsHomeRecyclerViewAdapter()

        val listaNovedades = inflaterD.findViewById<RecyclerView>(R.id.newsRecyclerView)
        val novedadesRecyclerViewAdapter = RecomendationsHomeRecyclerViewAdapter()

        val listaPopulares = inflaterD.findViewById<RecyclerView>(R.id.popularRecyclerView)
        val popularesRecyclerViewAdapter = RecomendationsHomeRecyclerViewAdapter()

        listaRecomendaciones.adapter = recomendacionesRecyclerViewAdapter
        listaRecomendaciones.setHasFixedSize(true)

        listaNovedades.adapter = novedadesRecyclerViewAdapter
        listaNovedades.setHasFixedSize(true)

        listaPopulares.adapter = popularesRecyclerViewAdapter
        listaPopulares.setHasFixedSize(true)

        listaRecomendaciones.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaRecomendaciones.itemAnimator = DefaultItemAnimator()

        listaNovedades.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaNovedades.itemAnimator = DefaultItemAnimator()

        listaPopulares.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listaPopulares.itemAnimator = DefaultItemAnimator()

        recomendacionesRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        recomendacionesRecyclerViewAdapter.changeData(obtainRecommendations(activity!!.applicationContext))

        popularesRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        popularesRecyclerViewAdapter.changeData(obtainPopularSongs(activity!!.applicationContext))

        novedadesRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }

        novedadesRecyclerViewAdapter.changeData(obtainNewsSongs(activity!!.applicationContext))

        // Inflate the layout for this fragment
        return inflaterD
    }

    companion object {
        fun newInstance(mmSongSelectedListener: (Song) -> Unit, mmUserSelectedListener: (User) -> Unit,
                        mmPlaylistSelectedListener: (Playlist) -> Unit): HomeFragment {

            val fragment = HomeFragment()
            fragment.mSongSelectedListener = mmSongSelectedListener
            fragment.mUserSelectedListener = mmUserSelectedListener
            fragment.mPlaylistSelectedListener = mmPlaylistSelectedListener
            return fragment
        }
    }
}// Required empty public constructor
