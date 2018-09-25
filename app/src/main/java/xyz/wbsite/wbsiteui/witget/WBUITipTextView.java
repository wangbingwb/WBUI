package xyz.wbsite.wbsiteui.witget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class WBUITipTextView extends android.support.v7.widget.AppCompatTextView {
    private long time = 3000;
    private String lastValue = "";
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time-=1000;
            if (time <= 0){
                WBUITipTextView.this.setText("");
            }else {
                postDelayed(runnable,1000);
            }
        }
    };

    public WBUITipTextView(Context context) {
        super(context);
    }

    public WBUITipTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WBUITipTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.time = 3000;
        postDelayed(runnable,1000);

        super.setText(text, type);
    }
}
