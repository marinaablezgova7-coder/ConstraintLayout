package ru.netology.nmedia.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText =
                getString(R.string.channel_remote_description)

            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(
                channelId,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val manager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {

        message.data[action]?.let {

            // Безопасно преобразуем строку в Action.
            // Если пришло неизвестное значение — приложение не упадёт.
            val action = runCatching {
                Action.valueOf(it)
            }.getOrNull()

            when (action) {

                Action.LIKE -> {
                    handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )
                }

                Action.NEW_POST -> {
                    handleNewPost(
                        gson.fromJson(
                            message.data[content],
                            NewPost::class.java
                        )
                    )
                }

                // Неизвестный action просто игнорируем.
                null -> return
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }


    private fun handleLike(content: Like) {

        val notification = NotificationCompat.Builder(
            this,
            channelId
        )
            .setSmallIcon(R.drawable.ic_netology_48dp)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notify(notification)
    }


    private fun handleNewPost(content: NewPost) {

        val notification = NotificationCompat.Builder(
            this,
            channelId
        )
            .setSmallIcon(R.drawable.ic_netology_48dp)
            .setContentTitle(
                "${content.author} опубликовал новый пост:"
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content.content)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notify(notification)
    }


    private fun notify(notification: Notification) {

        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat
                .from(this)
                .notify(
                    Random.nextInt(100_000),
                    notification
                )
        }
    }
}


enum class Action {
    LIKE,
    NEW_POST,
}


data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

data class NewPost(
    val author: String,
    val content: String,
)