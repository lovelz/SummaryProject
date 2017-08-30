package com.hao.summaryproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hao.summaryproject.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhu
 * on 2017/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private Unbinder mUnBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        //隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mUnBinder = ButterKnife.bind(this);

        initView();

        initEvent();
    }

    /**
     * 显示toast
     * @param msg
     */
    protected void showMessage(String msg) {
        ToastUtil.showMessage(this, msg);
    }

    /**
     * 显示自定义持续时间的toast
     * @param msg
     * @param duration
     */
    protected void showMessage(String msg, int duration) {
        ToastUtil.showLongMessage(this, msg, duration);
    }

    protected void initView() {

    }

    protected void initEvent() {

    }

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }
}
