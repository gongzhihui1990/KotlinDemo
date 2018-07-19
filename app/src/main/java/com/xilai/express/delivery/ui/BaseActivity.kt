package com.xilai.express.delivery.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import framework.activity.RxAppCompatActivity

/**
 *
 * @author caroline
 * @date 2018/7/18
 */

abstract class BaseActivity : RxAppCompatActivity(), UI {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    @LayoutRes override fun getLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}