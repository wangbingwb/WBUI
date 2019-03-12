package xyz.wbsite.wbui.base.ui.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.Button;

public class WbButton extends Button {
    private int mColor;
    private float mScale;

    public WbButton(Context context) {
        super(context);
        init();
    }

    public WbButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScale = getResources().getDisplayMetrics().density;
        ColorStateList textColors = this.getTextColors();
        mColor = textColors.getDefaultColor();

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.setEnterFadeDuration(200);
        stateListDrawable.setExitFadeDuration(200);
        int radius = (int) (5 * mScale);
        int width = (int) (1 * mScale);

        //不可按时的drawable
        {
            RoundRectShape roundRectShape = new RoundRectShape(
                    new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                    new RectF(width, width, width, width),
                    new float[]{radius - width, radius - width, radius - width, radius - width, radius - width, radius - width, radius - width, radius - width}
            );
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(Color.parseColor("#aaaaaa"));
            stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, shapeDrawable);
        }
        //正常的drawable
        {
            RoundRectShape roundRectShape = new RoundRectShape(
                    new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                    new RectF(width, width, width, width),
                    new float[]{radius - width, radius - width, radius - width, radius - width, radius - width, radius - width, radius - width, radius - width}
            );
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(mColor);

            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, shapeDrawable);
        }
        //按下去的drawable
        {
            RoundRectShape roundRectShape = new RoundRectShape(
                    new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                    null,
                    null
            );
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(mColor);
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, shapeDrawable);
        }
        this.setBackground(stateListDrawable);

        //各状态对应的字体颜色
        int[][] satate = {
                {-android.R.attr.state_enabled},
                {-android.R.attr.state_pressed},
                {android.R.attr.state_pressed}
        };
        int[] colors = {Color.parseColor("#aaaaaa"), mColor, Color.WHITE,};

        ColorStateList colorStateList = new ColorStateList(satate, colors);
        this.setTextColor(colorStateList);
    }
}
