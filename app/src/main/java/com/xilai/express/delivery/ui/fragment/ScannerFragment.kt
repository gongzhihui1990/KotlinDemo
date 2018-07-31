package com.xilai.express.delivery.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import com.xilai.express.delivery.R
import kotlinx.android.synthetic.main.fragment_scanner.*


/**
 * Created by caroline on 2018/7/19.
 */

class ScannerFragment : BaseFragment() {
    private val SCANNER_POWER_ON: String = "SCANNER_POWER_ON"
    private val SCANNER_OUTPUT_MODE: String = "SCANNER_OUTPUT_MODE"
    private val SCANNER_TERMINAL_CHAR: String = "SCANNER_TERMINAL_CHAR"
    private val SCANNER_PREFIX: String = "SCANNER_PREFIX"
    private val SCANNER_SUFFIX: String = "SCANNER_SUFFIX"
    private val SCANNER_VOLUME: String = "SCANNER_VOLUME"
    private val SCANNER_TONE: String = "SCANNER_TONE"
    private val SCANNER_PLAYTONE_MODE: String = "SCANNER_PLAYTONE_MODE"

    override fun getLayout(): Int {
        return R.layout.fragment_scanner
    }


    private var mPowerOnOff: Int = 1
    private var mVolume: Int = 0
    private var mTone: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPowerOnOff = Settings.System.getInt(context!!.contentResolver, SCANNER_POWER_ON, 1)
        mVolume = Settings.System.getInt(context!!.contentResolver, SCANNER_VOLUME, 0)
        mTone = Settings.System.getInt(context!!.contentResolver, SCANNER_TONE, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPowerOnOff.isChecked = mPowerOnOff == 1
        viewVolume.text = mVolume.toString()
        viewActionOpen.setOnClickListener {
            //设置底层接口
            val intent1 = Intent("com.android.server.scannerservice.onoff")
            intent1.putExtra("scanneronoff", 1)
            context!!.sendBroadcast(intent1)
            //改变上层图标
            val intent2 = Intent("android.intent.action.ACTION_SCANNER_ENABLE")
            intent2.putExtra("state", 1)
            context!!.sendBroadcast(intent2)
        }
        viewActionClose.setOnClickListener{
            //设置底层接口
            val intent1 = Intent("com.android.server.scannerservice.onoff")
            intent1.putExtra("scanneronoff", 0)
            context!!.sendBroadcast(intent1)
            //改变上层图标
            val intent2 = Intent("android.intent.action.ACTION_SCANNER_ENABLE")
            intent2.putExtra("state", 0)
            context!!.sendBroadcast(intent2)
        }
        viewActionScan.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startScan()
                MotionEvent.ACTION_UP -> endScan()
                else -> {
                    false
                }
            }
        }


    }

    private fun endScan(): Boolean {
        viewActionScan.text = "松开结束"
        val intent = Intent("android.intent.action.SCANNER_BUTTON_DOWN", null)
        context!!.sendOrderedBroadcast(intent, null)
        return true
    }

    private fun startScan(): Boolean {
        viewActionScan.text = "按下开始"
        val intent = Intent("android.intent.action.SCANNER_BUTTON_UP", null)
        context!!.sendOrderedBroadcast(intent, null)
        return true
    }

    private val reveiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action.equals("com.android.server.scannerservice.broadcast")) {

            }
        }

    }
}
