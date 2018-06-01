package com.spreadyourmusic.spreadyourmusic.apis

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.helpers.*
import com.spreadyourmusic.spreadyourmusic.models.*
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

//region Server Info and Low level functions
//---------------------------------------------------------------------------------------

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
    return fetchJSONFromUrl(serverAddress + urlString, postData, type)
}

//---------------------------------------------------------------------------------------
//endregion

//region Data-Server requests
//---------------------------------------------------------------------------------------

/**
 * Devuelve true si la dirección produce respuesta
 */
private fun checkIfResourceExistInServer(url: String): Boolean {
    val u = URL(url)
    val huc = u.openConnection() as HttpURLConnection
    huc.requestMethod = "HEAD"
    huc.connect()
    return huc.responseCode == HttpURLConnection.HTTP_OK
}

/**
 * Devuelve la dirección de las letras de una canción
 */
private fun getSongLyricsPath(songId: Long): String? {
    val dir = "$dataServerAdress/Public/$songLyricsUploadPrefix$songId"
    return if (checkIfResourceExistInServer(dir)) dir else null
}

/**
 * Devuelve la dirección del archivo de música de una canción
 */
private fun getSongLocationPath(songId: Long): String {
    val dir = "$dataServerAdress/Public/$songLocationUploadPrefix$songId"
    return if (checkIfResourceExistInServer(dir)) dir
    // En caso de que no se pueda acceder a la canción se devuelve una genérica
    else "http://storage.googleapis.com/automotive-media/The_Messenger.mp3"
}

/**
 * Devuelve la dirección de la foto de perfil de un usuario
 */
private fun getUserProfilePicturePath(username: String): String? {
    val dir = "$dataServerAdress/Public/$userUploadPrefix$username"
    return if (checkIfResourceExistInServer(dir)) dir else null
}

/**
 * Devuelve la dirección de la foto de portada de una playlist
 */
private fun getPlaylistCoverPath(playlistId: Long): String? {
    val dir = "$dataServerAdress/Public/$playlistUploadPrefix$playlistId"
    return if (checkIfResourceExistInServer(dir)) dir else null
}

/**
 * Devuelve la dirección de la foto de portada de un álbum
 */
private fun getAlbumCoverPath(albumId: Long): String? {
    val dir = "$dataServerAdress/Public/$albumUploadPrefix$albumId"
    return if (checkIfResourceExistInServer(dir)) dir else null
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
    Thread.sleep(300)

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
    Thread.sleep(3000)

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
    Thread.sleep(1000)

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
    Thread.sleep(1000)

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
    Thread.sleep(1000)

    return true
}

//---------------------------------------------------------------------------------------
//endregion

