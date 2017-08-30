package com.hao.summaryproject.ui.home;

import android.util.Log;

import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.base.RxPresenter;
import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.bean.base.BaseBean;
import com.hao.summaryproject.http.CommonSubscriber;
import com.hao.summaryproject.http.DataManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class HomePresenter extends RxPresenter<HomeContact.View> implements HomeContact.Presenter {

    private DataManager mDataManager;

    @Inject
    public HomePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void getHomePager() {
        addSubscribe(mDataManager.fetchHomePager()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseBean<List<HomePagerData>>, List<HomePagerData>>() {
                    @Override
                    public List<HomePagerData> apply(@NonNull BaseBean<List<HomePagerData>> listBaseBean) throws Exception {
                        return listBaseBean.getData();
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomePagerData>>(mView) {
                    @Override
                    public void onNext(List<HomePagerData> pagerDataList) {
                        mView.showHomePager(pagerDataList);
                        Log.d(Constants.APP_LOG, pagerDataList.size() + "");

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.d(Constants.APP_LOG, e.toString());

                    }
                }));
    }

    @Override
    public void getHomeCategory() {
        addSubscribe(mDataManager.fetchHomeCategory()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Function<BaseBean<List<HomeCategory>>, List<HomeCategory>>() {
            @Override
            public List<HomeCategory> apply(@NonNull BaseBean<List<HomeCategory>> listBaseBean) throws Exception {
                return listBaseBean.getData();
            }
        })
        .subscribe(new Consumer<List<HomeCategory>>() {
            @Override
            public void accept(@NonNull List<HomeCategory> categoryList) throws Exception {
                mView.showHomeCategory(categoryList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }

    @Override
    public void getHomeTop() {
        addSubscribe(mDataManager.fetchHomeTop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .map(new Function<BaseBean<List<ThemeInfo>>, List<ThemeInfo>>() {
            @Override
            public List<ThemeInfo> apply(@NonNull BaseBean<List<ThemeInfo>> listBaseBean) throws Exception {
                return listBaseBean.getData();
            }
        })
        .subscribe(new Consumer<List<ThemeInfo>>() {
            @Override
            public void accept(@NonNull List<ThemeInfo> themeInfoList) throws Exception {
                mView.showHomeTop(themeInfoList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }
}
