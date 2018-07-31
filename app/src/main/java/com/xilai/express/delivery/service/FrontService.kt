package com.xilai.express.delivery.service

import android.annotation.SuppressLint
import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.RemoteViews
import com.xilai.express.delivery.R
import com.xilai.express.delivery.ui.activity.MainActivity
import framework.app.AppReceiver
import framework.app.ScheduleService
import framework.util.Loger
import java.util.*


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
        showNotification()
        //定时检查任务
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
        val jobBuilder = JobInfo.Builder(1024, ComponentName(packageName, ScheduleService::class.java.name))
        jobBuilder.setPeriodic((60 * 1000).toLong())
        jobBuilder.setPersisted(true)
        jobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        val schedule = jobScheduler.schedule(jobBuilder.build())
        if (schedule <= 0) {
            Log.e(AppReceiver::class.java.name, "schedule error！")
        }

        //测试广播
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.xilai.express.delivery")
        registerReceiver(receiver, intentFilter)
    }


    private fun showNotification() {
        val id = "com.xilai.express.delivery"
        val builder = NotificationCompat.Builder(this, id)

        val remoteView = RemoteViews(packageName, R.layout.notification_light_layout)

        remoteView.setImageViewResource(R.id.nIcon, R.mipmap.icon_user)
        remoteView.setTextViewText(R.id.opt1, "接单")
        remoteView.setTextViewText(R.id.opt2, "打单")
        remoteView.setTextViewText(R.id.opt3, "消息")

        val openIntent01 = Intent(this, MainActivity::class.java)
        openIntent01.putExtra("itemIndex", 1)
        val openIntent02 = Intent(this, MainActivity::class.java)
        openIntent02.putExtra("itemIndex", 2)
        val openIntent03 = Intent(this, MainActivity::class.java)
        openIntent03.putExtra("itemIndex", 3)

        val intentOpt1 = PendingIntent.getActivity(this, UUID.randomUUID().hashCode(), openIntent01, PendingIntent.FLAG_UPDATE_CURRENT)
        val intentOpt2 = PendingIntent.getActivity(this, UUID.randomUUID().hashCode(), openIntent02, PendingIntent.FLAG_UPDATE_CURRENT)
        val intentOpt3 = PendingIntent.getActivity(this, UUID.randomUUID().hashCode(), openIntent03, PendingIntent.FLAG_UPDATE_CURRENT)

        remoteView.setOnClickPendingIntent(R.id.lay1, intentOpt1)
        remoteView.setOnClickPendingIntent(R.id.lay2, intentOpt2)
        remoteView.setOnClickPendingIntent(R.id.lay3, intentOpt3)

        remoteView.setOnClickPendingIntent(R.id.opt1, intentOpt1)
        remoteView.setOnClickPendingIntent(R.id.opt2, intentOpt2)
        remoteView.setOnClickPendingIntent(R.id.opt3, intentOpt3)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setCustomBigContentView(remoteView)
        }
        builder.setContent(remoteView)

        //val remoteViewBig = RemoteViews(packageName, R.layout.notification_layout_big)
        //builder.setCustomBigContentView(remoteViewBig)

        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setOngoing(true)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.setWhen(System.currentTimeMillis())
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "后台运行频道"
            val notificationChannel: NotificationChannel
            notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }

        val notification: Notification = builder.build()
        startForeground(1, notification)

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val vibrator = getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100L)
            Loger.w(intent.action + ":data [" + intent.extras.getString("data") + "]")
        }
    }
}
