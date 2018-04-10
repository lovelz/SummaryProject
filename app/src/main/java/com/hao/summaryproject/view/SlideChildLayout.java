package com.hao.summaryproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by lovelz
 * on 2017/11/27.
 */

public class SlideChildLayout extends FrameLayout implements NestedScrollingChild {

    private static final String TAG = SlideChildLayout.class.getSimpleName();

    private NestedScrollingChildHelper mChildHelper;

    private int[] mConsumed = new int[2];

    private int[] mOffset = new int[2];

    private int downY;
    private int showHeight;

    public SlideChildLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideChildLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideChildLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 执行初始化操作
     */
    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showHeight = getMeasuredHeight();

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        //开启嵌套滚动流程
        //当找到能配合当前子view进行嵌套滑动的父view时，返回值为true
        Log.d(TAG, "子view startNestedScroll----------------");
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.d(TAG, "子view stopNestedScroll----------------");
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.d(TAG, "子view hasNestedScrollingParent----------------");
        return mChildHelper.hasNestedScrollingParent();
    }

    /**
     * 在view消费滚动距离之前把总的滑动距离传给父布局
     *
     * @param dx             表示view消费了x方向的距离长度
     * @param dy             表示view消费了x方向的距离长度
     * @param consumed       表示父布局消费的距离    consumed[0]为x方向，consumed[1]为y方向
     * @param offsetInWindow 表示剩下的dxUnconsumed和dyUnconsumed使得view在父布局中的位置偏移了多少
     * @return
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        Log.d(TAG, "子view dispatchNestedPreScroll----------------");
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     * 把view消费滚动距离之后,把剩下的滑动距离再次传给父布局
     *
     * @param dxConsumed     表示view在x轴方向消费的距离长度
     * @param dyConsumed     表示view在y轴方向消费的距离长度
     * @param dxUnconsumed   表示滚动产生的x滚动距离还有多少没有被消费
     * @param dyUnconsumed   表示滚动产生的y滚动距离还有多少没有被消费
     * @param offsetInWindow 表示剩下的dxUnconsumed和dyUnconsumed使得view在父布局中的位置偏移了多少
     * @return
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        Log.d(TAG, "子view dispatchNestedScroll----------------");

        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                int distance = moveY - downY;
                downY = moveY;
                if (startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL) && dispatchNestedPreScroll(0, distance, mConsumed, mOffset)) {
                    int remain = distance - mConsumed[1];
                    if (remain != 0) {
                        scrollBy(0, -remain);
                    }
                } else {
                    scrollBy(0, -distance);
                }
                Log.d(TAG, "offset---x=" + mOffset[0] + "offset--y=" + mOffset[1]);
//                dispatchNestedScroll(50, 50, 50, 50, mOffset);
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        int maxHeight = getMeasuredHeight() - showHeight;
        if (y > maxHeight) {
            y = maxHeight;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);

    }
}
