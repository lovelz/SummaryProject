package com.hao.summaryproject.base;

import com.hao.summaryproject.app.SummaryApp;
import com.hao.summaryproject.di.component.ActivityComponent;
import com.hao.summaryproject.di.component.DaggerActivityComponent;
import com.hao.summaryproject.di.module.ActivityModule;

import javax.inject.Inject;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityCompat() {
        return DaggerActivityComponent.builder()
                .appComponent(SummaryApp.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    protected void initView() {
        super.initView();
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
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
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }
}
