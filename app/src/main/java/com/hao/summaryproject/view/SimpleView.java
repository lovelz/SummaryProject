package com.hao.summaryproject.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by liuzhu
 * on 2017/8/29.
 */

public class SimpleView extends View {

    //view 宽度
    private int width;

    //view高度
    private int height;

    //背景颜色
    private int bg_color = 0xffbc7d53;

    //画笔
    private Paint paint;

    //文字画笔
    private Paint textPaint;

    //√画笔
    private Paint okPaint;

    //矩形
    private RectF rectF = new RectF();

    //文字所在矩形
    private Rect textRect = new Rect();

    //两圆角圆心之间的距离
    private int default_two_circle_distance;

    //两圆角圆心之间的距离(变化中)
    private int two_circle_distance;

    //圆角半径
    private int circleAngle;

    //是否开始绘制√
    private boolean isOk;

    //√路径
    private Path path = new Path();

    //取√路径的长度
    private PathMeasure pathMeasure;

    //动画持续时间
    private int duration = 1000;

    //组合动画
    private AnimatorSet animatorSet = new AnimatorSet();

    //矩形变圆角的动画
    private ValueAnimator rect_to_angle_animator;

    //圆角矩形变圆形动画
    private ValueAnimator rect_to_circle_animator;

    //上升的动画
    private ObjectAnimator move_to_up_animator;

    //绘制√的动画
    private ValueAnimator draw_path_animator;

    public SimpleView(Context context) {
        this(context, null);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet.start();
            }
        });

    }

    /**
     * 初始化画笔信息
     */
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bg_color);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(38);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        okPaint = new Paint();
        okPaint.setAntiAlias(true);
        okPaint.setStrokeWidth(10);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        draw_oval_circle(canvas);

        drawText(canvas);

        if (isOk) {
            canvas.drawPath(path, okPaint);
        }
    }

    private void draw_oval_circle(Canvas canvas) {
        rectF.left = two_circle_distance;
        rectF.top = 0;
        rectF.right = width - two_circle_distance;
        rectF.bottom = height;

        canvas.drawRoundRect(rectF, circleAngle, circleAngle, paint);
    }

    private void drawText(Canvas canvas) {
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = width;
        textRect.bottom = height;

        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        //获取基线
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText("确认完成", textRect.centerX(), baseline, textPaint);

    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        set_rect_to_angle_animation();
        set_rect_to_circle_animation();
        set_move_to_up_animation();
        set_draw_path_animation();

        animatorSet.play(move_to_up_animator)
                .before(draw_path_animator)
                .after(rect_to_circle_animator)
                .after(rect_to_angle_animator);
    }

    /**
     * 矩形过度到圆角的动画
     */
    private void set_rect_to_angle_animation() {
        rect_to_angle_animator = ValueAnimator.ofInt(0, height / 2);
        rect_to_angle_animator.setDuration(duration);
        rect_to_angle_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    /**
     * 圆角矩形过度到圆形的动画
     */
    private void set_rect_to_circle_animation() {
        rect_to_circle_animator = ValueAnimator.ofInt(0, default_two_circle_distance);
        rect_to_circle_animator.setDuration(duration);
        rect_to_circle_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two_circle_distance = (int) animation.getAnimatedValue();

                int alpha = 255 - (two_circle_distance * 255) / default_two_circle_distance;
                textPaint.setAlpha(alpha);

                invalidate();
            }
        });
    }

    /**
     * 上移动画
     */
    private void set_move_to_up_animation() {
        final float curTranslationY = this.getTranslationY();
        move_to_up_animator = ObjectAnimator.ofFloat(this, "translationY",
                curTranslationY, curTranslationY - 300);
        move_to_up_animator.setDuration(duration);
        move_to_up_animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    /**
     * 绘制√动画
     */
    private void set_draw_path_animation() {
        draw_path_animator = ValueAnimator.ofFloat(1, 0);
        draw_path_animator.setDuration(duration);
        draw_path_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isOk = true;
                float value = (float) animation.getAnimatedValue();

                DashPathEffect dashEffect = new DashPathEffect(new float[]{pathMeasure.getLength(),
                        pathMeasure.getLength()}, value * pathMeasure.getLength());
                okPaint.setPathEffect(dashEffect);
                invalidate();
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        default_two_circle_distance = (w - h) / 2;

        initAnimation();

        initOkPath();
    }

    /**
     * 绘制√路径
     */
    private void initOkPath() {
        path.moveTo(default_two_circle_distance + height / 8 * 3, height / 2);
        path.lineTo(default_two_circle_distance + height / 2, height / 5 * 3);
        path.lineTo(default_two_circle_distance + height / 3 * 2, height / 5 * 2);

        pathMeasure = new PathMeasure(path, true);
    }
}
