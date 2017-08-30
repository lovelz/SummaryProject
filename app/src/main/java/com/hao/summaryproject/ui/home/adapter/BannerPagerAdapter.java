package com.hao.summaryproject.ui.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.bean.HomePagerData;

import java.util.List;

/**
 * Created by liuzhu
 * on 2017/8/22.
 */

public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<HomePagerData> mPagerDataList;

    public BannerPagerAdapter(Context context, List<HomePagerData> pagerDataList) {
        this.mContext = context;
        this.mPagerDataList = pagerDataList;
    }

    @Override
    public int getCount() {
        return mPagerDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mContext).load(Constants.BASE_IMG_URL + mPagerDataList.get(position).getThumb()).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
