package xyz.wbsite.wbsiteui.base.ui.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;


public class AnimationView extends View {

    private int fullDegrees = 300;

    private float progress = 0;

    private int offset = 0;
    private int indicatorRadius;
    private int indicatorWidth;

    private int arrowWidth;

    private int shadowRadius;

    private int shadowWidth;

    private Animation rotateAnimation;

    public AnimationView(Context context) {
        super(context);
        init();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        indicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getContext().getResources().getDisplayMetrics());
        shadowWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getContext().getResources().getDisplayMetrics());
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        arrowWidth = indicatorWidth * 3;
    }

    private int getOffset() {
        int o = 0;
        if (getPaddingTop() > o) {
            o = getPaddingTop();
        }

        if (getPaddingRight() > o) {
            o = getPaddingRight();
        }

        if (getPaddingBottom() > o) {
            o = getPaddingBottom();
        }

        if (getPaddingLeft() > o) {
            o = getPaddingLeft();
        }

        if (getPaddingStart() > o) {
            o = getPaddingStart();
        }

        if (getPaddingEnd() > o) {
            o = getPaddingEnd();
        }

        if (o == 0) {
            o = shadowWidth + 10;
        }

        return o;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        {//阴影部分
            shadowRadius = Math.min(width, height) / 2;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(shadowWidth);
            RadialGradient radialGradient = new RadialGradient(
                    width / 2,
                    height / 2,
                    shadowRadius,
                    Color.parseColor("#888888"),
                    Color.TRANSPARENT,
                    Shader.TileMode.REPEAT);
            paint.setShader(radialGradient);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, shadowRadius - shadowWidth / 2 - 1, paint);
        }

        {//指示器部分
            offset = getOffset();
            indicatorRadius = shadowRadius - offset - shadowWidth;
            int currentDegress = (int) (fullDegrees * progress);
            int currentArrowWidth = (int) (arrowWidth * progress);

            Path path = new Path();
            path.moveTo(width / 2, height / 2 - indicatorRadius);
            RectF outer = new RectF(
                    width / 2 - indicatorRadius,
                    height / 2 - indicatorRadius,
                    width / 2 + indicatorRadius,
                    height / 2 + indicatorRadius
            );
            path.addArc(outer, -90, currentDegress);
            Paint paint1 = new Paint();
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setStrokeWidth(indicatorWidth);
            paint1.setAntiAlias(true);
            canvas.drawPath(path, paint1);

            Path arrowPath = new Path();
            arrowPath.moveTo(width / 2, height / 2 - indicatorRadius - currentArrowWidth / 2);
            arrowPath.lineTo(width / 2, height / 2 - indicatorRadius + currentArrowWidth / 2);
            arrowPath.lineTo(width / 2 + currentArrowWidth, height / 2 - indicatorRadius);
            arrowPath.close();
            paint1.setStyle(Paint.Style.FILL);
            canvas.rotate(currentDegress, width / 2, height / 2);
            canvas.drawPath(arrowPath, paint1);
        }
    }

    public void startAnimation() {
        progress = 1;
        this.startAnimation(rotateAnimation);
    }

    public void stopAnimation() {
        this.clearAnimation();
    }


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
