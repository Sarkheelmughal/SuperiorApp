package learningapp.superior.org

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyMessigingService:FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        showNotification(p0.notification?.title.toString(), p0.notification?.body.toString())
    }

    fun showNotification(title:String ,message:String ){
       val builder =NotificationCompat.Builder(this,"MyNotificaton")

            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentText(message)
           .setSmallIcon(R.drawable.logo)


        val manager= NotificationManagerCompat.from(this)
        manager.notify(999,builder.build())
    }
}