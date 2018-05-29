package com.spreadyourmusic.spreadyourmusic.apis

import android.annotation.SuppressLint
import android.content.Context
import com.spreadyourmusic.spreadyourmusic.R.string.*
import com.spreadyourmusic.spreadyourmusic.helpers.*
import android.util.Log
import com.spreadyourmusic.spreadyourmusic.R.string.password
import com.spreadyourmusic.spreadyourmusic.helpers.*
import com.spreadyourmusic.spreadyourmusic.models.*
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService
import com.spreadyourmusic.spreadyourmusic.test.ServerEmulator
import org.json.JSONObject
import java.util.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Address of Back-End Server
 */
private const val serverAddress = "http://155.210.13.105:7800"

/**
 * Address of Data Server
 */
private const val dataServerAdress = "http://155.210.13.105:7480"

/**
 * Direcciónes de los diferentes tipos de datos en el servidor de almacenamiento
 */
private const val songLocationUploadPrefix = "song_"
private const val songLyricsUploadPrefix = "lyric_"
private const val userUploadPrefix = "user_"
private const val albumUploadPrefix = "album_"
private const val playlistUploadPrefix = "playlist_"

/**
 * Created by abel on 8/03/18.
 */
/**
 * Función auxiliar que permite obtener el JSON de una request al back end
 * Si la request exige GET postData ha de ser null
 */
private fun getJSONFromRequest(urlString: String, postData: List<Pair<String, String>>?, type: Int): JSONObject? {
    return if (postData == null) fetchJSONFromUrl(serverAddress + urlString)
    else fetchJSONFromUrl(serverAddress + urlString, postData, type)
}

/**
 * Devuelve la dirección de las letras de una canción
 */
private fun getSongLyricsPath(songId: Long): String {
    return "$dataServerAdress/Public/$songLyricsUploadPrefix$songId"
}

/**
 * Devuelve la dirección del archivo de música de una canción
 */
private fun getSongLocationPath(songId: Long): String {
    return "$dataServerAdress/Public/$songLocationUploadPrefix$songId"
}

/**
 * Devuelve la dirección de la foto de perfil de un usuario
 */
private fun getUserProfilePicturePath(username: String): String {
    return "$dataServerAdress/Public/$userUploadPrefix$username"
}

/**
 * Devuelve la dirección de la foto de portada de una playlist
 */
private fun getPlaylistCoverPath(playlistId: Long): String {
    return "$dataServerAdress/Public/$playlistUploadPrefix$playlistId"
}

/**
 * Devuelve la dirección de la foto de portada de un álbum
 */
private fun getAlbumCoverPath(albumId: Long): String {
    return "$dataServerAdress/Public/$albumUploadPrefix$albumId"
}

/**
 * Elimina las letras de una canción
 * Devuelve true si la operación ha tenido exito
 */
private fun deleteSongLyrics(songId: Long, context: Context): Boolean {
    AmazonS3UploadFileService.deleteFile("$songLyricsUploadPrefix$songId", context, {
    })
    return true
}

/**
 * Elimina el archivo de música de una canción
 * Devuelve true si la operación ha tenido exito
 */
private fun deleteSongLocation(songId: Long, context: Context): Boolean {
    AmazonS3UploadFileService.deleteFile("$songLocationUploadPrefix$songId", context, {
    })
    return true
}

/**
 * Elimina la foto de perfil de un usuario
 * Devuelve true si la operación ha tenido exito
 */
private fun deleteUserProfilePicture(username: String, context: Context): Boolean {
    AmazonS3UploadFileService.deleteFile("$userUploadPrefix$username", context, {
    })
    return true
}

/**
 * Elimina la foto de portada de una playlist
 * Devuelve true si la operación ha tenido exito
 */
private fun deletePlaylistCover(playlistId: Long, context: Context): Boolean {
    AmazonS3UploadFileService.deleteFile("$playlistUploadPrefix$playlistId", context, {
    })
    return true
}

/**
 * Elimina la foto de portada de un álbum
 * Devuelve true si la operación ha tenido exito
 */
