package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import kotlinx.android.synthetic.main.content_sign_up_screen_1.*
import kotlinx.android.synthetic.main.content_sign_up_screen_2.*
import java.util.*
import android.app.DatePickerDialog
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.DatePickerFragment
import com.spreadyourmusic.spreadyourmusic.helpers.getPathFromUri
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.content_sign_up_screen_1.view.*
import kotlinx.android.synthetic.main.content_sign_up_screen_2.view.*


class SignUpActivity : AppCompatActivity() {
    private var idEditActivity = false
    private var numOfScreen = 1
    private var username: String? = null
    private var realname: String? = null
    private var password: String? = null
    private var mail: String? = null
    private var userBirth: Date? = null
    private var userTwitterAccount: String? = null
    private var userInstagramAccount: String? = null
    private var userFacebookAccount: String? = null
    private var userPictureLocationUri: String? = null

    private var userId: String? = null

    private var fragmentPage1: FragmentSignUpPage1? = null
    private var fragmentPage2: FragmentSignUpPage2? = null


    private val onContinueFunction: ((String?, String?, String?, String?) -> Unit) = { username, mail, password, userPictureLocationUri ->
        this.username = username
        this.mail = mail
        this.password = password
        this.userPictureLocationUri = userPictureLocationUri

        numOfScreen = 2
        changeActualFragment(fragmentPage2!!)
    }

