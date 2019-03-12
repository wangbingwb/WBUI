package xyz.wbsite.wbui.base.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by bingwang on 2016/5/14.
 */
public class LoadingView extends View {
    /**
     * 周期
     */
    private int mDuring = 1000;
    /**
     * 旋转步长
     */
    private int mStep = 10;
    /**
     * 当前旋转角度
     */
    private int mSweep = 0;
    /**
     * 前景色
     */
    private int mColor = Color.BLACK;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int widhtMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widhtMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (50 * displayMetrics.density);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (50 * displayMetrics.density);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int scale = 0;
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            scale = height;
        } else {
            scale = width;
        }
        //画圈
        Paint paint = new Paint();
        //设置画笔
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        //设置角度渐变
        SweepGradient radialGradient = new SweepGradient(width / 2, height / 2, mColor, Color.argb(0, 0, 0, 255));
        paint.setShader(radialGradient);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.rotate(mSweep, width / 2, height / 2);
        canvas.drawCircle(width / 2, height / 2, scale / 2 - 10, paint);
        mSweep += mStep;
        if (mSweep >= 360) {
            mSweep = 0;
        }
        postInvalidateDelayed(360 / mStep);
    }

    public int getDuring() {
        return mDuring;
    }

    public void setDuring(int mDuring) {
        this.mDuring = mDuring;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }
}
