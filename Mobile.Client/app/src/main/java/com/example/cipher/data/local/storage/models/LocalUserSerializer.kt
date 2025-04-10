package com.example.cipher.data.local.storage.models

import androidx.datastore.core.Serializer
import com.example.cipher.LocalUserProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream


object LocalUserSerializer: Serializer<LocalUserProto> {
    override val defaultValue: LocalUserProto = LocalUserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LocalUserProto {
        return try {
            LocalUserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: LocalUserProto, output: OutputStream) = t.writeTo(output)
}