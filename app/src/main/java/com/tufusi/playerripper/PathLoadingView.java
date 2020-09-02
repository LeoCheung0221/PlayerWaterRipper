package com.tufusi.playerripper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by 鼠夏目 on 2020/9/2.
 *
 * @author 鼠夏目
 * @description 入口   绘制 Path
 * 组合控件  组合   播放界面
 * 背景单独组合
 * 唱针   唱盘组合
 * 底盘来适配
 */
public class PathLoadingView extends View {

    private Path mPath;
    private Paint mPaint;
    private float mLength;
    private Path dst;
    private float mAnimatorValue;

    private PathMeasure mPathMeasure;

    public PathLoadingView(Context context) {
        this(context, null);
    }

    public PathLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStrokeWidth(10f);
        mPaint.setStyle(Paint.Style.STROKE);
        //Path
        mPath = new Path();
        //加入一个半径为100圆
        mPath.addCircle(300f, 300f, 100f, Path.Direction.CW);

        // 闭合  圆     直线 非闭合  0  --length
        mPathMeasure = new PathMeasure(mPath, true);
        mLength = mPathMeasure.getLength();
        dst = new Path();
        //属性动画
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        //设置动画过程的监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        //无限循环
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        dst.reset();
        float distance = mLength * mAnimatorValue;
        float start = (float) (distance - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
        mPathMeasure.getSegment(start, distance, dst, true);
        canvas.drawPath(dst, mPaint);
    }
}