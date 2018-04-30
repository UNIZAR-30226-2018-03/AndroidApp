package com.spreadyourmusic.spreadyourmusic.models

/**
 * Created by abel
 * On 7/03/18.
 */
class Playlist(val name: String, val creator: User, val artLocationUri: String, val content: List<Song>) : Recommendation{
    var id: Long? = null

    //TODO: El link devuelto ha de ser el que apunta a la misma playlist desde la interfaz web
    override var shareLink: String = "http://SpreadYourMusic/playlist/"

    constructor(id: Long, name: String, creator: User, artLocationUri: String, content: List<Song>):this(name,creator,artLocationUri, content){
        this.id = id
    }
}