package com.example.cipher.data.network.interceptor

import com.example.cipher.data.NetworkKeys.HEADER_AUTHORIZATION
import com.example.cipher.data.NetworkKeys.TOKEN_TYPE
import com.example.cipher.domain.repository.auth.JwtTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private var tokenManager: JwtTokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessJwt()
        }
        val request = chain.request().newBuilder()
            .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
            .build()
        return chain.proceed(request)
    }
}