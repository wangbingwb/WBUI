package xyz.wbsite.wbsiteui.base.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
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

        Paint localPaint; //设置画笔
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.FILL); //画笔实心

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            localPaint.setShader(new LinearGradient(childAt.getLeft(), childAt.getBottom(), childAt.getRight(), childAt.getBottom(), new int[]{
                    getResources().getColor(R.color.colorGray_09), getResources().getColor(R.color.colorGray_09), getResources().getColor(R.color.colorGray_09)}, null, Shader.TileMode.REPEAT));
            canvas.drawLine(childAt.getLeft(), childAt.getBottom() - 1, childAt.getRight(), childAt.getBottom() - 1, localPaint);

            if ((i + 1) % cols != 0) {
                localPaint.setShader(new LinearGradient(childAt.getRight(), childAt.getTop(), childAt.getRight(), childAt.getBottom(), new int[]{
                        getResources().getColor(R.color.colorGray_09), getResources().getColor(R.color.colorGray_09), getResources().getColor(R.color.colorGray_09)}, null, Shader.TileMode.MIRROR));
                canvas.drawLine(childAt.getRight() - 1, childAt.getTop(), childAt.getRight() - 1, childAt.getBottom(), localPaint);
            }
        }
    }
}