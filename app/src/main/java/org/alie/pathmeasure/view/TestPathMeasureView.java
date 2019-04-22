package org.alie.pathmeasure.view;

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
 * Created by Alie on 2019/4/22.
 * 类描述
 * 版本
 */
public class TestPathMeasureView extends View {
    private static final String TAG = "TestPathMeasureView";

    private Paint mPaint;
    private Path mPath;

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
    }
    public TestPathMeasureView(Context context) {
        super(context);
        init();
    }

    public TestPathMeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestPathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.lineTo(0,200);
        mPath.lineTo(200,200);
        mPath.lineTo(200,0);

        PathMeasure pathMeasure1 = new PathMeasure(mPath,false);
        PathMeasure pathMeasure2 = new PathMeasure(mPath,true);

        Log.i(TAG," false:"+pathMeasure1.getLength()+ " true:"+pathMeasure2.getLength());
    }
}
