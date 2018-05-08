package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.addAlbumToUser
import com.spreadyourmusic.spreadyourmusic.controller.obtainCurrentUserData
import com.spreadyourmusic.spreadyourmusic.models.Album
import kotlinx.android.synthetic.main.activity_create_album.*
import java.util.*

class CreateAlbumActivity : AppCompatActivity() {
    var pathCaratula: String? = null
    val selectCaratula: Int = 455
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        val selecconarCaratula = findViewById<Button>(R.id.pathSelector)
        selecconarCaratula.setOnClickListener {
            val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.seleccione_fichero)), selectCaratula)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            onSelectFailure()
        } else if (requestCode == selectCaratula && resultCode == RESULT_OK) {
            pathCaratula = data!!.getData().toString()
            if(!fileExtension(pathCaratula!!).equals("jpg") && !fileExtension(pathCaratula!!).equals("png") && !fileExtension(pathCaratula!!).equals("jpge") ) {
                pathCaratula=null
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
        val albumName: String = newAlbumName.text.toString().trim()
        val current: Calendar = Calendar.getInstance()
        if(pathCaratula==null) {
            Toast.makeText(this,R.string.error_caratula,Toast.LENGTH_LONG).show()
        }
        else if(albumName.equals("")){
            Toast.makeText(this,R.string.error_nombre_album,Toast.LENGTH_LONG).show()
        }
        else{
            obtainCurrentUserData({
                val newAlbum = Album(0, albumName, it!!, current, pathCaratula!!)
                addAlbumToUser(it, this, newAlbum)
            }, this)
            //TODO(La parte del Pini)
            finish()
        }
    }
}

