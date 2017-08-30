package com.hao.summaryproject.http;

import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.bean.base.BaseBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class DataManager implements HttpHelper {

    HttpHelper httpHelper;

    public DataManager(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Override
    public Observable<BaseBean<List<HomePagerData>>> fetchHomePager() {
        return httpHelper.fetchHomePager();
    }

    @Override
    public Observable<BaseBean<List<HomeCategory>>> fetchHomeCategory() {
        return httpHelper.fetchHomeCategory();
    }

    @Override
    public Observable<BaseBean<List<ThemeInfo>>> fetchHomeTop() {
        return httpHelper.fetchHomeTop();
    }
}
