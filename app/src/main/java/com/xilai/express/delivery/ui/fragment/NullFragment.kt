package com.xilai.express.delivery.ui.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.layout_disable.*

/**
 * Created by caroline on 2018/7/19.
 */

class NullFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hint.text = arguments?.getString(String::class.java.name)
    }
}
