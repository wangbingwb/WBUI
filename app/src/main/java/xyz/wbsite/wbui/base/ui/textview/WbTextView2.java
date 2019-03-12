package xyz.wbsite.wbui.base.ui.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * TextView 自带右箭头
 */
public class WbTextView2 extends TextView {
    public WbTextView2(Context context) {
        super(context);
        init();
    }

    public WbTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        int drawableWidth = 20;
        int drawableColor = Color.parseColor("#aaaaaa");

        float density = getResources().getDisplayMetrics().density;
        Bitmap bitmap = Bitmap.createBitmap((int) (1 * drawableWidth * density), (int) (1 * drawableWidth * density), Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Path path = new Path();
        path.moveTo(0.775f * drawableWidth * density, 0.5f * drawableWidth * density);
        path.lineTo(0.4f * drawableWidth * density, 0.875f * drawableWidth * density);
        path.arcTo(new RectF(0.35f * drawableWidth * density, 0.725f * drawableWidth * density, 0.45f * drawableWidth * density, 0.875f * drawableWidth * density), 90, 90, false);
        path.lineTo(0.65f * drawableWidth * density, 0.5f * drawableWidth * density);
        path.lineTo(0.35f * drawableWidth * density, 0.2f * drawableWidth * density);
        path.arcTo(new RectF(0.35f * drawableWidth * density, 0.125f * drawableWidth * density, 0.45f * drawableWidth * density, 0.275f * drawableWidth * density), 180, 90, false);
        path.lineTo(0.775f * drawableWidth * density, 0.5f * drawableWidth * density);

        Paint paint = new Paint();
        paint.setColor(drawableColor);
        canvas.drawPath(path, paint);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setGravity(Gravity.RIGHT);
        this.setPadding(this.getPaddingLeft(), this.getPaddingTop(), (int) (drawableWidth * density + this.getPaddingRight()), this.getPaddingBottom());
        this.setBackground(bitmapDrawable);
    }
}
