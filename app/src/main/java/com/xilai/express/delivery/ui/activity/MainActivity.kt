package com.xilai.express.delivery.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.xilai.express.delivery.R
import com.xilai.express.delivery.service.FrontService
import com.xilai.express.delivery.ui.BaseActivity
import com.xilai.express.delivery.ui.fragment.NullFragment
import com.xilai.express.delivery.ui.fragment.PlayFragment
import kotlinx.android.synthetic.main.activity_main.*

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
