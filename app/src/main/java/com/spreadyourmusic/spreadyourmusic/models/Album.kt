package com.spreadyourmusic.spreadyourmusic.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.spreadyourmusic.spreadyourmusic.R

/**
 * Created by abel on 8/03/18.
 */
//TODO Constructor provisional para hacer pruebas
class Album(val nombre: String, val context: Context) {
    fun getCover(): Bitmap {
        /* Thread{
     val url = URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464")
     val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

     runOnUiThread ({
         player_background.setImageBitmap(bmp)
     })
 }.start()*/

        return BitmapFactory.decodeResource(context.resources, R.drawable.sample_image)
    }
}