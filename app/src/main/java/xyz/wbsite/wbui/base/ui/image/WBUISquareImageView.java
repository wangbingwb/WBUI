package xyz.wbsite.wbui.base.ui.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import xyz.wbsite.wbui.R;

public class WBUISquareImageView extends android.support.v7.widget.AppCompatImageView {

    private int weight_width = 1;
    private int weight_height = 1;

    public WBUISquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WBUISquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.uiSquare);
        weight_width = typedArray.getInt(R.styleable.uiSquare_weight_width,1);
        weight_height = typedArray.getInt(R.styleable.uiSquare_weight_height,1);
    }

    public WBUISquareImageView(Context context) {
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