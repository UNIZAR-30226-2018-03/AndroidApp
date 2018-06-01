package com.spreadyourmusic.spreadyourmusic

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.spreadyourmusic.spreadyourmusic.apis.*
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User

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
    fun UserRequestTest() {
        val username = "usuarioPruebasAndroid223"
        val username2 = "usuarioPruebasAndroid334"
        var token: String
        var token2: String
        val password = "1234"
        val context = InstrumentationRegistry.getTargetContext()
        val user = User(username, password, "Prueba", "d", "prueba@prupru.com", null, Date(1998, 1, 1))
        val user2 = User(username2, password, "Prueba", "d", "prueb2a@prupru.com", null, Date(1998, 1, 1))
        //user.twitterAccount = "dfsdf"

        token = doSignUpServer(user, context)
        token2 = doSignUpServer(user2, context)

        //user.instagramAccount = "dfsdf"
        //user.facebookAccount = "dfsdf"
        //doUpdateAccountServer(user,token,context)

        doLogoutServer(username, token)
        token = doLoginServer(username, password)

        obtainUserDataServer(username, "")
        obtainUserDataServer(username, token)

        addFollowerToUserServer(username, token, username2)

        if (getFollowedUsersServer(username).size != 1) throw Exception("getFollowedUsers")

        if (!isUserFollowedByUserServer(username, username2)) throw Exception("isUserFollowedByUserServer")

        if (getNumberOfFollowersOfUserServer(username2) != 1L) throw Exception("getNumberOfFollowersOfUserServer")

        deleteFollowerToUserServer(username, token, username2)

        doDeleteAccountServer(username, token, context)
        doDeleteAccountServer(username2, token2, context)
    }

    @Test
    fun AlbumRequestTest() {
        val username = "usuarioPruebasAndroid555"
        val username2 = "usuarioPruebasAndroid444"
        var token: String
        var token2: String
        val password = "1234"
        val context =  InstrumentationRegistry.getTargetContext()
        val user = User(username, password, "Prueba", "d", "prueba@prupru.com", null, Date(1998, 1, 1))
        val user2 = User(username2, password, "Prueba", "d", "prueb2a@prupru.com", null, Date(1998, 1, 1))
        //user.twitterAccount = "dfsdf"

        val album = Album("Spiderman", user, GregorianCalendar(1998, 1, 1), "sdf")

        token = doSignUpServer(user, context)
        token2 = doSignUpServer(user2, context)

        val id = createAlbumsServer(username, token, album, context)

        obtainAlbumFromID(id)

        if (obtainAlbumsFromUserServer(username).size != 1) throw Exception("obtainAlbumsFromUserServer")

        doDeleteAccountServer(username, token, context)
        doDeleteAccountServer(username2, token2, context)
    }

    @Test
    fun SongsRequestTest() {
        val username = "usuarioPruebasAndroid666"
        val username2 = "usuarioPruebasAndroid777"
        var token: String
        var token2: String
        val password = "1234"
        val context =  InstrumentationRegistry.getTargetContext()
        val user = User(username, password, "Prueba", "d", "prueba@prupru.com", null, Date(1998, 1, 1))
        val user2 = User(username2, password, "Prueba", "d", "prueb2a@prupru.com", null, Date(1998, 1, 1))
        val album = Album("Spiderman", user, GregorianCalendar(1998, 1, 1), "sdf")

        token = doSignUpServer(user, context)
        token2 = doSignUpServer(user2, context)

        val id = createAlbumsServer(username, token, album, context)

        val albumpass = obtainAlbumFromID(id)
        val song = Song("nobre", "df", albumpass!!, "ds", null)

        val idS = uploadSongServer(username, token, song, context)
        val songD = obtainSongFromID(idS)

        addReproductionToSongServer(username2, token2, idS)

        setSongFavoutireServer(username2, token2, idS)
        if (!isSongFavoutireByUserServer(username2, token2, idS)) throw Exception("isSongFavoutireByUserServer")
        obtainFavouriteSongsByUserServer(username2, token2)

        obtainPopularSongsServer(50)

        //obtainNewSongsFromFollowedArtistOfUserServer(username2, token2, 20)

        obtainTrendSongsServer(50)

        obtainTrendSongsInUserCountryServer(username2, token2, 20)

        obtainLastSongListenedServer(username2, token2)

        unSetSongFavoutireServer(username2, token2, idS)

        deleteSongServer(username, token, songD!!, context)
        doDeleteAccountServer(username, token, context)
        doDeleteAccountServer(username2, token2, context)
    }

    @Test
    fun PlaylistRequestTest() {
        val username = "usuarioPruebasAndroid888"
        val username2 = "usuarioPruebasAndroid999"
        var token: String
        var token2: String
        val password = "1234"
        val context =  InstrumentationRegistry.getTargetContext()
        val user = User(username, password, "Prueba", "d", "prueba@prupru.com", null, Date(1998, 1, 1))
        val user2 = User(username2, password, "Prueba", "d", "prueb2a@prupru.com", null, Date(1998, 1, 1))
        val album = Album("Spiderman", user, GregorianCalendar(1998, 1, 1), "sdf")

        token = doSignUpServer(user, context)
        token2 = doSignUpServer(user2, context)

        val id = createAlbumsServer(username, token, album, context)

        val albumpass = obtainAlbumFromID(id)
        val song = Song("nobre", "df", albumpass!!, "ds", null)

        val idS = uploadSongServer(username, token, song, context)
        val songD = obtainSongFromID(idS)

        val songList = ArrayList<Song>()
        songList.add(songD!!)

        val playlist = Playlist("nombre", albumpass.creator, "sadas", songList)

        val pid = createPlaylistServer(playlist.creator.username, token, playlist, context)

        val playlistData = obtainPlaylistDataServer(pid)

        addFollowerToPlaylistServer(username2, token2, pid)

        if (!isPlaylistFollowedByUserServer(username2, pid)) throw Exception("isPlaylistFollowedByUserServer")
        getNumberOfFollowersOfPlaylistServer(pid)
        getFollowedPlaylistsServer(username2)
        obtainUpdatedPlaylistsFollowedByUserServer(username2, token2, 30)
        deleteFollowerToPlaylistServer(username2, token2, pid)


        deleteSongServer(username, token, songD, context)

        deletePlaylistServer(playlist.creator.username, token, playlistData!!, context)

        doDeleteAccountServer(username, token, context)
        doDeleteAccountServer(username2, token2, context)
    }

    @Test
    fun ComplexRequestsTest() {
        val username = "usuarioPruebasAndroid10101r"
        val username2 = "usuarioPruebasAndroid1010d"
        var token: String
        var token2: String
        val password = "1234"
        val context =  InstrumentationRegistry.getTargetContext()
        val user = User(username, password, "Prueba", "d", "prueba@prupru.com", null, Date(1998, 1, 1))
        val user2 = User(username2, password, "Prueba", "d", "prueb2a@prupru.com", null, Date(1998, 1, 1))
        val album = Album("Spiderman", user, GregorianCalendar(1998, 1, 1), "sdf")
        token = doSignUpServer(user, context)
        token2 = doSignUpServer(user2, context)

        val id = createAlbumsServer(username, token, album, context)

        val albumpass = obtainAlbumFromID(id)
        val song = Song("nobre", "df", albumpass!!, "ds", null)

        val idS = uploadSongServer(username, token, song, context)
        val songD = obtainSongFromID(idS)

        val songList = ArrayList<Song>()
        songList.add(songD!!)

        val playlist = Playlist("nombre", albumpass.creator, "sadas", songList)

        val pid = createPlaylistServer(playlist.creator.username, token, playlist, context)

        val playlistData = obtainPlaylistDataServer(pid)

        addFollowerToPlaylistServer(username2, token2, pid)

        if (!isPlaylistFollowedByUserServer(username2, pid)) throw Exception("isPlaylistFollowedByUserServer")
        getNumberOfFollowersOfPlaylistServer(pid)
        getFollowedPlaylistsServer(username2)
        obtainUpdatedPlaylistsFollowedByUserServer(username2, token2, 30)
        deleteFollowerToPlaylistServer(username2, token2, pid)

        obtainSongsFromUserServer(albumpass.creator.username)

        obtainPlaylistsFromUserServer(albumpass.creator.username)

        obtainRecomendationsForUserServer(username, token, 20)

        obtainResultForQueryServer(20, "gosepumbs", null)
        obtainResultForQueryServer(20, "gosepumbs", 1)
        obtainPopularByGenreServer(20)

        isOtherSessionOpenFromSameUserServer(username, token)
        obtainGeneresServer()

        //deleteSongServer(username, token, songD, context)
        //deletePlaylistServer(playlist.creator.username, token, playlistData!!, context)

        doDeleteAccountServer(username, token, context)
        doDeleteAccountServer(username2, token2, context)
    }

}
