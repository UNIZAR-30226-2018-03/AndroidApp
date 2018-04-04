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
import com.spreadyourmusic.spreadyourmusic.models.Recommendation

class HomeBaseFragment : Fragment() {
    private var mClickListener: (Recommendation) -> Unit = {}
    private var mValues: List<Pair<String, List<Recommendation>>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_recyclerview, container, false)

        val lista = view.findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerViewAdapter = NamedListRecyclerViewAdapter(context)

        lista.adapter = recyclerViewAdapter
        lista.setHasFixedSize(true)

        lista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lista.itemAnimator = DefaultItemAnimator()

        recyclerViewAdapter.setOnClickListener (mClickListener)
        recyclerViewAdapter.changeData(mValues!!)

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(mmClickListener: (Recommendation) -> Unit, mmValues: List<Pair<String, List<Recommendation>>>): HomeBaseFragment {
            val fragment = HomeBaseFragment()
            fragment.mClickListener = mmClickListener
            fragment.mValues = mmValues
            return fragment
        }
    }
}// Required empty public constructor
