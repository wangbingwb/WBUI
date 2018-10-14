package xyz.wbsite.wbsiteui.base.ui.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

public class WbEditText extends EditText {
    private float density;
    private Paint paint;

    public WbEditText(Context context) {
        super(context);
        init();
    }

    public WbEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;

        StateListDrawable stateListDrawable = new StateListDrawable();

        {
            Bitmap bitmap = Bitmap.createBitmap((int) (density * 20), (int) (density * 20), Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            paint = new Paint();
            canvas.drawCircle(density * 20 / 2, density * 20 / 2, density * 20 / 2, paint);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            bitmapDrawable.setBounds((int) (density * 20), (int) (density * 20), (int) (density * 20), (int) (density * 20));
            bitmapDrawable.setGravity(Gravity.RIGHT);

            stateListDrawable.addState(new int[]{android.R.attr.state_focused}, bitmapDrawable);
        }


        this.setBackground(stateListDrawable);
        this.setPadding(20, 20, 50, 20);
        this.setMinimumWidth(200);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int width = getWidth();
        int height = getHeight();

        if (x > (width - density * 20) && y > height / 2 - density * 10 && y < (height / 2 + density * 10)) {
            paint.setColor(Color.BLUE);
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