//region Back-End User requests
//---------------------------------------------------------------------------------------

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
    if (pass != null) postData.add(Pair("pass0", pass))
    if (pass != null) postData.add(Pair("pass1", pass))
    if (mail != null) postData.add(Pair("mail", mail))
    if (name != null) postData.add(Pair("user", name))
    postData.add(Pair("birth", birth))

    // Primera llamada a sign-Up
    val json = getJSONFromRequest("/users/$username/signup", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }


    val token = json.getString("token")
    uploadUserProfilePicture(username, user.pictureLocationUri!!, context)

    val twitterAccount = user.twitterAccount
    val facebookAccount = user.facebookAccount
    val instagramAccount = user.instagramAccount


    val postDataAccounts = ArrayList<Pair<String, String>>()
    postDataAccounts.add(Pair("token", token))

    val accounts = ArrayList<Pair<String, String>>()
    if (!twitterAccount.isNullOrEmpty()) accounts.add(Pair("twitter", twitterAccount!!))
    if (!facebookAccount.isNullOrEmpty()) accounts.add(Pair("facebook", facebookAccount!!))
    if (!instagramAccount.isNullOrEmpty()) accounts.add(Pair("instagram", instagramAccount!!))

    if (accounts.size > 0) {
        val arrayBody = accounts.map {
            "${it.first}:\"${it.second}\""
        }.reduce { acc, s ->
            "$acc,$s"
        }
        val c = "{\"updates\":{$arrayBody}}"
        val jsonAccounts = getJSONFromRequest("/users/$username?token=$token&body=$c", null, TYPE_PUT)
        if (jsonAccounts == null) {
            throw Exception("Error: Servidor no accesible")
        } else {
            var errorCapturado: String? = null
            try {
                errorCapturado = jsonAccounts.getString("error")
            } catch (e: Exception) {

            }
            if (errorCapturado != null) {
                throw Exception("Error: $errorCapturado")
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
fun doDeleteAccountServer(user: String, sessionToken: String, context: Context) {
    val json = getJSONFromRequest("/users/$user?token=$sessionToken", null, TYPE_DELETE)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            deleteUserProfilePicture(user, context)
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
    val username = user.username
    val oldUserData = obtainUserDataServer(username, sessionToken)
    val updateData = ArrayList<Pair<String, String>>()
    if (oldUserData!!.birthDate != user.birthDate) updateData.add(Pair("birth_date", user.birthDate?.time.toString()))
    if (oldUserData.name != user.name) updateData.add(Pair("user", user.name ?: ""))
    if (oldUserData.email != user.email) updateData.add(Pair("mail", user.email ?: ""))
    if (oldUserData.twitterAccount != user.twitterAccount) updateData.add(Pair("twitter", user.twitterAccount
            ?: ""))
    if (oldUserData.facebookAccount != user.facebookAccount) updateData.add(Pair("instagram", user.facebookAccount
            ?: ""))
    if (oldUserData.instagramAccount != user.instagramAccount) updateData.add(Pair("facebook", user.instagramAccount
            ?: ""))

    if (updateData.size > 0) {
        val arrayBody = updateData.map {
            "${it.first}:\"${it.second}\""
        }.reduce { acc, s ->
            "$acc,$s"
        }
        val c = "{\"updates\":{$arrayBody}}"
        val jsonAccounts = getJSONFromRequest("/users/$username?token=$sessionToken&body=$c", null, TYPE_PUT)
        if (jsonAccounts == null) {
            throw Exception("Error: Servidor no accesible")
        } else {
            var errorCapturado: String? = null
            try {
                errorCapturado = jsonAccounts.getString("error")
            } catch (e: Exception) {

            }
            if (errorCapturado != null) {
                throw Exception("Error: $errorCapturado")
            }
        }
    }
    if (oldUserData.pictureLocationUri != user.pictureLocationUri) {
        deleteUserProfilePicture(username, context)
        if (user.pictureLocationUri != null)
            uploadUserProfilePicture(username, user.pictureLocationUri!!, context)
    }
}

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
 * Realiza un logout en el servidor
 * Warning: esta operación puede ser costosa en tiempo
 */
fun doLogoutServer(username: String, sessionToken: String) {
    val postData = ArrayList<Pair<String, String>>()
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
 * Obtiene la información asociada al usuario con id @userID
 * Warning: esta operación puede ser costosa en tiempo
 */
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
        if (error != "ok") throw Exception("Error")

        val users = json.getJSONArray("users")

        val usersList = ArrayList<User>()
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
    return if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        when (error) {
            "ok" -> true
            "notFollows" -> false
            else -> throw Exception("Error: $error")
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
        if (error != "ok") throw Exception("Error: $error")
        else return json.getLong("size")
    }
}

//---------------------------------------------------------------------------------------
//endregion

//region Back-End Album requests
//---------------------------------------------------------------------------------------

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
            uploadAlbumCover(objet.getLong("id"), album.artLocationUri!!, context)
            return objet.getLong("id")

        }
    }
}

/**
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

/**
 * Obtiene los albumes del usuario @username
 */
@Throws(Exception::class)
fun obtainAlbumsFromUserServer(username: String): List<Album> {
    val json = getJSONFromRequest("/users/$username/albums", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error: $error")

        val albums = json.getJSONArray("albums")

        val list = ArrayList<Album>()
        for (i in 0 until albums.length()) {
            val albumID = albums.getLong(i)
            val album = obtainAlbumFromID(albumID)
            if (album != null) list.add(album)
        }
        return list.toList()
    }
}

