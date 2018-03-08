package com.spreadyourmusic.spreadyourmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User


/**
 * Created by abel
 * On 8/03/18.
 */
class RecomendationsHomeRecyclerViewAdapter : RecyclerView.Adapter<GeneralRecyclerViewViewHolder>() {

    private val VIEW_TYPE_SONG = 0
    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_PLAYLIST = 2

    private var datos: List<Recommendation> = ArrayList()
    private var clickListener: (Recommendation) -> Unit = {}

    /**
     * Modifica los datos del adaptador
     */
    fun changeData(datas: List<Recommendation>) {
        datos = datas
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GeneralRecyclerViewViewHolder?, position: Int) {
        holder!!.bind(datos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GeneralRecyclerViewViewHolder {
        val itemView = when (viewType) {
            VIEW_TYPE_SONG ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_song_home, parent, false)

            VIEW_TYPE_USER ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_user_home, parent, false)

            VIEW_TYPE_PLAYLIST ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_playlist_home, parent, false)

            else ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_song_home, parent, false)
        }

        val devolver = when (viewType) { VIEW_TYPE_SONG ->
            SongHomeRecyclerViewViewHolder(itemView)

            VIEW_TYPE_USER ->
                UserHomeRecyclerViewViewHolder(itemView)

            VIEW_TYPE_PLAYLIST ->
                PlaylistHomeRecyclerViewViewHolder(itemView)
            else ->
                SongHomeRecyclerViewViewHolder(itemView)

        }
        itemView!!.setOnClickListener(devolver)
        return devolver
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    fun setOnClickListener(l: (Recommendation) -> Unit) {
        clickListener = l
    }

    override fun getItemViewType(position: Int): Int {
        return when (datos[position]){
            is Playlist -> VIEW_TYPE_PLAYLIST
            is Song -> VIEW_TYPE_SONG
            is User -> VIEW_TYPE_USER
            else -> VIEW_TYPE_SONG
        }
    }

    /*
    fun setOnClickListener(l: (ClientVo) -> Unit) {
        clickListener = l
    }*/


    inner class SongHomeRecyclerViewViewHolder(itemView: View) : GeneralRecyclerViewViewHolder(itemView) {
        override fun bind(obj: Any) {
            if(obj is Song){
                val titulo = itemView.findViewById<View>(R.id.nombre_cancion) as TextView
                val autor = itemView.findViewById<View>(R.id.nombre_artista) as TextView
                titulo.text = obj.nombre
                autor.text = obj.creador.nombre
            }
        }

        override fun onClick(v: View?) {
            clickListener(datos[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            return true
        }


    }

    inner class UserHomeRecyclerViewViewHolder(itemView: View) : GeneralRecyclerViewViewHolder(itemView) {
        override fun bind(obj: Any) {
            if(obj is User){
                val usuario = itemView.findViewById<View>(R.id.nombre_artista) as TextView
                usuario.text = obj.nombre
            }
        }

        override fun onClick(v: View?) {
            clickListener(datos[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            return true
        }
    }

    inner class PlaylistHomeRecyclerViewViewHolder(itemView: View) : GeneralRecyclerViewViewHolder(itemView) {
        override fun bind(obj: Any) {
            if(obj is Playlist){
                val usuario = itemView.findViewById<View>(R.id.nombre_artista) as TextView
                val titulo = itemView.findViewById<View>(R.id.nombre_playlist) as TextView
                usuario.text = obj.nombre
                titulo.text = obj.autor.nombre
            }
        }

        override fun onClick(v: View?) {
            clickListener(datos[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            return true
        }
    }
}