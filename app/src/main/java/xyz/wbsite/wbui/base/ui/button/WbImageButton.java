package xyz.wbsite.wbui.base.ui.button;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class WbImageButton extends ImageButton {
    private int mSrcWitdh;
    private int mSrcHeight;
    private int mExactlyWidth;
    private int mEactlyHeight;

    public WbImageButton(Context context) {
        super(context);
    }

    public WbImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Drawable drawable = getDrawable();
        Rect bounds = drawable.getBounds();
        mSrcWitdh = bounds.width();
        mSrcHeight = bounds.height();

        System.out.println(mSrcWitdh);
        System.out.println(mSrcHeight);
        if (mSrcWitdh != 0 && mSrcHeight != 0) {
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
                //宽度确定，高度自适应
                mExactlyWidth = MeasureSpec.getSize(widthMeasureSpec);
                mEactlyHeight = mExactlyWidth * mSrcHeight / mSrcWitdh;
                setMeasuredDimension(mExactlyWidth, mEactlyHeight);
            } else if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                //高度确定，宽度自适应
                mEactlyHeight = MeasureSpec.getSize(heightMeasureSpec);
                mExactlyWidth = mEactlyHeight * mSrcWitdh / mSrcHeight;
                setMeasuredDimension(mExactlyWidth, mEactlyHeight);
            }
        }
    }
}
