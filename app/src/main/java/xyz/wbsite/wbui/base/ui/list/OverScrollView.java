package xyz.wbsite.wbui.base.ui.list;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;

public class OverScrollView extends ScrollView {
    LinearLayout mLinearLayout;
    LinearLayout mHeader;
    LinearLayout mFooter;
    private int mLastScrollY = -1;
    private int mOverY = 0;
    private int mLastMotionY = 0;

    public static int OVERSCROLL_STATE_NORMAL = 0;//正常
    public static int OVERSCROLL_STATE_PULL = 1;//拉
    public static int OVERSCROLL_STATE_PUSH = -1;//推

    private int mOverScrollState = OVERSCROLL_STATE_NORMAL;

    public OverScrollView(Context context) {
        super(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOverScrollMode(int mode) {
        //去掉边缘发光效果
        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        Log.i("--->", "addView");

        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        mHeader = new LinearLayout(getContext());
        mFooter = new LinearLayout(getContext());
        mLinearLayout.addView(mHeader, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLinearLayout.addView(child, params);
        mLinearLayout.addView(mFooter, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.addView(mLinearLayout, index, layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                mOverScrollState = OVERSCROLL_STATE_NORMAL;
                onBound();
        }

        if (mOverScrollState == OVERSCROLL_STATE_PULL) {
            if (ev.getY() > mLastMotionY) {
                mOverY = (int) (ev.getY() - mLastMotionY);
                this.fullScroll(ScrollView.FOCUS_UP);
                mHeader.setMinimumHeight(mOverY);
                Log.i("-->", "mOverY=" + mOverY);
            } else {
                mOverScrollState = OVERSCROLL_STATE_NORMAL;
            }
            return true;
        } else if (mOverScrollState == OVERSCROLL_STATE_PUSH) {
            if (mLastMotionY > ev.getY()) {
                mOverY = (int) (ev.getY() - mLastMotionY);
                mFooter.setMinimumHeight(-mOverY);
                this.fullScroll(ScrollView.FOCUS_DOWN);
                Log.i("-->", "mOverY=" + mOverY);
            } else {
                mOverScrollState = OVERSCROLL_STATE_NORMAL;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 回弹归位
     */
    private void onBound() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mOverY > 0) {
                    OverScrollView.this.fullScroll(ScrollView.FOCUS_UP);
                    mHeader.setMinimumHeight(mOverY);
                } else if (mOverY < 0) {
                    OverScrollView.this.fullScroll(ScrollView.FOCUS_DOWN);
                    mFooter.setMinimumHeight(-mOverY);
                } else {
                    mHeader.setMinimumHeight(0);
                    mFooter.setMinimumHeight(0);
                }
            }
        };
        new Thread() {
            int during = 1000;
            int d = 10;

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
                    handler.sendEmptyMessage(1);
                }
                mOverY = 0;
            }
        }.start();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        //只捕捉边界值
        if (mOverScrollState == OVERSCROLL_STATE_NORMAL && (scrollY == 0 || scrollY == scrollRangeY)) {
            if (scrollY == 0) {
                mOverScrollState = OVERSCROLL_STATE_PULL;
            } else {
                mOverScrollState = OVERSCROLL_STATE_PUSH;
            }
            try {
                Class aClass = this.getClass().getSuperclass();
                Field mLastMotionYField = aClass.getDeclaredField("mLastMotionY");
                mLastMotionYField.setAccessible(true);
                mLastMotionY = (int) mLastMotionYField.get(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        switch (mOverScrollState){
//            case -1:
//                Log.i("-->","PUSH");
//                break;
//            case 1:
//                Log.i("-->","PULL");
//                break;
//            case 0:
//                Log.i("-->","NORMAL");
//                break;
//        }

//        Log.i("-->-->","deltaY-->"+getScaleY()+"/"+scrollY+"---"+scrollRangeY);
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
