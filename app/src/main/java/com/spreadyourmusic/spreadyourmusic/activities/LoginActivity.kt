package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.graphics.drawable.GradientDrawable
import com.spreadyourmusic.spreadyourmusic.R

import java.util.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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

    fun loginAction(v: View){
        val int = Intent(applicationContext, HomeActivity::class.java)
        startActivity(int)
    }

    fun signupAction(v: View){

    }

    fun loginGoogleAction(v: View){

    }
}
