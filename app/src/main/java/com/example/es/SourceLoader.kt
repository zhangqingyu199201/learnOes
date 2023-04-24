package com.example.es

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class SourceLoader {
    companion object {

        fun loadSource(ctx: Context, id: Int) : String {
            var resource = StringBuilder()

            try {
                var inputStream = ctx.resources.openRawResource(id)
                var readStream = InputStreamReader(inputStream)
                var bufferReader = BufferedReader(readStream)

                bufferReader.lineSequence().forEach {
                    resource.append(it)
                    resource.append("\n");

                }
            } catch (e: IOException) {

            }

            return resource.toString()
        }
    }

}