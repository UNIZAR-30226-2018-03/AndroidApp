package com.spreadyourmusic.spreadyourmusic

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService
import com.spreadyourmusic.spreadyourmusic.test.ServerEmulator

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
    fun loginUserServerTest() {
        val angle = doLoginServer("abelcht", "1234")
        println(angle)
        Log.i("TAGG", angle)
    }

    @Test
    fun deleteUserTest() {
        doDeleteAccountServer("abelcht", "l4lk0vopfqc285mg")
    }

    @Test
    fun doSignUpServerTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val usuario = User("abelcht4", "1234", "Este Es Un Nombre", "/storage/emulated/0/Music/Prueba/profile_postmalone.jpg", "prueba@prueba.com", "fghgfhgfhgfhgf", Date(1992, 12, 12))
        //usuario.twitterAccount = "pruebaTwittee"
        doSignUpServer(usuario, appContext)
    }


    ///////////////////////////////
    // Probar a partir de aqui
    ///////////////////////////////

    @Test
    fun doLogoutTest() {
        val token = doLoginServer("abelcht", "1234")
        doLogoutServer("abelcht", token)
    }


    @Test
    fun userGetServer() {
        val token = doLoginServer("abelcht4", "1234")
        val usr = obtainUserDataServer("abelcht4", token)
        Log.i("TAGG", usr!!.pictureLocationUri)
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
        val user = User("usuarioParaEliminar15", "dfgdfgfd", "Este Es Un Nombre", "/storage/emulated/0/Music/Prueba/profile_postmalone.jpg", "prueba@prueba.com", "fghgfhgfhgfhgf", Date(1992, 12, 12))
        //user.twitterAccount = "dfdsfds"
        val token = doSignUpServer(user, appContext)
        doDeleteAccountServer("usuarioParaEliminar15", token)
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
    fun uploadSong(){
        val appContext = InstrumentationRegistry.getTargetContext()
        uploadSongServer("abelcht","dfs", ServerEmulator.songList[0]!!,appContext)
    }

    @Test
    fun multipletest2(){
        val token = doLoginServer("abelcht", "1234")
        addReproductionToSongServer("abelcht", token, 0)
    }

    @Test
    fun multipleTest() {
        //getNumberOfFollowersOfUserServer("abelcht")
        val token = doLoginServer("abelcht", "1234")
        addFollowerToUserServer("abelcht", token, "lAngelP")
        deleteFollowerToUserServer("abelcht", token, "lAngelP")
        getFollowedPlaylistsServer("abelcht")

        // Se supone que la playlist con id 1 esta creada
        isPlaylistFollowedByUserServer("abelcht", 1)
        getNumberOfFollowersOfPlaylistServer(1)

        addFollowerToPlaylistServer("abelcht", token, 1)
        deleteFollowerToPlaylistServer("abelcht", token, 1)
        obtainPlaylistDataServer(1)

        // Se supone que la cancion con id 1 esta creada
        addReproductionToSongServer("abelcht", token, 1)

        setSongFavoutireServer("abelcht", token, 1)
        unSetSongFavoutireServer("abelcht", token, 1)
        isSongFavoutireByUserServer("abelcht", token, 1)

        obtainFavouriteSongsByUserServer("abelcht", token)

        obtainRecomendationsForUserServer("abelcht", token, 20)

        obtainPopularSongsServer(10)

        obtainNewSongsFromFollowedArtistOfUserServer("abelcht", token, 20)

        obtainTrendSongsServer(10)

        obtainTrendSongsInUserCountryServer("abelcht", 20)

        obtainUpdatedPlaylistsFollowedByUserServer("abelcht", token, 20)

        obtainResultForQueryServer(20, "abel", null)

        obtainPopularByGenreServer(30)

        isOtherSessionOpenFromSameUserServer("abelcht", token)

        obtainLastSongListenedServer("abelcht", token)

        obtainGeneresServer()

        obtainAlbumsFromUserServer("abelcht")
    }

    @Test
    fun updateUser() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val usuario = User("abelcht3", "1234", "Este Es Un Nombress", "/storage/emulated/0/Music/Prueba/profile_postmalone.jpg", "prueba@prueba.com", "fghgfhgfhgfhgf", Date(1992, 12, 12))
        usuario.twitterAccount = "pruebasdfsdfdsTwittee"
        doLoginServer("abelcht3", "1234")

        doUpdateAccountServer(usuario, "1234", appContext)

    }

}
