package com.spreadyourmusic.spreadyourmusic

import com.spreadyourmusic.spreadyourmusic.apis.doLoginServer
import com.spreadyourmusic.spreadyourmusic.apis.doLogoutServer
import com.spreadyourmusic.spreadyourmusic.apis.doSignUpServer
import com.spreadyourmusic.spreadyourmusic.apis.getFollowedPlaylistsServer
import com.spreadyourmusic.spreadyourmusic.models.User
import org.junit.Test

import org.junit.Assert.*
import java.util.*

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


    @Test
    fun doSignUpServerTest() {
        val usuario = User("abelcht", "dfgdfgfd","Este Es Un Nombre","","prueba@prueba.com","fghgfhgfhgfhgf", Date(1992,12,12))
    }

    @Test
    fun loginUserServerd() {
        val angle = doLoginServer("lAngelP", "1234")
        println(angle)
    }

    @Test
    fun logoutUserServerd() {
        val angle = doLogoutServer("lAngelP", "1234")
        println(angle)
    }
}
