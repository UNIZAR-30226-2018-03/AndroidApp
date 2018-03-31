package com.spreadyourmusic.spreadyourmusic.controller

import android.app.Activity
import android.content.Intent
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.models.Song

/**
 * Created by abel
 * On 8/03/18.
 */

fun getCurrentSong():Song{
   return MusicQueueManager.getInstance().currentSong
}

fun changeToNextSong(){
    MusicQueueManager.getInstance().skipQueuePosition(1)
}

fun changeToPreviousSong(){
    MusicQueueManager.getInstance().skipQueuePosition(-1)
}

fun setFavoriteCurrentSong(favorite: Boolean){

}

fun isCurrentSongFavorite(): Boolean{
    // TODO: Hacer
    return false
}

// Si es true descarga la cancion, sino la elimina
fun downloadCurrentSong(download: Boolean){

}


fun isCurrentSongDownloaded(): Boolean{
    // TODO: Hacer
    return false
}

// Si es true crea reproduccion aleatoria
fun randomReproduction(nextState: Boolean) {

}

fun isRandomReproductionEnabled():Boolean{
    // TODO: Hacer
    return false
}

fun shareCurrentSong(activity: Activity){
    // TODO: Hacer
    val sendIntent = Intent()
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
    sendIntent.setType("text/plain");
    activity.startActivity(sendIntent)
}
