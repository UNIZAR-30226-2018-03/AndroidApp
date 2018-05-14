package com.spreadyourmusic.spreadyourmusic.helpers

import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
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
        var line: String = reader.readLine()
        while (line.isNotEmpty()) {
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