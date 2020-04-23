package com.wcp.pickmutipleimage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object CompressUtil {
    fun compressImageUri(context: Context, bitmapResource: Bitmap, reducedWidth: Double = 0.7, reducedHeight: Double = 0.7, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): Uri {
        // create new file
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.png")
        file.createNewFile()
        // bipmap compress
        val byteArrayOutputStream = ByteArrayOutputStream()
        val mResources = Bitmap.createScaledBitmap(bitmapResource, (bitmapResource.width * reducedWidth).toInt(), (bitmapResource.height * reducedHeight).toInt(), true)
        mResources.compress(compressFormat, 90, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()

        // write it now
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(bitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()

        return Uri.fromFile(file)
    }
}