//---------------------------------------------------------------------------------------
//endregion

//region Back-End Songs requests
//---------------------------------------------------------------------------------------

/**
 * Sube una canción al servidor y devuelve el id e la cancion creada.
 * Sube los datos tanto al back-end (título, autor, ...) como el fichero de audio y las letras en caso de que existan al servidor de
 * almacenamiento
 * Atención: El id lo ha de devolver la llamada al servidor, el id del parametro @song siempre tendra un valor indefinido
 */

@Throws(Exception::class)
fun uploadSongServer(username: String, sessionToken: String, song: Song, context: Context): Long {
    val country: String
    val jsonUserData = getJSONFromRequest("/users/$username?token=$sessionToken", null, TYPE_GET)
    if (jsonUserData == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = jsonUserData.getBoolean("error")
        if (error) throw Exception("Error")
        val profile = jsonUserData.getJSONObject("profile")
        country = profile.getString("country")
    }
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("nick", username))
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("title", song.name))
    postData.add(Pair("albumID", song.album.id.toString()))
    postData.add(Pair("country", country))
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

/**
 * Obtiene la canción con id @id
 */
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
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("songId", song.toString()))

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
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("songId", song.toString()))

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
    val json = getJSONFromRequest("/songs/user/$username/faved/$song", null, TYPE_GET)
    return if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        when (error) {
            "ok" -> true
            "noFav" -> false
            else -> throw Exception("Error: $error")
        }
    }
}


