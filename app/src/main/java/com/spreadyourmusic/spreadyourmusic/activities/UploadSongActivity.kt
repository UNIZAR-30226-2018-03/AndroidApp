package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spreadyourmusic.spreadyourmusic.R
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.*
import com.spreadyourmusic.spreadyourmusic.R.layout.activity_upload_song
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_upload_song.*


class UploadSongActivity : AppCompatActivity() {
    var pathCancion: Uri? = null
    var pathLyrics: Uri? = null
    var albums = arrayOf("Hola","Adios")
    var generos = arrayOf("Es bien", "Es mal")
    var spinnerAlbums: Spinner? = null
    var spinnerGeneros: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_upload_song)

        spinnerAlbums= findViewById(R.id.spinner)
        spinnerAlbums!!.adapter= ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, albums)
        spinnerAlbums!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                TODO()
                /* Asignar el album a una variable para luego registrar la cancion */
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
                TODO()
                /* Marcar para crear el album */
            }
        }

        spinnerGeneros= findViewById(R.id.spinnerGenero)
        spinnerGeneros!!.adapter= ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, generos)
        spinnerGeneros!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                TODO()
                /* Asignar el genero a una variable para luego registrar la cancion */
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
                /* Undefined genero PREGUNTAR ABEL */
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        var selectedfile: Uri;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            pathCancion = data.getData()
        }
        else if (requestCode == 2 && resultCode == RESULT_OK){
            pathLyrics = data.getData()
        }
    }

    fun createSongAction(v: View){
        val songname = newSongName.text.toString()
        TODO()
    }
}