private fun deleteAlbumCover(albumId: Long, context: Context): Boolean {
    AmazonS3UploadFileService.deleteFile("$albumUploadPrefix$albumId", context, {
    })
    return true
}


/**
 * Sube las letras de una canción
 * Devuelve true si la operación ha tenido exito
 */
private fun uploadSongLyrics(songId: Long, filePath: String, context: Context): Boolean {
    AmazonS3UploadFileService.uploadFile(filePath, "$songLyricsUploadPrefix$songId", context, {
        // CUIDADO: Si falla al subir no se está tratando el error
    })

    return true
}

/**
 * Sube el archivo de música de una canción
 * Devuelve true si la operación ha tenido exito
 */
private fun uploadSongLocation(songId: Long, filePath: String, context: Context): Boolean {
    AmazonS3UploadFileService.uploadFile(filePath, "$songLocationUploadPrefix$songId", context, {
        // CUIDADO: Si falla al subir no se está tratando el error
    })
    return true
}

/**
 * Sube la foto de perfil de un usuario
 * Devuelve true si la operación ha tenido exito
 */
private fun uploadUserProfilePicture(username: String, filePath: String, context: Context): Boolean {
    AmazonS3UploadFileService.uploadFile(filePath, "$userUploadPrefix$username", context, {
        // CUIDADO: Si falla al subir no se está tratando el error
    })
    return true
}

/**
 * Sube la foto de portada de una playlist
 * Devuelve true si la operación ha tenido exito
 */
private fun uploadPlaylistCover(playlistId: Long, filePath: String, context: Context): Boolean {
    AmazonS3UploadFileService.uploadFile(filePath, "$playlistUploadPrefix$playlistId", context, {
        // CUIDADO: Si falla al subir no se está tratando el error
    })
    return true
}

/**
 * Sube la foto de portada de un álbum
 * Devuelve true si la operación ha tenido exito
 */
private fun uploadAlbumCover(albumId: Long, filePath: String, context: Context): Boolean {
    AmazonS3UploadFileService.uploadFile(filePath, "$albumUploadPrefix$albumId", context, {
        // CUIDADO: Si falla al subir no se está tratando el error
    })
    return true
}

/*
                            PETICIONES AUXILIARES
 */

/*
* Obtiene la información asociada al usuario con nick @username
* Warning: esta operación puede ser costosa en tiempo
*/
fun obtainAlbumFromID(id: Long): Album? {
    val json = getJSONFromRequest("/albums/$id", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok")
            throw Exception("Error:  $error")

        val album = json.getJSONObject("album")
        val idAlbum = album.getLong("id")
        val userID = album.getLong("user_id")
        val title = album.getString("title")
        val publishYear = album.getLong("publish_year")

        val user = obtainUserDataServerFromID(userID, "")
        val date = GregorianCalendar(publishYear.toInt(), 1, 1)
        val albumPath = getAlbumCoverPath(idAlbum)

        return Album(idAlbum, title, user!!, date, albumPath)
    }
}

fun obtainSongFromID(id: Long): Song? {
    val json = getJSONFromRequest("/songs/$id", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error: $error")
        else {

            val song = json.getJSONObject("song")
            val title = song.getString("title")
            val albumId = song.getLong("album_id")
            val path = getSongLocationPath(id)
            val lyricsPath = getSongLyricsPath(id)
            val album = obtainAlbumFromID(albumId)
            return Song(id, title, path, album!!, "", lyricsPath)
        }
    }
}

@Throws(Exception::class)
fun obtainUserDataServerFromID(userID: Long, sessionToken: String): User? {
    val json = getJSONFromRequest("/users/$userID/id?token=$sessionToken", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val profile = json.getJSONObject("profile")
        val name = profile.getString("user")
        val mailVisible = profile.getBoolean("mail_visible")

        var birthDate: Date? = null
        var email: String? = null

        if (mailVisible) {
            email = profile.getString("mail")
            val dateStr = profile.getString("birth_date")
            birthDate = SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
        }

        val verifiedAccount = profile.getBoolean("verified")
        val twitterAccount = profile.getString("twitter")
        val facebookAccount = profile.getString("facebook")
        val instagramAccount = profile.getString("instagram")
        val username = profile.getString("nick")
        val pictureLocationURI = getUserProfilePicturePath(username)

        val user = User(username, name, pictureLocationURI, verifiedAccount, email, null, birthDate)
        user.twitterAccount = twitterAccount
        user.facebookAccount = facebookAccount
        user.instagramAccount = instagramAccount
        return user
    }
}

