package xyz.wbsite.wbui.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wangbing on 2016/07/01.
 */
public class MessageDialog extends Dialog {
    private LinearLayout mLinearLayout;

    private MessageDialog(Context context) {
        super(context);
    }

    public MessageDialog(Context context, String message) {
        super(context);
        //px与dp比例
        float mScale = getContext().getResources().getDisplayMetrics().density;

        //设置基本参数
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        this.getWindow().setDimAmount(0);
        this.setCanceledOnTouchOutside(false);

        //父容器
        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setPadding(10, 10, 10, 10);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(new RoundRectShape(new float[]{8, 8, 8, 8, 8, 8, 8, 8}, null, null));
        shapeDrawable.getPaint().setColor(Color.argb(180, 20, 20, 20));
        mLinearLayout.setBackground(shapeDrawable);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (mScale * 50));
        this.setContentView(mLinearLayout, layoutParams);

//        LoadingView1 loadingView1 = new LoadingView1(getContext());
//        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int)(mScale*35),(int)(mScale*35));
//        mLinearLayout.addView(loadingView1, layoutParams1);

        //消息显示框
        TextView mTextView = new TextView(context);
        mTextView.setText(message);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setMinWidth((int) (mScale * 150));
        mLinearLayout.addView(mTextView);
    }

    @Override
    public void show() {
        super.show();

        //启动异步任务，1秒之后自动关闭
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    doAnimation();
                }
            }
        }.execute();
    }

    public void doAnimation() {
        //启动消失动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLinearLayout.startAnimation(scaleAnimation);
    }
}
