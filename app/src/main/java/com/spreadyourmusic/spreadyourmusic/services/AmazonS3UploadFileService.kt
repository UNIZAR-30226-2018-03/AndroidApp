package com.spreadyourmusic.spreadyourmusic.services

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.model.CannedAccessControlList
import java.io.File
import java.lang.Exception


object AmazonS3UploadFileService {
    val connection:AmazonS3Client
    init {
        val accessKey = "69RY1JSB5DQLTWXV2TT9"
        val secretKey = "575Mp0DbjzXbZ8AKaswxu9KytL4uqX9S6GDBF9PW"

        val credentials = BasicAWSCredentials(accessKey, secretKey)
        connection = AmazonS3Client(credentials)
        connection.endpoint = "http://155.210.13.105:7480"
    }
    fun uploadFile(filePath : String, key:String, context:Context, l: (String) -> Unit ){
        val transferUtility = TransferUtility.builder().s3Client(connection).context(context).build()
        val transferObserver = transferUtility.upload("Public",key, File(filePath),CannedAccessControlList.PublicRead)///storage/emulated/0/Music/Prueba/postmalone.mp3
        transferObserver.setTransferListener(object : TransferListener {
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if(state == TransferState.COMPLETED){
                    l("StateChanged completado")
                }else if (state == TransferState.FAILED){
                    l("StateChanged fallo")
                }else{
                    l("StateChanged irreconocible")
                }
            }

            override fun onError(id: Int, ex: Exception?) {
                l("onError ${ex!!.message}")
            }
        })
    }
    //transferUtility = new TransferUtility(s3, getApplicationContext());
}