package com.xilai.express.delivery.ui.fragment

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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
            Thread.sleep(100)
            if (run) {
                progress++
                if (progress > 100) {
                    run = false
                }
                RxHelper.bindOnUI(Observable.just(progress), object : ProgressObserverImplementation<Int>() {
                    override fun onNext(t: Int) {
                        super.onNext(t)
                        playProgress.progress = t
                        if (progress > 100) {
                            run = false
                        }
                    }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        play.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    playProgress.progress = 0
                    run = true
                }
                MotionEvent.ACTION_UP -> {
                    playProgress.progress = 0
                    run = false
                }
                else -> Loger.i("action:" + event.action)
            }
            true
        }
        Thread(progressRun).start()
        super.onViewCreated(view, savedInstanceState)
    }
}
