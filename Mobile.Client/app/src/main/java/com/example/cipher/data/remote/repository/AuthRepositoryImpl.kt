package com.example.cipher.data.remote.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.cipher.data.mappers.toLocalUser
import com.example.cipher.data.remote.api.AuthApi
import com.example.cipher.data.remote.api.dto.ErrorResponse
import com.example.cipher.domain.models.auth.AuthResult
import com.example.cipher.domain.models.auth.SignInRequest
import com.example.cipher.domain.models.auth.SignUpRequest
import com.example.cipher.domain.repository.auth.AuthRepository
import com.example.cipher.domain.repository.auth.JwtTokenManager
import com.example.cipher.domain.repository.user.LocalUserManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: JwtTokenManager,
    private val localUserManager: LocalUserManager,
    private val context: Context
) : AuthRepository {

    override suspend fun signUp(request: SignUpRequest, avatarUrl: String?): AuthResult<Nothing> {
        return try {
            val response = api.signUp(request, convertImgUrlToMultipart(context, avatarUrl))

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let {
                    tokenManager.saveAccessJwt(it.token)
                    localUserManager.saveUser(it.toLocalUser())
                }

                AuthResult.Authorized
            } else {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(ErrorResponse::class.java)

                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                AuthResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            AuthResult.Error("Connection failed")
        } catch (exception: Exception) {
            AuthResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    override suspend fun signIn(request: SignInRequest): AuthResult<Nothing> {
        return try {
            val response = api.signIn(request)

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.let {
                    tokenManager.saveAccessJwt(it.token)
                    localUserManager.saveUser(it.toLocalUser())
                }

                AuthResult.Authorized
            } else {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(ErrorResponse::class.java)

                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                AuthResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            AuthResult.Error("Connection failed")
        } catch (exception: Exception) {
            AuthResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    override suspend fun ifUserExist(username: String): AuthResult<Boolean> {
        return try {
            val response = api.isUserExist(username)

            if (response.isSuccessful) {
                response.body()?.let {
                    AuthResult.WithData(it.value)
                } ?: AuthResult.Error("Unexpected response body format.")
            } else {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(ErrorResponse::class.java)

                val errorBody = response.errorBody()
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it.string()) }

                AuthResult.Error(errorResponse?.error ?: "Unknown error")
            }
        } catch (exception: IOException) {
            AuthResult.Error("Connection failed")
        } catch (exception: Exception) {
            AuthResult.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    override suspend fun logout(context: Context) {
        tokenManager.clearAllTokens()
        localUserManager.clearUser()

        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    private fun convertImgUrlToMultipart(context: Context, imageUri: String?): MultipartBody.Part? {
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

