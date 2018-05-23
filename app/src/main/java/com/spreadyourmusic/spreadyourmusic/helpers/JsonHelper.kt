package com.spreadyourmusic.spreadyourmusic.helpers

import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

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
fun fetchJSONFromUrl(urlString: String, postData: List<Pair<String, String>>): JSONObject? {
    var reader: BufferedReader? = null
    try {

        var urlParameters  = ""

        for (i in postData){
            urlParameters="$urlParameters&${i.first}=${i.second}"
        }
        urlParameters = urlParameters.substring(1)

        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection

        urlConnection.requestMethod = "POST"

        val output = BufferedOutputStream(urlConnection.getOutputStream())
        val writer = BufferedWriter ( OutputStreamWriter(output, "UTF-8"))
        writer.write(urlParameters)
        writer.flush()
        writer.close()
        output.close()
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