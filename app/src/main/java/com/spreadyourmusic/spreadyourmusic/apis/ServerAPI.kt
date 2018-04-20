package com.spreadyourmusic.spreadyourmusic.apis

import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.test.ServerEmulator

/**
 * Created by abel on 8/03/18.
 */

/**
 * Devuelve true si se puede acceder al servidor, false en caso contrario
 * Warning: esta operación puede ser costosa en tiempo
 */
fun isServerOnline(): Boolean {
    // TODO:
    return true
}

/**
 * Realiza un login en el servidor y devuelve el token de sesión
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun doLoginServer(username: String, password: String): String {
    //TODO:
    if(ServerEmulator.userList.containsKey(username)){
        Thread.sleep(300)
        return "dfsdfdsdf"
    }else{
        throw Exception("Error")
    }
}

/**
 * Realiza un logout en el servidor
 * Warning: esta operación puede ser costosa en tiempo
 */
fun doLogoutServer(username: String, sessionToken: String) {
    //TODO:
    Thread.sleep(300)
}

/**
 * Obtiene la información asociada al usuario con nick @username
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun obtainUserDataServer(username: String, sessionToken: String): User? {
    //TODO:
    if(ServerEmulator.userList.containsKey(username)){
        return ServerEmulator.userList[username]
    }else{
        throw Exception("Error")
    }
}

fun obtainSongsFromUserServer(username: String, sessionToken: String): List<Song> {
    val resultado = ArrayList<Song>()
    for (i in ServerEmulator.songList){
        if(i.value.album.creator.username == username){
            resultado.add(i.value)
        }
    }
    return resultado
}

fun obtainPlaylistsFromUserServer(username: String, sessionToken: String): List<Playlist> {
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
fun getFollowedUsersServer(username: String, sessionToken: String): List<User>{
    //TODO:
    if(ServerEmulator.artistasSeguidos.containsKey(username)){
        return ServerEmulator.artistasSeguidos[username]!!
    }else{
        return ArrayList()
    }
}

/**
 * Devuelve true si el usuario @user es seguido por el usuario @username
 */
@Throws(Exception::class)
fun isUserFollowedByUserServer(username: String, sessionToken: String, user: String): Boolean{
//TODO:
    if(ServerEmulator.artistasSeguidos.containsKey(username)){
        for (i in ServerEmulator.artistasSeguidos[username]!!){
            if(i.username == user) return true
        }
        return false
    }else{
        return false
    }
}

/**
 * Devuelve una lista con los usuarios que siguen al usuario @username
 */
@Throws(Exception::class)
fun getFollowersOfUserServer(username: String, sessionToken: String): List<User>{
    //TODO:
    return ArrayList()
}

/**
 * Devuelve el numero de seguidores del usuario @username
 */
@Throws(Exception::class)
fun getNumberOfFollowersOfUserServer(username: String, sessionToken: String): Long{
    //TODO:
    return 4
}

/**
 * Añade el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun addFollowerToUserServer(username: String, sessionToken: String, followed: String){
    //TODO:
    val seguido = ServerEmulator.userList[followed]
    ServerEmulator.artistasSeguidos[username]!!.add(seguido!!)
}

/**
 * Elimina el usuario @username como seguidor del usuario @followed
 */
@Throws(Exception::class)
fun deleteFollowerToUserServer(username: String, sessionToken: String, followed: String){
//TODO:
    val seguido = ServerEmulator.userList[followed]
    ServerEmulator.artistasSeguidos[username]!!.remove(seguido!!)
}

/**
 * Devuelve una lista con las playlist que sigue el usuario @username
 */
@Throws(Exception::class)
fun getFollowedPlaylistsServer(username: String, sessionToken: String): List<Playlist>{
//TODO:
    return ServerEmulator.playlistSeguidos[username]!!
}

/**
 * Devuelve true si la playlist @playlist es seguida por el usuario @username
 */
@Throws(Exception::class)
fun isPlaylistFollowedByUserServer(username: String, sessionToken: String, playlist: Long): Boolean{
//TODO:
    for (i in ServerEmulator.playlistSeguidos[username]!!){
        if(i.id == playlist) return true
    }
    return false
}

/**
 * Devuelve una lista con los usuarios que siguen a la playlist @playlist
 */
@Throws(Exception::class)
fun getFollowersOfPlaylistServer(playlist: Long, sessionToken: String): List<User>{
//TODO:
    return ArrayList()
}

/**
* Devuelve el numero de seguidores de la playlist @playlist
*/
@Throws(Exception::class)
fun getNumberOfFollowersOfPlaylistServer(playlist: Long, sessionToken: String): Long{
//TODO:
    return 8
}

/**
 * Añade el usuario @username como seguidor de la playlist @followed
 */
@Throws(Exception::class)
fun addFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long){
//TODO:
    val seguido = ServerEmulator.playlistList[followed]
    ServerEmulator.playlistSeguidos[username]!!.add(seguido!!)
}

/**
 * Elimina el usuario @username como seguidor de la playlist @followed
 */
@Throws(Exception::class)
fun deleteFollowerToPlaylistServer(username: String, sessionToken: String, followed: Long){
//TODO:
    val seguido = ServerEmulator.playlistList[followed]
    ServerEmulator.playlistSeguidos[username]!!.remove(seguido!!)
}

/**
* Añade una reproducción a la canción @song
*/
@Throws(Exception::class)
fun addReproductionToSongServer(username: String, sessionToken: String, song: Long){
//TODO:
}

/**
 * Añade la cancion @song como favorita del usuario @username
 */
@Throws(Exception::class)
fun setSongFavoutireServer(username: String, sessionToken: String, song: Long){
    //TODO:
    val seguido = ServerEmulator.songList[song]
    ServerEmulator.cancionesFavoritas[username]!!.add(seguido!!)
}

/**
 * Elimina la cancion @song como favorita del usuario @username
 */
@Throws(Exception::class)
fun unSetSongFavoutireServer(username: String, sessionToken: String, song: Long){
    //TODO:
    val seguido = ServerEmulator.songList[song]
    ServerEmulator.cancionesFavoritas[username]!!.remove(seguido!!)
}

/**
 * Devuelve true si la cancion @song es favorita del usuario @username
 */
@Throws(Exception::class)
fun isSongFavoutireByUserServer(username: String, sessionToken: String, song: Long):Boolean{
    //TODO:
    for (i in ServerEmulator.cancionesFavoritas[username]!!){
        if(i.id == song) return true
    }
    return false
}


/**
 * Obtiene la información asociada a la playlist con id @id
 * Warning: esta operación puede ser costosa en tiempo
 */
@Throws(Exception::class)
fun obtainPlaylistDataServer(id: Long, sessionToken: String): Playlist? {
    //TODO:
    return ServerEmulator.playlistList[id]
}