    private val onCreateFunction: ((String?, Date?, String?, String?, String?) -> Unit) = { realname, userBirth, userFacebookAccount, userTwitterAccount, userInstagramAccount ->
        this.realname = realname
        this.userBirth = userBirth
        this.userFacebookAccount = userFacebookAccount
        this.userTwitterAccount = userTwitterAccount
        this.userInstagramAccount = userInstagramAccount

        val user = User(username!!, password!!, realname!!, userPictureLocationUri!!, mail, null, userBirth)
        user.twitterAccount = userTwitterAccount
        user.facebookAccount = userFacebookAccount
        user.instagramAccount = userInstagramAccount
        if (!idEditActivity) {
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
        } else {
            updateUserData(user, this, {
                if (!it.isNullOrEmpty()) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                } else {
                    finish()
                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userId = intent.getStringExtra(resources.getString(R.string.user_id))

        if (userId != null)
            toolbar.setTitle(R.string.update_user)
        else
            toolbar.setTitle(R.string.signup)


        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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

                    fragmentPage1 = FragmentSignUpPage1.newInstance(username, mail, userPictureLocationUri, true, onContinueFunction)
                    fragmentPage2 = FragmentSignUpPage2.newInstance(realname, userBirth, userTwitterAccount, userInstagramAccount, userFacebookAccount, true, onCreateFunction)
                    changeActualFragment(fragmentPage1!!)
                }

            }, this)
        } else {
            fragmentPage1 = FragmentSignUpPage1.newInstance(null, null, null, false, onContinueFunction)
            fragmentPage2 = FragmentSignUpPage2.newInstance(null, null, null, null, null, false, onCreateFunction)
            changeActualFragment(fragmentPage1!!)
        }
    }

    override fun onBackPressed() {
        if (numOfScreen == 2) {
            numOfScreen = 1
            changeActualFragment(fragmentPage1!!)

        } else
            super.onBackPressed()
    }

    private fun changeActualFragment(fragmento: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentForFragments, fragmento)
                .commit()
    }

    class FragmentSignUpPage1 : Fragment() {
        var username: String? = null
        var mail: String? = null
        var password: String? = null
        var userPictureLocationUri: String? = null
        var l: ((String?, String?, String?, String?) -> Unit)? = null
        var isEdition: Boolean = false
        private var selectPictureCode: Int = 567

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.content_sign_up_screen_1, container, false)
            view.signUpButton.setOnClickListener { onContinueClick() }
            view.usernameEditText.setText(username)
            view.mailEditText.setText(mail)
            if (userPictureLocationUri != null)
                Glide.with(this).load(userPictureLocationUri).into(view.coverCircleImageView)

            if (isEdition)
                view.usernameEditText.isEnabled = false

            view.coverCircleImageView.setOnClickListener { onProfilePictureClick() }
            return view
        }

        private fun onContinueClick() {
            username = view!!.usernameEditText.text.toString().trim()
            password = view!!.passwordEditText.text.toString().trim()
            mail = view!!.mailEditText.text.toString().trim()

            if (password.isNullOrEmpty() || username.isNullOrEmpty() || (!isEdition && userPictureLocationUri.isNullOrEmpty())) {
                Toast.makeText(activity, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                Toast.makeText(activity, R.string.empty_fields_1, Toast.LENGTH_SHORT).show()

            } else
                l!!.invoke(username, mail, password, userPictureLocationUri)
        }

        private fun onProfilePictureClick() {
            val intent = Intent()
                    .setType("image/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.select_file)), selectPictureCode)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode != RESULT_OK) {
                Toast.makeText(activity, R.string.select_valid_file, Toast.LENGTH_SHORT).show()
            } else if (requestCode == selectPictureCode) {
                val uriImage = data!!.data
                userPictureLocationUri = getPathFromUri(context!!, uriImage!!)

                if (userPictureLocationUri != null)
                    Glide.with(this).load(userPictureLocationUri).into(coverCircleImageView)
            }
        }

        companion object {
            fun newInstance(username: String?, mail: String?, userPictureLocationUri: String?,
                            isEdition: Boolean, l: (String?, String?, String?, String?) -> Unit): FragmentSignUpPage1 {
                val v = FragmentSignUpPage1()
                v.username = username
                v.mail = mail
                v.userPictureLocationUri = userPictureLocationUri
                v.l = l
                v.isEdition = isEdition
                return v
            }
        }
    }

    class FragmentSignUpPage2 : Fragment() {
        var realname: String? = null
        var userBirth: Date? = null
        var userTwitterAccount: String? = null
        var userInstagramAccount: String? = null
        var userFacebookAccount: String? = null
        var isEdition: Boolean = false

        var l: ((String?, Date?, String?, String?, String?) -> Unit)? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.content_sign_up_screen_2, container, false)
            view.realnameEditText.setText(realname)
            if (userBirth != null) {
                val selectedDate = userBirth!!.day.toString() + " / " + (userBirth!!.month + 1).toString() + " / " + userBirth!!.year.toString()
                view.birthDateEditText.setText(selectedDate)
            }
            view.twitterAccountEditText.setText(userTwitterAccount)
            view.instagramAccountEditText.setText(userInstagramAccount)
            view.facebookAccountEditText.setText(userFacebookAccount)
            view.createUserButton.setOnClickListener { onCreateClick() }
            view.birthDateEditText.setOnClickListener { onSelectDate() }
            if (isEdition)
                view.createUserButton.setText(R.string.update)
            return view
        }

        private fun onCreateClick() {
            realname = view!!.realnameEditText.text.toString().trim()
            userFacebookAccount = view!!.facebookAccountEditText.text.toString().trim()
            userTwitterAccount = view!!.twitterAccountEditText.text.toString().trim()
            userInstagramAccount = view!!.instagramAccountEditText.text.toString().trim()
            if (realname.isNullOrEmpty()) {
                Toast.makeText(activity, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                Toast.makeText(activity, R.string.empty_fields_2, Toast.LENGTH_SHORT).show()

            } else
                l!!.invoke(realname, userBirth, userFacebookAccount, userTwitterAccount, userInstagramAccount)
        }

        private fun onSelectDate() {
            val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // +1 because january is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
                birthDateEditText.setText(selectedDate)
                userBirth = GregorianCalendar(year, month, day).time
            })
            newFragment.show(activity!!.supportFragmentManager, "datePicker")
        }

        companion object {
            fun newInstance(realname: String?, userBirth: Date?, userTwitterAccount: String?, userInstagramAccount: String?,
                            userFacebookAccount: String?, isEdition: Boolean, l: (String?, Date?, String?, String?, String?) -> Unit): FragmentSignUpPage2 {
                val frag = FragmentSignUpPage2()
                frag.realname = realname
                frag.userBirth = userBirth
                frag.userTwitterAccount = userTwitterAccount
                frag.userInstagramAccount = userInstagramAccount
                frag.userFacebookAccount = userFacebookAccount
                frag.l = l
                frag.isEdition = isEdition
                return frag
            }
        }
    }
}
