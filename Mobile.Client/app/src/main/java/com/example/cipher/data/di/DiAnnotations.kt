package com.example.cipher.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticatedClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JwtTokenPreference