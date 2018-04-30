package com.spreadyourmusic.spreadyourmusic.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spreadyourmusic.spreadyourmusic.R
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.*
import com.google.android.gms.common.api.ApiException
import com.spreadyourmusic.spreadyourmusic.R.layout.activity_upload_song
import com.spreadyourmusic.spreadyourmusic.apis.obtainAlbumsFromUserServer
import com.spreadyourmusic.spreadyourmusic.controller.addSongToUser
import com.spreadyourmusic.spreadyourmusic.controller.obtainAlbumsFromUser
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.controller.obtainGeneres
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_upload_song.*
import java.time.LocalDateTime
import java.util.*


class UploadSongActivity : AppCompatActivity() {
    var pathCancion: String? = null
    var pathLyrics: String? = null
    var albums: List<Album>? = null
    var generos: List<String>? = null
    var spinnerAlbums: Spinner? = null
    var spinnerGeneros: Spinner? = null
    var user: User? = null
    var selectedAlbum: Album? = null
    var selectedGenere: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_upload_song)
        spinnerGeneros = findViewById(R.id.spinnerGenero)
        spinnerAlbums = findViewById(R.id.spinner)

        obtainGeneres(this, {
            generos = it
            spinnerGeneros!!.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, generos)
            spinnerGeneros!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                    selectedGenere = generos!!.get(position)
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    selectedGenere = generos!!.get(0)
                }
            }
        })

        obtainCurrentUserData({
            obtainAlbumsFromUser(it!!, this, {
                albums = it
                val albumNames = ArrayList<String>()
                /* Opcion por defecto, si se selecciona, pasar a la pantalla de crear album */
                albumNames.add("Crear album")
                /* Obtención de los nombres de los albumes */
                for (i in albums!!) {
                    albumNames.add(i.name)
                }
                spinnerAlbums!!.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, albumNames)
                spinnerAlbums!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                        if (position != 0)
                            selectedAlbum = albums!!.get(position - 1)
                        else selectedAlbum = null
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>) {
                        selectedAlbum = null
                    }
                }

            })
        }, this)

        val selecconarCancionBoton = findViewById<Button>(R.id.audioSelector)
        selecconarCancionBoton.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 1)
        }

        val selecconarLetras = findViewById<Button>(R.id.lyricsSelector)
        selecconarLetras.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2)
        }
    }

    fun onContinueClick(v: View) {
        //TODO: Almacenar todos loas datos y hacer bien la transicion
        if (selectedAlbum != null) {
            createSongAction(v)
            //TODO(La parte del Pini)
            setContentView(R.layout.activity_user)
        } else {
            setContentView(R.layout.activity_create_album)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode != RESULT_OK) {
                onSelectFailure()
            } else if (requestCode == 1 && resultCode == RESULT_OK) {
                pathCancion = data!!.getData().toString()
            } else if (requestCode == 2 && resultCode == RESULT_OK) {
                pathLyrics = data!!.getData().toString()
            }
        } catch (e: RuntimeException) {
            // The RuntimeException status code indicates the detailed failure reason.
            onSelectFailure()
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, "No has selecionado un fichero válido", Toast.LENGTH_SHORT).show()
    }

    fun createSongAction(v: View) {
        obtainCurrentUserData({
            val songname: String = newSongName.text.toString()
            val current: String = Calendar.getInstance().time.toString()
            val newSong = Song(0, songname, pathCancion!!, 360, selectedAlbum!!, selectedGenere!!, pathLyrics)
            addSongToUser(it!!, this, newSong)
        },this)
    }
}