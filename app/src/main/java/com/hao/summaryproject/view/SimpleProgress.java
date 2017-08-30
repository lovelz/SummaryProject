package com.hao.summaryproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import static android.R.attr.width;

/**
 * 自定义progress
 * Created by liuzhu
 * on 2017/8/30.
 */

public class SimpleProgress extends View {

    private static final String TAG = SimpleProgress.class.getSimpleName();

    //view宽
    private int mWidth;

    //view高
    private int mHeight;
    private Paint paint;

    //进度条画笔宽度
    private int progressPaintWidth;

    public SimpleProgress(Context context) {
        this(context, null);
    }

    public SimpleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initValue();
        initPaint();
    }

    /**
     * 初始化长度
     */
    private void initValue() {
        progressPaintWidth = dp2px(4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthSize, widthMode), measureHeight(heightSize, heightMode));
    }

    /**
     * 测量宽度
     *
     * @param widthSize
     * @param widthMode
     * @return
     */
    private int measureWidth(int widthSize, int widthMode) {
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mWidth = dp2px(300);
                Log.e(TAG, "you should set exactly value on width or height");
                break;
            case MeasureSpec.EXACTLY:
                mWidth = widthSize;
                break;
        }
        return mWidth;
    }

    /**
     * 测量高度
     *
     * @param heightSize
     * @param heightMode
     * @return
     */
    private int measureHeight(int heightSize, int heightMode) {
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                Log.e(TAG, "you should set exactly value on width or height");
                mHeight = dp2px(100);
                break;
            case MeasureSpec.EXACTLY:
                mHeight = heightSize;
                break;
        }
        return mHeight;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(dp2px(4));
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 得到一个画笔对象
     * @param strokeWidth
     * @param colorRes
     * @param style
     * @return
     */
    private Paint getPaint(int strokeWidth, int colorRes, Paint.Style style) {
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG, mWidth + "");

        canvas.drawLine(0, 0, width, 0, paint);
    }

    /**
     * dp --> px
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
