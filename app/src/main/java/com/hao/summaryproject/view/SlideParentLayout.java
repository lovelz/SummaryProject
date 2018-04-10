package com.hao.summaryproject.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lovelz
 * on 2017/11/24.
 */

public class SlideParentLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = SlideParentLayout.class.getSimpleName();

    private NestedScrollingParentHelper mParentHelper;

    public SlideParentLayout(Context context) {
        this(context, null);
    }

    public SlideParentLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideParentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    /**
     * 是否接受即将来临的嵌套滑动
     *
     * @param child            嵌套滑动对应的父类的子类（并不一定是直接子view，中间可以有几层嵌套关系）
     * @param target           具体嵌套滑动的子类
     * @param nestedScrollAxes 支持嵌套滚动轴（水平方向、垂直方向或者不指定）
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //接受其内部view的滑动参数（根据nestedScrollAxes判断）
        Log.d(TAG, "child==target:" + (child == target));
        Log.d(TAG, "父布局onStartNestedScroll--------target:" + target + "-------------this" + this);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.d(TAG, "父布局onNestedScrollAccepted----------");
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.d(TAG, "父布局onStopNestedScroll----------");
        mParentHelper.onStopNestedScroll(child);
    }

    /**
     * 嵌套滑动的子view在滑动之后报告的滑动情况
     *
     * @param target       具体嵌套滑动的子类
     * @param dxConsumed   嵌套滑动的子view在x方向消费的距离
     * @param dyConsumed   嵌套滑动的子view在y方向消费的距离
     * @param dxUnconsumed 嵌套滑动的子view在x方向未消费的距离
     * @param dyUnconsumed 嵌套滑动的子view在y方向未消费的距离
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "父布局onNestedScroll----------");
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    /**
     * 嵌套滑动的子view在滑动之前报告准备滑动的情况
     *
     * @param target    具体嵌套滑动的子类
     * @param dx        嵌套滑动的子view在x方向想要变化的距离
     * @param dy        嵌套滑动的子view在y方向想要变化的距离
     * @param consumed  自己指定，告诉子view当前父view消费的距离，
     *                  consumed[0]:水平方向消费的距离，consumed[1]:垂直方向消费的距离，好让子view做出相应的调整
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //内部view移动的dx、dy,如果我们需要消耗dy,需要consumed[1] = ?
        Log.d(TAG, "父布局onNestedPreScroll----------");
//        scrollBy(0, -dy);
        consumed[0] = 0;
        consumed[1] = dy;
//        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    /**
     * 嵌套滑动的子view在fling之后报告过来的fling情况
     *
     * @param target     具体嵌套滑动的子类
     * @param velocityX  水平方向的速度
     * @param velocityY  垂直方向的速度
     * @param consumed   子view是否fling了
     * @return           父view是否消费了fling
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //可以捕捉到内部view的fling事件
        Log.d(TAG, "父布局onNestedFling----------");
//        return super.onNestedFling(target, velocityX, velocityY, consumed);
        return true;
    }

    /**
     * 嵌套滑动的子view在fling之前报告准备fling的情况
     *
     * @param target     具体嵌套滑动的子类
     * @param velocityX  水平方向的速度
     * @param velocityY  垂直方向的速度
     * @return           父view是否消费了fling
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d(TAG, "父布局onNestedPreFling----------");
//        return super.onNestedPreFling(target, velocityX, velocityY);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

}
