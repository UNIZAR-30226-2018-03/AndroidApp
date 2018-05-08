package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import kotlinx.android.synthetic.main.activity_sign_up_screen_1.*
import kotlinx.android.synthetic.main.activity_sign_up_screen_2.*
import java.util.*
import android.net.Uri
import android.widget.DatePicker
import android.app.DatePickerDialog
import com.spreadyourmusic.spreadyourmusic.fragment.DatePickerFragment


class SignUpActivity : AppCompatActivity() {
    var idEditActivity = false
    var numOfScreen = 1
    var username: String? = null
    var realname: String? = null
    var password: String? = null
    var mail: String? = null
    var userBirth: Date? = null
    var userTwitterAccount: String? = null
    var userInstagramAccount: String? = null
    var userFacebookAccount: String? = null
    var userPictureLocationUri: String? = null

    var uriImage: Uri? = null

    var selectPictureCode: Int = 567

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen_1)

        val userId = intent.getStringExtra(resources.getString(R.string.user_id))
        if (userId != null) {
            idEditActivity = true
            // TODO: En el caso de que se reciba un usuario rellenar los datos porque es edición

        }
    }

    fun onContinueClick(v: View) {
        username = usernameEditText.text.toString().trim()
        password = passwordEditText.text.toString().trim()
        mail = mailEditText.text.toString().trim()

        if (password.isNullOrEmpty() || username.isNullOrEmpty() || userPictureLocationUri.isNullOrEmpty()) {
            Toast.makeText(applicationContext, R.string.error_rellenar, Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, R.string.campos_obligatorios_1, Toast.LENGTH_SHORT).show()

        } else {
            numOfScreen = 2
            setContentView(R.layout.activity_sign_up_screen_2)
        }
    }

    fun onCreateClick(v: View) {
        realname = nameEditText.text.toString().trim()
        userFacebookAccount = facebookAccountEditText.text.toString().trim()
        userTwitterAccount = twitterAccountEditText.text.toString().trim()
        userInstagramAccount = instagramAccountEditText.text.toString().trim()
        if (realname.isNullOrEmpty()) {
            Toast.makeText(applicationContext, R.string.error_rellenar, Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, R.string.campos_obligatorios_1, Toast.LENGTH_SHORT).show()

        } else {
            // TODO: Crear usuario
        }

    }

    override fun onBackPressed() {
        if (numOfScreen == 2) {
            usernameEditText.setText(username)
            passwordEditText.setText(password)
            mailEditText.setText(mail)
            if (userPictureLocationUri != null) {
                Glide.with(this).load(uriImage).into(foto_perfil)
            }

            numOfScreen = 1
            setContentView(R.layout.activity_sign_up_screen_1)
        } else {
            super.onBackPressed()
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
            userPictureLocationUri = uriImage!!.path

            if (userPictureLocationUri != null) {
                Glide.with(this).load(uriImage).into(foto_perfil)
            }
        }
    }

    fun onSelectDate(v: View) {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because january is zero
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            birthDateEditText.setText(selectedDate)
            userBirth = GregorianCalendar(year, month, day).time
        })
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun onSelectFailure() {
        Toast.makeText(applicationContext, R.string.error_fichero, Toast.LENGTH_SHORT).show()
    }
}
