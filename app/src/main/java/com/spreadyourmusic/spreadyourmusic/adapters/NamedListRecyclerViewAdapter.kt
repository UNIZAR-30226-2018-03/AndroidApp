package com.spreadyourmusic.spreadyourmusic.adapters

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.models.Recommendation

/**
 * Created by abel
 * On 4/04/18.
 */
class NamedListRecyclerViewAdapter(val context: Context?) : RecyclerView.Adapter<NamedListRecyclerViewAdapter.NamedListRecyclerViewViewHolder>() {

    private var datos: List<Pair<String, List<Recommendation>>> = ArrayList()
    private var clickListener: (Recommendation) -> Unit = {}

    /**
     * Modifica los datos del adaptador
     */
    fun changeData(datas: List<Pair<String, List<Recommendation>>> ) {
        datos = datas
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NamedListRecyclerViewViewHolder, position: Int) {
        holder.bind(datos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NamedListRecyclerViewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listitem_named_list, parent, false)
        return NamedListRecyclerViewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    fun setOnClickListener(l: (Recommendation) -> Unit) {
        clickListener = l
    }

    inner class NamedListRecyclerViewViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        fun bind(obj: Pair<String, List<Recommendation>>) {
            val nombre = itemView.findViewById<View>(R.id.listTextView) as TextView
            val lista = itemView.findViewById<View>(R.id.listRecyclerView) as RecyclerView

            val listRecyclerViewAdapter = RecomendationsHorizontalRecyclerViewAdapter(context)
            lista.adapter = listRecyclerViewAdapter
            lista.setHasFixedSize(true)
            lista.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            lista.itemAnimator = DefaultItemAnimator()
            listRecyclerViewAdapter.setOnClickListener(clickListener)
            nombre.text = obj.first
            listRecyclerViewAdapter.changeData(obj.second)
        }

    }
}