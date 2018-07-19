package framework.fragment


import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import framework.activity.RxAppCompatActivity
import framework.rx.DialogObserverHolder
import framework.rx.ObserverResourceManager
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription

/**
 * @author caroline
 * @date 2018/4/24
 */

abstract class RxBaseDialogFragment : DialogFragment(), DialogObserverHolder {
    protected lateinit var baseActivity: RxAppCompatActivity
    /**
     * use to manage resource
     */
    internal var observerResourceManager = ObserverResourceManager()

    override val supportFragmentManager: FragmentManager
        get() = super.getFragmentManager()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun findViewById(@IdRes id: Int): View? {
        return if (view == null) {
            null
        } else view!!.findViewById(id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.baseActivity = (activity as RxAppCompatActivity?)!!
    }


    fun addFragment(layout: Int, fragment: Fragment) {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(layout, fragment).commitAllowingStateLoss()
    }

    fun addFragment(layout: Int, fragment: Fragment, isSave: Boolean) {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(layout, fragment)
        if (isSave) {
            transaction.addToBackStack(null)
        }
        transaction.commitAllowingStateLoss()
    }

    fun addSupportFragment(layout: Int, fragment: Fragment) {
        val manager = baseActivity.supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(layout, fragment).commitAllowingStateLoss()
    }

    override fun onDestroy() {
        clearWorkOnDestroy()
        super.onDestroy()
    }

    /**
     * [ObserverResourceManager.clearWorkOnDestroy]
     */
    override fun clearWorkOnDestroy() {
        observerResourceManager.clearWorkOnDestroy()
    }

    /**
     * [ObserverResourceManager.addDisposable]
     */
    override fun addDisposable(disposable: Disposable?) {
        observerResourceManager.addDisposable(disposable)
    }

    /**
     * [ObserverResourceManager.addSubscription]
     */
    override fun addSubscription(subscription: Subscription?) {
        observerResourceManager.addSubscription(subscription)
    }

    /**
     * [ObserverResourceManager.removeDisposable]
     */
    override fun removeDisposable(disposable: Disposable?) {
        observerResourceManager.removeDisposable(disposable)
    }

    /**
     * [ObserverResourceManager.removeSubscription]
     */
    override fun removeSubscription(subscription: Subscription?) {
        observerResourceManager.removeSubscription(subscription)
    }

}
