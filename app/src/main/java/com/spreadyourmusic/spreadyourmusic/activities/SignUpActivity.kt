package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.obtainUserFromID
import com.spreadyourmusic.spreadyourmusic.models.Album
import kotlinx.android.synthetic.main.activity_sign_up_screen_1.*
import kotlinx.android.synthetic.main.activity_sign_up_screen_2.*
import android.widget.TextView
import android.R.drawable.edit_text
import android.widget.EditText
import com.spreadyourmusic.spreadyourmusic.models.User


class SignUpActivity : AppCompatActivity() {

    var numOfScreen = 1
    val REQUEST_IMAGE_CAPTURE = 1067
    var userName: String? = null
    var userPass: String? = null
    var userMail: String? = null
    var userBirth: String? = null
    var userTwitter: String? = null
    var userInsta: String? = null
    var userFace: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen_1)
    }

    fun onContinueClick(v: View){
        //TODO: Almacenar todos loas datos y hacer bien la transicion
        //val inflator = layoutInflater
        userName = usernameEditText.text.toString()
        userPass = passwordEditText.text.toString()
        userMail = mailEditText.text.toString()
        if(userPass !=null && userMail!=null){
            if(userName!=null) {
                // TODO: En el caso de que se reciba un usuario rellenar los datos porque es edición
                val editText = findViewById<View>(R.id.nameEditText) as EditText
                editText.setText(userName, TextView.BufferType.EDITABLE)
            }
            numOfScreen = 2
            setContentView(R.layout.activity_sign_up_screen_2)
        } else{
            Toast.makeText(applicationContext, R.string.error_rellenar, Toast.LENGTH_SHORT).show()
        }
        //val view = inflator.inflate(R.layout.activity_sign_up_screen_2, null, false)
        //view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right))
    }

    fun onCreateClick(v:View){
        userBirth = birthDateEditText.text.toString()
        userFace = facebookAccountEditText.text.toString()
        userTwitter = twitterAccountEditText.text.toString()
        userInsta= instagramAccountEditText.text.toString()
        if (userBirth != null) {
            createUser(v)
            finish()
        } else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
       if(numOfScreen == 2){
           userName = null
           userPass = null
           userMail = null
           usernameEditText.text.clear()
           passwordEditText.text.clear()
           mailEditText.text.clear()
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
    }

    fun createUser(v: View){
        val newUser = User(userName!!,userPass!!)
    }

    }*/
}
