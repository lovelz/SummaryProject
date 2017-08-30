package com.hao.summaryproject.base;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
