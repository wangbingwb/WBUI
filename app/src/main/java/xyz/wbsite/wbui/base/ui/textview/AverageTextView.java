package xyz.wbsite.wbui.base.ui.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

public class AverageTextView extends android.support.v7.widget.AppCompatTextView {

    public AverageTextView(Context context) {
        super(context);
    }

    public AverageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getCurrentTextColor());

        String mText = getText().toString();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int x_offset = getPaddingLeft();
        int y_offset = getPaddingTop();

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float offset = (Math.abs(fontMetrics.top) - fontMetrics.bottom) / 2;

        if (mText.length() >= 2) {
            String firstF = String.valueOf(mText.charAt(0));
            String lastF = String.valueOf(mText.charAt(mText.length() - 1));

            float firstF_W = paint.measureText(firstF);
            float lastF_W = paint.measureText(lastF);

            {// 绘制首位字符
                canvas.drawText(firstF, firstF_W / 2 + x_offset, height / 2 + offset + y_offset, paint);
                canvas.drawText(lastF, width - lastF_W / 2 + x_offset, height / 2 + offset + y_offset, paint);
            }

            // 如果大于2个才均分
            if (mText.length() > 2) {
                float remainingWidth = width - firstF_W / 2 - lastF_W / 2;//剩余宽度
                float averageW = remainingWidth / (mText.length() - 2 + 1);//等分距离

                for (int i = 1; i < mText.length() - 1; i++) {//循环绘文字
                    String f = String.valueOf(mText.charAt(i));
                    canvas.drawText(f, firstF_W/2 + i * averageW + x_offset, height / 2 + offset + y_offset, paint);
                }
            }
        }
    }
}
