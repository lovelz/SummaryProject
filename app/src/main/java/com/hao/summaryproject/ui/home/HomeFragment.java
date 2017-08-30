package com.hao.summaryproject.ui.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hao.summaryproject.R;
import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.base.BaseMvpFragment;
import com.hao.summaryproject.bean.HomeCategory;
import com.hao.summaryproject.bean.HomePagerData;
import com.hao.summaryproject.bean.ThemeInfo;
import com.hao.summaryproject.ui.home.adapter.CategoryAdapter;
import com.hao.summaryproject.ui.home.adapter.HomeAdapter;
import com.hao.summaryproject.view.Banner;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Created by liuzhu
 * on 2017/8/22.
 */

public class HomeFragment extends BaseMvpFragment<HomePresenter> implements HomeContact.View {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.home_easy_recycler)
    EasyRecyclerView mHomeRecycler;

    private Banner vpHome;

    private CategoryAdapter categoryAdapter;
    private HomeAdapter homeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        mHomeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeAdapter = new HomeAdapter(getActivity());
        mHomeRecycler.setAdapter(homeAdapter);

        homeAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View homeHeader = LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_home_header, parent, false);

                vpHome = (Banner) homeHeader.findViewById(R.id.vp_home);
                RecyclerView categoryRecycler = (RecyclerView) homeHeader.findViewById(R.id.home_category_recycler);

                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
                categoryRecycler.setLayoutManager(layoutManager);
                categoryAdapter = new CategoryAdapter(getActivity());
                categoryRecycler.setAdapter(categoryAdapter);

                return homeHeader;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        //刷新
        mHomeRecycler.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHomePager();
                mPresenter.getHomeCategory();
                mPresenter.getHomeTop();
            }
        });

        //请求数据
        mPresenter.getHomeTop();
        mPresenter.getHomePager();
        mPresenter.getHomeCategory();

    }

    @Override
    public void showHomePager(final List<HomePagerData> pagerDataList) {
        Log.d(TAG, pagerDataList.size() + "");
        List<String> mImgList = new ArrayList<>();
        for (HomePagerData img : pagerDataList) {
            mImgList.add(Constants.BASE_IMG_URL + img.getThumb());
        }
        vpHome.setImgData(mImgList);
        vpHome.start();
    }

    @Override
    public void showHomeCategory(final List<HomeCategory> categoryList) {
        categoryAdapter.clear();
        categoryAdapter.addAll(categoryList);
    }

    @Override
    public void showHomeTop(List<ThemeInfo> themeInfoList) {
        homeAdapter.clear();
        homeAdapter.addAll(themeInfoList);
    }

}
