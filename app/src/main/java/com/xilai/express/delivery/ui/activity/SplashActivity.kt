package com.xilai.express.delivery.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xilai.express.delivery.R
import com.xilai.express.delivery.ui.BaseActivity
import framework.rx.ProgressObserverImplementation
import framework.rx.RxHelper
import framework.util.Loger
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by caroline on 2018/7/20.
 */

class SplashActivity : BaseActivity() {


    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //同步初始化代码
        val taskInit = Observable.just("应用启动中").map({ param ->
            Loger.i(param)
            true
        })
        //蓝牙 电话通讯录 位置 相机、麦克风 存储空间android.permission.WRITE_SETTINGS
        val taskPermission = RxPermissions(this).request(Manifest.permission.WRITE_SETTINGS,Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS)
        //延迟操作
        val taskDelay = Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
        val zipperSplash = io.reactivex.functions.Function3<Boolean, Boolean, Boolean, Boolean>({ permission, delay, init -> permission && delay && init })
        val splashObservable = Observable.zip<Boolean, Boolean, Boolean, Boolean>(taskPermission, taskDelay, taskInit, zipperSplash)
        val splashObserver = object : ProgressObserverImplementation<Boolean>(this@SplashActivity) {
            override fun onNext(t: Boolean) {
                super.onNext(t)
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }.setShow(false)
        RxHelper.bindOnUI<Boolean>(splashObservable, splashObserver)
    }


}

