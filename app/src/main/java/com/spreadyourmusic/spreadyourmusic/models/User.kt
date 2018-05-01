package com.spreadyourmusic.spreadyourmusic.models

import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */
// TODO:
class User(val username: String) : Recommendation {
    var name: String? = null
    var pictureLocationUri: String? = null
    var biography: String? = null
    var email: String? = null
    var password: String? = null
    var birthDate: Date? = null
    var verifiedAccount = false
    var twitterAccount: String? = null
    var facebookAccount: String? = null
    var instagramAccount: String? = null

    //TODO: El link devuelto ha de ser el que apunta a la misma playlist desde la interfaz web
    override var shareLink: String = "http://SpreadYourMusic/playlist/"

    constructor(username: String, password: String) : this(username) {
        this.password = password
    }

    constructor(username: String, name: String, pictureLocationUri: String, verifiedAccount: Boolean) : this(username) {
        this.name = name
        this.verifiedAccount = verifiedAccount
        this.pictureLocationUri = pictureLocationUri
    }

    constructor(username: String, name: String, pictureLocationUri: String, verifiedAccount: Boolean,
                email: String?, biography: String?, birthDate: Date?) : this(username) {
        this.name = name
        this.email = email
        this.pictureLocationUri = pictureLocationUri
        this.verifiedAccount = verifiedAccount
        this.biography = biography
        this.birthDate = birthDate
    }

    fun getTwitterAccountURL(): String? {
        return if (twitterAccount != null) "https://twitter.com/$twitterAccount"
        else null
    }

    fun getFacebookAccountURL(): String? {
        return if (facebookAccount != null) "https://www.facebook.com/$facebookAccount"
        else null
    }

    fun getInstagramAccountURL(): String? {
        return if (instagramAccount != null) "https://www.instagram.com/$instagramAccount"
        else null
    }
}