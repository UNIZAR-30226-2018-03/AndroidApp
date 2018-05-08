package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import kotlinx.android.synthetic.main.activity_create_playlist.*
import java.util.*

class CreatePlaylistActivity : AppCompatActivity() {
    var pathPortada: String? = null
    val selectPortada: Int = 455
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        val seleccionarPortada = findViewById<Button>(R.id.pathSelector)
        seleccionarPortada.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.seleccione_fichero)), selectPortada)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectPortada && resultCode == RESULT_OK) {
            pathPortada= data!!.getData().toString()
            if(!fileExtension(pathPortada!!).equals("jpg") && !fileExtension(pathPortada!!).equals("png") && !fileExtension(pathPortada!!).equals("jpge") ) {
                pathPortada=null
                onSelectFailure()
            }
        }
    }

    fun fileExtension(file: String): String {
        val extension: String? = file.substring(file.lastIndexOf(".") + 1, file.length)
        return extension!!
    }


    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.error_fichero, Toast.LENGTH_SHORT).show()
    }

    fun onContinueClick(v: View) {
        val playlistName: String = newPlaylistName.text.toString().trim()
        val current: Calendar = Calendar.getInstance()
        if(pathPortada==null) {
            Toast.makeText(this,R.string.error_caratula,Toast.LENGTH_LONG).show()
        }
        else if(playlistName.equals("")){
            Toast.makeText(this,R.string.error_nombre_playlist,Toast.LENGTH_LONG).show()
        }
        else{
            obtainCurrentUserData({
                val newPlaylist = Playlist(0,playlistName,it!!,pathPortada!!,TODO())
            }, this)
            //TODO(La parte del Pini)
            finish()
        }
    }
}

