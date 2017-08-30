package com.hao.summaryproject.ui.home;

import com.hao.summaryproject.base.BasePresenter;
import com.hao.summaryproject.base.BaseView;
import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;

import java.util.List;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public interface HomeContact {

    interface View extends BaseView {
        //显示首页轮播页的数据
        void showHomePager(List<HomePagerData> pagerDataList);
        //显示首页推荐风格
        void showHomeCategory(List<HomeCategory> categoryList);
        //显示top 10
        void showHomeTop(List<ThemeInfo> themeInfoList);
    }

    interface Presenter extends BasePresenter<View> {
        //获取首页轮播页的数据
        void getHomePager();
        //获取首页推荐风格
        void getHomeCategory();
        //获取top 10
        void getHomeTop();
    }
}
