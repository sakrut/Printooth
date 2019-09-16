package com.mazenrashed.printooth.data.converter

import com.mazenrashed.printooth.utilities.UniCode864Mapping
import java.nio.charset.Charset

/**
 * Default converter
 */
class Utf8Converter : Converter() {

    /*override fun toByteArray(input: String): ByteArray {
        val text = convert(input)
        return try {
            text.toByteArray(Charset.forName("Cp852"))
        } catch (e: Exception) {
            e.printStackTrace()
            byteArrayOf()
        }
    }*/
    override fun convert(input: Char): Byte {
        var toByteArray = String(charArrayOf(input)).toByteArray(Charset.forName("Cp852"))
        return toByteArray[0]
    }
}