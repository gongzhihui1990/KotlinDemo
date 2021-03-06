/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.util

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log

import com.tencent.bugly.crashreport.BuglyLog

/**
 * 封装默认的Log类
 * @author caroline
 */
object Loger {

    private val DO_PRINT_STREAM = false

    private val IS_DEBUG = true
    private val IS_BuglyLog = false

    @JvmOverloads
    fun v(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            if (IS_BuglyLog) {
                BuglyLog.v(tag, msg)
            } else {
                Log.v(tag, msg)
            }
        }
    }

    @JvmOverloads
    fun i(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            if (IS_BuglyLog) {
                BuglyLog.i(tag, msg)
            } else {
                Log.i(tag, msg)
            }
        }
    }

    @JvmOverloads
    fun d(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            if (IS_BuglyLog) {
                BuglyLog.d(tag, msg)
            } else {
                Log.d(tag, msg)
            }
        }
    }

    @JvmOverloads
    fun w(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            if (IS_BuglyLog) {
                BuglyLog.w(tag, msg)
            } else {
                Log.w(tag, msg)
            }
        }
    }

    @JvmOverloads
    fun e(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            if (IS_BuglyLog) {
                BuglyLog.e(tag, msg)
            } else {
                Log.e(tag, msg)
            }
        }
    }

    @JvmOverloads
    fun a(msg: String, tag: String = "") {
        var msg = msg
        var tag = tag
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag()
                msg = generateMsg(msg)
            }
            Log.println(Log.ASSERT, tag, msg)
        }
    }

    private fun generateTag(): String {
        val stack = Throwable().stackTrace
        var i = 0
        for (stackItem in stack) {

            if (stackItem.className == Loger::class.java.name) {
                //当前类堆栈不打印
                i = i + 1
                continue
            }
            break
        }
        val ste = stack[i]
        return ste.className
    }

    @SuppressLint("DefaultLocale")
    private fun generateMsg(msg: String): String {
        var msg = msg
        val stack = Throwable().stackTrace
        var i = 0
        for (stackItem in stack) {
            if (stackItem.className == Loger::class.java.name) {
                //当前类堆栈不打印
                i = i + 1
                continue
            }
            if (DO_PRINT_STREAM) {
                msg += "\n\tat " + stackItem
            }
            break
        }
        val ste = stack[i]
        return String.format("[%s][%d]%s", ste.methodName,
                ste.lineNumber, msg)
    }

}
