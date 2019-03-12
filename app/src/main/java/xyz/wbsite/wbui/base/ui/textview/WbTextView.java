package xyz.wbsite.wbui.base.ui.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wangbing on 16/6/2.
 */
public class WbTextView extends TextView {
    private int mOffsetY = 0;
    public Bitmap lastBitmap;

    public WbTextView(Context context) {
        super(context);
    }

    public WbTextView(Context context, AttributeSet attrs) {
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
