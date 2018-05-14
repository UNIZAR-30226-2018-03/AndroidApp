package com.spreadyourmusic.spreadyourmusic

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
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
    fun amazonServicestest() {
       AmazonS3UploadFileService.uploadFile("/storage/emulated/0/Music/Prueba/postmalone.mp3","postmalone3", InstrumentationRegistry.getTargetContext(), {
           val i = it
       })

        Thread.sleep(10000)
    }
}
