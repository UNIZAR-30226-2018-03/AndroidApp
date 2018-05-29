package com.spreadyourmusic.spreadyourmusic

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestBackEndSpreadYoutMusic {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.spreadyourmusic.spreadyourmusic", appContext.packageName)
    }

    @Test
    fun amazonServicesDelete() {
        AmazonS3UploadFileService.deleteFile("postmalone", InstrumentationRegistry.getTargetContext(), {
            val i = it
        })
    }

    @Test
    fun loginUserServer() {
        val angle = doLoginServer("abelcht", "dfgdfgfd")
        println(angle)
        Log.i("TAGG", angle)
    }

    @Test
    fun doSignUpServerTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val usuario = User("abelcht", "dfgdfgfd", "Este Es Un Nombre", "", "prueba@prueba.com", "fghgfhgfhgfhgf", Date(1992, 12, 12))
        doSignUpServer(usuario, appContext)
    }


    ///////////////////////////////
    // Probar a partir de aqui
    ///////////////////////////////

    @Test
    fun doLogoutTest() {
        val token = doLoginServer("abelcht", "dfgdfgfd")
        doLogoutServer("abelcht", token)
    }


    @Test
    fun userGetServer() {
        val token = doLoginServer("abelcht", "dfgdfgfd")
        val usr = obtainUserDataServer("abelcht", token)
        doLogoutServer("abelcht", token)
    }

    @Test
    fun setFavouriteServer() {
        val token = doLoginServer("abelcht", "dfgdfgfd")
        setSongFavoutireServer("abelcht", token, 0)
        doLogoutServer("abelcht", token)
    }

    @Test
    fun deleteUserServer() {
        val appContext = InstrumentationRegistry.getTargetContext()
        // Utiliza internamente UpdateAccount
        val user = User("usuarioParaEliminar", "dfgdfgfd", "Este Es Un Nombre", "", "prueba@prueba.com", "fghgfhgfhgfhgf", Date(1992, 12, 12))
        val token = doSignUpServer(user, appContext)
        doDeleteAccountServer("usuarioParaEliminar", token)
    }

    @Test
    fun obtainSongsFromUserServerTest() {
        obtainSongsFromUserServer("abelcht")
    }

    @Test
    fun obtainPlaylistsFromUserServerTest() {
        obtainPlaylistsFromUserServer("abelcht")
    }

    @Test
    fun getFollowedUsersServerTest() {
        getFollowedUsersServer("abelcht")
    }

    @Test
    fun isUserFollowedByUserServerTest() {
        isUserFollowedByUserServer("abelcht", "lAngelP")
    }

    @Test
    fun multipleTest(){
        getNumberOfFollowersOfUserServer("abelcht")
        val token = doLoginServer("abelcht", "dfgdfgfd")
        addFollowerToUserServer("abelcht", token, "lAngelP")
        deleteFollowerToUserServer("abelcht", token, "lAngelP")
        getFollowedPlaylistsServer("abelcht")

        // Se supone que la playlist con id 1 esta creada
        isPlaylistFollowedByUserServer("abelcht", 1)
        getNumberOfFollowersOfPlaylistServer(1)

        addFollowerToPlaylistServer("abelcht", token,1)
        deleteFollowerToPlaylistServer("abelcht", token,1)
        obtainPlaylistDataServer(1)

        // Se supone que la cancion con id 1 esta creada
        addReproductionToSongServer("abelcht", token,1)

        setSongFavoutireServer("abelcht", token,1)
        unSetSongFavoutireServer("abelcht", token,1)
        isSongFavoutireByUserServer("abelcht", token,1)

        obtainFavouriteSongsByUserServer("abelcht", token)

        obtainRecomendationsForUserServer("abelcht", token, 20)

        obtainPopularSongsServer(10)

        obtainNewSongsFromFollowedArtistOfUserServer("abelcht", token, 20)

        obtainTrendSongsServer(10)

        obtainTrendSongsInUserCountryServer("abelcht", 20)

        obtainUpdatedPlaylistsFollowedByUserServer("abelcht", token, 20)

        obtainResultForQueryServer(20,"abel", null)

        obtainPopularByGenreServer(30)

        isOtherSessionOpenFromSameUserServer("abelcht", token)

        obtainLastSongListenedServer("abelcht", token)

        obtainGeneresServer()

        obtainAlbumsFromUserServer("abelcht")
    }

}
