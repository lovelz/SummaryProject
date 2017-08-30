package com.hao.summaryproject.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    private CompositeDisposable compositeDisposable;

    /**
     * 订阅
     * @param disposable
     */
    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 取消订阅
     */
    protected void unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