/**
 * Obtiene la información asociada al usuario con nick @username
 * Warning: esta operación puede ser costosa en tiempo
 */
@SuppressLint("SimpleDateFormat")
@Throws(Exception::class)
fun obtainUserDataServer(username: String, sessionToken: String): User? {
    val json = getJSONFromRequest("/users/$username?token=$sessionToken", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val profile = json.getJSONObject("profile")
        val name = profile.getString("user")
        val mailVisible = profile.getBoolean("mail_visible")

        var birthDate: Date? = null
        var email: String? = null

        if (mailVisible) {
            email = profile.getString("mail")
            val dateStr = profile.getString("birth_date")
            birthDate = SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
        }

        val verifiedAccount = profile.getBoolean("verified")
        val twitterAccount = profile.getString("twitter")
        val facebookAccount = profile.getString("facebook")
        val instagramAccount = profile.getString("instagram")
        val pictureLocationURI = getUserProfilePicturePath(username)

        val user = User(username, name, pictureLocationURI, verifiedAccount, email, null, birthDate)
        user.twitterAccount = twitterAccount
        user.facebookAccount = facebookAccount
        user.instagramAccount = instagramAccount
        return user
    }
}

fun obtainSongsFromUserServer(username: String): List<Song> {
    val json = getJSONFromRequest("/users/$username/songs", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")

        val songs = json.getJSONArray("songs")

        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {
            val i = songs[j]
            val songI = obtainSongFromID((i as Int).toLong())
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

fun obtainPlaylistsFromUserServer(username: String): List<Playlist> {
    //TODO-TEST
    val json = getJSONFromRequest("/user-lists/$username/lists", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error!="ok") throw Exception("Error:  $error")

        //TODO
    }
    val lista: List<Playlist> = ArrayList()
    return  lista
}

/////////////////////// FIN DE PETICIONES AUXILIARES///////////////////////////////

/*
 *                          PETICIONES DE TIPO USERS
 */

/**
 * Realiza un login en el servidor y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun doLoginServer(username: String, password: String): String {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("pass", password))
    val json = getJSONFromRequest("/users/$username/login", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error == "ok") {
            return json.getString("token")
        } else
            throw Exception("Error: $error")
    }
}

/**
 * Realiza una creación de usuario en el servidor y devuelve el token de sesión
 * Sube los datos tanto al back-end (nombre, ...) como la foto de perfil al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun doSignUpServer(user: User, context: Context): String {
    val mail = user.email
    val pass = user.password
    val name = user.name
    val birth = user.birthDate?.time.toString()
    val username = user.username

    val postData = ArrayList<Pair<String, String>>()
    if(pass!=null) postData.add(Pair("pass0", pass))
    if(pass!=null) postData.add(Pair("pass1", pass))
    if(mail!=null)postData.add(Pair("mail", mail))
    if(name!=null)postData.add(Pair("user", name))
    postData.add(Pair("birth", birth))

    // Primera llamada a sign-Up
    val json = getJSONFromRequest("/users/$username/signup", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else if (json.getString("error") != "ok") {
        throw Exception("Error: $error")
    }

    val token = json.getString("token")
    uploadUserProfilePicture(username, user.pictureLocationUri!!, context)

    //TODO: Fallo LLamada a update para actualizar cuentas twitter facebook y demás

    val twitterAccount = user.twitterAccount
    val facebookAccount = user.facebookAccount
    val instagramAccount = user.instagramAccount


    val postDataAccounts = ArrayList<Pair<String, String>>()
    if (!twitterAccount.isNullOrEmpty()) postDataAccounts.add(Pair("twitter", twitterAccount!!))
    if (!facebookAccount.isNullOrEmpty()) postDataAccounts.add(Pair("facebook", facebookAccount!!))
    if (!instagramAccount.isNullOrEmpty()) postDataAccounts.add(Pair("instagram", instagramAccount!!))

    if(postDataAccounts.size > 0){
        val jsonAccounts = getJSONFromRequest("/users/$username?token=$token", postDataAccounts, TYPE_PUT)

        if (jsonAccounts == null) {
            throw Exception("Error: Servidor no accesible")
        } else {
            val error = jsonAccounts.getString("error")
            if (error != "ok") {
                throw Exception("Error: $error")
            }
        }
    }
    return token
}

/**
 * Elimina cuenta servidor
 * Elimina los datos tanto del back-end como la carátula del servidor de almacenamiento
 */
@Throws(Exception::class)
fun doDeleteAccountServer(user: String, sessionToken: String) {
    val json = getJSONFromRequest("/users/$user?token=$sessionToken", null, TYPE_DELETE)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Actualiza cuenta
 * Sube los datos tanto al back-end (nombre, ...) como la foto de perfil al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun doUpdateAccountServer(user: User, sessionToken: String, context: Context) {
    //TODO-TEST FALLA
    val username = user.username
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))

    val mail = user.email
    val bio = user.biography
    val birth = user.birthDate

    val update = JSONObject()
    val body = JSONObject()
    body.put("username",username)
    body.put("mail",mail)
    body.put("bio",bio)
    body.put("birth_date",birth)
    update.put("updates", body)

    val json = getJSONFromRequest("/users/$username?token=$sessionToken&body={$update}", postData, TYPE_PUT)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            uploadUserProfilePicture(username, user.pictureLocationUri!!, context)
        }
    }
}

