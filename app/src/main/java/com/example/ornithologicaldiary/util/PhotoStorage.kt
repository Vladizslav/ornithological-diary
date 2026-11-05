package com.example.ornithologicaldiary.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

object PhotoStorage {

    private const val DIRECTORY = "photos"

    fun createFile(context: Context): File {
        val directory = File(context.filesDir, DIRECTORY)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return File(directory, "photo_${System.currentTimeMillis()}.jpg")
    }

    fun contentUri(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    fun copyToStorage(context: Context, uri: Uri): String? =
        try {
            val target = createFile(context)
            context.contentResolver.openInputStream(uri)?.use { input ->
                target.outputStream().use { output -> input.copyTo(output) }
            }
            target.absolutePath
        } catch (exception: IOException) {
            null
        }

    fun delete(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).delete() }
    }
}
