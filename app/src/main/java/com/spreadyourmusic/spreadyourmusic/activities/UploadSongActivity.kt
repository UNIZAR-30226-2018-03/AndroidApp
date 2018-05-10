package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spreadyourmusic.spreadyourmusic.R
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.spreadyourmusic.spreadyourmusic.R.layout.activity_upload_song
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.helpers.getPathFromUri
import com.spreadyourmusic.spreadyourmusic.models.Album
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import kotlinx.android.synthetic.main.activity_upload_song.*
import java.util.*
import kotlin.collections.ArrayList


class UploadSongActivity : AppCompatActivity() {
    var pathCancion: String? = null
    var pathLyrics: String? = null
    var albums: ArrayList<Album>? = null
    var generos: List<String>? = null
    var user: User? = null
    var selectedAlbum: Album? = null
    var selectedGenere: String? = null
    private val selectSong: Int = 355
    private val selectLyrics: Int = 356

    var setAlbum = false

    private val NO_CATEGORY_ID = Menu.FIRST

    private val CREATE_ALBUM_RESULT = 545

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_upload_song)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setTitle(R.string.upload_song)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        obtainGeneres(this, {
            generos = it
        })
        obtainCurrentUserData({
            obtainAlbumsFromUser(it!!, this, {
                if (it != null) {
                    albums = ArrayList(it)
                }
            })
        }, this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectSong) {
            pathCancion = getPathFromUri(this, data!!.data)
            audioEditText.setText(data.data.toString())
        } else if (requestCode == selectLyrics) {
            pathLyrics = getPathFromUri(this, data!!.data)
            lyricsEditText.setText(data.data.toString())
        } else if (requestCode == CREATE_ALBUM_RESULT) {
            val extras = data!!.extras
            val nombreAlbum = extras.getString(resources.getString(R.string.album_name))
            val idAlbum = extras.getLong(resources.getString(R.string.album_id))
            obtainCurrentUserData({
                val newAlbum = Album(idAlbum, nombreAlbum, it!!, Calendar.getInstance(), "")
                selectedAlbum = newAlbum
                albums!!.add(newAlbum)
                albumEditText.setText(selectedAlbum!!.name)
            }, this)
        }
    }

    private fun fileExtension(file: String): String {
        return file.substring(file.lastIndexOf(".") + 1, file.length)
    }

    fun onContinueClick(v: View) {
        val songname: String = nameEditText.text.toString()
        if (selectedAlbum == null || songname.isEmpty() || selectedGenere.isNullOrEmpty() || pathCancion.isNullOrEmpty()) {
            Toast.makeText(applicationContext, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, R.string.empty_fields_3, Toast.LENGTH_SHORT).show()
        } else {
            obtainCurrentUserData({
                val newSong = Song(songname, pathCancion!!, selectedAlbum!!, selectedGenere!!, pathLyrics)
                createSong(it!!, newSong, this, {
                    if (it != null) {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                    }
                })
            }, this)
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.select_valid_file, Toast.LENGTH_SHORT).show()
    }


    fun selectGenre(v: View) {
        setAlbum = false
        registerForContextMenu(v)
        openContextMenu(v)
        unregisterForContextMenu(v)
    }

    fun selectAlbum(v: View) {
        setAlbum = true
        registerForContextMenu(v)
        openContextMenu(v)
        unregisterForContextMenu(v)
    }

    fun selectLyrics(v: View) {
        val intent = Intent()
                .setType("application/x-subrip")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file)), selectLyrics)
    }

    fun selectAudio(v: View) {
        val intent = Intent()
                .setType("audio/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file)), selectSong)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (setAlbum) {
            menu!!.add(Menu.NONE, NO_CATEGORY_ID, Menu.NONE, R.string.create_album)
            if (albums != null) {
                for (i in 1..albums!!.size) {
                    val value = albums!![i - 1]
                    menu.add(Menu.NONE, NO_CATEGORY_ID + i, Menu.NONE, value.name)
                }
            }
        } else {
            if (generos != null) {
                for (i in 1..generos!!.size) {
                    val value = generos!![i - 1]
                    menu!!.add(Menu.NONE, NO_CATEGORY_ID + i, Menu.NONE, value)
                }
            }
        }

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item != null) {
            if (setAlbum) {
                if (item.itemId == NO_CATEGORY_ID) {
                    val intent = Intent(this, CreateAlbumActivity::class.java)
                    startActivityForResult(intent, CREATE_ALBUM_RESULT)
                } else {
                    selectedAlbum = albums!![item.itemId - NO_CATEGORY_ID - 1]
                    albumEditText.setText(selectedAlbum!!.name)
                }
            } else {
                selectedGenere = generos!![item.itemId - NO_CATEGORY_ID - 1]
                genreEditText.setText(selectedGenere)
            }
            true
        } else
            super.onContextItemSelected(item)
    }

}