/**
 * Realiza un logout en el servidor
 * Warning: esta operación puede ser costosa en tiempo
 */
fun doLogoutServer(username: String, sessionToken: String) {
    val postData = ArrayList<Pair<String, String>>()
    // DELETE /users/{nick}/login?token={TOKEN}
    val json = getJSONFromRequest("/users/$username/login?token=$sessionToken", postData, TYPE_DELETE)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Devuelve una lista con los usuarios a los que sigue el usuario @username
 */
@Throws(Exception::class)
fun getFollowedUsersServer(username: String): List<User> {
    val json = getJSONFromRequest("/users/$username/follows", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error!="ok") throw Exception("Error")

        val users = json.getJSONArray("users")

        val usersList: MutableList<User> = ArrayList()
        for (i in 0 until users.length()) {
            val userID = users.getLong(i)
            val user = obtainUserDataServerFromID(userID, "")
            usersList.add(user!!)
        }
        return usersList.toList()
    }
}

/**
 * Devuelve true si el usuario @user es seguido por el usuario @username
 */
@Throws(Exception::class)
fun isUserFollowedByUserServer(username: String, user: String): Boolean {
    val json = getJSONFromRequest("/users/$username/follows/$user", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error == "ok") {
            return true
        } else if (error == "noFollows") {
            return false
        } else {
            throw Exception("Error: $error")
        }
    }
}


/**
 * Devuelve el numero de seguidores del usuario @username
 */
@Throws(Exception::class)
fun getNumberOfFollowersOfUserServer(username: String): Long {
    val json = getJSONFromRequest("/users/$username/followers", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error!="ok") throw Exception("Error")

        return json.getLong("size")
    }
}

/**
 * Añade el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun addFollowerToUserServer(username: String, sessionToken: String, followed: String) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))

    val json = getJSONFromRequest("/users/$username/follow/$followed", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Elimina el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun deleteFollowerToUserServer(username: String, sessionToken: String, followed: String) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))

    val json = getJSONFromRequest("/users/$username/unfollow/$followed", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/////////////////////// FIN DE PETICIONES USERS///////////////////////////////////

/*
 *                      PETTICIONES DE TIPO SONGS
 */

/**
 * Añade una reproducción a la canción @song
 */
