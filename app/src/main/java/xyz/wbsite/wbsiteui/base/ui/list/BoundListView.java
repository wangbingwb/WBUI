package xyz.wbsite.wbsiteui.base.ui.list;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class BoundListView extends ListView {
    public static int OVERSCROLL_STATE_NORMAL = 0;//正常
    public static int OVERSCROLL_STATE_PULL = -1;//拉
    public static int OVERSCROLL_STATE_PUSH = 1;//推

    private int mOverScrollState = OVERSCROLL_STATE_NORMAL;
    private float lastY = -1;//最近一次Y轴位置

    /**
     * 小于0-->pull,大于0-->push
     */
    private int mOverY = 0;

    public BoundListView(Context context) {
        super(context);
    }

    public BoundListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOverScrollMode(int mode) {
        //去掉边缘发光效果
        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOverY < 0) {
                    mOverScrollState = OVERSCROLL_STATE_PULL;
                } else if (mOverY > 0) {
                    mOverScrollState = OVERSCROLL_STATE_PUSH;
                }
        }
        if (mOverScrollState != OVERSCROLL_STATE_NORMAL) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    mOverScrollState = OVERSCROLL_STATE_NORMAL;
                    onBound();
                    return true;
            }

            if (lastY < 0) {
                lastY = ev.getY();
            } else {
                if (Math.abs(ev.getY() - lastY) > 30) {
                    lastY = ev.getY();
                    return true;
                }
                if (ev.getY() - lastY != 0) {
                    if (mOverScrollState == OVERSCROLL_STATE_PULL) {
                        mOverY -= (ev.getY() - lastY);
                        if (mOverY > 0) {
                            mOverY = 0;
                            mOverScrollState = OVERSCROLL_STATE_NORMAL;
                            ev.setAction(MotionEvent.ACTION_DOWN);
                            super.onTouchEvent(ev);
                        }
                    } else if (mOverScrollState == OVERSCROLL_STATE_PUSH) {
                        mOverY -= (ev.getY() - lastY);
                        if (mOverY < 0) {
                            mOverY = 0;
                            mOverScrollState = OVERSCROLL_STATE_NORMAL;
                            ev.setAction(MotionEvent.ACTION_DOWN);
                            super.onTouchEvent(ev);
                        }
                    }
                }
                lastY = ev.getY();
                invalidate();
            }
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        if (isTouchEvent) {
//            if (deltaY < 0) {
//                mOverScrollState = OVERSCROLL_STATE_PULL;
//            } else {
//                mOverScrollState = OVERSCROLL_STATE_PUSH;
//            }
//            mOverY += deltaY;
//            invalidate();
//        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(0, -mOverY / 2);
        super.draw(canvas);
    }

    /**
     * 回弹归位
     */
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
                    while (i < during && Math.abs(temp) >= 5 && mOverScrollState == OVERSCROLL_STATE_NORMAL) {
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
}
