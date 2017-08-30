package com.hao.summaryproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hao.summaryproject.app.SummaryApp;
import com.hao.summaryproject.di.component.DaggerFragmentComponent;
import com.hao.summaryproject.di.component.FragmentComponent;
import com.hao.summaryproject.di.module.FragmentModule;

import javax.inject.Inject;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements BaseView {

    @Inject
    protected T mPresenter;

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(SummaryApp.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        if (mPresenter != null) mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void initInject();

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String msg) {
        showMessage(msg);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }
}