@Throws(Exception::class)
fun addReproductionToSongServer(username: String, sessionToken: String, song: Long) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("nick", username))
    val json = getJSONFromRequest("/songs/$song/listen", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Añade la cancion @song como favorita del usuario @username
 */
@Throws(Exception::class)
fun setSongFavoutireServer(username: String, sessionToken: String, song: Long) {
    //TODO-TEST
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("songID", song.toString()))

    val json = getJSONFromRequest("/songs/user/$username/fav", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Elimina la cancion @song como favorita del usuario @username
 */
@Throws(Exception::class)
fun unSetSongFavoutireServer(username: String, sessionToken: String, song: Long) {
    //TODO-TEST
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("songID", song.toString()))

    val json = getJSONFromRequest("/songs/user/$username/unfav", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Devuelve true si la cancion @song es favorita del usuario @username
 */
@Throws(Exception::class)
fun isSongFavoutireByUserServer(username: String, sessionToken: String, song: Long): Boolean {
    //TODO-TEST
    val json = getJSONFromRequest("/songs/$username/faved/$song", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error == "ok") {
            return true;
        } else if (error == "noFav") {
            return false;
        } else {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Sube una canción al servidor y devuelve el id e la cancion creada.
 * Sube los datos tanto al back-end (título, autor, ...) como el fichero de audio y las letras en caso de que existan al servidor de
 * almacenamiento
 * Atención: El id lo ha de devolver la llamada al servidor, el id del parametro @song siempre tendra un valor indefinido
 */

@Throws(Exception::class)
fun uploadSongServer(username: String, sessionToken: String, song: Song, context: Context): Long {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("title", song.name))
    postData.add(Pair("albumID", song.album.id.toString()))
    postData.add(Pair("country", "SP"))

    val json = getJSONFromRequest("/songs/$username/create", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            val songJSOn = json.getJSONObject("song")
            val id = songJSOn.getLong("id")
            uploadSongLocation(id, song.locationUri, context)
            if (song.lyricsPath != null)
                uploadSongLyrics(id, song.lyricsPath, context)
            return id
        }
    }
}

/**
 * Elimina los datos tanto del back-end como del servidor de almacenamiento
 */
@Throws(Exception::class)
fun deleteSongServer(username: String, sessionToken: String, song: Song, context: Context) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))
    postData.add(Pair("token", sessionToken))

    val json = getJSONFromRequest("/songs/${song.id}/delete", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            deleteSongLocation(song.id, context)
            deleteSongLyrics(song.id, context)
        }
    }
}

@Throws(Exception::class)
fun obtainFavouriteSongsByUserServer(username: String, sessionToken: String): List<Song>? {
//TODO(PREGUNTAR)
    return ServerEmulator.cancionesFavoritas[username]
}

/**
 * Devuelve las canciones populares (plazo de una semana o un mes)
 */
@Throws(Exception::class)
fun obtainPopularSongsServer(cantidad: Long): List<Song> {

    // TODO:

    val json = getJSONFromRequest("songs/popular?n=$cantidad", null, TYPE_GET)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "unknownUser") {

        } else {
            throw Exception("Error: $error")
        }
    }

    val list : List<Song> = ArrayList()
    return list
}

/**
 * Devuelve la lista de maximo @cantidad canciones nuevas de los artistas seguidos
 */
@Throws(Exception::class)
fun obtainNewSongsFromFollowedArtistOfUserServer(username: String, sessionToken: String, cantidad: Long): List<Song>? {
//TODO(PREGUNTAR)
    return ServerEmulator.trends
}

/**
 * Devuelve las canciones populares (ultimo dia o ultimos dias)
 */
@Throws(Exception::class)
fun obtainTrendSongsServer(cantidad: Long): List<Song>? {
//TODO(PREGUNTAR)
    return ServerEmulator.trends
}

/**
 * Devuelve las canciones populares en el pais de origen del usuario (ultimo dia o ultimos dias)
 */
@Throws(Exception::class)
fun obtainTrendSongsInUserCountryServer(username: String, cantidad: Long): List<Song>? {
    //TODO(PREGUNTAR)
    return ServerEmulator.trends
}

/**
 * Devuelve la última canción escuchada por el usuario en alguna de sus sesiones
 */
