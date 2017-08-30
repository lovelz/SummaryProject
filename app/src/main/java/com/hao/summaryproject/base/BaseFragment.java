package com.hao.summaryproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hao.summaryproject.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhu
 * on 2017/8/19.
 */

public abstract class BaseFragment extends Fragment {

    protected View mView;
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEvent();
    }

    /**
     * 显示toast
     * @param msg
     */
    protected void showMessage(String msg) {
        ToastUtil.showMessage(getActivity(), msg);
    }

    /**
     * 显示自定义持续时间的toast
     * @param msg
     * @param duration
     */
    protected void showMessage(String msg, int duration) {
        ToastUtil.showLongMessage(getActivity(), msg, duration);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    public void initEvent() {

    }

    protected abstract int getLayoutId();
}
