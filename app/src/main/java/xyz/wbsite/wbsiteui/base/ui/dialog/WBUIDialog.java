package xyz.wbsite.wbsiteui.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

public class WBUIDialog extends Dialog {
    public WBUIDialog(@NonNull Context context) {
        super(context);
    }

    public void show() {
        super.show();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContentView
                .getLayoutParams();
        layoutParams.width = display.getWidth();
        layoutParams.height = display.getHeight();
        mContentView.setLayoutParams(layoutParams);

    }
}
