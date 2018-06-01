package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.createAlbum
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.helpers.getPathFromUri
import com.spreadyourmusic.spreadyourmusic.models.Album
import kotlinx.android.synthetic.main.content_create_album.*
import kotlinx.android.synthetic.main.activity_create_album.*
import java.util.*

class CreateAlbumActivity : AppCompatActivity() {
    private var imagePath: String? = null
    private var selectPictureCode: Int = 567

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        toolbar.setTitle(R.string.create_album)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun onProfilePictureClick(v: View) {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file)), selectPictureCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectPictureCode) {
            imagePath = getPathFromUri(this, data!!.data)
            if (imagePath != null) {
                Glide.with(this).load(imagePath).into(coverCircleImageView)
            }
        }
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.select_valid_file, Toast.LENGTH_SHORT).show()
    }

    fun onContinueClick(v: View) {
        val albumName: String = albumNameEditText.text.toString().trim()
        val current: Calendar = Calendar.getInstance()
        if (imagePath.isNullOrEmpty()) {
            Toast.makeText(this, R.string.select_valid_cover, Toast.LENGTH_LONG).show()
        } else if (albumName.isEmpty()) {
            Toast.makeText(this, R.string.select_valid_album_name, Toast.LENGTH_LONG).show()
        } else {
            obtainCurrentUserData({
                val newAlbum = Album(albumName, it!!, current, imagePath!!)
                Toast.makeText(this, R.string.creating, Toast.LENGTH_SHORT).show()
                createAlbum(newAlbum, this, { error, idAlbum ->
                    if (!error.isNullOrEmpty()) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    } else {
                        val passIntent = Intent()
                        passIntent.putExtra(resources.getString(R.string.album_id), idAlbum)
                        passIntent.putExtra(resources.getString(R.string.album_name), albumName)
                        setResult(RESULT_OK, passIntent)
                        finish()
                    }
                })
            }, this)
        }
    }
}

