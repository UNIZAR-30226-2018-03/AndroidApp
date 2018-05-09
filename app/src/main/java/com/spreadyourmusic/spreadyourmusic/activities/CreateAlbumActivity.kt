package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.createAlbum
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.models.Album
import kotlinx.android.synthetic.main.activity_sign_up_screen_1.*
import kotlinx.android.synthetic.main.content_create_album.*
import java.util.*

class CreateAlbumActivity : AppCompatActivity() {
    var uriImage: Uri? = null
    var selectPictureCode: Int = 567

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setTitle(R.string.create_album)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun onProfilePictureClick(v: View) {
        val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.seleccione_fichero)), selectPictureCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectPictureCode) {
            uriImage = data!!.data
            if (uriImage != null && uriImage!!.path != null) {
                Glide.with(this).load(uriImage).into(foto_perfil)
            }
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.error_fichero, Toast.LENGTH_SHORT).show()
    }

    fun onContinueClick(v: View) {
        val albumName: String = newAlbumName.text.toString().trim()
        val current: Calendar = Calendar.getInstance()
        if (uriImage == null || uriImage!!.path.isNullOrEmpty()) {
            Toast.makeText(this, R.string.error_caratula, Toast.LENGTH_LONG).show()
        } else if (albumName.isEmpty()) {
            Toast.makeText(this, R.string.error_nombre_album, Toast.LENGTH_LONG).show()
        } else {
            obtainCurrentUserData({
                val newAlbum = Album(albumName, it!!, current, uriImage!!.path)
                createAlbum(newAlbum, this, {
                    if (!it.isNullOrEmpty()) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    } else
                        finish()
                })
            }, this)
        }
    }
}

