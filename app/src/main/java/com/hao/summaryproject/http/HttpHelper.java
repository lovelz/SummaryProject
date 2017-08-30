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

public interface HttpHelper {

    Observable<BaseBean<List<HomePagerData>>> fetchHomePager();

    Observable<BaseBean<List<HomeCategory>>> fetchHomeCategory();

    Observable<BaseBean<List<ThemeInfo>>> fetchHomeTop();

}
