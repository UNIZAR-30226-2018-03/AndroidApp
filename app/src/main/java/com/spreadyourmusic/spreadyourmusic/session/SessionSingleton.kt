package com.spreadyourmusic.spreadyourmusic.session

import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager

/**
 * Created by abel on 27/02/18.
 */

object SessionSingleton {
    private val musicQueueManager : MusicQueueManager  = MusicQueueManager.getInstance()

    fun getMusicQueueManager() : MusicQueueManager {
        return musicQueueManager
    }
}
