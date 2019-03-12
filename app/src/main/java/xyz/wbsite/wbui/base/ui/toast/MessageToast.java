package xyz.wbsite.wbui.base.ui.toast;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageToast extends Toast {
    private Context mContext;
    private FrameLayout mRootView;
    private String message;

    private MessageToast(Context context) {
        super(context);
    }

    public MessageToast(Context context, String message) {
        super(context);
        this.mContext = context;
        this.message = message;
        this.mRootView = new FrameLayout(context);
        this.setView(mRootView);
    }

    @Override
    public void show() {
        //加入控件
        TextView tvMessage = new TextView(mContext);
        tvMessage.setText(message);
        tvMessage.setGravity(Gravity.CENTER);
        tvMessage.setBackgroundColor(Color.argb(100, 100, 100, 100));
        mRootView.addView(tvMessage);

        //高度
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        tvMessage.setHeight((int) (45 * metrics.density));

        //动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 45 * metrics.density, 0.0f);
        translateAnimation.setDuration(400);
        tvMessage.startAnimation(translateAnimation);

        //位置
        this.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);

        super.show();
    }
}
