package xyz.wbsite.wbsiteui.base.ui.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import xyz.wbsite.wbsiteui.base.utils.DensityUtil;

public class PaternalLayout extends ViewGroup implements NestedScrollingParent, AbsListView.OnScrollListener {
    private static final String TAG = "WBUIListView";
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    boolean mIsRefreshing = false;
    private View mTargetView;
    private View mPullView;
    private View mPushView;
    private FrameLayout headView;
    private FrameLayout footView;
    private int mPullViewIndex = -1;
    private int mPushViewIndex = -1;
    private int mTouchSlop;
    private boolean mNestedScrollInProgress;
    private float mInitialDownY;
    private float mInitialDownX;
    private float mInitialMotionY;
    private float mLastMotionY;
    private float mDragRate = 0.8f;
    private Scroller mScroller;
    private boolean mNestScrollDurationRefreshing = false;

    private IPullViewBuilder pullViewBuilder;
    private IPushViewBuilder pushViewBuilder;

    private int pullOffset = 0;
    private int pullHeight = DensityUtil.dp2px(getContext(),150);
    private int pushOffset = 0;
    private int pushHeight = DensityUtil.dp2px(getContext(),150);

    private boolean mIsPull;
    private boolean mIsPush;
    boolean isTop = false;
    boolean isBottom = false;

    int width = 0;
    int height = 0;

    @Override
    public void onViewAdded(View child) {
        if (child instanceof AbsListView) {
            AbsListView listView = (AbsListView) child;
            listView.setOnScrollListener(this);
        }
        super.onViewAdded(child);
    }

    public PaternalLayout(Context context) {
        this(context, null);
    }

    public PaternalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.QMUIPullRefreshLayoutStyle);
    }

    public PaternalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = QMUIDisplayHelper.px2dp(context, vc.getScaledTouchSlop()); //系统的值是8dp,如何配置？

        mScroller = new Scroller(getContext());
        mScroller.setFriction(ViewConfiguration.getScrollFriction());

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        headView = new FrameLayout(getContext());
        headView.setBackgroundColor(getResources().getColor(xyz.wbsite.wbsiteui.R.color.app_color_theme_5));
        headView.setLayoutParams(new LinearLayout.LayoutParams(width, pullHeight));
        footView = new FrameLayout(getContext());
        footView.setLayoutParams(new LinearLayout.LayoutParams(width, pushHeight));
