package xyz.wbsite.wbsiteui.base.witget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class WBUITextView extends android.support.v7.widget.AppCompatTextView {
    private int mOffsetY = 0;
    public Bitmap lastBitmap;

    public WBUITextView(Context context) {
        super(context);
    }

    public WBUITextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (lastBitmap != null) {
            canvas.drawBitmap(lastBitmap, 0, mOffsetY - getHeight(), null);
            canvas.translate(0, mOffsetY);
        }
        super.onDraw(canvas);

        if (mOffsetY == 0) {
            lastBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            super.onDraw(new Canvas(lastBitmap));
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOffsetY = getHeight();
        post(new Runnable() {
            @Override
            public void run() {
                if (mOffsetY > 0) {
                    mOffsetY -= 5;
                    if (mOffsetY < 0) mOffsetY = 0;
                    postDelayed(this, 10);
                } else {
                    mOffsetY = 0;
                }
                System.out.println(mOffsetY);
                invalidate();
            }
        });
        super.setText(text, type);
    }
}
