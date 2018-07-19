/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import framework.rx.ApplicationObserverResourceHolder
import framework.rx.ObserverResourceManager
import framework.util.Loger
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription

/**
 * @author caroline
 */
abstract class RxAppCompatActivity : AppCompatActivity(), ApplicationObserverResourceHolder {

    /**
     * observer 观察者管理
     */
    internal var observerResourceManager = ObserverResourceManager()

    override fun getContext(): Context? {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Loger.w("-" + this)
    }

    override fun onPostResume() {
        super.onPostResume()
        Loger.w("-" + this)
    }

    override fun onRestart() {
        super.onRestart()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onDestroy() {
        clearWorkOnDestroy()
        super.onDestroy()
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Loger.w("-" + this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Loger.w("-" + this)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle, persistentState: PersistableBundle) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Loger.w("-" + this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Loger.w("-" + this)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Loger.w("-" + this)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Loger.w("-" + this)
    }

    /**
     * 清除FragmentManager内的Fragment层级到指定个数
     *
     * @param popLevel popLevel
     */

    protected fun popFragmentToLevel(popLevel: Int) {
        val manager = supportFragmentManager
        while (manager.backStackEntryCount >= popLevel) {
            manager.popBackStackImmediate()
        }
    }

    override fun clearWorkOnDestroy() {
        observerResourceManager.clearWorkOnDestroy()
    }

    override fun addDisposable(disposable: Disposable?) {
        observerResourceManager.addDisposable(disposable)
    }

    override fun addSubscription(subscription: Subscription?) {
        observerResourceManager.addSubscription(subscription)
    }

    override fun removeDisposable(disposable: Disposable?) {
        observerResourceManager.removeDisposable(disposable)
    }

    override fun removeSubscription(subscription: Subscription?) {
        observerResourceManager.removeSubscription(subscription)
    }


}