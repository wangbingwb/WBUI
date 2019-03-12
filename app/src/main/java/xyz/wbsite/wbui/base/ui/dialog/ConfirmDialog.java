package xyz.wbsite.wbui.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfirmDialog extends Dialog {
    private LinearLayout mLinearLayout;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ConfirmDialog(Context context, String message) {
        super(context);
        //px与dp比例
        float mScale = getContext().getResources().getDisplayMetrics().density;

        //设置基本参数
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
//        this.getWindow().setDimAmount(0);
        this.setCanceledOnTouchOutside(false);

        //父容器
        {
            mLinearLayout = new LinearLayout(getContext());
            mLinearLayout.setGravity(Gravity.CENTER);
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.setPadding(10, 10, 10, 10);
            mLinearLayout.setBackground(new ColorDrawable(Color.WHITE));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (mScale * 50));
            this.setContentView(mLinearLayout, layoutParams);
        }


        //消息显示框
        TextView mTextView = new TextView(context);
        mTextView.setText(message);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setMinWidth((int) (mScale * 150));
        mLinearLayout.addView(mTextView);

        {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (mScale * 50));

            Button button = new Button(getContext());
            button.setText("确认");
            button.setTextColor(Color.parseColor("#7AECC4"));
            button.setBackground(null);
            button.setGravity(Gravity.RIGHT);
            mLinearLayout.addView(button, layoutParams);
        }

    }

}
