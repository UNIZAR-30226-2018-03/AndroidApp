package com.spreadyourmusic.spreadyourmusic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User


/**
 * Created by abel
 * On 8/03/18.
 */
class RecomendationsHorizontalRecyclerViewAdapter(val context: Context?) : RecyclerView.Adapter<GeneralRecyclerViewViewHolder>() {
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

    override fun onBindViewHolder(holder: GeneralRecyclerViewViewHolder, position: Int) {
        holder.bind(datos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GeneralRecyclerViewViewHolder {
        val itemView = when (viewType) {
            VIEW_TYPE_SONG ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_song_vertical, parent, false)

            VIEW_TYPE_USER ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_user_vertical, parent, false)

            VIEW_TYPE_PLAYLIST ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_playlist_vertical, parent, false)

            else ->
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_song_vertical, parent, false)
        }

        return when (viewType) { VIEW_TYPE_SONG ->
            SongHomeRecyclerViewViewHolder(itemView)

            VIEW_TYPE_USER ->
                UserHomeRecyclerViewViewHolder(itemView)

            VIEW_TYPE_PLAYLIST ->
                PlaylistHomeRecyclerViewViewHolder(itemView)
            else ->
                SongHomeRecyclerViewViewHolder(itemView)

        }
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    fun setOnClickListener(l: (Recommendation) -> Unit) {
        clickListener = l
    }

    override fun getItemViewType(position: Int): Int {
        return when (datos[position]) {
            is Playlist -> VIEW_TYPE_PLAYLIST
            is Song -> VIEW_TYPE_SONG
            is User -> VIEW_TYPE_USER
            else -> VIEW_TYPE_SONG
        }
    }


    inner class SongHomeRecyclerViewViewHolder(itemView: View) : GeneralRecyclerViewViewHolder(itemView) {
        override fun bind(obj: Any) {
            if (obj is Song) {
                val titulo = itemView.findViewById<View>(R.id.songNameTextView) as TextView
                val autor = itemView.findViewById<View>(R.id.creatorNameTextView) as TextView
                val imagen = itemView.findViewById<View>(R.id.coverCircleImageView) as ImageView
                titulo.text = obj.name
                autor.text = obj.album.creator.username
                if (context != null)
                    Glide.with(context).load(obj.album.artLocationUri).into(imagen)
            }
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
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
            if (obj is User) {
                val usuario = itemView.findViewById<View>(R.id.creatorNameTextView) as TextView
                val imagen = itemView.findViewById<View>(R.id.coverCircleImageView) as de.hdodenhof.circleimageview.CircleImageView
                usuario.text = obj.username
                if (context != null)
                    Glide.with(context).load(obj.pictureLocationUri).into(imagen)
            }
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
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
            if (obj is Playlist) {
                val usuario = itemView.findViewById<View>(R.id.creatorNameTextView) as TextView
                val titulo = itemView.findViewById<View>(R.id.nombre_playlist) as TextView
                val imagen = itemView.findViewById<View>(R.id.coverCircleImageView) as de.hdodenhof.circleimageview.CircleImageView
                usuario.text = obj.creator.username
                titulo.text = obj.name
                if (context != null)
                    Glide.with(context).load(obj.artLocationUri).into(imagen)
            }
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener(datos[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            return true
        }
    }
}