@Throws(Exception::class)
fun obtainFavouriteSongsByUserServer(username: String, sessionToken: String): List<Song>? {
    val json = getJSONFromRequest("/songs/user/$username/faved", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")

        val songs = json.getJSONArray("songs")
        val songList = ArrayList<Song>()
        for (i in 0 until songs.length()) {

            val songI = obtainSongFromID((songs[i] as Int).toLong())
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Devuelve las canciones populares (plazo de una semana o un mes)
 */
@Throws(Exception::class)
fun obtainPopularSongsServer(amount: Long): List<Song> {
    val json = getJSONFromRequest("/songs/popular?n=$amount", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val songs = json.getJSONArray("songs")
        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = songs[j] as JSONObject
            val i = k.getLong("id")

            val songI = obtainSongFromID(i)
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Devuelve la lista de maximo @cantidad canciones nuevas de los artistas seguidos
 */
@Throws(Exception::class)
fun obtainNewSongsFromFollowedArtistOfUserServer(username: String, sessionToken: String, amount: Long): List<Song>? {
    val json = getJSONFromRequest("/users/$username/feed", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val songs = json.getJSONObject("follow").getJSONArray("data")
        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = songs[j] as JSONObject
            val i = k.getLong("id")

            val songI = obtainSongFromID(i)
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Devuelve las canciones populares (ultimo dia o ultimos dias)
 */
@Throws(Exception::class)
fun obtainTrendSongsServer(amount: Long): List<Song>? {
    val json = getJSONFromRequest("/songs/popular?n=$amount&daily=true", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val songs = json.getJSONArray("songs")
        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = songs[j] as JSONObject
            val i = k.getLong("id")

            val songI = obtainSongFromID(i)
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Devuelve las canciones populares en el pais de origen del usuario (ultimo dia o ultimos dias)
 */
@Throws(Exception::class)
fun obtainTrendSongsInUserCountryServer(username: String, sessionToken: String, amount: Long): List<Song>? {
    val jsonUser = getJSONFromRequest("/users/$username?token=$sessionToken", null, TYPE_GET)

    val country: String
    // Se obtiene el pais del usuario
    if (jsonUser == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = jsonUser.getBoolean("error")
        if (error) throw Exception("Error")
        else {
            val profile = jsonUser.getJSONObject("profile")
            country = profile.getString("country")
        }
    }

    // Se obtienen las canciones mas populares en el pais del usuario
    val json = getJSONFromRequest("/songs/popular/$country/?n=$amount", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val songs = json.getJSONArray("songs")
        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = songs[j] as JSONObject
            val i = k.getLong("id")

            val songI = obtainSongFromID(i)
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Devuelve la última canción escuchada por el usuario en alguna de sus sesiones
 */
@Throws(Exception::class)
fun obtainLastSongListenedServer(username: String, sessionToken: String): Song? {
    val json = getJSONFromRequest("/users/$username/songs/lastListened", null, TYPE_GET)
    return if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val songs = json.getJSONArray("songs")
        if (songs.length() > 0) {
            val songId = songs.getLong(0)
            obtainSongFromID(songId)
        } else {
            null
        }
    }
}

//---------------------------------------------------------------------------------------
//endregion

//region Back-End Playlist requests
//---------------------------------------------------------------------------------------

/**
 * Añade la canción @songId a la playlist @playlistId
 */
private fun addSongToPlaylistServer(username: String, sessionToken: String, playlistId: Long, songId: Long) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("nick", username))
    postData.add(Pair("songId", "$songId"))
    val json = getJSONFromRequest("/user-lists/$username/$playlistId/add", postData, TYPE_POST)
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
 * Elimina la canción @songId de la playlist @playlistId
 */
private fun removeSongFromPlaylistServer(username: String, sessionToken: String, playlistId: Long, songId: Long) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("nick", username))
    postData.add(Pair("songId", "$songId"))
    val json = getJSONFromRequest("/user-lists/$username/$playlistId/remove", postData, TYPE_POST)
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
 * Crea una lista en el servidor
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun createPlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context): Long {
    val title = playlist.name
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    postData.add(Pair("title", title))
    val json = getJSONFromRequest("/user-lists/$username/create", postData, TYPE_PUT)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            val objet = json.getJSONObject("list")
            if (!playlist.artLocationUri.isNullOrEmpty())
                uploadPlaylistCover(objet.getLong("id"), playlist.artLocationUri!!, context)
            val id = objet.getLong("id")
            for (song in playlist.content) {
                addSongToPlaylistServer(username, sessionToken, id, song.id)
            }
            return id
        }
    }
}

/**
 * Elimina una lista en el servidor
 * Elimina los datos tanto en el back-end como la carátula en el servidor de almacenamiento
 */
@Throws(Exception::class)
fun deletePlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context) {
    val postData = ArrayList<Pair<String, String>>()
    //postData.add(Pair("listid", playlist.id.toString()))
    //postData.add(Pair("token", sessionToken))
    val json = getJSONFromRequest("/user-lists/$username/delete?token=$sessionToken&listid=${playlist.id}", postData, TYPE_DELETE)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        } else {
            deletePlaylistCover(playlist.id!!, context)
        }
    }
}

/**
 * Actualiza una lista en el servidor
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun updatePlaylistServer(username: String, sessionToken: String, playlist: Playlist, context: Context) {
    val id = playlist.id ?: throw Exception("Error: Id Playlist nulo")
    val originalSongs = obtainPlaylistDataServer(id)!!.content
    val originalSongsList = ArrayList<Long>()
    val newSongsList = ArrayList<Long>()
    for (i in originalSongs) {
        originalSongsList.add(i.id)
    }
    for (i in playlist.content) {
        newSongsList.add(i.id)
    }
    val cancionesEliminar = ArrayList<Long>()
    val cancionesPoner = ArrayList<Long>()
    for (i in originalSongsList) {
        if (!newSongsList.contains(i)) {
            cancionesEliminar.add(i)
        }
    }
    for (i in newSongsList) {
        if (!originalSongsList.contains(i)) {
            cancionesPoner.add(i)
        }
    }
    for (i in cancionesPoner) {
        addSongToPlaylistServer(username, sessionToken, id, i)
    }
    for (i in cancionesEliminar) {
        removeSongFromPlaylistServer(username, sessionToken, id, i)
    }
}


/**
 * Obtiene la información asociada a la playlist con id @id
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun obtainPlaylistDataServer(id: Long): Playlist? {
    val json = getJSONFromRequest("/user-lists/$id", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error: $error")

        val interJson = json.getJSONObject("list")

        val title = interJson.getString("title")
        val author = interJson.getLong("author")

        val songs = interJson.getJSONArray("songs")
        val songList = ArrayList<Song>()

        for (j in 0 until songs.length()) {
            val k = songs[j] as Int
            val songI = obtainSongFromID(k.toLong())
            if (songI != null)
                songList.add(songI)
        }
        val autor = obtainUserDataServerFromID(author, "")
                ?: throw Exception("Error: Autor no encontrado")

        return Playlist(id, title, autor, getPlaylistCoverPath(id), songList)
    }
}

/**
 * Añade el usuario @username como seguidor de la playlist @followed
 */
@Throws(Exception::class)
fun addFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long) {
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
    val postData = ArrayList<Pair<String, String>>()
    //postData.add(Pair("token", sessionToken))
    val json = getJSONFromRequest("/user-lists/$username/$followed/unfollow?token=$sessionToken", postData, TYPE_DELETE)
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
 * Devuelve true si la playlist @playlist es seguida por el usuario @username
 */
@Throws(Exception::class)
fun isPlaylistFollowedByUserServer(username: String, playlist: Long): Boolean {
    val json = getJSONFromRequest("/user-lists/$username/follows/$playlist", null, TYPE_GET)
    return if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        when (error) {
            "ok" -> true
            "notFollows" -> false
            else -> throw Exception("Error: $error")
        }
    }
}

/**
 * Devuelve el numero de seguidores de la playlist @playlist
 */
@Throws(Exception::class)
fun getNumberOfFollowersOfPlaylistServer(playlist: Long): Long {
    val json = getJSONFromRequest("/user-lists/$playlist", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error: $error")
        val interJson = json.getJSONObject("list")
        val followers = interJson.getJSONArray("followers")
        return followers.length().toLong()
    }
}

/**
 * Devuelve una lista con las playlist que sigue el usuario @username
 */
@Throws(Exception::class)
fun getFollowedPlaylistsServer(username: String): List<Playlist> {
    val json = getJSONFromRequest("/user-lists/$username/following", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val ids = json.getJSONArray("id")
        val playlistList = ArrayList<Playlist>()
        for (i in 0 until ids.length()) {
            val id = ids[i] as Int
            val playlist = obtainPlaylistDataServer(id.toLong())
            if (playlist != null)
                playlistList.add(playlist)
        }
        return playlistList
    }
}

/**
 * Devuelve la lista de maximo @cantidad playlists seguidas que se han actualizado
 */
@Throws(Exception::class)
fun obtainUpdatedPlaylistsFollowedByUserServer(username: String, sessionToken: String, cantidad: Long): List<Playlist>? {
    return getFollowedPlaylistsServer(username)

    //TODO HACER (feed Falla BE)

}

//---------------------------------------------------------------------------------------
//endregion

//region Back-End Oauth requests
//---------------------------------------------------------------------------------------

/**
 * Realiza un login en el servidor mediante google y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun doGoogleLoginServer(serverAuthCode: String): String {
    // TODO: Esperar especificaicon Pinilla
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", serverAuthCode))
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

//---------------------------------------------------------------------------------------
//endregion

//region Back-End Complex requests
//---------------------------------------------------------------------------------------


/**
 * Obtiene las playlists asociadas al usuario con nick @username
 */
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

/**
 * Obtiene las playlists asociadas al usuario con nick @username
 */
fun obtainPlaylistsFromUserServer(username: String): List<Playlist> {
    val json = getJSONFromRequest("/user-lists/$username/lists", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") throw Exception("Error:  $error")
        val lists = json.getJSONArray("lists")

        val playlistList = ArrayList<Playlist>()
        for (j in 0 until lists.length()) {
            val k = lists[j] as Int
            val playlistId = obtainPlaylistDataServer(k.toLong())
            if (playlistId != null)
                playlistList.add(playlistId)
        }
        return playlistList
    }
}

/**
 * Devuelve una lista de @cantidad de Artistas, Playlist y Canciones recomendadas para el usuario @username
 */
@Throws(Exception::class)
fun obtainRecomendationsForUserServer(username: String, sessionToken: String, cantidad: Long): List<Recommendation>? {
//TODO: Esperar especificacion
    return obtainPopularSongsServer(cantidad)
}

/**
 * Funcion auxiliar para obtainResultForQuery
 */
@Throws(Exception::class)
private fun obtainResultForQueryInSongServer(amount: Long, query: String): List<Song> {
    val json = getJSONFromRequest("/songs/search/?query=$query&n=$amount", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val songs = json.getJSONArray("songs")
        val songList = ArrayList<Song>()
        for (j in 0 until songs.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = songs[j] as JSONObject
            val i = k.getLong("id")

            val songI = obtainSongFromID(i)
            if (songI != null)
                songList.add(songI)
        }
        return songList
    }
}

/**
 * Funcion auxiliar para obtainResultForQuery
 */
@Throws(Exception::class)
private fun obtainResultForQueryInUsersServer(amount: Long, query: String): List<User> {
    val json = getJSONFromRequest("/users/search/?query=$query&n=$amount", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val users = json.getJSONArray("users")
        val usersList = ArrayList<User>()
        for (j in 0 until users.length()) {

            // Warning: Aunque la respuesta devuelve ya el objeto canción completo, paea evitar complicar el código
            // se vuelve a llamar a la rest api para obtener a partir del id toda la información. En caso de que el
            // rendimiento sea muy pobre, esta llamada se puede rehacer
            val k = users[j] as JSONObject
            val i = k.getLong("id")

            val userI = obtainUserDataServerFromID(i, "")
            if (userI != null)
                usersList.add(userI)
        }
        return usersList
    }
}

/**
 * Funcion auxiliar para obtainResultForQuery
 */
@Throws(Exception::class)
private fun obtainResultForQueryInPlaylistsServer(cantidad: Long, query: String): List<Playlist> {
// TODO: Esperar nueva especificacion
    return ArrayList<Playlist>()
}

/**
 * Devuelve la lista de maximo @cantidad resultados a la consulta @query
 * Si tipo es null devuelve todos los resultados a la query realizada, si tipo es 1 devuelve solamente canciones, si tipo = 2
 * devuelve solamente playlists y si tipo = 3 devuelve solamente autores
 */
@Throws(Exception::class)
fun obtainResultForQueryServer(cantidad: Long, query: String, type: Int?): List<Recommendation>? {
    val list = ArrayList<Recommendation>()
    when (type) {
        1 -> list.addAll(obtainResultForQueryInSongServer(cantidad, query))
        2 -> list.addAll(obtainResultForQueryInPlaylistsServer(cantidad, query))
        3 -> list.addAll(obtainResultForQueryInUsersServer(cantidad, query))
        else -> {
            list.addAll(obtainResultForQueryInSongServer(cantidad, query))
            list.addAll(obtainResultForQueryInPlaylistsServer(cantidad, query))
            list.addAll(obtainResultForQueryInUsersServer(cantidad, query))
        }
    }
    return list
}

/**
 * Devuelve una lista compuesta por pares nombre del genero, canciones populares. Las canciones populares
 * tendrán un máximo de @cantidad canciones
 */
@Throws(Exception::class)
fun obtainPopularByGenreServer(cantidad: Long): List<Pair<String, List<Recommendation>>>? {
//TODO: Esperar Pinilla lo haga
    val devolver = ArrayList<Pair<String, List<Recommendation>>>()
    for (i in obtainGeneresServer()) {
        devolver.add(Pair(i, obtainTrendSongsServer(cantidad)!!))
    }
    return ArrayList()
}

/**
 * Devuelve true si el usuario tiene una sesión abierta
 */
@Throws(Exception::class)
fun isOtherSessionOpenFromSameUserServer(username: String, sessionToken: String): Boolean {
    val json = getJSONFromRequest("/users/$username/session_open?token=$sessionToken", null, TYPE_GET)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error == "ok") {
            return true
        } else if (error == "sessionClosed") {
            return false
        } else {
            throw Exception("Error: $error")
        }
    }
}

@Throws(Exception::class)
fun obtainGeneresServer(): List<String> {
    //TODO: Esperar Pinilla lo haga
    val devolver = ArrayList<String>()
    devolver.add("Trap")
    devolver.add("Rap")
    return devolver
}

//---------------------------------------------------------------------------------------
//endregion