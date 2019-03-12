package xyz.wbsite.wbui.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * Created by bingwang on 2016/5/17.
 */
public class WbConfirmDialog extends Dialog {
    LinearLayout mLinearLayout;
    private float demsity = 1;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private OnPostiveListener mOnPostiveListener = null;
    private OnNegativeListener mOnNegativeListener = null;

    public WbConfirmDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setDimAmount(0.2f);
        this.getWindow().setWindowAnimations(-1);
        this.demsity = context.getResources().getDisplayMetrics().density;

        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setPadding((int) (5 * demsity), 0, (int) (5 * demsity), (int) (5 * demsity));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        this.setContentView(mLinearLayout, lp);

        {
            mNegativeButton = new Button(context);
            mNegativeButton.setText("取消");
            mNegativeButton.setGravity(Gravity.CENTER);
            mNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNegative();
                }
            });

            float[] out = {5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity};
            RoundRectShape roundRectShape = new RoundRectShape(out, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(Color.argb(255, 255, 255, 255));

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, shapeDrawable);

            ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape);
            shapeDrawable1.getPaint().setColor(Color.argb(255, 240, 240, 240));
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, shapeDrawable1);

            mNegativeButton.setBackground(stateListDrawable);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (45 * demsity));
            layoutParams.setMargins(0, (int) (5 * demsity), 0, 0);
            mLinearLayout.addView(mNegativeButton, layoutParams);
        }


        {
            mPositiveButton = new Button(context);
            mPositiveButton.setText("确认");
            mPositiveButton.setGravity(Gravity.CENTER);
            float[] out = {5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity, 5 * demsity};
            RoundRectShape roundRectShape = new RoundRectShape(out, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(Color.argb(255, 255, 255, 255));

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, shapeDrawable);

            ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape);
            shapeDrawable1.getPaint().setColor(Color.argb(255, 240, 240, 240));
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, shapeDrawable1);
            mPositiveButton.setBackground(stateListDrawable);

            mPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPositive();
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (45 * demsity));
            mLinearLayout.addView(mPositiveButton, 0, layoutParams);
        }

    }

    @Override
    public void show() {
        super.show();
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(200);

        mLinearLayout.startAnimation(translateAnimation);
    }

    public void onPositive() {
        dismiss();
        if (mOnPostiveListener != null) {
            mOnPostiveListener.onPostive();
        }
    }

    public void onNegative() {
        dismiss();
        if (mOnNegativeListener != null) {
            mOnNegativeListener.onNegative();
        }
    }

    public OnNegativeListener getOnNegativeListener() {
        return mOnNegativeListener;
    }

    public void setOnNegativeListener(OnNegativeListener mOnNegativeListener) {
        this.mOnNegativeListener = mOnNegativeListener;
    }

    public OnPostiveListener getOnPostiveListener() {
        return mOnPostiveListener;
    }

    public void setOnPostiveListener(OnPostiveListener mOnPostiveListener) {
        this.mOnPostiveListener = mOnPostiveListener;
    }

    public interface OnPostiveListener {
        void onPostive();
    }

    public interface OnNegativeListener {
        void onNegative();
    }

}
