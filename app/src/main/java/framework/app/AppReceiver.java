package framework.app;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.xilai.express.delivery.service.FrontService;

import java.util.ArrayList;

import framework.util.Loger;

/**
 * @author caroline
 * @date 2018/7/20
 */

public class AppReceiver extends BroadcastReceiver {
    /**
     * 判断服务是否运行
     *
     * @param context
     * @param serviceName
     * @return
     */
    public boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取当前所有服务
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Loger.INSTANCE.e("action:" + intent.getAction());
        startForegroundService(context);
    }

    /**
     * 开个保活服务
     */
    private void startForegroundService(Context context) {
        if (isServiceWorked(context, FrontService.class.getName())) {
            //无需再开启
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, FrontService.class));
        } else {
            // Pre-O behavior.
            context.startService(new Intent(context, FrontService.class));
        }
    }
}
