package com.spreadyourmusic.spreadyourmusic.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.activities.PlayerActivity
import com.spreadyourmusic.spreadyourmusic.activities.PlaylistActivity
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsHomeRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.helpers.obtainNewsSongs
import com.spreadyourmusic.spreadyourmusic.helpers.obtainPopularSongs
import com.spreadyourmusic.spreadyourmusic.helpers.obtainRecommendations

class HomeFragment : Fragment() {



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
            val intent = Intent(context, PlayerActivity::class.java)
            startActivity(intent)
        }

        recomendacionesRecyclerViewAdapter.changeData(obtainRecommendations(activity!!.applicationContext))

        popularesRecyclerViewAdapter.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            startActivity(intent)
        }

        popularesRecyclerViewAdapter.changeData(obtainPopularSongs(activity!!.applicationContext))

        novedadesRecyclerViewAdapter.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            startActivity(intent)
        }

        novedadesRecyclerViewAdapter.changeData(obtainNewsSongs(activity!!.applicationContext))

        // Inflate the layout for this fragment
        return inflaterD
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}// Required empty public constructor
