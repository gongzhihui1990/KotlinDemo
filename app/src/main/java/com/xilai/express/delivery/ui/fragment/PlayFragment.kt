package com.xilai.express.delivery.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.xilai.express.delivery.R
import framework.rx.ProgressObserverImplementation
import framework.rx.RxHelper
import framework.util.Loger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_play.*

/**
 * Created by caroline on 2018/7/19.
 */

class PlayFragment : BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_play
    }

    private var run = false
    private var progress: Int = 0
    private val progressRun = Runnable {
        while (true) {
            Thread.sleep(20)
            if (run) {
                progress++
                if (progress > 100) {
                    run = false
                }
                RxHelper.bindOnUI(Observable.just(progress), object : ProgressObserverImplementation<Int>(this@PlayFragment) {
                    override fun onNext(t: Int) {
                        super.onNext(t)
                        if (run) {
                            playProgress.progress = t
                            if (progress >= 100) {
                                Toast.makeText(baseActivity, "加载完成", Toast.LENGTH_LONG).show()
                                run = false
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val position = arrayOfNulls<Int>(2)
        val position = intArrayOf(0, 0)

        play.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->

            v.getLocationInWindow(position)
            Loger.w("x,y :" + position[0] + " " + position[1])
            v.getLocationOnScreen(position)
            Loger.e("x,y :" + position[0] + " " + position[1])

        }
        play.setOnTouchListener { v, event ->
            Loger.e("x,y :" + event.x + " " + event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context!!.sendBroadcast(Intent("com.test.onStartJob"))
                    progress = 0
                    playProgress.progress = 0
                    run = true
                    play.disallowInterceptTouchEvent()
                }
                MotionEvent.ACTION_UP -> {
                    progress = 0
                    playProgress.progress = 0
                    run = false
                    play.allowInterceptTouchEvent()
                }
                MotionEvent.ACTION_MOVE -> {
                    Loger.i("x,y :" + play.width + " " + play.height)
                    if (event.x > play.width || event.x < 0 || event.y > play.height || event.y < 0) {
//                    if (Math.abs(event.x - position[0]) > play.width / 2 || Math.abs(event.y - position[1]) > play.height / 2) {
                        progress = 0
                        playProgress.progress = 0
                        run = false
                        play.allowInterceptTouchEvent()
                    }
                }
            }
            true
        }
        Thread(progressRun).start()
        super.onViewCreated(view, savedInstanceState)
    }

}
