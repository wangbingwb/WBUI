package xyz.wbsite.wbsiteui.witget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class TipTextView extends android.support.v7.widget.AppCompatTextView {
    private long time = 3000;
    private String lastValue = "";
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time-=1000;
            if (time <= 0){
                TipTextView.this.setText("");
            }else {
                postDelayed(runnable,1000);
            }
        }
    };

    public TipTextView(Context context) {
        super(context);
    }

    public TipTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TipTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.time = 3000;
        postDelayed(runnable,1000);

        super.setText(text, type);
    }
}
