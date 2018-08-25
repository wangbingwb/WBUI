package xyz.wbsite.wbsiteui.witget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import xyz.wbsite.wbsiteui.R;

public class WBUIGridView extends GridView {
    public WBUIGridView(Context context) {
        super(context);
    }

    public WBUIGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WBUIGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cols = getNumColumns(); //获取列数
        int rows = getChildCount() / cols + getChildCount() % cols > 0 ? 1 : 0;  //获取行数

        Paint localPaint; //设置画笔
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
        localPaint.setColor(getContext().getResources().getColor(R.color.colorDividerLine5));//画笔颜色

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);

            canvas.drawLine(childAt.getLeft(), childAt.getBottom(), childAt.getRight(), childAt.getBottom(), localPaint);

            if ((i + 1) % cols != 0) {
                canvas.drawLine(childAt.getRight(), childAt.getTop(), childAt.getRight(), childAt.getBottom(), localPaint);
            }
        }
    }
}