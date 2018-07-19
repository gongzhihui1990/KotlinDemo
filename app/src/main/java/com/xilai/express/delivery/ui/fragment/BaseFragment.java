package com.xilai.express.delivery.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilai.express.delivery.R;
import com.xilai.express.delivery.ui.UI;

import framework.fragment.RxBaseFragment;


/**
 * @author caroline
 * @date 2018/2/8
 */
public abstract class BaseFragment extends RxBaseFragment implements UI {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayout();
        return inflater.inflate(layoutId, container, false);
    }


    @Override
    public int getLayout() {
        return R.layout.layout_disable;
    }


    /**
     * 数据保存统一动作
     *
     * @param outState
     */
    public void saveInstanceState(Bundle outState) {

    }

    /**
     * 数据恢复统一动作
     *
     * @param savedInstanceState
     */
    public void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            saveInstanceState(outState);
        }
    }


}
