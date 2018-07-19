/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.app

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.text.TextUtils
import framework.util.Loger
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * @author caroline
 */

abstract class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.baseContext
        initApk()
    }

    override fun onTrimMemory(level: Int) {
        Loger.i("level " + level)
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        Loger.i("onTerminate")
        super.onTerminate()
    }

    /**
     * 初始化各种sdk,各种工具
     */
    abstract fun initApk()

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set

        /**
         * 获取进程号对应的进程名
         *
         * @return 进程名
         */
        val processName: String?
            get() {
                val pid = android.os.Process.myPid()
                var reader: BufferedReader? = null
                try {
                    reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
                    var processName = reader.readLine()
                    if (!TextUtils.isEmpty(processName)) {
                        processName = processName.trim { it <= ' ' }
                    }
                    return processName
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                } finally {
                    try {
                        if (reader != null) {
                            reader.close()
                        }
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    }

                }
                val am = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningApps = am.runningAppProcesses ?: return "unKnow"
                runningApps
                        .filter { it.pid == pid }
                        .forEach { return it.processName }
                return "unKnow"
            }
    }

}
