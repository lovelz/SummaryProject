package com.hao.summaryproject.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hao.summaryproject.R;
import com.hao.summaryproject.ui.home.adapter.BannerPagerAdapter;
import com.hao.summaryproject.util.WeakHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhu
 * on 2017/8/24.
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener{

    private static final String TAG = Banner.class.getSimpleName();

    private Context mContext;
    //banner的viewpager
    private ViewPager bannerPager;
    //小圆点的容器
    private LinearLayout bannerPoint;

    private BannerScroller mScroller;

    //存储小圆点集合
    private List<ImageView> mPointList;

    //设置的图片
    private List<String> mImgList;

    //轮播页显示的view集合
    private List<ImageView> mBannerViews;

    //记录图片数量
    private int mCount;

    private int mCurrentItem;

    //记录滑动后的位置
    private int lastPosition = 1;

    //轮播间隔时间
    private static final int DEFAULT_DELAY_TIME = 3000;

    private BannerPagerAdapter bannerAdapter;

    private WeakHandler mHandle = new WeakHandler();

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPointList = new ArrayList<>();
        mBannerViews = new ArrayList<>();

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_banner, this, true);
        bannerPager = (ViewPager) view.findViewById(R.id.banner_pager);
        bannerPoint = (LinearLayout) view.findViewById(R.id.banner_point);

        initScroller();
    }

    /**
     * 通过反射设置viewpager滑动的时间
     */
    private void initScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(bannerPager.getContext());
            mField.set(bannerPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置banner数据
     * @param imgList
     */
    public void setImgData(List<String> imgList) {
        this.mImgList = imgList;
        this.mCount = imgList.size();
    }

    /**
     * 开启banner
     */
    public void start() {
        mBannerViews.clear();
        setBannerList(mImgList);
    }

    /**
     * 设置banner中的数据
     * @param imgList
     */
    private void setBannerList(List<String> imgList) {
        if (imgList == null || imgList.size() == 0) {
            Log.e(TAG, "you should set no empty data");
            return;
        }

        for (int i = 0; i <= mCount + 1; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String imgUrl;
            if (i == 0) {
                imgUrl = imgList.get(mCount - 1);
            } else if (i == mCount + 1) {
                imgUrl = imgList.get(0);
            } else {
                imgUrl = imgList.get(i -1);
            }
            mBannerViews.add(imageView);
            Glide.with(mContext).load(imgUrl).into(imageView);

        }

        //当轮播图数量大于1时开启指示器
        if (mCount > 1) {
            createIndicator(mCount);
        }

        setData();

    }

    private void setData() {
        mCurrentItem = 1;
        if (bannerAdapter == null) {
            bannerAdapter = new BannerPagerAdapter();
            bannerPager.addOnPageChangeListener(this);
        }

        bannerPager.setAdapter(bannerAdapter);
        bannerPager.setCurrentItem(mCurrentItem);

        startAutoPlay();
    }

    /**
     * 开启轮播
     */
    public void startAutoPlay() {
        mHandle.removeCallbacks(autoTask);
        mHandle.postDelayed(autoTask, DEFAULT_DELAY_TIME);
    }

    /**
     * 关闭轮播
     */
    public void stopAutoPlay() {
        mHandle.removeCallbacks(autoTask);
    }

    private final Runnable autoTask = new Runnable() {
        @Override
        public void run() {
            if (mCount > 1) {
                mCurrentItem = mCurrentItem % (mCount + 1) + 1;
//                Log.i(TAG, "mCurrentItem-->  " + mCurrentItem);
                if (mCurrentItem == 1) {
                    bannerPager.setCurrentItem(mCurrentItem, false);
                    mHandle.post(autoTask);
                } else {
                    bannerPager.setCurrentItem(mCurrentItem);
                    mHandle.postDelayed(autoTask, DEFAULT_DELAY_TIME);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //手指按下时停止轮播
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            startAutoPlay();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stopAutoPlay();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 创建底部圆点指示器
     * @param count 圆点数量
     */
    private void createIndicator(int count) {
        bannerPoint.removeAllViews();
        mPointList.clear();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(18, 18);
            if (i != 0) {
                lp.leftMargin = 30;
                imageView.setImageResource(R.drawable.shape_banner_unselect_point);
            } else {
                imageView.setImageResource(R.drawable.shape_banner_select_point);
            }
            imageView.setLayoutParams(lp);
            mPointList.add(imageView);
            bannerPoint.addView(imageView);
        }
    }

    /**
     * banner viewpager的适配器
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mBannerViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mBannerViews.get(position);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, ((lastPosition - 1 + mCount) % mCount) + "    -------------    " + (position - 1 + mCount) % mCount);
        mPointList.get((lastPosition - 1 + mCount) % mCount).setImageResource(R.drawable.shape_banner_unselect_point);
        mPointList.get((position - 1 + mCount) % mCount).setImageResource(R.drawable.shape_banner_select_point);
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mCurrentItem = bannerPager.getCurrentItem();
        switch (state) {
            case 0:
                if (mCurrentItem == 0) {
                    bannerPager.setCurrentItem(mCount, false);
                } else if (mCurrentItem == mCount + 1) {
                    bannerPager.setCurrentItem(1, false);
                }
                break;
            case 1:
                if (mCurrentItem == 0) {
                    bannerPager.setCurrentItem(mCount, false);
                } else if (mCurrentItem == mCount + 1) {
                    bannerPager.setCurrentItem(1, false);
                }
                break;
            case 2:
                break;
        }

    }
}
