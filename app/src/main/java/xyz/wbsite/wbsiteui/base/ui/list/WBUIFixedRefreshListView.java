package xyz.wbsite.wbsiteui.base.ui.list;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import xyz.wbsite.wbsiteui.R;

public class WBUIFixedRefreshListView extends RelativeLayout implements AbsListView.OnScrollListener {
    private FrameLayout mHeader;
    private FrameLayout mFooter;
    private WbListView mListView;

    public WbListView getListView() {
        return mListView;
    }

    public void setListView(WbListView mListView) {
        this.mListView = mListView;
    }

    private int mLastY;
    private Handler mHandler = new Handler();


    private PullActionListener mPullActionListener = null;
    private PushActionListener mPushActionListener = null;


    private int mRefreshHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50.0f, getContext().getResources().getDisplayMetrics());


    public WBUIFixedRefreshListView(Context context) {
        super(context);
    }

    public WBUIFixedRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListView = new WbListView(getContext());
        mListView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(mListView, layoutParams);
        mHeader = new FrameLayout(getContext());
        LayoutParams mHeaderParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mHeaderParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        this.addView(mHeader, mHeaderParams);
        mFooter = new FrameLayout(getContext());
        LayoutParams mFooterParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mFooterParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mFooter, mFooterParams);
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public int getRefreshHeight() {
        return mRefreshHeight;
    }

    public void setRefreshHeight(int mRefreshHeight) {
        this.mRefreshHeight = mRefreshHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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
        return super.dispatchTouchEvent(ev);
    }

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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 内部ListView方便调用外部类
     */
    public class WbListView extends ListView {
        public WbListView(Context context) {
            super(context);
            super.setOverScrollMode(OVER_SCROLL_NEVER);
        }

        public WbListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            super.setOverScrollMode(OVER_SCROLL_NEVER);
        }

        @Override
        public void invalidate() {
            if (mOverScrollState == OVERSCROLL_STATE_PUSH) {
                this.setSelection(super.computeVerticalScrollRange() - super.computeVerticalScrollExtent());
            } else if (mOverScrollState == OVERSCROLL_STATE_PULL) {
                this.setSelection(0);
            }
            super.invalidate();
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            if (isTouchEvent) {
                if (deltaY < 0) {
                    mOverScrollState = OVERSCROLL_STATE_PULL;
                } else {
                    mOverScrollState = OVERSCROLL_STATE_PUSH;
                }
            }
            return true;
        }
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
