package com.spreadyourmusic.spreadyourmusic.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.addAlbumToUser
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import kotlinx.android.synthetic.main.activity_create_album.*
import kotlinx.android.synthetic.main.activity_upload_song.*
import java.util.*

class CreateAlbumActivity : AppCompatActivity() {
    var pathCaratula: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        val selecconarCaratula = findViewById<Button>(R.id.pathSelector)
        selecconarCaratula.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 1)
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, "No has selecionado un fichero válido", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode != RESULT_OK) {
                onSelectFailure()
            } else if (requestCode == 1 && resultCode == RESULT_OK) {
                pathCaratula = data!!.getData().toString()
            }
        } catch (e: RuntimeException) {
            // The RuntimeException status code indicates the detailed failure reason.
            onSelectFailure()
        }
    }

    fun onContinueClick(v: View) {
        //TODO: Almacenar todos loas datos y hacer bien la transicion
        createAlbumAction(v)
        //TODO(La parte del Pini)
        setContentView(R.layout.activity_upload_song)
    }

    fun createAlbumAction(v: View) {
        obtainCurrentUserData({
            val albumname: String = createAlbum.text.toString()
            val current: Calendar = Calendar.getInstance()
            val newAlbum = Album(0, albumname, it!!, current, pathCaratula!!)
            addAlbumToUser(it, this, newAlbum)
        }, this)
    }
}

