package xyz.wbsite.wbsiteui.base.ui.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by bingwang on 2016/5/14.
 */
public class LoadingView2 extends View {
    /**
     * 周期
     */
    private int mDuring = 600;
    private int timeStamp = 0;
    private int currentAngle = 0;

    public LoadingView2(Context context) {
        super(context);
    }

    public LoadingView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        currentAngle = (int) (360 * interpolator.getInterpolation(((float) (timeStamp % mDuring) / (mDuring + 1))));
        canvas.rotate(currentAngle, width / 2, height / 2);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, 3, paint);

        int abs = Math.abs((currentAngle % 360) % 180 - 180 * ((currentAngle % 360) / 180));
        for (int i = 0; i < 5; i++) {
            canvas.rotate(-abs * 30 / 180, width / 2, height / 2);
            paint.setAlpha(255 - 255 * i / 5);
            canvas.drawCircle(width / 2, height / 2 - 50, 10, paint);
        }

//        for (int i = 0; i < 200; i++) {
//            float interpolation = interpolator.getInterpolation(i / 100f);
//
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(i/100f);
//            for (int j = 0; j < interpolation*100; j++) {
//                stringBuffer.append(".");
//            }
//            System.out.println(stringBuffer.toString());
//        }

        timeStamp += 10;
        postInvalidateDelayed(30);
    }
}
