package xyz.wbsite.wbsiteui.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import xyz.wbsite.wbsiteui.base.BaseActivity;

public abstract class BaseFragment extends Fragment {

    protected abstract void init();
    protected BaseActivity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity = (BaseActivity)getActivity();
        inject();
        init();
    }

    /**
     * 注入绑定注解
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface Bind {
        int value() default -1;
    }

    /**
     * 自动注入解析
     */
    private void inject() {
        Class Class = this.getClass();
        Field[] fields = Class.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(BaseFragment.Bind.class)) {
                BaseFragment.Bind bind = f.getAnnotation(BaseFragment.Bind.class);
                int id = bind.value();
                if (id > 0) {
                    f.setAccessible(true);
                    try {
                        f.set(this, this.getView().findViewById(id));
                    } catch (IllegalAccessException e) {
                        Log.e("Bind", f.getName() + "注入失败!");
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(false);
                    }
                }
            }
        }
    }
}
