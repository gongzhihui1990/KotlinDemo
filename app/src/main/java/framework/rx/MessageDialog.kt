/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.rx

import android.content.Context
import android.view.View

/**
 * Created by heisenberg on 2017/10/20.
 * heisenberg.gong@koolpos.com
 */

interface MessageDialog {


    fun isShowing(): Boolean

    fun setCancelable(mCancelable: Boolean)

    fun setDialogMessage(mMessage: CharSequence)

    fun setDialogTitle(mTitle: CharSequence)

    fun setConfirmButtonText(mConfirm: CharSequence)

    fun setConfirmButtonOnClickListener(onClickListener: View.OnClickListener)

    fun setCancelButtonText(mCancel: CharSequence)

    fun show()

    fun dismiss()
    class Builder(context: Context) {
        fun title(s: String): Builder {
            return this
        }

        fun content(message: CharSequence): Builder {
            return this;
        }

        fun show(): MessageDialog {
            return object :MessageDialog{
                override fun isShowing(): Boolean{
                    return false
                }

                override fun setCancelable(mCancelable: Boolean) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun setDialogMessage(mMessage: CharSequence) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun setDialogTitle(mTitle: CharSequence) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun setConfirmButtonText(mConfirm: CharSequence) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun setConfirmButtonOnClickListener(onClickListener: View.OnClickListener) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun setCancelButtonText(mCancel: CharSequence) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun show() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun dismiss() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
        }

        fun onPositive(any: View.OnClickListener): Builder {
            return this
        }

        fun positiveText(string: String?): Builder {
            return this
        }

    }

}
