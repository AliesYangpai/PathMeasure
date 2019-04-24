package org.alie.pathmeasure.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by Alie on 2019/4/24.
 * 类描述
 * 版本
 */
public class LoadView extends View {
    private static final String TAG = "LoadView";

    private Path mBasePath;
    private Path mDtsPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimationValue;
    private int mWidth;
    private int mHeight;

    private void init() {
        mBasePath = new Path();
        // 这里可以先给mBasePath添加圆，原因：
        // 1.因为我们可以在onDraw的时候再进行绘图坐标系平移
        // 2.onDraw时候可以直接进截取绘制业务，避免在onDraw的时候不停地addCircle
        mBasePath.addCircle(0, 0, 70, Path.Direction.CCW);
        mPathMeasure = new PathMeasure(mBasePath,false);
        mDtsPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    private void initAnimationValue() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationValue = (float) animation.getAnimatedValue();
                invalidate();
                Log.i(TAG, "当前线程：" + Thread.currentThread().getId() + " 当前value：" + mAnimationValue);
            }
        });
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    public LoadView(Context context) {
        super(context);
        init();
        initAnimationValue();
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAnimationValue();
    }

    public LoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAnimationValue();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        mDtsPath.reset();
        float endD = mPathMeasure.getLength() * mAnimationValue;
        float startD = (float) (endD - (0.5 - Math.abs(mAnimationValue - 0.5)) * mPathMeasure.getLength());
        mPathMeasure.getSegment(startD, endD, mDtsPath, true);
        canvas.drawPath(mDtsPath, mPaint);
    }
}