@Throws(Exception::class)
fun obtainLastSongListenedServer(username: String, sessionToken: String): Song? {
//TODO(PREGUNTAR)
    return ServerEmulator.songList[1]
}

/////////////////////// FIN DE PETICIONES SONGS///////////////////////////////////

/*
 *                      PETICIONES DE TIPO PLAYLISTS
 */

/**
 * Devuelve una lista con las playlist que sigue el usuario @username
 */

@Throws(Exception::class)
fun getFollowedPlaylistsServer(username: String): List<Playlist> {
    //TODO(PREGUNTAR)
    return ServerEmulator.playlistSeguidos[username]!!
}

/**
 * Devuelve true si la playlist @playlist es seguida por el usuario @username
 */
@Throws(Exception::class)
fun isPlaylistFollowedByUserServer(username: String, playlist: Long): Boolean {
    //TODO(PREGUNTAR)
    for (i in ServerEmulator.playlistSeguidos[username]!!) {
        if (i.id == playlist) return true
    }
    return false
}


/**
 * Devuelve el numero de seguidores de la playlist @playlist
 */
@Throws(Exception::class)
fun getNumberOfFollowersOfPlaylistServer(playlist: Long): Long {
//TODO(PREGUNTAR)
    return 8
}

/**
 * Añade el usuario @username como seguidor de la playlist @followed
 */

@Throws(Exception::class)
fun addFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long) {
    // TODO
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))

    val json = getJSONFromRequest("/user-lists/$username/$followed/follow", postData, TYPE_PUT)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Elimina el usuario @username como seguidor de la playlist @followed
 */

@Throws(Exception::class)
fun deleteFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long) {
    // TODO
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))

    val json = getJSONFromRequest("/user-lists/$username/$followed/unfollow", postData, TYPE_DELETE)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

/**
 * Obtiene la información asociada a la playlist con id @id
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun obtainPlaylistDataServer(id: Long): Playlist? {
    // TODO
    val json = getJSONFromRequest("/user-lists/$id", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val title = json.getString("title")
        val author = json.getString("author")
        val creation_time = json.getLong("creation_time")
        val songs_size = json.getLong("songs_size")
        val songs = json.getJSONArray("songs")
        val users = json.getJSONArray("users")

        val songsIDlist: MutableList<Long> = ArrayList()
        for (i in 0..songs.length() - 1) {
            songsIDlist.add(songs.getLong(i))
        }

        val songslist: MutableList<Song> = ArrayList()
        for (i in 0..songsIDlist.size - 1) {
            songslist.add(obtainSongFromID(songsIDlist.get(i))!!)
        }

        val usersIDlist: MutableList<Long> = ArrayList()
        for (i in 0..songs_size - 1) {
            usersIDlist.add(songs.getLong(i.toInt()))
        }

        val user = obtainUserDataServer(author, "")
        val albumpath = getPlaylistCoverPath(id)

        val playlist = Playlist(id, title, user!!, albumpath, songslist.toList())
        return playlist
    }
}

/**
 * Devuelve la lista de maximo @cantidad playlists seguidas que se han actualizado
 */
@Throws(Exception::class)
fun obtainUpdatedPlaylistsFollowedByUserServer(username: String, sessionToken: String, cantidad: Long): List<Playlist>? {
//TODO(PREGUNTAR)
    return ServerEmulator.playlistSeguidos[username]
}

/**
 * Crea una lista en el servidor
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun createPlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context): Long {
//TODO(PREGUNTAR)
    val position = ServerEmulator.playlistList.size + 4
    playlist.id = position.toLong()
    ServerEmulator.playlistList[playlist.id!!] = playlist
    return playlist.id!!
}

/**
 * Elimina una lista en el servidor
 * Elimina los datos tanto en el back-end como la carátula en el servidor de almacenamiento
 */
