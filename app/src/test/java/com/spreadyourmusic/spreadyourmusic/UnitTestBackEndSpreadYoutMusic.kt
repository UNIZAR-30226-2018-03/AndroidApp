package com.spreadyourmusic.spreadyourmusic

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.util.StringUtils
import com.spreadyourmusic.spreadyourmusic.apis.getFollowedPlaylistsServer
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTestBackEndSpreadYoutMusic {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun nombre() {
        val x = getFollowedPlaylistsServer("abelcht")
        val i = 0;
    }

    @Test
    fun upload_song() {
        val accessKey = "69RY1JSB5DQLTWXV2TT9"
        val secretKey = "575Mp0DbjzXbZ8AKaswxu9KytL4uqX9S6GDBF9PW"

        val credentials = BasicAWSCredentials(accessKey, secretKey)
        val conn = AmazonS3Client(credentials)
        conn.endpoint = "http://155.210.13.105:7480"

        val buckets = conn.listBuckets()
        for (bucket in buckets) {
            println(bucket.name + "\t" +
                    StringUtils.fromDate(bucket.creationDate))
        }
    }
}
