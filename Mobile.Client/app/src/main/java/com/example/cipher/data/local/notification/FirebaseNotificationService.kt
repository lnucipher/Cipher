package com.example.cipher.data.local.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.cipher.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
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

        val channelId = "messenger_channel"
        createNotification(
            senderDisplayName = senderDisplayName,
            senderAvatarUrl = senderAvatarUrl,
            messageText = messageText,
            channelId = channelId
        )
    }

    private fun createNotification(
        senderDisplayName: String,
        senderAvatarUrl: String,
        messageText: String,
        channelId: String
    ) {
        createNotificationChannel(this, channelId)
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this@FirebaseNotificationService, channelId)
            .setSmallIcon(R.drawable.mail_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        Glide.with(this)
            .asBitmap()
            .load(senderAvatarUrl)
            .circleCrop()
            .error(R.drawable.cipher_logo_dark)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    avatarBitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val messagingStyle = buildMessagingStyle(senderDisplayName, avatarBitmap, messageText)
                    notification.setStyle(messagingStyle)

                    if (ActivityCompat.checkSelfPermission(
                            this@FirebaseNotificationService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    notificationManager.notify(NotificationID.iD, notification.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
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


    private fun createNotificationChannel(context: Context, channelId: String) {
        val channel = NotificationChannel(
            channelId,
            "Messenger Notifications",
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
}
