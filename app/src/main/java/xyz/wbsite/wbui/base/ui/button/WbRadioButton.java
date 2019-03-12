package xyz.wbsite.wbui.base.ui.button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;


/**
 * Created by bingwang on 2016/7/17.
 */
public class WbRadioButton extends RadioButton {

    public WbRadioButton(Context context) {
        super(context);
        initial();
    }

    public WbRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    /**
     * 构造自定义radio
     */
    public void initial() {
        float density = getResources().getDisplayMetrics().density;
        StateListDrawable stateListDrawable = new StateListDrawable();

        Bitmap unchecked = Bitmap.createBitmap((int) (30 * density), (int) (30 * density), Bitmap.Config.ARGB_8888);
        Bitmap checked = Bitmap.createBitmap((int) (30 * density), (int) (30 * density), Bitmap.Config.ARGB_8888);

        {//未选中状态
            Canvas canvas = new Canvas(unchecked);
            Paint paint = new Paint();
            paint.setColor(Color.DKGRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5 * density);
            canvas.drawRect(4.5f * density, 4.5f * density, 25.5f * density, 25.5f * density, paint);
        }

        {//选中状态
            Canvas canvas = new Canvas(checked);
            Paint paint = new Paint();
            paint.setColor(Color.DKGRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5 * density);
            canvas.drawRect(4.5f * density, 4.5f * density, 25.5f * density, 25.5f * density, paint);
            paint.setColor(Color.argb(255, 00, 94, 00));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(9 * density, 9 * density, 21 * density, 21 * density, paint);
        }

        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, new BitmapDrawable(getResources(), unchecked));
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, new BitmapDrawable(getResources(), checked));
        this.setButtonDrawable(stateListDrawable);
    }
}
