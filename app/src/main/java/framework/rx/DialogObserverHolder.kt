package framework.rx

import android.support.v4.app.FragmentManager

/**
 * Created by caroline on 2018/4/24.
 */

interface DialogObserverHolder : ApplicationObserverResourceHolder {
    val supportFragmentManager: FragmentManager

    fun showDialog(actions4SimpleDlg: Actions4SimpleDlg)
}
