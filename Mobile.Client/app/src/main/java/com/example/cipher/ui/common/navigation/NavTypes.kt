package com.example.cipher.ui.common.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.example.cipher.domain.models.user.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val UserType = object : NavType<User>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getString(key)?.let { Json.decodeFromString<User>(it) }
    }

    override fun parseValue(value: String) = Json.decodeFromString<User>(value)

    override fun serializeAsValue(value: User): String = Json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: User) {
        bundle.putString(key, Json.encodeToString(value))
    }
}