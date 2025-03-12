package com.example.cipher.ui.common.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioAttributes
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
import com.example.cipher.domain.models.settings.NotificationSound
import com.example.cipher.domain.models.settings.NotificationVibration
import com.example.cipher.domain.repository.notification.PushNotificationService
import com.example.cipher.domain.repository.settings.SettingsRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var pushNotificationService: PushNotificationService

    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val avatarCache = ConcurrentHashMap<String, Bitmap>()
    private val defaultAvatar by lazy {
        ContextCompat.getDrawable(this, R.drawable.account_circle_icon)?.toBitmap()!!
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val senderId = message.data["senderId"]
        val senderDisplayName = message.data["senderDisplayName"]
        val senderAvatarUrl = message.data["senderAvatarUrl"]
        val messageText = message.data["messageText"]
        val isMuted = message.data["isMuted"]?.toBoolean() ?: false

        if (senderId.isNullOrEmpty() ||
            senderDisplayName.isNullOrEmpty() ||
            senderAvatarUrl.isNullOrEmpty() ||
            messageText.isNullOrEmpty() ||
            senderId == ActiveScreenTracker.activeChatUserId.value) { return }

        serviceScope.launch {
            pushNotificationService.insertToUnreadNotification(senderId, isMuted, messageText)
            pushNotificationService.setMutedStatusBySender(senderId, isMuted)

            val notificationEnabled = settingsRepository.isNotificationEnabled()
            val notificationSound = settingsRepository.getNotificationSound()
            val notificationVibration = settingsRepository.getNotificationVibration()

            if (notificationEnabled && !isMuted) {
                createNotification(
                    senderAvatarUrl = NetworkKeys.IDENTITY_SERVER_BASE_URL + senderAvatarUrl,
                    notificationVibration = notificationVibration,
                    notificationSound = notificationSound,
                    senderDisplayName = senderDisplayName,
                    messageText = messageText
                )
            }
        }
    }

    private fun createNotification(
        notificationVibration: NotificationVibration,
        notificationSound: NotificationSound,
        senderDisplayName: String,
        senderAvatarUrl: String,
        messageText: String
    ) {
        createNotificationChannelIfNeeded(this, notificationSound)
        val notificationManager = NotificationManagerCompat.from(this)

        serviceScope.launch {
            val avatarBitmap = loadBitmap(senderAvatarUrl) ?: defaultAvatar
            withContext(Dispatchers.Main) {
                val messagingStyle = buildMessagingStyle(senderDisplayName, avatarBitmap, messageText)

                val notification = NotificationCompat.Builder(this@FirebaseNotificationService, "channel_${notificationSound.name}")
                    .setSmallIcon(R.drawable.mail_icon)
                    .setStyle(messagingStyle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setSound(notificationSound.getUri(this@FirebaseNotificationService))
                    .setVibrate(notificationVibration.vibration)
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
        avatarCache[url] ?: runCatching {
            Glide.with(this@FirebaseNotificationService)
                .asBitmap()
                .load(url)
                .circleCrop()
                .submit()
                .get()
        }.getOrNull()?.also { avatarCache[url] = it }
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

    private fun createNotificationChannelIfNeeded(context: Context, notificationSound: NotificationSound) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channelId = "channel_${notificationSound.name}"

        if (notificationManager.getNotificationChannel(channelId) != null) return

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(
            channelId,
            "Messenger Notifications ${notificationSound.name}",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            setSound(notificationSound.getUri(context), audioAttributes)
        }

        notificationManager.createNotificationChannel(channel)
    }

    internal object NotificationID {
        private val c = AtomicInteger(100)
        val iD: Int
            get() = c.incrementAndGet()
    }
}
