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
fun fetchJSONFromUrl(urlString: String): JSONObject? {
    var reader: BufferedReader? = null
    try {
        val urlConnection = URL(urlString).openConnection()
        reader = BufferedReader(InputStreamReader(
                urlConnection.getInputStream(), "iso-8859-1"))
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

/**
 * Download a JSON file from a server, parse the content and return the JSON
 * object.
 *
 * @return result JSONObject containing the parsed representation.
 */
@Throws(JSONException::class)
fun fetchJSONFromUrl(urlString: String, postData: List<Pair<String, String>>, type: Int): JSONObject? {
    var reader: BufferedReader? = null
    try {

        var urlParameters = ""

        for (i in postData) {
            urlParameters = "$urlParameters&${i.first}=${i.second}"
        }
        urlParameters = urlParameters.substring(1)

        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.requestMethod = when(type){
            TYPE_POST -> "POST"
            TYPE_PUT -> "PUT"
            TYPE_DELETE -> "DELETE"
            else -> "POST"
        }

        if(!urlParameters.isNullOrBlank()){
            val output = BufferedOutputStream(urlConnection.getOutputStream())
            val writer = BufferedWriter(OutputStreamWriter(output, "UTF-8"))
            writer.write(urlParameters)
            writer.flush()
            writer.close()
            output.close()
        }

        urlConnection.connect()

        reader = BufferedReader(InputStreamReader(
                urlConnection.getInputStream(), "iso-8859-1"))
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