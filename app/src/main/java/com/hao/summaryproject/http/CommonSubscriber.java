package com.hao.summaryproject.http;

import android.util.Log;
import android.widget.Toast;

import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.base.BaseView;

import org.json.JSONException;

import java.io.EOFException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by liuzhu
 * on 2017/8/23.
 */

public abstract class CommonSubscriber<T> extends ResourceObserver<T> {

    private BaseView mView;

    public CommonSubscriber(BaseView view) {
        this.mView = view;
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) return;

        if (e instanceof EOFException || e instanceof ConnectException || e instanceof SocketException
                || e instanceof SocketTimeoutException || e instanceof UnknownHostException){
            mView.onError("网络异常，请稍后重试");
        } else if (e instanceof JSONException){
            Log.d(Constants.APP_LOG, e.toString());
        } else {
            mView.onError("未知错误，请稍后重试");
        }
        Log.d(Constants.APP_LOG, e.toString());

    }
}
