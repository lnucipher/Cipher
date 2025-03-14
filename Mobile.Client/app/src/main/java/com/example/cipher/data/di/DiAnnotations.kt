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

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalUserStore

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsPreference
