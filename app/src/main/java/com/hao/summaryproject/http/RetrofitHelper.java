package com.hao.summaryproject.http;

import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.bean.base.BaseBean;
import com.hao.summaryproject.http.api.APIService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class RetrofitHelper implements HttpHelper {

    private APIService apiService;

    @Inject
    public RetrofitHelper(APIService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<BaseBean<List<HomePagerData>>> fetchHomePager() {
        return apiService.getHomePager();
    }

    @Override
    public Observable<BaseBean<List<HomeCategory>>> fetchHomeCategory() {
        return apiService.getHomeCategory();
    }

    @Override
    public Observable<BaseBean<List<ThemeInfo>>> fetchHomeTop() {
        return apiService.getHomeTop();
    }
}
