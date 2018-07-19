package com.xilai.express.delivery.ui.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.view.PagerAdapter
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xilai.express.delivery.R
import com.xilai.express.delivery.service.FrontService
import com.xilai.express.delivery.ui.BaseActivity
import framework.util.Loger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by caroline on 2018/7/18.
 */

class MainActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(20L)
            tvContent.text = intent.action + ":data [" + intent.extras.getString("data") + "]"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvContent.text = "开启监听"
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.xilai.express.delivery")
        registerReceiver(receiver, intentFilter)
        btnSend.setOnClickListener({
            val intent = Intent("com.xilai.express.delivery")
            intent.putExtra("data", Date().toGMTString())
            sendBroadcast(Intent(intent))
        })
        startService(Intent(baseContext, FrontService::class.java))

        val viewList = ArrayList<View>()
        var i = 0
        while (i < 3) {
            i++
            val vChild = TextView(baseContext)
            vChild.text = "view" + i.toString()
            vChild.setTextColor(Color.BLACK)
            vChild.gravity = Gravity.CENTER
            vChild.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            viewList.add(vChild)
        }
        viewPager.adapter = AdViewPagerAdapter(this, viewList)

        btnPress.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewB.visibility = View.VISIBLE
                }
                MotionEvent.ACTION_UP -> {
                    viewB.visibility = View.GONE
                }
                else -> Loger.i("action:" + event.action)
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class AdViewPagerAdapter(private val context: Context, private val list: List<View>) : PagerAdapter() {

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(list[position])// 删除页卡
        }

        // 这个方法用来实例化页卡
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(list[position], 0)// 添加页卡
            return list[position]
        }

        // 返回页卡的数量
        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1// 官方提示这样写
        }
    }
}
