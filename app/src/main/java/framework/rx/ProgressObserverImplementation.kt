/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.rx


import android.content.Context
import android.content.Intent
import android.os.Looper
import android.provider.Settings
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.xilai.express.delivery.R
import com.xilai.express.delivery.ui.LoginActivity
import framework.app.BaseApp
import framework.exception.IServerAuthException
import framework.exception.IServerException
import framework.exception.IgnoreShow
import framework.util.Loger
import io.reactivex.annotations.NonNull
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection.*
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * AbstractProgressObserver 的基础实现
 *
 * @author heisenberg
 * @date 2017/4/18
 */
open class ProgressObserverImplementation<T> : AbstractProgressResourceSubscriber<T> {
    private var msgDialog: MessageDialog? = null
    private var msgDialogBuilder: MessageDialog.Builder? = null
    private var loadingDialog: LoadingDialog? = null
    private var mCancelable: Boolean = false
    private var mShow = true
    private var pMessage: CharSequence? = null
    var context: Context? = null
        private set

    /**
     * @param holder ApplicationObserverResourceHolder
     */
    constructor(@NonNull holder: ApplicationObserverResourceHolder?) {
        if (holder != null) {
            setObserverHolder(holder)
            context = holder.getContext()
            if (Looper.myLooper() == Looper.getMainLooper()) {
                // UI主线程
                val view = View.inflate(holder.getContext(), R.layout.dialog_waiting, null)
                loadingDialog = CustomDialog(holder.getContext(), view, R.style.dialog)
                loadingDialog!!.setTitle("提示")
                pMessage = "加载中..."
                loadingDialog!!.setCancelable(mCancelable)
            }
        }
    }


    @Deprecated("无applicationObserverHolder 不建议")
    constructor() {
        if (defaultHolder == null) {
            defaultHolder = DefaultObserverResourceHolder()
        }
        setObserverHolder(defaultHolder!!)
    }

    override fun onError(t: Throwable) {
        super.onError(t)
        showError(t)
    }

    private fun showError(t: Throwable) {
        if (t is IgnoreShow) {
            return
        }
        t.printStackTrace()
        if (context == null) {
            return
        }
        setDialogConfirmBtn(context!!.getText(android.R.string.ok), null)
        if (t is IServerAuthException) {
            if (checkDialog()) {
                msgDialog = msgDialogBuilder!!.title(context!!.getString(R.string.hint))
                        .content(t.message!!).positiveText(context!!.getString(R.string.sure))
                        .onPositive(View.OnClickListener {
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context!!.startActivity(intent)
                        }).show()
            }
        } else {
            val parsedError = parseException(t)
            if (t is ConnectException) {
                val message = "无法连上服务器,请检查您的网络和设置"
                if (checkDialog()) {
                    msgDialog = msgDialogBuilder!!.title("提示").content(message).onPositive(View.OnClickListener {
                        try {
                            context!!.startActivity(Intent(Settings.ACTION_SETTINGS))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }).positiveText(context!!.getString(R.string.system_set)).show()
                }
            } else {
                showDialogByMessage(parsedError)
            }
            onParsedError(parsedError)
        }
    }

    fun onParsedError(msg: String) {
        Loger.e(msg)
    }

    protected fun parseException(e: Throwable): String {
        var errMsg: String
        if (e is HttpException) {
            var httpErrorMsg = (e as HttpException).message()
            val httpCode = (e as HttpException).code()
            Loger.d("onError:" + httpErrorMsg)
            when (httpCode) {
                HTTP_BAD_REQUEST -> httpErrorMsg = "400-(错误请求）"
                HTTP_UNAUTHORIZED -> httpErrorMsg = "401-(未授权）"
                HTTP_FORBIDDEN -> httpErrorMsg = "403-(服务器拒绝访问）"
                HTTP_NOT_FOUND -> httpErrorMsg = "404-(服务器未找到响应）"
                HTTP_INTERNAL_ERROR -> httpErrorMsg = "500-(服务器内部错误）"
                HTTP_BAD_GATEWAY -> httpErrorMsg = "502-(错误网关）"
                HTTP_UNAVAILABLE -> httpErrorMsg = "503-(服务不可用）"
                HTTP_GATEWAY_TIMEOUT -> httpErrorMsg = "504-(网关连接超时）"
                else -> httpErrorMsg = httpCode.toString() + "-(" + httpErrorMsg + "）"
            }
            errMsg = "数据加载失败(≧Д≦)ノ，" + httpErrorMsg
        } else if (e is UnknownHostException) {
            errMsg = "服务器DNS解析失败,请检查您的网络"
        } else if (e is ConnectException) {
            errMsg = "无法连上服务器,请检查您的网络"
        } else if (e is SocketTimeoutException) {
            errMsg = "网络连接超时,请检查您的网络"
        } else if (e is SocketException) {
            errMsg = "网络连接超时,请检查您的网络"
        } else if (e is IServerException) {
            errMsg = e.message!!
        } else {
            errMsg = e.message!!
        }
        if (TextUtils.isEmpty(errMsg)) {
            errMsg = "未知错误" + e.toString()
        }
        return errMsg
    }


    /**
     * Sets whether this dialog is cancelable with the
     * [BACK][KeyEvent.KEYCODE_BACK] key.
     */
    fun setCancelable(flag: Boolean): ProgressObserverImplementation<T> {
        mCancelable = flag
        return this
    }

    fun setMessage(message: CharSequence): ProgressObserverImplementation<T> {
        pMessage = message
        return this
    }

    fun setMessageWithSymbol(message: CharSequence): ProgressObserverImplementation<T> {
        pMessage = message.toString() + context!!.getString(R.string.progress_symbol)
        return this
    }

    fun setMessageWithSymbol(@StringRes message: Int): ProgressObserverImplementation<T> {
        pMessage = context!!.getString(message) + context!!.getString(R.string.progress_symbol)
        return this
    }

    fun setMessage(@StringRes messageID: Int): ProgressObserverImplementation<T> {
        pMessage = BaseApp.context!!.getString(messageID)
        return this
    }

    fun setShow(show: Boolean): ProgressObserverImplementation<T> {
        mShow = show
        return this
    }

    private fun checkDialog(): Boolean {
        if (context == null) {
            return false
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return false
        }
        if (msgDialog != null && msgDialog!!.isShowing()) {
            try {
                msgDialog!!.dismiss()
            } catch (e: Exception) {
                //底层比较渣
                Loger.e("底层比较渣" + e.javaClass)
            }

        } else {
            msgDialogBuilder = MessageDialog.Builder(context!!)
        }
        return true

    }

    fun setDialogConfirmBtn(btnText: CharSequence, onClickListener: View.OnClickListener?) {
        if (checkDialog()) {
            msgDialogBuilder!!.positiveText(btnText.toString()).onPositive(View.OnClickListener {
                onClickListener?.onClick(null)
            })
        }
    }

    fun showDialogByMessage(message: CharSequence) {
        if (checkDialog()) {
            msgDialog = msgDialogBuilder!!.title("提示").content(message).show()
        }
    }

    override fun showProgress() {
        if (loadingDialog != null && mShow) {
            loadingDialog!!.setCancelable(mCancelable)
            loadingDialog!!.setMessage(pMessage!!)
            loadingDialog!!.show()
            return
        }
        if (!mShow) {
            dismissProgress()
        }
    }

    override fun dismissProgress() {
        if (loadingDialog != null) {
            try {
                loadingDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        private var defaultHolder: ObserverResourceHolder? = null
    }
}
