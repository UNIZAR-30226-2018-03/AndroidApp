package com.spreadyourmusic.spreadyourmusic

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.spreadyourmusic.spreadyourmusic.apis.doLoginServer
import com.spreadyourmusic.spreadyourmusic.apis.obtainUserDataServer
import com.spreadyourmusic.spreadyourmusic.services.AmazonS3UploadFileService

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestBackEndSpreadYoutMusic {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.spreadyourmusic.spreadyourmusic", appContext.packageName)
    }

    @Test
    fun amazonServicesDelete() {
        AmazonS3UploadFileService.deleteFile("postmalone", InstrumentationRegistry.getTargetContext(), {
            val i = it
        })
    }

    @Test
    fun userGetServer() {
        obtainUserDataServer("lAngelP", "dfdgffgdfgdf")
    }

    @Test
    fun loginUserServer() {
        doLoginServer("lAngelP", "dfdgffgdfgdf")
    }
}
