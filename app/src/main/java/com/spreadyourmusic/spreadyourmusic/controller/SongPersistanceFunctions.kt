package com.spreadyourmusic.spreadyourmusic.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.URLConnection

// Datos hacer con room
/*
// TODO:
fun saveSongInternalStorage(songData:, songId:Int, context: Context){
    val filename = "song_" + songId.toString()
    val file = File(context.filesDir, filename)
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(songData.toByteArray())
    }
}
 try {
        URL url = new URL("url of your .mp3 file");
        URLConnection conexion = url.openConnection();
        conexion.connect();
        // this will be useful so that you can show a tipical 0-100% progress bar
        int lenghtOfFile = conexion.getContentLength();

        // downlod the file
        InputStream input = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.mp3");

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
            total += count;
            // publishing the progress....
            publishProgress((int)(total*100/lenghtOfFile));
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
    } catch (Exception e) {}
*/

fun saveSongInternalStorage(songDataURL: String, songId:Long, context: Context) : String?{

    return try {
        val url = URL(songDataURL)
        val conexion = url.openConnection()
        conexion.connect()

        val extension = songDataURL.substring(songDataURL.lastIndexOf("."))

        val filename =  Environment.getExternalStorageDirectory().getPath()+ "/Music/Prueba/" + "song_" + songId.toString() + extension

        // download the file
        val input =  BufferedInputStream(url.openStream())
        val output = FileOutputStream(filename)

        val data = ByteArray(1024)

        var total = 0
        var count = input.read(data)

        while (count != -1) {
            total += count
            output.write(data, 0, count)
            count = input.read(data)
        }

        output.flush()
        output.close()
        input.close()
        filename
    } catch (e: Exception) {
        null
    }
}

fun saveAlbumArtInternalStorage(albumPhoto: Bitmap, albumId:Int, context: Context){
    val filename = "photo_" + albumId.toString()
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        albumPhoto.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
}

fun deleteSongInternalStorage(path: String, context: Context){
    val file = File(context.filesDir, path)
    file.delete()
}

fun deleteAlbumArtInternalStorage(path: String, context: Context){
    val file = File(context.filesDir, path)
    file.delete()
}

fun openAlbumArtInternalStorage(){
    //TODO:
}