package xyz.wbsite.wbsiteui.base.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by bingwang on 2016/5/14.
 */
public class LoadingView1 extends View {
    /**
     * 周期
     */
    private int mDuring = 1000;

    private int firstFlag;
    /**
     * 前景色
     */
    private int mColor = Color.BLACK;

    public LoadingView1(Context context) {
        super(context);
    }

    public LoadingView1(Context context, AttributeSet attrs) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int scale = 0;
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            scale = height;
        } else {
            scale = width;
        }

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(mColor);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int blockWidth = scale / 20;
        int blockHeight = scale / 4;
        int radiou = blockWidth / 2;
        RectF rect = new RectF(centerX - blockWidth / 2, centerY - 3 * blockHeight / 2, centerX + blockWidth / 2, centerY - blockHeight / 2);
        canvas.rotate(firstFlag, centerX, centerY);
        firstFlag += 30;
        if (firstFlag == 360) firstFlag = 0;
        canvas.drawRoundRect(rect, radiou, radiou, p);

        for (int i = 0; i < 11; i++) {
            canvas.rotate(30, centerX, centerY);
            p.setAlpha(i * (255 / 12));
            canvas.drawRoundRect(rect, radiou, radiou, p);
        }
        postInvalidateDelayed(mDuring / 12);
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
