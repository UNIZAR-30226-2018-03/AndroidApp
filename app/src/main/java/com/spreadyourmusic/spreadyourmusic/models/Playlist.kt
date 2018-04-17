package com.spreadyourmusic.spreadyourmusic.models

/**
 * Created by abel
 * On 7/03/18.
 */
// TODO:
class Playlist(val id: Int, val name: String, val creator: User, val artLocationUri: String, val content: List<Song>) : Recommendation{
    fun getShareLink(): String{
        //TODO: El link devuelto ha de ser el que apunta a la misma playlist desde la interfaz web
        return "https://www.google.es/"
    }
}