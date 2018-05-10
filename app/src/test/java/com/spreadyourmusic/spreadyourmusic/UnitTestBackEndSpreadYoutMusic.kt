package com.spreadyourmusic.spreadyourmusic

import com.spreadyourmusic.spreadyourmusic.apis.getFollowedPlaylistsServer
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTestBackEndSpreadYoutMusic {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun nombre() {
        val x = getFollowedPlaylistsServer("abelcht")
        val i = 0;
    }
}
