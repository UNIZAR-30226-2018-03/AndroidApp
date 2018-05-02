package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.obtainUserFromID

class SignUpActivity : AppCompatActivity() {

    var numOfScreen = 1
    val REQUEST_IMAGE_CAPTURE = 1067

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen_1)

        val userId = intent.getStringExtra(resources.getString(R.string.user_id))
        // TODO: En el caso de que se reciba un usuario rellenar los datos porque es edición
        if(userId != null){

        }
    }

    fun onContinueClick(v: View){
        //TODO: Almacenar todos loas datos y hacer bien la transicion
        //val inflator = layoutInflater
        numOfScreen = 2
        //val view = inflator.inflate(R.layout.activity_sign_up_screen_2, null, false)
        //view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right))
        setContentView(R.layout.activity_sign_up_screen_2)
    }

    fun onCreateClick(v:View){
        //TODO: Almacenar todos loas datos
    }

    override fun onBackPressed() {
       if(numOfScreen == 2){
           //TODO: Hacer bien la transicion
           numOfScreen = 1
           setContentView(R.layout.activity_sign_up_screen_1)
        }else {
            super.onBackPressed()
        }
    }

    fun onProfilePictureClick(v: View){
        /*val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }*/
    }

   /* override fun onActivityResult(requestCode : Int,  resultCode : Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data.extras
            //val imageBitmap = extras.get("data")
        }
    }*/
}
