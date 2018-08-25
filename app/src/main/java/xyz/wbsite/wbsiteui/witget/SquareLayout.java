package xyz.wbsite.wbsiteui.witget;

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

        int expectWidth = height * weight_width / weight_height;
        int expectHeght = width * weight_height / weight_width;

        if (expectWidth == 0){
            expectWidth = expectHeght;
        }else if (expectHeght == 0){
            expectHeght = expectWidth;
        }else {
            if ((double) expectWidth / width <= (double) expectHeght / height) {
                expectHeght = expectWidth * weight_height / weight_width;
            } else {
                expectWidth = expectHeght * weight_width / weight_height;
            }
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(expectWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(expectHeght, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}