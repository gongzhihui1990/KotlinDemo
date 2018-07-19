/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.rx

import android.content.Context

/**
 * @author heisenberg
 * @date 2017/7/21
 * heisenberg.gong@koolpos.com
 */

interface ApplicationObserverResourceHolder : ObserverResourceHolder {
    fun getContext(): Context?
}
