package com.spreadyourmusic.spreadyourmusic.apis

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.R.string.password
import com.spreadyourmusic.spreadyourmusic.helpers.TYPE_POST
import com.spreadyourmusic.spreadyourmusic.helpers.TYPE_PUT
import com.spreadyourmusic.spreadyourmusic.helpers.fetchJSONFromUrl
import com.spreadyourmusic.spreadyourmusic.models.*
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService
import com.spreadyourmusic.spreadyourmusic.test.ServerEmulator
import org.json.JSONObject
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
private fun getJSONFromRequest(urlString: String, postData: List<Pair<String, String>>?, type:Int): JSONObject? {
    return if (postData == null) fetchJSONFromUrl(serverAddress + urlString,type)
    else fetchJSONFromUrl(serverAddress + urlString, postData,type)
}

/**
 * Devuelve la dirección de las letras de una canción
 */
private fun getSongLyricsPath(songId: Long): String {
    return "$dataServerAdress/$songLyricsUploadPrefix$songId"
}

/**
 * Devuelve la dirección del archivo de música de una canción
 */
private fun getSongLocationPath(songId: Long): String {
    return "$dataServerAdress/$songLocationUploadPrefix$songId"
}

/**
 * Devuelve la dirección de la foto de perfil de un usuario
 */
private fun getUserProfilePicturePath(username: String): String {
    return "$dataServerAdress/$userUploadPrefix$username"
}

/**
 * Devuelve la dirección de la foto de portada de una playlist
 */
private fun getPlaylistCoverPath(playlistId: Long): String {
    return "$dataServerAdress/$playlistUploadPrefix$playlistId"
}

/**
 * Devuelve la dirección de la foto de portada de un álbum
 */
private fun getAlbumCoverPath(albumId: Long): String {
    return "$dataServerAdress/$albumUploadPrefix$albumId"
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
 *
 *  REQUESTS
 */

/*
 * USERS
 */

/**
 * Realiza un login en el servidor y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */

@Throws(Exception::class)
fun doLoginServer(username: String, password: String): String {
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("pass",password))
    val json = getJSONFromRequest("/users/$username/login", postData, TYPE_PUT)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if(error == "ok"){
            return json.getString("token")
        }else
            throw Exception("Error: $error")
    }
}

/**
 * Realiza un login en el servidor mediante google y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun doGoogleLoginServer(serverAuthCode: String): String {
    //TODO(PREGUNTAR)
    throw Exception("Error")
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
    val user_ = user.name
    val birth = user.birthDate?.time?.toString()
    val bio = user.biography
    val username = user.username

    val postData = ArrayList<Pair<String,String>>()

    postData.add(Pair("mail",mail!!))
    postData.add(Pair("pass0",pass!!))
    postData.add(Pair("pass1", pass))
    if(user_!=null)postData.add(Pair("user", user_))
    if(birth!=null)postData.add(Pair("birth",birth))
    postData.add(Pair("bio",bio!!))

    val json = getJSONFromRequest("/users/$username/login", postData, TYPE_POST)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if(error == "ok"){
            return json.getString("token")
        }else
            throw Exception("Error: $error")
    }
}

/**
 * Elimina cuenta servidor
 * Elimina los datos tanto del back-end como la carátula del servidor de almacenamiento
 */
@Throws(Exception::class)
fun doDeleteAccountServer(user: String, sessionToken: String) {
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("token",sessionToken))
    val json = getJSONFromRequest("/users/$user/login", postData,TYPE_DELETE)

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
 * Elimina cuenta servidor
 * Sube los datos tanto al back-end (nombre, ...) como la foto de perfil al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun doUpdateAccountServer(user: User, sessionToken: String, context: Context) {
    //TODO(PREGUNTAR)
    val username = user.username
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("nick",username))

    val json = getJSONFromRequest("/users/$username?token=$sessionToken", postData,TYPE_PUT)

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
 * Realiza un logout en el servidor
 * Warning: esta operación puede ser costosa en tiempo
 */
