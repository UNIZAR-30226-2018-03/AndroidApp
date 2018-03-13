package com.spreadyourmusic.spreadyourmusic.models

import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */
class Song(public val id: Long, public val nombre: String, public val album: Album, public val fecha: Calendar, public val numeroDeVisualizaciones: Long, public val numeroDeFavoritos: Long, public val creador: User) : Recommendation {
    fun isDownloaded() : Boolean{
        return false
    }
    fun getUrl() : String{
        return "https://upload.wikimedia.org/wikipedia/commons/d/d5/Pop_Goes_the_Weasel.ogg"
    }
}