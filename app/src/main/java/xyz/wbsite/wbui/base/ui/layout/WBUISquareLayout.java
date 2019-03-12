package xyz.wbsite.wbui.base.ui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import xyz.wbsite.wbui.R;

public class WBUISquareLayout extends RelativeLayout {

    private int weight_width = 1;
    private int weight_height = 1;
    private int mWidth;
    private int mHeight;

    private View targetView;

    public WBUISquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WBUISquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.uiSquare);
        weight_width = typedArray.getInt(R.styleable.uiSquare_weight_width, weight_width);
        weight_height = typedArray.getInt(R.styleable.uiSquare_weight_height, weight_height);
    }

    public WBUISquareLayout(Context context) {
        super(context);
    }

    private void ensureView() {
        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("WBUISquareLayout can host only one child");
        } else if (childCount == 1) {
            targetView = getChildAt(0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
            mHeight = hSize;
        } else if (wMode == MeasureSpec.EXACTLY && hMode != MeasureSpec.EXACTLY) {
            mWidth = wSize;
            mHeight = wSize * weight_height / weight_width;
        } else if (wMode != MeasureSpec.EXACTLY && hMode == MeasureSpec.EXACTLY) {
            mWidth = hSize * weight_width / weight_height;
            mHeight = hSize;
        } else if (wMode != MeasureSpec.EXACTLY && hMode != MeasureSpec.EXACTLY) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            ensureView();
            if (targetView != null) {
                int measuredWidth = targetView.getMeasuredWidth();
                int measuredHeight = targetView.getMeasuredHeight();
                mWidth = mHeight = Math.max(measuredWidth, measuredHeight);
            }
        }
        measureChildren(MeasureSpec.makeMeasureSpec(mWidth,MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(mHeight,MeasureSpec.AT_MOST));
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureView();
        if (targetView != null) {
            int measuredWidth = targetView.getMeasuredWidth();
            int measuredHeight = targetView.getMeasuredHeight();
            if (measuredWidth > measuredHeight) {
                int dy = measuredWidth - measuredHeight;
                targetView.layout(0, dy / 2, measuredWidth, measuredHeight + dy / 2);
            } else {
                int dx = measuredHeight - measuredWidth;
                targetView.layout(dx / 2, 0, measuredWidth + dx / 2, measuredHeight);
            }
        }
    }
}