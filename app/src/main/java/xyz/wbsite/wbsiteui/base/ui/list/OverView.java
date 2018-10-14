package xyz.wbsite.wbsiteui.base.ui.list;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wangbing on 2016/4/17.
 */
public class OverView extends LinearLayout {
    private int mOverY = 0;
    private int mLastY = -1;

    public OverView(Context context) {
        super(context);
    }

    public OverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("-->", "Y" + event.getY());
        Log.i("-->", "mLastY" + mLastY);
        if (mLastY == -1) {
            mLastY = (int) event.getY();
        } else {
            mOverY = (int) (event.getY() - mLastY);
            invalidate();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                onBound();
                return true;
        }
        return true;
    }

    private void onBound() {
        final int during = 1000;
        final int d = 10;
        if (mOverY != 0) {
            new Thread() {
                @Override
                public void run() {
                    int flag = 1;
                    if (mOverY < 0) {
                        flag = -1;
                        mOverY = -mOverY;
                    }
                    int temp = mOverY;

                    int i = 0;
                    while (i < during && Math.abs(temp) >= 5) {
                        try {
                            Thread.sleep(d);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i += d;
                        temp = (int) (Math.pow((double) i / during - 1, 2) * temp);
                        mOverY = flag * temp;
                        postInvalidate();
                    }
                    mOverY = 0;
                    postInvalidate();
                }
            }.start();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(0, mOverY / 2);
        super.draw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }


}
