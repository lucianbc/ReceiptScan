package com.lucianbc.receiptscan.infrastructure.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.domain.model.file
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject

class ImagesDao @Inject constructor(
    private val context: Context
) {
    fun saveImage(image: Bitmap): String {
        val filename = ImagePath.random().value
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            image.compress(ImagePath.FORMAT, QUALITY, it)
        }
        return filename
    }

    fun readImage(path: String): Bitmap {
        val directory = context.filesDir
        val file = File(directory, path)
        val inputStream = FileInputStream(file)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun accessFile(path: ImagePath): Uri {
        val directory = context.filesDir
        val file = path.file(directory)
        if (!file.exists()) {
            throw FileNotFoundException("File at path $path does not exists in the internal storage")
        }
        return FileProvider.getUriForFile(context, "com.lucianbc.receiptscan", file)
    }

    fun deleteImage(path: String) {
        context.deleteFile(path)
    }

    companion object {
        const val QUALITY = 100
    }
}