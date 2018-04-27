package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.spreadyourmusic.spreadyourmusic.R
import kotlinx.android.synthetic.main.activity_create_album.*
import kotlinx.android.synthetic.main.activity_upload_song.*

class CreateAlbum : AppCompatActivity() {
    var pathCaratula: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        val selecconarCaratula = findViewById<Button>(R.id.caratulaSelector)
        selecconarCaratula.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        var selectedfile: Uri;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            pathCaratula = data.getData()
        }
    }

    fun createSongAction(v: View){
        val albumName = albumName.text.toString()
        TODO()
    }

}
