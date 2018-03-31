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
import com.spreadyourmusic.spreadyourmusic.adapters.RecomendationsVerticalRecyclerViewAdapter
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

class BrowserFragment : Fragment() {
    private var mSongSelectedListener: (Song) -> Unit = {}
    private var mUserSelectedListener: (User) -> Unit = {}
    private var mPlaylistSelectedListener: (Playlist) -> Unit = {}
    private var queryResults: List<Recommendation>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_browser, container, false)
        val listaQueryResults = view.findViewById<RecyclerView>(R.id.browserRecyclerView)
        val queryResultsRecyclerViewAdapter = RecomendationsVerticalRecyclerViewAdapter(context)


        listaQueryResults.adapter = queryResultsRecyclerViewAdapter
        listaQueryResults.setHasFixedSize(true)


        listaQueryResults.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listaQueryResults.itemAnimator = DefaultItemAnimator()


        queryResultsRecyclerViewAdapter.setOnClickListener {
            when (it) {
                is Song -> mSongSelectedListener(it)
                is User -> mUserSelectedListener(it)
                is Playlist -> mPlaylistSelectedListener(it)
            }
        }


        queryResultsRecyclerViewAdapter.changeData(queryResults!!)

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(mmSongSelectedListener: (Song) -> Unit, mmUserSelectedListener: (User) -> Unit,
                        mmPlaylistSelectedListener: (Playlist) -> Unit, queryResults : List<Recommendation>): BrowserFragment {

            val fragment = BrowserFragment()
            fragment.mSongSelectedListener = mmSongSelectedListener
            fragment.mUserSelectedListener = mmUserSelectedListener
            fragment.mPlaylistSelectedListener = mmPlaylistSelectedListener
            fragment.queryResults = queryResults
            return fragment
        }
    }
}// Required empty public constructor
