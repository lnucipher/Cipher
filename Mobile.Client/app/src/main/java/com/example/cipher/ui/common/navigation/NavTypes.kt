package com.example.cipher.ui.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

val UserType = navTypeOf<User>()
val LocalUserType = navTypeOf<LocalUser>()

private inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))

}

