package com.spreadyourmusic.spreadyourmusic.helpers

import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

const val TYPE_DELETE = 0
const val TYPE_PUT = 1
const val TYPE_GET = 2
const val TYPE_POST = 3

/**
 * Download a JSON file from a server, parse the content and return the JSON
 * object.
 *
 * @return result JSONObject containing the parsed representation.
 */
@Throws(JSONException::class)
fun fetchJSONFromUrl(urlString: String, bodyData: List<Pair<String, String>>?, type: Int): JSONObject? {
    var reader: BufferedReader? = null
    try {
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        // Se coloca el método correcto
        urlConnection.requestMethod = when (type) {
            TYPE_POST -> "POST"
            TYPE_PUT -> "PUT"
            TYPE_DELETE -> "DELETE"
            TYPE_GET -> "GET"
            else -> "POST"
        }

        urlConnection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded")

        if (bodyData != null && !bodyData.isEmpty()) {

            // Se establece que tiene body
            urlConnection.doOutput = true

            val urlParameters = bodyData.map {
                "${it.first}=${it.second}"
            }.reduce { acc, s ->
                "$acc&$s"
            }

            val output = BufferedOutputStream(urlConnection.outputStream)
            val writer = BufferedWriter(OutputStreamWriter(output, "UTF-8"))
            writer.write(urlParameters)
            writer.flush()
            writer.close()
            output.close()
        }

        urlConnection.connect()

        reader = BufferedReader(InputStreamReader(
                urlConnection.inputStream, "iso-8859-1"))
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while (!line.isNullOrEmpty()) {
            sb.append(line)
            line = reader.readLine()
        }
        return JSONObject(sb.toString())
    } catch (e: JSONException) {
        throw e
    } catch (e: Exception) {

        return null
    } finally {
        if (reader != null) {
            try {
                reader.close()
            } catch (e: IOException) {
                // ignore
            }

        }
    }
}