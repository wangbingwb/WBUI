package xyz.wbsite.wbui.base.ui.other;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class RefreshView extends RelativeLayout {
    private FrameLayout mHeader;
    private FrameLayout mFooter;
    private View target = null;

    private int mLastY = 0;
    private Handler mHandler = new Handler();


    private PullActionListener mPullActionListener = null;
    private PushActionListener mPushActionListener = null;


    private int mRefreshHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50.0f, getContext().getResources().getDisplayMetrics());


    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeader = new FrameLayout(getContext());
        mHeader.setTag("mHeader");
        LayoutParams mHeaderParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mHeaderParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        this.addView(mHeader, mHeaderParams);
        mFooter = new FrameLayout(getContext());
        mFooter.setTag("mFooter");
        LayoutParams mFooterParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mFooterParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mFooter, mFooterParams);

    }

    @Override
    public void onViewAdded(View child) {
        if ("mHeader".equals(child.getTag()) || "mFooter".equals(child.getTag())) {
            super.onViewAdded(child);
        } else if (target == null) {
            target = child;
            super.onViewAdded(child);
        } else {
            throw new RuntimeException("只能有一个子VIEW");
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    public int getRefreshHeight() {
        return mRefreshHeight;
    }

    public void setRefreshHeight(int mRefreshHeight) {
        this.mRefreshHeight = mRefreshHeight;
    }

    public boolean isListViewReachTopEdge(AbsListView listView) {
        boolean result = false;
        if (listView.getFirstVisiblePosition() == 0) {
            final View topChildView = listView.getChildAt(0);
            result = topChildView.getTop() == 0;
        }
        return result;
    }

    public boolean isListViewReachBottomEdge(AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        ;
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);

        Log.i("------",""+mOverY);

        AbsListView target = (AbsListView) this.target;

        if (isListViewReachTopEdge(target)) {
            int y = (int) ev.getY();
            if (y > mLastY && mLastY != 0) {
                mOverScrollState = OVERSCROLL_STATE_PULL;
            }
        } else if (isListViewReachBottomEdge(target)) {
            int y = (int) ev.getY();
            if (y < mLastY && mLastY != 0) {
                mOverScrollState = OVERSCROLL_STATE_PUSH;
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mOverScrollState = OVERSCROLL_STATE_NORMAL;
            onBound();
        }

        if (mOverScrollState != OVERSCROLL_STATE_NORMAL) {
            mOverY += mLastY - (int) ev.getY();
            if (Math.abs(mOverY) > mOverYLimit) {
                mOverYLimit = Math.abs(mOverY);
            }
            mLastY = (int) ev.getY();

            if ((mOverY > 0 && mOverScrollState == OVERSCROLL_STATE_PULL) || (mOverY < 0 && mOverScrollState == OVERSCROLL_STATE_PUSH)) {
                mOverY = 0;
                mOverScrollState = OVERSCROLL_STATE_NORMAL;
            }
            if (mOverScrollState == OVERSCROLL_STATE_PULL) {
                ViewGroup.LayoutParams layoutParams = mHeader.getLayoutParams();
                layoutParams.height = Math.abs(mOverY) / 2;
                mHeader.setLayoutParams(layoutParams);
            } else if (mOverScrollState == OVERSCROLL_STATE_PUSH) {
                ViewGroup.LayoutParams layoutParams = mFooter.getLayoutParams();
                layoutParams.height = Math.abs(mOverY) / 2;
                mFooter.setLayoutParams(layoutParams);
            }
            onOverHeightChangeListener(mOverY / 2);
            return true;
        }
        mLastY = (int) ev.getY();
        return true;
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (mOverScrollState != OVERSCROLL_STATE_NORMAL){
//            return true;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    /**
     * 基本参数
     */
    private static int OVERSCROLL_STATE_NORMAL = 0;//正常
    private static int OVERSCROLL_STATE_PULL = -1;//拉
    private static int OVERSCROLL_STATE_PUSH = 1;//推
    private int mOverScrollState = OVERSCROLL_STATE_NORMAL;

    /**
     * 越界值 mOverY<0是pull mOverY>0是push
     */
    private int mOverY = 0;
    private boolean isRefreshing = false;

    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(2);

    private int timestamp = 0;


    /**
     * 从越界位置返回正常位置的周期
     */
    private int mDuring = 450;
    /**
     * 帧画面步值，越小画面越流畅，但应为系统配置性能各有差异，不是越低越好
     */
    private int mStep = 30;

    /**
     * 下拉或上推时的峰值，用于恢复时阻尼效果计算
     */
    private int mOverYLimit = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mOverScrollState != OVERSCROLL_STATE_NORMAL || mOverY == 0) {
                timestamp = 0;
                mOverYLimit = 0;
                return;
            }

            if (!isRefreshing || (isRefreshing && mOverScrollState == OVERSCROLL_STATE_NORMAL && Math.abs(mOverY) > 2 * mRefreshHeight)) {
                timestamp += mStep;

                if (Math.abs(mOverY) > 2 * mRefreshHeight &&
                        Math.abs(mOverYLimit - (int) (mOverYLimit * decelerateInterpolator.getInterpolation(timestamp * 1.0f / mDuring))) < 2 * mRefreshHeight) {
                    if (mOverY < 0) {
                        mOverY = -2 * mRefreshHeight;
                    } else {
                        mOverY = 2 * mRefreshHeight;
                    }
                    onRefresh();
                } else {
                    mOverY = (Math.abs(mOverY) / mOverY) * (mOverYLimit - (int) (mOverYLimit * decelerateInterpolator.getInterpolation(timestamp * 1.0f / mDuring)));
                }
                if (Math.abs(mOverY) <= 5) {
                    mOverY = 0;
                }

                if (mOverY < 0) {
                    ViewGroup.LayoutParams layoutParams = mHeader.getLayoutParams();
                    layoutParams.height = Math.abs(mOverY) / 2;
                    mHeader.setLayoutParams(layoutParams);
                } else if (mOverY > 0) {
                    ViewGroup.LayoutParams layoutParams = mFooter.getLayoutParams();
                    layoutParams.height = Math.abs(mOverY) / 2;
                    mFooter.setLayoutParams(layoutParams);
                } else {
                    ViewGroup.LayoutParams layoutParams = mHeader.getLayoutParams();
                    layoutParams.height = 0;
                    mHeader.setLayoutParams(layoutParams);
                    layoutParams = mFooter.getLayoutParams();
                    layoutParams.height = 0;
                    mFooter.setLayoutParams(layoutParams);
                }
                onOverHeightChangeListener(mOverY / 2);
            }

            mHandler.postDelayed(runnable, mStep);
        }
    };


    /**
     * 释放时，回弹方法
     */
    private void onBound() {
        mHandler.postDelayed(runnable, mStep);
    }

    /**
     * 当有越界事件发生时调用该方法
     *
     * @param mOverY mOverY<0是pull mOverY>0是push
     */
    public void onOverHeightChangeListener(int mOverY) {
        if (mOverY < 0 && mPullActionListener != null) {
            mPullActionListener.onHeightChange(Math.abs(mOverY), mRefreshHeight, mHeader);
        } else if (mOverY > 0 && mPushActionListener != null) {
            mPushActionListener.onHeightChange(mOverY, mRefreshHeight, mFooter);
        } else {
            //do nothing
        }
    }

    /**
     * 尝试刷新动作
     */
    private void onRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        if (mOverY < 0 && mPullActionListener != null) {
            mOverY = -2 * mRefreshHeight;
            mPullActionListener.onRefresh(mRefreshHeight, mHeader, notify);
        } else if (mOverY < 0 && mPullActionListener == null) {
            isRefreshing = false;
        } else if (mOverY > 0 && mPushActionListener != null) {
            mOverY = 2 * mRefreshHeight;
            mPushActionListener.onRefresh(mRefreshHeight, mFooter, notify);
        } else if (mOverY > 0 && mPushActionListener == null) {
            isRefreshing = false;
        }
    }

    private Notify notify = new Notify() {

        @Override
        public void refreshNotify() {
            if (mOverY < 0 && mPullActionListener != null) {
                mPullActionListener.onCompleted(Math.abs(mOverY), mHeader, notify);
            } else if (mOverY > 0 && mPushActionListener != null) {
                mPushActionListener.onCompleted(mOverY, mFooter, notify);
            } else {
                //do nothing
            }
        }

        @Override
        public void completedNotify() {
            isRefreshing = false;
        }

    };

    /**
     * pull事件的listener接口
     */
    public interface PullActionListener {
        void onHeightChange(int currentHeight, int refreshHeight, ViewGroup rootView);

        void onRefresh(int mRefreshHeight, ViewGroup viewGroup, Notify notify);

        void onCompleted(int mRefreshHeight, ViewGroup viewGroup, Notify notify);
    }

    /**
     * push事件的listener接口
     */
    public interface PushActionListener {
        void onHeightChange(int currentHeight, int refreshHeight, ViewGroup rootView);

        void onRefresh(int mRefreshHeight, ViewGroup viewGroup, Notify notify);

        void onCompleted(int mRefreshHeight, ViewGroup viewGroup, Notify notify);
    }

    public interface Notify {
        void refreshNotify();

        void completedNotify();
    }

    public PullActionListener getPullActionListener() {
        return mPullActionListener;
    }

    public void setPullActionListener(PullActionListener mPullActionListener) {
        this.mPullActionListener = mPullActionListener;
    }

    public PushActionListener getPushActionListener() {
        return mPushActionListener;
    }

    public void setPushActionListener(PushActionListener mPushActionListener) {
        this.mPushActionListener = mPushActionListener;
    }
}
