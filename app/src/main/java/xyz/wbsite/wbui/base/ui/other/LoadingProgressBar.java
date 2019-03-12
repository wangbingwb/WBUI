package xyz.wbsite.wbui.base.ui.other;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import xyz.wbsite.wbui.base.utils.DensityUtil;

public class LoadingProgressBar extends View {
    private ValueAnimator mAnimator;

    private Paint mShadowPaint = new Paint();
    private Paint mPaint = new Paint();
    private int shadowWidth = DensityUtil.dp2px(getContext(), 4);
    private int backgroundWidth = DensityUtil.dp2px(getContext(), 4);
    private int indicatorWidth = DensityUtil.dp2px(getContext(), 3);

    private int startAngle = -90;
    private int sweepAngle = 100;
    private int mDuration = 1000;

    private int defaultSize = DensityUtil.dp2px(getContext(), 50);

    private boolean isAnimating = false;

    public LoadingProgressBar(Context context) {
        super(context);
    }

    public LoadingProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultSize, MeasureSpec.getSize(heightMeasureSpec));
            return;
        } else if (hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), defaultSize);
            return;
        } else if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultSize, defaultSize);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        if (radius >= shadowWidth + backgroundWidth) {
            RadialGradient radialGradient = new RadialGradient(width / 2, height / 2, radius, new int[]{Color.GRAY, Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(radialGradient);
            canvas.drawCircle(width / 2, height / 2, radius, mShadowPaint);

            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            canvas.drawCircle(width / 2, height / 2, radius - shadowWidth, mPaint);
        }


        if (radius >= shadowWidth + backgroundWidth + indicatorWidth) {
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(indicatorWidth);

            RectF rectF = new RectF(
                    width / 2 - radius + shadowWidth + backgroundWidth + indicatorWidth / 2,
                    height / 2 - radius + shadowWidth + backgroundWidth + indicatorWidth / 2,
                    width / 2 + radius - shadowWidth - backgroundWidth - indicatorWidth / 2,
                    height / 2 + radius - shadowWidth - backgroundWidth - indicatorWidth / 2);
            canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);
        }
    }

    public void setProgress(float process) {
        startAngle = (int) (process * 360 - 90);
        sweepAngle = (int) (Math.abs(startAngle + 90 - 180) * 1.0f / 180 * 120 + 45);
        invalidate();
    }

    public void startAnimation() {
        mAnimator = ValueAnimator.ofInt(-90, 270);
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (int) animation.getAnimatedValue();
                sweepAngle = (int) (Math.abs(startAngle + 90 - 180) * 1.0f / 180 * 120 + 45);
                invalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimator.start();
    }

    public void stopAnimation() {
        if (isAnimating) {
            mAnimator.cancel();
            startAngle = -90;
            sweepAngle = 100;
        }
    }
}
