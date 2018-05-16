package com.spreadyourmusic.spreadyourmusic.services

import android.content.Context
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.spreadyourmusic.spreadyourmusic.R
import java.io.File
import java.lang.Exception


object AmazonS3UploadFileService {
    private val connection: AmazonS3Client

    init {
        val accessKey = "_"
        val secretKey = "_"

        val credentials = BasicAWSCredentials(accessKey, secretKey)
        connection = AmazonS3Client(credentials)
        connection.endpoint = "http://155.210.13.105:7480"
    }

    /**
     * Sube al servidor el fichero localizado en @filePath con el nombre key al servidor de almacenamiento
     * Como resultado se llama a la función l con parametro null en caso de que haya sido correcta la operación
     * en caso contrario devuelve el error
     */
    fun uploadFile(filePath: String, key: String, context: Context, l: (String?) -> Unit) {
        val transferUtility = TransferUtility.builder().s3Client(connection).context(context).build()
        val transferObserver = transferUtility.upload("Public", key, File(filePath), CannedAccessControlList.PublicRead)///storage/emulated/0/Music/Prueba/postmalone.mp3
        transferObserver.setTransferListener(object : TransferListener {
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    l(null)
                } else if (state == TransferState.FAILED) {
                    l(context.resources.getString(R.string.error_upload_file_error))
                }
            }

            override fun onError(id: Int, ex: Exception?) {
                l("Error Amazon S3: ${ex!!.message}")
            }
        })
    }

    /**
     * Elimina el archivo con el key @key del servidor
     */
    fun deleteFile(key: String, context: Context, l: (String?) -> Unit) {
        // TODO : Esta fallando
        try {
            connection.deleteObject("Public", key)
            l(null)
        } catch (e: AmazonServiceException) {
            l(e.errorMessage)
        } catch (e: Exception) {
            l(e.message)
        }
    }
}