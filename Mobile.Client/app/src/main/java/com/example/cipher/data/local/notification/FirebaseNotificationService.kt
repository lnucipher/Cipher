package com.example.cipher.data.local.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.cipher.R
import com.example.cipher.data.NetworkKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val senderDisplayName = message.data["senderDisplayName"]
        val senderAvatarUrl = message.data["senderAvatarUrl"]
        val messageText = message.data["messageText"]

        if (senderDisplayName.isNullOrEmpty() ||
            senderAvatarUrl.isNullOrEmpty() ||
            messageText.isNullOrEmpty()) return

        createNotification(
            senderDisplayName = senderDisplayName,
            senderAvatarUrl = NetworkKeys.IDENTITY_SERVER_BASE_URL + senderAvatarUrl,
            messageText = messageText
        )
    }

    private fun createNotification(
        senderDisplayName: String,
        senderAvatarUrl: String,
        messageText: String
    ) {
        createNotificationChannel(this)
        val notificationManager = NotificationManagerCompat.from(this)

        CoroutineScope(Dispatchers.IO).launch {
            val avatarBitmap = loadBitmap(senderAvatarUrl) ?: getDefaultBitmap()
            withContext(Dispatchers.Main) {
                val messagingStyle = buildMessagingStyle(senderDisplayName, avatarBitmap, messageText)
                val notification = NotificationCompat.Builder(this@FirebaseNotificationService, CHANNEL_ID)
                    .setSmallIcon(R.drawable.mail_icon)
                    .setStyle(messagingStyle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                if (ActivityCompat.checkSelfPermission(
                        this@FirebaseNotificationService,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(NotificationID.iD, notification)
                }
            }
        }
    }

    private suspend fun loadBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Glide.with(this@FirebaseNotificationService)
                .asBitmap()
                .load(url)
                .circleCrop()
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }
    }

    private fun getDefaultBitmap(): Bitmap {
        return ContextCompat.getDrawable(this, R.drawable.cipher_logo_dark)?.toBitmap()!!
    }

    private fun buildMessagingStyle(
        senderDisplayName: String,
        avatarBitmap: Bitmap,
        messageText: String
    ): NotificationCompat.MessagingStyle {
        val sender = Person.Builder()
            .setName(senderDisplayName)
            .setIcon(IconCompat.createWithBitmap(avatarBitmap))
            .build()

        return NotificationCompat.MessagingStyle(sender)
            .setConversationTitle("Chat with $senderDisplayName")
            .addMessage(messageText, Date().time, sender)
    }


    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    internal object NotificationID {
        private val c = AtomicInteger(100)
        val iD: Int
            get() = c.incrementAndGet()
    }

    companion object {
        const val CHANNEL_ID = "messenger_channel"
        const val CHANNEL_NAME = "Messenger Notifications"
    }
}
