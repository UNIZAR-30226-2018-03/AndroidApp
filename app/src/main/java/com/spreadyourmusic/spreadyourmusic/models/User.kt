package com.spreadyourmusic.spreadyourmusic.models

import android.graphics.Bitmap
import java.util.*

/**
 * Created by abel
 * On 7/03/18.
 */
class User(val username: String, val name: String, val pictureLocationUri: String) : Recommendation {
    var bibliography: String? = null
    var email: String? = null
    var password: String? = null
    var birthDate: Date? = null
    //String locale = context.getResources().getConfiguration().locale.getCountry();
    var country: String? = null
    var twitterAccount: String? = null
    var facebookAccount: String? = null
    var instagramAccount: String? = null
    var picture:Bitmap? = null
}