fun doLogoutServer(username: String, sessionToken: String) {
    val postData = ArrayList<Pair<String, String>>()
    postData.add(Pair("token", sessionToken))
    val json = getJSONFromRequest("/users/$username/login", postData)
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
 * Obtiene la información asociada al usuario con nick @username
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun obtainUserDataServer(username: String, sessionToken: String): User? {
    val json = getJSONFromRequest("/users/$username?token=$sessionToken", null)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val profile = json.getJSONObject("profile")
        val name = profile.getString("user")
        val email = profile.getString("mail")
        val biography = profile.getString("bio")

        //val dateStr = profile.getString("birth_date")

        /*
        TODO: Hacer en lo que se especifique
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val birthDate = sdf.parse(dateStr)*/

        val birthDate = null

        val verifiedAccount = profile.getBoolean("verified")
        val twitterAccount = profile.getString("twitter")
        val facebookAccount = profile.getString("facebook")
        val instagramAccount = profile.getString("instagram")
        val pictureLocationURI = getUserProfilePicturePath(username)

        val user = User(username, name, pictureLocationURI, verifiedAccount, email, biography, birthDate)
        user.twitterAccount = twitterAccount
        user.facebookAccount = facebookAccount
        user.instagramAccount = instagramAccount
        return user
    }
}

fun obtainSongsFromUserServer(username: String): List<Song>? {
    return null
}

fun obtainPlaylistsFromUserServer(username: String): List<Playlist> {
    //TODO:
    val resultado = ArrayList<Playlist>()
    for (i in ServerEmulator.playlistList) {
        if (i.value.creator.username == username) {
            resultado.add(i.value)
        }
    }
    return resultado
}

/**
 * Devuelve una lista con los usuarios a los que sigue el usuario @username
 */
@Throws(Exception::class)
fun getFollowedUsersServer(username: String): List<User> {
    //TODO:
    if (ServerEmulator.artistasSeguidos.containsKey(username)) {
        return ServerEmulator.artistasSeguidos[username]!!
    } else {
        return ArrayList()
    }
}

/**
 * Devuelve true si el usuario @user es seguido por el usuario @username
 */
@Throws(Exception::class)
fun isUserFollowedByUserServer(username: String, user: String): Boolean {
    //TODO(PREGUNTAR)
    return false;
}


/**
 * Devuelve el numero de seguidores del usuario @username
 */
@Throws(Exception::class)
fun getNumberOfFollowersOfUserServer(username: String): Long {
    //TODO(PREGUNTAR)
    return 1;
}

/**
 * Añade el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun addFollowerToUserServer(username: String, sessionToken: String, followed: String) {
    //TODO(PREGUNTAR)
    val seguido = ServerEmulator.userList[followed]
    ServerEmulator.artistasSeguidos[username]!!.add(seguido!!)
}

/**
 * Elimina el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun deleteFollowerToUserServer(username: String, sessionToken: String, followed: String) {
    //TODO(PREGUNTAR)
    val seguido = ServerEmulator.userList[followed]
    ServerEmulator.artistasSeguidos[username]!!.remove(seguido!!)
}

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
//TODO(PREGUNTAR)
    val seguido = ServerEmulator.playlistList[followed]
    ServerEmulator.playlistSeguidos[username]!!.add(seguido!!)
}

/**
 * Elimina el usuario @username como seguidor de la playlist @followed
 */
@Throws(Exception::class)
fun deleteFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long) {
//TODO(PREGUNTAR)
    val seguido = ServerEmulator.playlistList[followed]
    ServerEmulator.playlistSeguidos[username]!!.remove(seguido!!)
}

/**
 * Añade una reproducción a la canción @song
 */
@Throws(Exception::class)
fun addReproductionToSongServer(username: String, sessionToken: String, song: Long) {
//TODO(PREGUNTAR)
}

/**
 * Añade la cancion @song como favorita del usuario @username
 */
@Throws(Exception::class)
fun setSongFavoutireServer(username: String, sessionToken: String, song: Long) {
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("nick",username))
    postData.add(Pair("token",sessionToken))
    postData.add(Pair("songID",song.toString()))

    val json = getJSONFromRequest("/users/$username/fav", postData)

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
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("nick",username))
    postData.add(Pair("token",sessionToken))
    postData.add(Pair("songID",song.toString()))

    val json = getJSONFromRequest("/songs/$username/unfav", postData)

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
    val json = getJSONFromRequest("/songs/$username/faved/$song", null)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error=="ok") {
            return true;
        } else if (error=="noFav") {
            return false;
        }else{
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
//TODO(PREGUNTAR)
    return ServerEmulator.playlistList[id]
}

/**
 * Sube una canción al servidor y devuelve el id e la cancion creada.
 * Sube los datos tanto al back-end (título, autor, ...) como el fichero de audio y las letras en caso de que existan al servidor de
 * almacenamiento
 * Atención: El id lo ha de devolver la llamada al servidor, el id del parametro @song siempre tendra un valor indefinido
 */
