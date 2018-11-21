package xyz.wbsite.wbsiteui.base.ui.layout;

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
import android.widget.Scroller;

import xyz.wbsite.wbsiteui.base.utils.DensityUtil;

public class WBUIPaternalLayout extends ViewGroup implements NestedScrollingParent, AbsListView.OnScrollListener {
    private static final String TAG = "WBUIPaternalLayout";
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;

    private View mTargetView;
    private View mPullView;
    private View mPushView;

    private int mPullViewIndex = -1;
    private int mPushViewIndex = -1;
    private boolean mNestedScrollInProgress;
    private float mInitialDownY;
    private float mInitialDownX;
    private float mLastMotionY;
    private float mDragRate = 0.8f;
    private int mTheshold = 20;
    private Scroller mScroller;

    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    private IPullViewBuilder pullViewBuilder;
    private IPushViewBuilder pushViewBuilder;

    private int pullOffset = 0;
    private int pullHeight = DensityUtil.dp2px(getContext(), 100);
    private int pushOffset = 0;
    private int pushHeight = DensityUtil.dp2px(getContext(), 100);

    private boolean mIsPull;
    private boolean mIsPush;
    boolean isTop = false;
    boolean isBottom = false;

    int width = 0;
    int height = 0;

    int mTouchSlop = 4;

    @Override
    public void onViewAdded(View child) {
        int maxChild = 1 + (mPullView != null ? 1 : 0) + (mPushView != null ? 1 : 0);
        if (getChildCount() > maxChild) {
            throw new IllegalStateException("WBUIPaternalLayout can host only one child");
        }

        if (child instanceof AbsListView) {
            AbsListView listView = (AbsListView) child;
            listView.setOnScrollListener(this);
        }
        super.onViewAdded(child);
    }

    public WBUIPaternalLayout(Context context) {
        this(context, null);
    }

    public WBUIPaternalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        mScroller = new Scroller(getContext());
        mScroller.setFriction(ViewConfiguration.getScrollFriction());

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    public interface IPullViewBuilder {
        View createView();

        void onChange(View view, int currentHeight, int pullHeight, boolean isNearHeight);

        void onAction(View view, int pullHeight, WBUIPaternalLayout layout);

        void onFinish(View view);
    }

    public interface IPushViewBuilder {
        View createView();

        void onChange(View view, int currentHeight, int pushHeight, boolean isNearHeight);

        void onAction(View view, int pushHeight, WBUIPaternalLayout layout);

        void onFinish(View view);
    }

    public void setPullViewBuilder(IPullViewBuilder pullViewBuilder) {
        this.pullViewBuilder = pullViewBuilder;
        if (mPullView == null) {
            mPullView = pullViewBuilder.createView();
            addView(mPullView);
        }
    }

    public void setPushViewBuilder(IPushViewBuilder pushViewBuilder) {
        this.pushViewBuilder = pushViewBuilder;
        if (mPushView == null) {
            mPushView = pushViewBuilder.createView();
            addView(mPushView);
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mPullViewIndex < 0 || mPushViewIndex < 0) {
            return i;
        }
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

                if (Math.abs(dy) < mTouchSlop) {
                    return false;
                }

                if (mIsPull && pullViewBuilder != null && mPullView != null) {
                    pullOffset += dy;
                    if (pullOffset > pullHeight) {
                        pullOffset = pullHeight;
                    }
                    pullViewBuilder.onChange(mPullView, pullOffset, pullHeight, pullOffset >= pullHeight - mTheshold);
                    mPullView.setLayoutParams(new LayoutParams(width, pullOffset));
                    mPullView.layout(0, 0, width, pullOffset);
                    postInvalidate();
                }
                if (mIsPush && pushViewBuilder != null && mPushView != null) {
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
                    pushViewBuilder.onChange(mPushView, pushOffset, pushHeight, pushOffset >= pushHeight - mTheshold);
                    mPushView.setLayoutParams(new LayoutParams(width, pushOffset));
                    mPushView.layout(0, height - pushOffset, width, height);
                    postInvalidate();
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
                if (!view.equals(mPullView) && !view.equals(mPushView)) {
                    mTargetView = view;
                    break;
                }
            }
        }
    }

    public void reset() {
        mIsPull = mIsPush = false;

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (pullOffset >= pullHeight - mTheshold) {
            pullViewBuilder.onAction(mPullView, pullHeight, this);
        } else if (pullOffset > 0) {
            mScroller.startScroll(0, 0, 0, pullOffset, 800);
            postInvalidate();
        }

        if (pushOffset >= pushHeight - mTheshold) {
            pushViewBuilder.onAction(mPushView, pushHeight, this);
        } else if (pushOffset > 0) {
            mScroller.startScroll(0, 0, 0, pushOffset, 800);
            postInvalidate();
        }
    }

    public void finish() {
        if (pullOffset > 0) {
            pullViewBuilder.onFinish(mPullView);
            mScroller.startScroll(0, 0, 0, pullOffset, 800);
            postInvalidate();
        } else if (pushOffset > 0) {
            pushViewBuilder.onFinish(mPullView);
            mScroller.startScroll(0, 0, 0, pushOffset, 800);
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mIsPull || mIsPush) {
            return;
        }
        if (mScroller.computeScrollOffset()) {
            if (pullOffset > 0) {
                pullOffset = pullOffset - mScroller.getCurrY();
                if (pullOffset <= 0) {
                    pullOffset = 0;
                    mScroller.abortAnimation();
                }
                mPullView.setLayoutParams(new LayoutParams(width, pullOffset));
                mPullView.layout(0, 0, width, pullOffset);
                postInvalidate();
            } else if (pushOffset > 0) {
                pushOffset = pushOffset - mScroller.getCurrY();
                if (pushOffset <= 0) {
                    pushOffset = 0;
                    mScroller.abortAnimation();
                }
                mPushView.setLayoutParams(new LayoutParams(width, pushOffset));
                mPushView.layout(0, height - pushOffset, width, height);
                postInvalidate();
            } else {
                postInvalidate();
            }
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
            postInvalidate();
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(WBUIPaternalLayout parent, @Nullable View child);
    }
}