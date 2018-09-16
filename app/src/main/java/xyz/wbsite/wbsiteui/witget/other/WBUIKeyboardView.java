package xyz.wbsite.wbsiteui.witget.other;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import xyz.wbsite.wbsiteui.R;

public class WBUIKeyboardView extends KeyboardView {

    private Drawable mKeybgDrawable;
    private Resources res;

    public WBUIKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResources(context);
    }

    public WBUIKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResources(context);
    }

    private void initResources(Context context) {
        res = context.getResources();
        mKeybgDrawable = res.getDrawable(R.drawable.ui_keyboard_key);
    }

    @Override
    public void onDraw(Canvas canvas) {
        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            canvas.save();

            int offsety = 0;
            if (key.y == 0) {
                offsety = 1;
            }

            int initdrawy = key.y + offsety;

            Rect rect = new Rect(key.x, initdrawy, key.x + key.width, key.y
                    + key.height);

            canvas.clipRect(rect);

            int primaryCode = -1;
            if (null != key.codes && key.codes.length != 0) {
                primaryCode = key.codes[0];
            }

            Drawable dr = mKeybgDrawable;

            if (null != dr) {
                int[] state = key.getCurrentDrawableState();

                dr.setState(state);
                dr.setBounds(rect);
                dr.draw(canvas);
            }

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(50);
            paint.setColor(Color.BLACK);

            if (key.label != null) {
                canvas.drawText(
                        key.label.toString(),
                        key.x + (key.width / 2),
                        initdrawy + (key.height + paint.getTextSize() - paint.descent()) / 2,
                        paint);
            } else if (key.icon != null) {
                int intriWidth = key.icon.getIntrinsicWidth();
                int intriHeight = key.icon.getIntrinsicHeight();

                final int drawableX = key.x + (key.width - intriWidth) / 2;
                final int drawableY = initdrawy + (key.height - intriHeight) / 2;

                key.icon.setBounds(
                        drawableX, drawableY, drawableX + intriWidth,
                        drawableY + intriHeight);

                key.icon.draw(canvas);
            }

            canvas.restore();
        }
    }

}
