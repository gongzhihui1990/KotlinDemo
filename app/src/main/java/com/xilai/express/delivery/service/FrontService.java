package com.xilai.express.delivery.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xilai.express.delivery.R;
import com.xilai.express.delivery.ui.activity.MainActivity;

/**
 * @author caroline
 * @date 2018/7/18
 */

public class FrontService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //定义一个notification
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("补充内容");
        builder.setContentText("正在运行...");
        builder.setContentTitle(getBaseContext().getString(R.string.demo_app_name)+"正在后台运行");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setTicker("查看应用");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        //把该service创建为前台service
        startForeground(1, notification);
    }
}
