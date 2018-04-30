/**
 * Created by abel
 * On 8/03/18.
 */
package com.spreadyourmusic.spreadyourmusic.models

import java.util.*

/**
 * Constructor usado cuando se generan datos desde el dispositivo
 */
class Album(val name: String, val creator: User, val releaseDate: Calendar, var artLocationUri: String) {
    var id: Long? = null

    /**
     * Constructor usado cuando se obtienen datos desde el back-end
     */
    constructor(id: Long, name: String, creator: User, releaseDate: Calendar, artLocationUri: String)
            : this(name, creator, releaseDate, artLocationUri) {
        this.id = id
    }
}