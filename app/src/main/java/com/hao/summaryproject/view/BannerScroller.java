package com.hao.summaryproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ScrollView;
import android.widget.Scroller;

import static com.youth.banner.BannerConfig.DURATION;

/**
 * Created by liuzhu
 * on 2017/8/24.
 */

public class BannerScroller extends Scroller {

    private int mDuration = 800;


    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDURATION(int mDuration) {
        this.mDuration = mDuration;
    }
}
