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
import android.app.DatePickerDialog
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.DatePickerFragment
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton


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

    var userId: String? = null

    var uriImage: Uri? = null

    var selectPictureCode: Int = 567

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen_1)

        userId = intent.getStringExtra(resources.getString(R.string.user_id))
        if (userId != null) {
            idEditActivity = true
            obtainCurrentUserData({
                if (it != null) {
                    username = it.username
                    realname = it.name
                    mail = it.email
                    userBirth = it.birthDate
                    userTwitterAccount = it.twitterAccount
                    userInstagramAccount = it.instagramAccount
                    userFacebookAccount = it.facebookAccount
                    userPictureLocationUri = it.pictureLocationUri

                    usernameEditText.setText(username)
                    mailEditText.setText(mail)

                    if (userPictureLocationUri != null) {
                        Glide.with(this).load(userPictureLocationUri).into(foto_perfil)
                    }
                }

            },this)
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

            // TODO : ERROR esto no se realiza por algun motivo
            nameEditText.setText(realname)

            if (userBirth != null) {
                val selectedDate = userBirth!!.day.toString() + " / " + (userBirth!!.month + 1).toString() + " / " + userBirth!!.year.toString()
                birthDateEditText.setText(selectedDate)
            }

            twitterAccountEditText.setText(userTwitterAccount)
            instagramAccountEditText.setText(userInstagramAccount)
            facebookAccountEditText.setText(userFacebookAccount)
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
            val user = User(username!!, password!!, realname!!, userPictureLocationUri!!, mail, null, userBirth)
            user.twitterAccount = userTwitterAccount
            user.facebookAccount = userFacebookAccount
            user.instagramAccount = userInstagramAccount
            if(!idEditActivity){
                doSignUp(user, this, {
                    if (!it.isNullOrEmpty()) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    } else {
                        isCurrentUserLoggedinOtherSession(this, {
                            SessionSingleton.lastSongListened = it
                            SessionSingleton.isUserDataLoaded = false
                            val int = Intent(applicationContext, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(int)
                            finish()
                        })
                    }
                })
            }else{
                updateUserData(user,this,{
                    if (!it.isNullOrEmpty()) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                    }
                })
            }
        }

    }

    override fun onBackPressed() {
        if (numOfScreen == 2) {
            numOfScreen = 1
            setContentView(R.layout.activity_sign_up_screen_1)

            // TODO : ERROR esto no se realiza por algun motivo
            usernameEditText.setText(username)
            passwordEditText.setText(password)
            mailEditText.setText(mail)
            if (userPictureLocationUri != null) {
                Glide.with(this).load(uriImage).into(foto_perfil)
            }
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
