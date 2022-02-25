package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import java.util.*
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            println(message.data["content"])
            val push = gson.fromJson(message.data["content"], Push::class.java)
            when (push.recipientId) {
                AppAuth.getInstance().authStateFlow.value.id, null -> handlePush(push)
                else -> AppAuth.getInstance().sendPushToken()
            }
        } catch (e: JsonSyntaxException) {
            Log.e("FSMService", "can't parse :${message.data["content"]}")
        }
    }

    private fun handlePush (push: Push) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText(push.content)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    override fun onNewToken(token: String) {
       AppAuth.getInstance().sendPushToken(token)
        println(token)

    }
}

data class Push(val recipientId: Long?, val content: String)
