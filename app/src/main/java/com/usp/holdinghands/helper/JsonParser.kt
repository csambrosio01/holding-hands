package com.usp.holdinghands.helper

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class JsonParser(val context: Context) {
    fun getJsonFromFile(id: Int): JSONObject {
        val inputStream = context.resources.openRawResource(id)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            sb.append(
                """
                    $line
                    
                    """.trimIndent()
            )
        }

        inputStream.close()

        return JSONObject(sb.toString())
    }
}
