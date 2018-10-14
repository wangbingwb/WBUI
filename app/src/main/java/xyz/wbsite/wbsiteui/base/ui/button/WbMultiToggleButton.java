package xyz.wbsite.wbsiteui.base.ui.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 *
 */
public class WbMultiToggleButton extends LinearLayout implements View.OnClickListener {
    private Button b1;
    private Button b2;


    public WbMultiToggleButton(Context context) {
        super(context);
        init();
    }

    public WbMultiToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOrientation(HORIZONTAL);
        float density = this.getResources().getDisplayMetrics().density;
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;

        {
            // 边框
            ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{density * 5, density * 5, density * 5, density * 5, density * 5, density * 5, density * 5, density * 5}, null, null));
            shapeDrawable.getPaint().setColor(Color.parseColor("#aaaaaa"));
            this.setPadding((int) (density * 1), (int) (density * 1), (int) (density * 1), (int) (density * 1));
            this.setBackground(shapeDrawable);
        }

        {
            b1 = new Button(getContext());
            b1.setText("aa");
            b1.setTag("b1");
            b1.setOnClickListener(this);
            int[][] satate = {
                    {-android.R.attr.state_selected},
                    {android.R.attr.state_selected}
            };
            int[] colors = {Color.BLACK, Color.WHITE};
            ColorStateList colorStateList = new ColorStateList(satate, colors);
            b1.setTextColor(colorStateList);

            ShapeDrawable unselected = new ShapeDrawable(new RoundRectShape(new float[]{density * 4, density * 4, 0, 0, 0, 0, density * 4, density * 4}, null, null));
            unselected.getPaint().setColor(Color.WHITE);
            ShapeDrawable selected = new ShapeDrawable(new RoundRectShape(new float[]{density * 4, density * 4, 0, 0, 0, 0, density * 4, density * 4}, null, null));
            selected.getPaint().setColor(Color.parseColor("#66b3ff"));
            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(new int[]{-android.R.attr.state_selected}, unselected);
            listDrawable.addState(new int[]{android.R.attr.state_selected}, selected);
            b1.setBackground(listDrawable);
            this.addView(b1, layoutParams);
        }

        {
            b2 = new Button(getContext());
            b2.setText("bb");
            b2.setTag("b2");
            b2.setOnClickListener(this);
            int[][] satate = {
                    {-android.R.attr.state_selected},
                    {android.R.attr.state_selected}
            };
            int[] colors = {Color.BLACK, Color.WHITE};
            ColorStateList colorStateList = new ColorStateList(satate, colors);
            b2.setTextColor(colorStateList);
            ShapeDrawable unselected = new ShapeDrawable(new RoundRectShape(new float[]{0, 0, density * 4, density * 4, density * 4, density * 4, 0, 0}, null, null));
            unselected.getPaint().setColor(Color.WHITE);
            ShapeDrawable selected = new ShapeDrawable(new RoundRectShape(new float[]{0, 0, density * 4, density * 4, density * 4, density * 4, 0, 0}, null, null));
            selected.getPaint().setColor(Color.parseColor("#66b3ff"));
            StateListDrawable listDrawable = new StateListDrawable();
            listDrawable.addState(new int[]{-android.R.attr.state_selected}, unselected);
            listDrawable.addState(new int[]{android.R.attr.state_selected}, selected);
            b2.setBackground(listDrawable);
            this.addView(b2, layoutParams);
        }

        b1.setSelected(true);
        b2.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof String) {
            String t = (String) tag;
            switch (t) {
                case "b1":
                    b1.setSelected(true);
                    b2.setSelected(false);
                    break;
                case "b2":
                    b1.setSelected(false);
                    b2.setSelected(true);
                    break;
            }
        }
    }
}