@Throws(Exception::class)
fun deletePlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context) {
//TODO(PREGUNTAR)
    if (ServerEmulator.playlistList.containsKey(playlist.id) && ServerEmulator.playlistList[playlist.id]!!.creator.username.equals(username)) {
        ServerEmulator.playlistList.remove(playlist.id)
        for (i in ServerEmulator.playlistSeguidos.values) {
            for (j in i) {
                if (j.id == playlist.id) {
                    i.remove(j)
                    break
                }
            }
        }
    } else {
        throw Exception("Error")
    }
}

/**
 * Actualiza una lista en el servidor
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun updatePlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context) {
//TODO(PREGUNTAR)
}

/////////////////////// FIN DE PETICIONES PLAYLISTS///////////////////////////////

/*
 *                      PETICIONES DE TIPO ALBUM
 */

@Throws(Exception::class)
fun obtainAlbumsFromUserServer(username: String): List<Album> {
    val json = getJSONFromRequest("/users/$username/albums", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error!="ok") throw Exception("Error: $error")

        val albums = json.getJSONArray("albums")

        val list: MutableList<Album> = ArrayList()

        if (albums != null) {
            for (i in 0..albums.length()-1) {
                val albumID = albums.getLong(i)
                val album = obtainAlbumFromID(albumID)
                if(album!=null)list.add(album)
            }
        }
        return list.toList()
    }
}

/**
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun createAlbumsServer(username: String, sessionToken: String, album: Album, context: Context): Long {
    val title = album.name
    val year = album.releaseDate.get(Calendar.YEAR)

    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("title", title))
    postData.add(Pair("year", year.toString()))

    val json = getJSONFromRequest("/albums/$username/create", postData, TYPE_POST)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            val objet = json.getJSONObject("album")
            uploadAlbumCover(objet.getLong("id"),album.artLocationUri,context)
            return objet.getLong("id")

        }
    }
}

/////////////////////// FIN DE PETICIONES ALBUM///////////////////////////////////

/*
 *                      PETICIONES DE TIPO OAUTH
 */

/**
 * Realiza un login en el servidor mediante google y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun doGoogleLoginServer(serverAuthCode: String): String {
    // TODO
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("token",serverAuthCode))
    val json = getJSONFromRequest("/oauth/login", null, TYPE_GET)


    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error == "ok") {
            return json.getString("token")
        } else
            throw Exception("Error: $error")
    }
}

/////////////////////// FIN DE PETICIONES OAUTH///////////////////////////////////

/*
 *                      PETICIONES GLOBALES
 */

/**
 * Devuelve una lista de @cantidad de Artistas, Playlist y Canciones recomendadas para el usuario @username
 */
@Throws(Exception::class)
fun obtainRecomendationsForUserServer(username: String, sessionToken: String, cantidad: Long): List<Recommendation>? {
//TODO(PREGUNTAR)
    return ServerEmulator.recomendaciones[username]
}

/**
 * Devuelve la lista de maximo @cantidad resultados a la consulta @query
 * Si tipo es null devuelve todos los resultados a la query realizada, si tipo es 1 devuelve solamente canciones, si tipo = 2
 * devuelve solamente playlists y si tipo = 3 devuelve solamente autores
 */
@Throws(Exception::class)
fun obtainResultForQueryServer(cantidad: Long, query: String, tipo: Int?): List<Recommendation>? {
//TODO(PREGUNTAR)
    return ServerEmulator.trends
}

/**
 * Devuelve una lista compuesta por pares nombre del genero, canciones populares. Las canciones populares
 * tendrán un máximo de @cantidad canciones
 */
@Throws(Exception::class)
fun obtainPopularByGenreServer(cantidad: Long): List<Pair<String, List<Recommendation>>>? {
//TODO(PREGUNTAR)
    return ServerEmulator.generos
}

/**
 * Devuelve true si el usuario tiene una sesión abierta
 */
@Throws(Exception::class)
fun isOtherSessionOpenFromSameUserServer(username: String, sessionToken: String): Boolean {
//TODO(PREGUNTAR)
    return false
}

@Throws(Exception::class)
fun obtainGeneresServer(): List<String> {
    //TODO(PREGUNTAR)
    return ServerEmulator.generesList
}

///////////////////// FIN PETICIONES GLOBALES /////////////////////////////////////////