@Throws(Exception::class)
fun uploadSongServer(username: String, sessionToken: String, song: Song, context: Context): Long {
   //TODO(NO HAY LLAMADA!!! PREGUNTAR)
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("nick",username))
    postData.add(Pair("token",sessionToken))
    postData.add(Pair("songID",song.toString()))

    val json = getJSONFromRequest("//$username/0/add", postData)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
    return 0L
}

/**
 * Elimina los datos tanto del back-end como del servidor de almacenamiento
 */
@Throws(Exception::class)
fun deleteSongServer(username: String, sessionToken: String, song: Song, context: Context) {
    //TODO(NO HAY LLAMADA!!! PREGUNTAR)
    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("nick",username))
    postData.add(Pair("token",sessionToken))
    postData.add(Pair("songID",song.toString()))

    val json = getJSONFromRequest("/$username/0/add", postData)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
    }
}

@Throws(Exception::class)
fun obtainFavouriteSongsByUserServer(username: String, sessionToken: String): List<Song>? {
//TODO(PREGUNTAR)
    return ServerEmulator.cancionesFavoritas[username]
}

/**
 * Devuelve una lista de @cantidad de Artistas, Playlist y Canciones recomendadas para el usuario @username
 */
@Throws(Exception::class)
fun obtainRecomendationsForUserServer(username: String, sessionToken: String, cantidad: Long): List<Recommendation>? {
//TODO(PREGUNTAR)
    return ServerEmulator.recomendaciones[username]
}

/**
 * Devuelve las canciones populares (plazo de una semana o un mes)
 */
@Throws(Exception::class)
fun obtainPopularSongsServer(cantidad: Long): List<Song>? {
   //TODO(PREGUNTAR)
     return ServerEmulator.trends
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
 * Devuelve la lista de maximo @cantidad playlists seguidas que se han actualizado
 */
@Throws(Exception::class)
fun obtainUpdatedPlaylistsFollowedByUserServer(username: String, sessionToken: String, cantidad: Long): List<Playlist>? {
//TODO(PREGUNTAR)
    return ServerEmulator.playlistSeguidos[username]
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

/**
 * Devuelve la última canción escuchada por el usuario en alguna de sus sesiones
 */
@Throws(Exception::class)
fun obtainLastSongListenedServer(username: String, sessionToken: String): Song? {
//TODO(PREGUNTAR)
    return ServerEmulator.songList[1]
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

@Throws(Exception::class)
fun obtainGeneresServer(): List<String> {
    //TODO(PREGUNTAR)
    return ServerEmulator.generesList
}

fun obtainAlbumFromID(id: Long): Album{
    val json = getJSONFromRequest("/albums/$id", null)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val album = json.getJSONObject("album")

        val anyo = album.getLong("publish_year")
        val image = album.getString("image")
        val update_time = album.getLong("update_time")
        val user_id = album.getLong("user_id")
        val title = album.getString("title")

        val user = obtainUserDataServer()
        val retAlbum = Album(id,title,)
        user.twitterAccount = twitterAccount
        user.facebookAccount = facebookAccount
        user.instagramAccount = instagramAccount
        return user
    }
}

@Throws(Exception::class)
fun obtainAlbumsFromUserServer(username: String): List<Album> {
    //TODO(PREGUNTAR)
    val json = getJSONFromRequest("users/$username/albums", null)
    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getBoolean("error")
        if (error) throw Exception("Error")

        val albums = json.getJSONArray("albums")
        val size = json.getString("size")

        val list: MutableList<Album>? = null

        if (albums != null) {
            for (i in size){
                val albumID = albums.getLong(i.toInt())
            }
        }
        return list!!.toList()
    }
}

/**
 * Sube los datos tanto al back-end (nombre, autor, ...) como la carátula al servidor de
 * almacenamiento
 */
@Throws(Exception::class)
fun createAlbumsServer(username: String, sessionToken: String, album: Album, context: Context): Long {

    val title = album.name
    val year = album.releaseDate.time.year

    val postData = ArrayList<Pair<String,String>>()
    postData.add(Pair("token",sessionToken))
    postData.add(Pair("title",title))
    postData.add(Pair("year",year.toString()))

    val json = getJSONFromRequest("$username/create", postData)

    if (json == null) {
        throw Exception("Error: Servidor no accesible")
    } else {
        val error = json.getString("error")
        if (error != "ok") {
            throw Exception("Error: $error")
        }
        else{
            return album.id!!
        }
    }
}






