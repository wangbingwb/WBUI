package xyz.wbsite.wbsiteui.base.utils;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

    public static void shake(View view, int counts, Animation.AnimationListener listener) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        translateAnimation.setAnimationListener(listener);
        view.startAnimation(translateAnimation);
    }

    public static void shake(View view, int counts) {
        shake(view, counts, null);
    }

    public static void toBottom(final View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void toBottom(View view, long duration) {
        toBottom(view, duration, null);
    }

    public static void toTop(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void toTop(View view, long duration) {
        toTop(view, duration, null);
    }

    public static void toLeft(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void toLeft(View view, long duration) {
        toLeft(view, duration, null);
    }

    public static void toRight(final View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void toRight(final View view, long duration) {
        toRight(view, duration, null);
    }

    public static void fromBottom(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void fromBottom(View view, long duration) {
        fromBottom(view, duration, null);
    }

    public static void fromTop(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void fromTop(View view, long duration) {
        fromTop(view, duration, null);
    }

    public static void fromLeft(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void fromLeft(View view, long duration) {
        fromLeft(view, duration, null);
    }

    public static void fromRight(View view, long duration, Animation.AnimationListener listener) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(listener);
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public static void fromRight(View view, long duration) {
        fromRight(view, duration, null);
    }
}
