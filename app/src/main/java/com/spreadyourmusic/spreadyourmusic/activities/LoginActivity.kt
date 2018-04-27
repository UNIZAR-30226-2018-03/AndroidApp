package com.spreadyourmusic.spreadyourmusic.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.doGoogleLogin
import com.spreadyourmusic.spreadyourmusic.controller.doLogin
import com.spreadyourmusic.spreadyourmusic.controller.isLogin
import kotlinx.android.synthetic.main.activity_login.*

import java.util.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.spreadyourmusic.spreadyourmusic.models.User


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 9001

    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(isLogin(this)){
            openHomeActivity()
        }

        // TODO: Contrastar con backend https://developers.google.com/identity/sign-in/android/backend-auth
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun openHomeActivity(){
        val int = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(int)
        finish()
    }

    private fun openSignupActivity(){
        val int = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(int)
    }

    fun randomBackgroundColorGenerator(v: View){
        val randomGenerator = Random()
        val red:Int = randomGenerator.nextInt(180)
        val green:Int = randomGenerator.nextInt(180)
        val blue:Int = randomGenerator.nextInt(180)

        val maxRed:Int = red + randomGenerator.nextInt(256-red)
        val maxGreen:Int = red + randomGenerator.nextInt(256-green)
        val maxBlue:Int = red + randomGenerator.nextInt(256-blue)

        val cambiar:Int = randomGenerator.nextInt(3)
        val startColor:Int = 255 and 0xff shl 24 or (red and 0xff shl 16) or (green and 0xff shl 8) or (maxBlue and 0xff)

        val finalColor:Int =  when(cambiar){
            0 -> 255 and 0xff shl 24 or (red and 0xff shl 16) or (green and 0xff shl 8) or (blue and 0xff)
            1 -> 255 and 0xff shl 24 or (red and 0xff shl 16) or (maxGreen and 0xff shl 8) or (blue and 0xff)
            else -> 255 and 0xff shl 24 or (maxRed and 0xff shl 16) or (green and 0xff shl 8) or (maxBlue and 0xff)
        }

        val i:IntArray = intArrayOf(startColor, finalColor)
        val mDrawable = GradientDrawable(GradientDrawable.Orientation .BOTTOM_TOP,i)
        findViewById<View>(R.id.fondo).background = mDrawable
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
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
        var progressDialog = ProgressDialog(this,
                R.style.AppTheme_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()
        return progressDialog
    }
}
