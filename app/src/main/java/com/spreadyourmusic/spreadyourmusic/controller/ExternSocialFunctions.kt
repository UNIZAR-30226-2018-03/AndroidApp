package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Intent
import android.net.Uri

/**
 * Created by abel on 8/03/18.
 */
/**
 * Abre el perfil del artista
 */
fun openArtistSocialMediaProfile(profileUrl:String, activity: Activity){
    val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(profileUrl))
    activity.startActivity(intent)
}