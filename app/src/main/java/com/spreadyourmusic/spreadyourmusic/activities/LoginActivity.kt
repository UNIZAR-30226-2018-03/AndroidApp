package com.spreadyourmusic.spreadyourmusic.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.doGoogleLogin
import com.spreadyourmusic.spreadyourmusic.controller.doLogin
import com.spreadyourmusic.spreadyourmusic.controller.isLogin
import kotlinx.android.synthetic.main.activity_login.*

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.spreadyourmusic.spreadyourmusic.controller.isCurrentUserLoggedinOtherSession
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton




class LoginActivity : AppCompatActivity() {

    private val rcSignIn = 9001
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if(isLogin(this)){
            openHomeActivity()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // TODO: Cambiar client id a client id de release
        val serverClientId = "946633470887-7sre5m4dltntp7ijsc1looufu7seu77i.apps.googleusercontent.com"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun loginAction(v: View){
        val username = userEditText.text.toString()
        val pass = passwordEditText.text.toString()
        val progressDialog = showProgressDialog()
        Thread {
            val user = doLogin(User(username, pass),this)
            runOnUiThread {
                progressDialog.dismiss()
                if(user){
                    onLoginSuccessful()
                }else{
                    onLoginFailure()
                }
            }
        }.start()
    }

    fun signupAction(v: View){
        openSignupActivity()
    }

    fun loginGoogleAction(v: View){
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    private fun openHomeActivity(){
        isCurrentUserLoggedinOtherSession(this,{
            SessionSingleton.lastSongListened = it
            SessionSingleton.isUserDataLoaded = false
            val int = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(int)
            finish()
        })
    }

    private fun openSignupActivity(){
        val int = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(int)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == rcSignIn) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account:GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val progressDialog = showProgressDialog()
            Thread {
                val user = doGoogleLogin(account,this)
                runOnUiThread {
                    progressDialog.dismiss()
                    if(user){
                        onLoginSuccessful()
                    }else{
                        onLoginFailure()
                    }
                }
            }.start()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            onLoginFailure()
        }
    }

    private fun onLoginSuccessful(){
        openHomeActivity()
    }

    private fun onLoginFailure(){
        Toast.makeText(applicationContext,R.string.incorrect_user_or_password,Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() : ProgressDialog{
        // Cambiar mensaje mostrado
        val progressDialog = ProgressDialog(this,
                R.style.AppTheme_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage(resources.getString(R.string.authenticating))
        progressDialog.show()
        return progressDialog
    }
}
