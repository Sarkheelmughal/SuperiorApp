package com.android.videoencyptionproject

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class BitmapPrefs(private val prefs : SharedPreferences) {

    fun saveBitmap(id : String,bitmap: Bitmap){
        prefs.edit().putString(id, bitMapToString(bitmap)).apply()
    }

    fun getBitmap(id : String) : Bitmap?{
        val str = prefs.getString(id,null) ?: return null
        return stringToBitMap(str)
    }

    private fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }


}