//        addView(headView,-0);
//        addView(footView);
    }

    public interface Notify {
        void finish();
    }

    public interface IPullViewBuilder {
        View createView();

        void onChange(View view, int currentHeight, int pullHeight);

        void onAction(View view, int pullHeight, Notify notify);
    }

    public interface IPushViewBuilder {
        View createView();

        void onChange(View view, int currentHeight, int pushHeight);

        void onAction(View view, int pushHeight, Notify notify);
    }

    public void setPullViewBuilder(IPullViewBuilder pullViewBuilder) {
        this.pullViewBuilder = pullViewBuilder;
        if (mPullView == null) {
            mPullView = pullViewBuilder.createView();
//            headView.addView(mPullView);
            addView(mPullView);
        }
    }

    public void setPushViewBuilder(IPushViewBuilder pushViewBuilder) {
        this.pushViewBuilder = pushViewBuilder;
        if (mPushView == null) {
            mPushView = pushViewBuilder.createView();
//            footView.addView(mPushView);
            addView(mPushView);
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mPullViewIndex < 0 || mPushViewIndex < 0) {
            return i;
        }
        // 最后才绘制mRefreshView
        if (i == mPullViewIndex || i == mPushViewIndex) {
            return childCount - 1;
        }
        if (i > mPullViewIndex || i > mPushViewIndex) {
            return i - 1;
        }
        return i;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTargetView instanceof AbsListView)
                || (mTargetView != null && !ViewCompat.isNestedScrollingEnabled(mTargetView))) {
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureTargetView();
        if (mTargetView == null) {
            Log.d(TAG, "onMeasure: mTargetView == null");
            return;
        }
        int targetMeasureWidthSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int targetMeasureHeightSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTargetView.measure(targetMeasureWidthSpec, targetMeasureHeightSpec);
        if (mPullView != null) {
            measureChild(mPullView, widthMeasureSpec, heightMeasureSpec);
        }
        if (mPushView != null) {
            measureChild(mPushView, widthMeasureSpec, heightMeasureSpec);
        }
        mPullViewIndex = -1;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == mPullView && mPullView != null) {
                mPullViewIndex = i;
                break;
            }
            if (getChildAt(i) == mPushView && mPushView != null) {
                mPushViewIndex = i;
                break;
            }
        }
    }

    private void log(String s) {
        Log.i("===========>", s);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        ensureTargetView();
        if (mTargetView == null) {
            Log.d(TAG, "onLayout: mTargetView == null");
            return;
        }

        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        mTargetView.layout(0, 0, 0 + childWidth, 0 + childHeight);
        headView.layout(0, 0, width, pullHeight);
        footView.layout(0, height - pushHeight, width, height - pushHeight + pushHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTargetView();

        if (pullOffset > 0 || pushOffset > 0) {
            return true;
        }

        if (canChildScrollUp(mTargetView)) {
            isTop = false;
        } else {
            isTop = true;
        }

        if (canChildScrollDown(mTargetView)) {
            isBottom = false;
        } else {
            isBottom = true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownX = ev.getX();
                mInitialDownY = ev.getY();
                mLastMotionY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
            {
                final float dx = x - mInitialDownX;
                final float dy = y - mInitialDownY;
                boolean isYDrag = Math.abs(dy) > Math.abs(dx);

                if (isYDrag && isTop && dy > 0) {//pull
                    System.out.println();
                    mLastMotionY = y;
                    mIsPull = true;
                    return true;
                }

                if (isYDrag && isBottom && dy < 0) {//push
                    System.out.println();
                    mLastMotionY = y;
                    mIsPush = true;
                    return true;
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                reset();
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                float dy = (y - mLastMotionY) * mDragRate;

                if (mIsPull && pullViewBuilder != null) {
                    pullOffset += dy;
                    if (pullOffset > pullHeight) {
                        pullOffset = pullHeight;
                    }
                    pullViewBuilder.onChange(mPullView, pullOffset, pullHeight);
                    mPullView.setLayoutParams(new LayoutParams(width, pullOffset));
                    mPullView.layout(0, 0, width, pullOffset);
                    invalidate();
                }
                if (mIsPush && pushViewBuilder != null) {
                    if (dy > 0) {
                        pushOffset -= Math.abs(dy);
                    } else {
                        pushOffset += Math.abs(dy);
                    }

                    if (pushOffset > pushHeight) {
                        pushOffset = pushHeight;
                    }
                    final int width = getMeasuredWidth();
                    final int height = getMeasuredHeight();
                    pushViewBuilder.onChange(mPushView, pushOffset, pushHeight);
                    mPushView.setLayoutParams(new LayoutParams(width, pushOffset));
                    mPushView.layout(0, height - pushOffset, width, height);
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                reset();
                return false;
            }
        }
        mLastMotionY = ev.getY();
        return true;
    }

    private void ensureTargetView() {
        if (mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (!view.equals(mPullView)) {
                    mTargetView = view;
                    break;
                }
            }
        }
    }

    public void reset() {
        log("------------------------------reset----------------------------");
        mIsPull = false;
        mIsPush = false;

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (pullOffset >= pullHeight) {
            pullViewBuilder.onAction(mPullView, pullHeight, new Notify() {
                @Override
                public void finish() {
                    mScroller.startScroll(0, 0, 0, pullOffset, 1400);
                    invalidate();
                }
            });
        } else if (pullOffset > 0) {
            mScroller.startScroll(0, 0, 0, pullOffset, 1400);
            invalidate();
        }

        if (pushOffset >= pushHeight) {
            pushViewBuilder.onAction(mPushView, pushHeight, new Notify() {
                @Override
                public void finish() {
                    mScroller.startScroll(0, 0, 0, pushOffset, 1400);
                    invalidate();
                }
            });
        } else if (pushOffset > 0) {
            mScroller.startScroll(0, 0, 0, pushOffset, 1400);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mIsPull || mIsPush) {
            return;
        }
//        log("------------------------------computeScroll--------------" + pullOffset + "," + pushOffset + "--------------");
        if (mScroller.computeScrollOffset()) {
            if (pullOffset > 0) {
                pullOffset = pullOffset - mScroller.getCurrY();
                if (pullOffset <= 0) {
                    pullOffset = 0;
                    mIsPull = false;
                }
                mPullView.layout(0, 0, width, pullOffset);
                invalidate();
            } else if (pushOffset > 0) {
                pushOffset = pushOffset - mScroller.getCurrY();
                if (pushOffset <= 0) {
                    pushOffset = 0;
                    mIsPush = false;
                }
                mPushView.layout(0, height - pushOffset, width, height);
                invalidate();
            }
        } else {
//            if (pullOffset != 0 || pushOffset != 0) {
//                pullOffset = 0;
//                pushOffset = 0;
//                mPullView.layout(0, 0, width, pullOffset);
//                mPushView.layout(0, height - pushOffset, width, height - pushOffset + pushHeight);
//                invalidate();
//            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
            invalidate();
        }
    }

    public boolean canChildScrollUp(View mTargetView) {
        if (mTargetView == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1);
        }
    }

    public boolean canChildScrollDown(View mTargetView) {
        if (mTargetView == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
                return this.firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && lastChild.getBottom() >= absListView.getPaddingBottom();
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            if (dy >= 0) {
                consumed[1] = 0;
                pullOffset = 0;
            } else {
                consumed[1] = dy;
                pullOffset += dy;
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0 && !canChildScrollUp(mTargetView)) {
            pullOffset += dyUnconsumed;
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View child) {
        mNestedScrollingParentHelper.onStopNestedScroll(child);
        if (mNestedScrollInProgress) {
            mNestedScrollInProgress = false;
            reset();
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        mNestedScrollInProgress = false;
        reset();
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        try {
            return super.onNestedFling(target, velocityX, velocityY, consumed);
        } catch (Throwable e) {
            // android 24及以上ViewGroup会继续往上派发， 23以及以下直接返回false
            // 低于5.0的机器和RecyclerView配合工作时，部分机型会调用这个方法，但是ViewGroup并没有实现这个方法，会报错，这里catch一下
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mNestScrollDurationRefreshing = mIsRefreshing;
        } else if (mNestScrollDurationRefreshing) {
            if (action == MotionEvent.ACTION_MOVE) {
                if (!mIsRefreshing) {
                    mNestScrollDurationRefreshing = false;
                    ev.setAction(MotionEvent.ACTION_DOWN);
                }
            } else {
                mNestScrollDurationRefreshing = false;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(PaternalLayout parent, @Nullable View child);
    }

    public interface RefreshOffsetCalculator {

        /**
         * 通过 targetView 的当前位置、targetView 的初始和刷新位置以及 refreshView 的初始与结束位置计算 RefreshView 的位置。
         *
         * @param refreshInitOffset   RefreshView 的初始 offset。
         * @param refreshEndOffset    刷新时 RefreshView 的 offset。
         * @param refreshViewHeight   RerfreshView 的高度
         * @param targetCurrentOffset 下拉时 TargetView（ListView 或者 ScrollView 等）当前的位置。
         * @param targetInitOffset    TargetView（ListView 或者 ScrollView 等）的初始位置。
         * @param targetRefreshOffset 刷新时 TargetView（ListView 或者 ScrollView等）的位置。
         * @return RefreshView 当前的位置。
         */
        int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight,
                                   int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset);
    }

    public class DefaultRefreshOffsetCalculator implements PaternalLayout.RefreshOffsetCalculator {

        @Override
        public int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight, int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
            int refreshOffset;
            if (targetCurrentOffset >= targetRefreshOffset) {
                refreshOffset = refreshEndOffset;
            } else if (targetCurrentOffset <= targetInitOffset) {
                refreshOffset = refreshInitOffset;
            } else {
                float percent = (targetCurrentOffset - targetInitOffset) * 1.0f / (targetRefreshOffset - targetInitOffset);
                refreshOffset = (int) (refreshInitOffset + percent * (refreshEndOffset - refreshInitOffset));
            }
            return refreshOffset;
        }
    }

}