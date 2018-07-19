package com.xilai.express.delivery.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.IBinder
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import com.xilai.express.delivery.R
import com.xilai.express.delivery.ui.activity.MainActivity
import framework.util.Loger


/**
 * @author caroline
 * @date 2018/7/18
 */

class FrontService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {
        super.onCreate()
        //定义一个notification
        val builder = Notification.Builder(this)
        builder.setContentInfo("补充内容")
        builder.setContentText("正在运行...")
        builder.setContentTitle(baseContext.getString(R.string.demo_app_name) + "正在后台运行")
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.setTicker("查看应用")
        builder.setAutoCancel(true)
        builder.setWhen(System.currentTimeMillis())
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setContentIntent(pendingIntent)
        val notification: Notification? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val CHANNEL_ONE_ID = "com.xilai.express.delivery"
                val CHANNEL_ONE_NAME = "后台运行频道"
                val notificationChannel: NotificationChannel
                notificationChannel = NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.setLightColor(Color.RED)
                notificationChannel.setShowBadge(true)
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(notificationChannel)
            }
            builder.build()
        } else {
            builder.notification
        }
        //把该service创建为前台service
        startForeground(0, notification)

        Loger.w("把该service创建为前台service")
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.xilai.express.delivery")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val vibrator = getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(50L)
            Loger.w(intent.action + ":data [" + intent.extras.getString("data") + "]")
        }
    }
}
