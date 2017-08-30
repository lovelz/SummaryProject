package com.hao.summaryproject.http.api;

import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.bean.base.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public interface APIService {

    //得到首页banner
    @GET("index.php?m=api&c=Shop&a=getBanner")
    Observable<BaseBean<List<HomePagerData>>> getHomePager();

    //得到首页推荐风格信息
    @GET("index.php?m=api&c=Shop&a=getHomeGoodsCategory")
    Observable<BaseBean<List<HomeCategory>>> getHomeCategory();

    //得到首页top 10
    @GET("index.php?m=api&c=Shop&a=topten")
    Observable<BaseBean<List<ThemeInfo>>> getHomeTop();

}
