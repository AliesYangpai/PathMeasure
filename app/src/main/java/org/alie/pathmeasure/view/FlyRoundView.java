package org.alie.pathmeasure.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.alie.pathmeasure.R;

import java.nio.channels.FileLock;

/**
 * Created by Alie on 2019/4/21.
 * 类描述
 * 版本
 */
public class FlyRoundView extends View {
    private static final String TAG = "FlyRoundView";
    private Bitmap mBitmap; // 箭头的bitmap
    private Matrix mMatrix; // 用于对图片进行操作
    private Paint paint;  // 用于画出圆圈
    private Path path;
    private int mWidth;
    private int mHeight;
    private float[] pos;
    private float[] tan;

    private float count; // 这个瞬时切点的频率；

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        path = new Path();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();
        pos = new float[2];
        tan = new float[2];

    }


    public FlyRoundView(Context context) {
        super(context);
        init(context);
    }

    public FlyRoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlyRoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
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


        // count是瞬时切点的频率，这个必须是要小于1的，因为我们要获取瞬时切点的那一段的正切相关数据
        count += 0.005;
        if (count >= 1) {
            count = 0;
        }
        canvas.drawColor(Color.WHITE); // 这一步是为了让整个画布的背景色和箭头的多余色重合
        // 这一步是讲画布移动到中心点

        canvas.translate(mWidth / 2, mHeight / 2);
        path.reset();
        path.addCircle(0, 0, 200, Path.Direction.CCW);

        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * count, pos, tan);
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        mMatrix.reset();
        mMatrix.postRotate(degree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);

        canvas.drawPath(path, paint);
        canvas.drawBitmap(mBitmap, mMatrix, null);
        invalidate();
    }
}
