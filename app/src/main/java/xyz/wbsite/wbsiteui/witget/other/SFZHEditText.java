/**
 *
 */
package xyz.wbsite.wbsiteui.witget.other;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import xyz.wbsite.wbsiteui.R;

public class SFZHEditText extends android.support.v7.widget.AppCompatEditText implements OnKeyboardActionListener {

    private KeyboardView mKeyboardView;
    private Keyboard mKeyboard;

    private Window mWindow;
    private View mDecorView;
    private View mContentView;

    private PopupWindow mKeyboardWindow;

    private int scrolldis = 0;    //输入框在键盘被弹出时，要被推上去的距离

    public static int screenw = -1;//未知宽高
    public static int screenh = -1;
    public static int screenh_nonavbar = -1;    //不包含导航栏的高度
    public static int real_scontenth = -1;    //实际内容高度，  计算公式:屏幕高度-导航栏高度-电量栏高度

    public static float density = 1.0f;
    public static int densityDpi = 160;

    public SFZHEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context);
        initKeyboard(context);
    }

    public SFZHEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(context);
        initKeyboard(context);
    }

    private void initKeyboard(Context context) {
        mKeyboard = new Keyboard(context, R.xml.sfzh_keyboard);
        mKeyboardView = new WBUIKeyboardView(getContext(), null);
        mKeyboardView.setFocusable(true);
        mKeyboardView.setFocusableInTouchMode(true);
        mKeyboardView.setBackgroundColor(Color.WHITE);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(this);

        mKeyboardWindow = new PopupWindow(mKeyboardView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mKeyboardWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                if (scrolldis > 0) {
                    int temp = scrolldis;
                    scrolldis = 0;
                    if (null != mContentView) {
                        mContentView.scrollBy(0, -temp);
                    }
                }
            }
        });

    }

    private void showKeyboard() {
        if (null != mKeyboardWindow) {
            if (!mKeyboardWindow.isShowing()) {
                mKeyboardView.setKeyboard(mKeyboard);

                mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM, 0, 0);
                mKeyboardWindow.update();

                if (null != mDecorView && null != mContentView) {
                    int[] pos = new int[2];
                    getLocationOnScreen(pos);
                    float height = dpToPx(getContext(), 240);

                    Rect outRect = new Rect();
                    mDecorView.getWindowVisibleDisplayFrame(outRect);

                    int screen = real_scontenth;
                    scrolldis = (int) ((pos[1] + getMeasuredHeight() - outRect.top) - (screen - height));

                    if (scrolldis > 0) {
                        mContentView.scrollBy(0, scrolldis);
                    }
                }

            }
        }
    }

    private void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    private void hideSysInput() {
        if (this.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        requestFocus();
        requestFocusFromTouch();

        hideSysInput();
        showKeyboard();
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mKeyboardWindow) {
                if (mKeyboardWindow.isShowing()) {
                    mKeyboardWindow.dismiss();
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);

    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.mWindow = ((Activity) getContext()).getWindow();
        this.mDecorView = this.mWindow.getDecorView();
        this.mContentView = this.mWindow.findViewById(Window.ID_ANDROID_CONTENT);

        hideSysInput();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        hideKeyboard();

        mKeyboardWindow = null;
        mKeyboardView = null;
        mKeyboard = null;

        mDecorView = null;
        mContentView = null;
        mWindow = null;
    }

    @Override
    public void onPress(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRelease(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = this.getText();
        int start = this.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 隐藏键盘
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DONE) {// 回退
            hideKeyboard();
            this.setOnClickListener(null);
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else if (0x0 <= primaryCode && primaryCode <= 0x7f) {
            //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
            editable.insert(start, Character.toString((char) primaryCode));
        } else if (primaryCode > 0x7f) {
            Key mkey = getKeyByKeyCode(primaryCode);
            //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
            editable.insert(start, mkey.label);

        } else {
            //其他一些暂未开放的键指令，如next到下一个输入框等指令
        }
    }

    @Override
    public void onText(CharSequence text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeLeft() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeRight() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeDown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeUp() {
        // TODO Auto-generated method stub

    }

    private Key getKeyByKeyCode(int keyCode) {
        if (null != mKeyboard) {
            List<Key> mKeys = mKeyboard.getKeys();
            for (int i = 0, size = mKeys.size(); i < size; i++) {
                Key mKey = mKeys.get(i);

                int codes[] = mKey.codes;

                if (codes[0] == keyCode) {
                    return mKey;
                }
            }
        }

        return null;
    }

    private void initAttributes(Context context) {
        initScreenParams(context);
        this.setLongClickable(false);
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        removeCopyAbility();

        if (this.getText() != null) {
            this.setSelection(this.getText().length());
        }

        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });


    }

    @TargetApi(11)
    private void removeCopyAbility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
        }
    }

    private boolean isNumber(String str) {
        String wordstr = "0123456789";
        if (wordstr.indexOf(str) > -1) {
            return true;
        }
        return false;
    }

    class KeyModel {

        private Integer code;
        private String label;

        public KeyModel(Integer code, String lable) {
            this.code = code;
            this.label = lable;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getLable() {
            return label;
        }

        public void setLabel(String lable) {
            this.label = lable;
        }


    }

    /**
     * 密度转换为像素值
     *
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private void initScreenParams(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dMetrics);

        screenw = dMetrics.widthPixels;
        screenh = dMetrics.heightPixels;
        density = dMetrics.density;
        densityDpi = dMetrics.densityDpi;

        screenh_nonavbar = screenh;

        int ver = Build.VERSION.SDK_INT;

        // 新版本的android 系统有导航栏，造成无法正确获取高度
        if (ver == 13) {
            try {
                Method mt = display.getClass().getMethod("getRealHeight");
                screenh_nonavbar = (Integer) mt.invoke(display);
            } catch (Exception e) {
            }
        } else if (ver > 13) {
            try {
                Method mt = display.getClass().getMethod("getRawHeight");
                screenh_nonavbar = (Integer) mt.invoke(display);
            } catch (Exception e) {
            }
        }

        real_scontenth = screenh_nonavbar - getStatusBarHeight(context);


    }

    /**
     * 电量栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0,
                sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return sbar;
    }


}
