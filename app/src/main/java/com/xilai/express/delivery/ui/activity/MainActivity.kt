package com.xilai.express.delivery.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.xilai.express.delivery.R
import com.xilai.express.delivery.service.FrontService
import com.xilai.express.delivery.ui.BaseActivity
import com.xilai.express.delivery.ui.fragment.NullFragment
import com.xilai.express.delivery.ui.fragment.PlayFragment
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

    /**
     * 开个保活服务
     */
    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(baseContext, FrontService::class.java))
        } else {
            // Pre-O behavior.
            startService(Intent(baseContext, FrontService::class.java))
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvContent.text = "开启监听"
        btnSend.setOnClickListener({
            val intent = Intent("com.xilai.express.delivery")
            intent.putExtra("data", Date().toGMTString())
            sendBroadcast(Intent(intent))
        })

        startForegroundService()

        val fragmentList = ArrayList<Fragment>()
        var i = 0
        val playIndex = 1
        val myIndexSize = 5
        while (i < myIndexSize) {
            val vChild: Fragment
            if (i == playIndex) {
                vChild = PlayFragment()
            } else {
                vChild = NullFragment()
            }
            i++
            fragmentList.add(vChild)
        }
        viewPager.adapter = AdViewPagerAdapter(fragmentList, supportFragmentManager)

        //为按钮做事件
//        btnPress.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    viewPager.isEnabled = false
//                    //viewB.visibility = View.VISIBLE
//                }
//                MotionEvent.ACTION_UP -> {
//                    viewPager.isEnabled = true
//                    //viewB.visibility = View.GONE
//                }
//                else -> Loger.i("action:" + event.action)
//            }
//            true
//        }
    }


    inner class AdViewPagerAdapter(private val list: List<Fragment>, fm: FragmentManager) :
            FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Fragment {
            return list.get(position)
        }


    }
}
