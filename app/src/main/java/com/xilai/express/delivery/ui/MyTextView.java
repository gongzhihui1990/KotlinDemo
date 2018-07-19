package com.xilai.express.delivery.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by caroline on 2018/7/19.
 */

@SuppressLint("AppCompatCustomView")
public class MyTextView extends Button {
    private boolean disAllowInterceptTouchEvent = false;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void disallowInterceptTouchEvent() {
        disAllowInterceptTouchEvent = true;
    }

    public void allowInterceptTouchEvent() {
        disAllowInterceptTouchEvent = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(disAllowInterceptTouchEvent);
        return super.dispatchTouchEvent(event);
    }
}
