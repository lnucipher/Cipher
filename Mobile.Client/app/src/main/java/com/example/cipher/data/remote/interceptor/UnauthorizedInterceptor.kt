package com.example.cipher.data.remote.interceptor

import android.content.Context
import com.example.cipher.domain.repository.auth.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 401) {
            runBlocking {
                authRepository.logout(context)
            }
        }
        return response
    }
}