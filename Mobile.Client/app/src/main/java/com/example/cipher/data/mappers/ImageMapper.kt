package com.example.cipher.data.mappers

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object ImageMapper {
    fun convertImgUrlToMultipart(context: Context, imageUri: String?): MultipartBody.Part? {
        if (imageUri == null) return null
        val uri = Uri.parse(imageUri)

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val tempFile = File.createTempFile("selectedImage", ".jpg", context.cacheDir)
                tempFile.outputStream().use { fileOut ->
                    inputStream.copyTo(fileOut)
                }

                val requestBody = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("avatarFile", tempFile.name, requestBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}