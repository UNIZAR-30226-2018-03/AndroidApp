package com.spreadyourmusic.spreadyourmusic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.spreadyourmusic.spreadyourmusic.R

/**
 * Created by abel on 8/03/18.
 */
//TODO Constructor provisional para hacer pruebas
class Album(val nombre:String, val context:Context) {
    fun getCover():Bitmap{
        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_image)
    }
}