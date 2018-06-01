package com.spreadyourmusic.spreadyourmusic.controller

import android.content.Context
import com.spreadyourmusic.spreadyourmusic.data.AppDatabase
import com.spreadyourmusic.spreadyourmusic.data.SongVo
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import java.io.BufferedInputStream
import java.io.File
import java.net.URL

/**
 * Guarda la canción song en el almacenamiento interno
 * Además la modifica para poder ser reproducida desde el almacenamiento interno
 */
fun saveSongLocal(song: Song, context: Context): Boolean {
    if (isSongLocal(song, context))
    // La canción ya esta descargada
        return false

    val songFilename = saveSongInternalStorage(song.locationUri, song.id, context)

    val albumFilename = saveAlbumArtInternalStorage(song.album.artLocationUri!!, song.id, context)

    if (songFilename == null || albumFilename == null)
    // Ha existido un error al crear la cancion
        return false

    val antAlbum = song.album.artLocationUri
    val antLocation = song.album.artLocationUri

    song.album.artLocationUri = albumFilename
    song.locationUri = songFilename
    saveSongLocalDataBase(song, context)

    song.album.artLocationUri = antAlbum
    song.locationUri = antLocation!!

    return true
}

/**
 * Elimina la canción song del almacenamiento interno
 */
fun deleteSongLocal(song: Song, context: Context): Boolean {
    if (!isSongLocal(song, context)) {
        return false
    }
    deleteFileInternalStorage(song.locationUri, context)
    deleteFileInternalStorage(song.album.artLocationUri!!, context)
    deleteSongLocalDataBase(song, context)
    return true
}

/**
 * Devuelve true si la canción song está en el almacenamiento interno
 */
fun isSongLocal(song: Song, context: Context): Boolean {
    val db = AppDatabase.getDatabase(context)
    val numUsagesSong = db.songDao().exist(song.id)
    return numUsagesSong != 0L
}

/**
 * Devuelve la lista de canciones descargadas
 */
fun getDownloadedSongs(context: Context): List<Song> {
    val db = AppDatabase.getDatabase(context)
    val vuelta = db.songDao().all
    return if (vuelta != null)
        listSongConversion(vuelta)
    else
        ArrayList()
}

private fun listSongConversion(list: List<SongVo>): List<Song> {
    val devolver = ArrayList<Song>()
    for (i in list) {
        val user = User(i.creatorName)
        val album = Album(i.sid, i.albumName, user, i.releaseDate, i.artLocationPath)
        val song = Song(i.sid, i.name, i.songLocationUri, album, null, null)
        song.isDownloaded = true
        devolver.add(song)
    }
    return devolver
}

private fun saveSongInternalStorage(songDataURL: String, songId: Long, context: Context): String? {
    return try {
        val url = URL(songDataURL)
        val conexion = url.openConnection()
        conexion.connect()

        val filename = "song_" + songId.toString()

        context.openFileOutput(filename, Context.MODE_PRIVATE).use { output ->
            // download the file
            val input = BufferedInputStream(url.openStream())
            val data = ByteArray(1024)
            var total = 0
            var count = input.read(data)

            while (count != -1) {
                total += count
                output.write(data, 0, count)
                count = input.read(data)
            }

            output.flush()
            input.close()
        }

        (context.filesDir.path + "/" + filename)

    } catch (e: Exception) {
        null
    }
}

fun saveSongLocalDataBase(song: Song, context: Context) {
    val db = AppDatabase.getDatabase(context)
    db.songDao().insert(SongVo(song))
}

fun deleteSongLocalDataBase(song: Song, context: Context) {
    val db = AppDatabase.getDatabase(context)
    db.songDao().delete(SongVo(song))
}

fun deleteFileInternalStorage(path: String, context: Context) {
    val file = File(context.filesDir, path)
    file.delete()
}

fun saveAlbumArtInternalStorage(albumDataURL: String, songId: Long, context: Context): String? {
    return try {
        val url = URL(albumDataURL)
        val conexion = url.openConnection()
        conexion.connect()

        val filename = "album_" + songId.toString()

        context.openFileOutput(filename, Context.MODE_PRIVATE).use { output ->
            // download the file
            val input = BufferedInputStream(url.openStream())
            val data = ByteArray(1024)
            var total = 0
            var count = input.read(data)

            while (count != -1) {
                total += count
                output.write(data, 0, count)
                count = input.read(data)
            }

            output.flush()
            input.close()
        }

        (context.filesDir.path + "/" + filename)

    } catch (e: Exception) {
        null
    }
}