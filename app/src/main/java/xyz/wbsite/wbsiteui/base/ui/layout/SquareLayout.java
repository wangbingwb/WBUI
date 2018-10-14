package xyz.wbsite.wbsiteui.base.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareLayout extends RelativeLayout {

    private int weight_width = 1;
    private int weight_height = 1;

    public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(width * weight_height / weight_width, MeasureSpec.EXACTLY);
        } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            widthMeasureSpec = heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * weight_width / weight_height, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(width * weight_height / weight_width, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}