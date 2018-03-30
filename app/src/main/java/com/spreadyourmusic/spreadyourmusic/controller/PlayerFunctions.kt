package com.spreadyourmusic.spreadyourmusic.controller

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

fun setFavoriteCurrentSong(){

}

// Si es true descarga la cancion, sino la elimina
fun downloadCurrentSong(){

}

// Si es true crea reproduccion aleatoria
fun randomReproduction(){

}
