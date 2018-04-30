package com.spreadyourmusic.spreadyourmusic.models

import java.util.*

/**
 * Created by abel
 * On 8/03/18.
 */
class Album(val name: String, val creator: User, val releaseDate: Calendar, var artLocationUri: String) {
    var id: Long? = null

    constructor(id: Long, name: String, creator: User, releaseDate: Calendar, artLocationUri: String)
            : this(name, creator, releaseDate, artLocationUri) {
        this.id = id
    }
}