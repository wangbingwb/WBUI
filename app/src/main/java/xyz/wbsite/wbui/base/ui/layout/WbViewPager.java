package xyz.wbsite.wbui.base.ui.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by bingwang on 2016/5/16.
 */
public class WbViewPager extends RelativeLayout {

    private LinearLayout mIndexView;
    private ViewPager mViewPager;

    public WbViewPager(Context context) {
        super(context);
    }

    public WbViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewPager = new ViewPager(context);
        this.addView(mViewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mIndexView = new LinearLayout(context);
        mIndexView.setGravity(Gravity.CENTER);
        mIndexView.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, 0, 0, 10);
        this.addView(mIndexView, layoutParams);
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);

        for (int i = 0; i < adapter.getCount(); i++) {
            Circle circle = new Circle(getContext());
            if (i == 0) {
                circle.setColor(Color.argb(255, 100, 100, 100));
            }
            mIndexView.addView(circle, new ViewGroup.LayoutParams(40, 20));
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mIndexView.getChildCount(); i++) {
                    Circle circle = (Circle) mIndexView.getChildAt(i);
                    circle.setColor(Color.argb(255, 200, 200, 200));
                    circle.invalidate();
                }
                Circle circle = (Circle) mIndexView.getChildAt(position);
                circle.setColor(Color.argb(255, 100, 100, 100));
                circle.invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class Circle extends View {
        private int mColor = Color.argb(255, 200, 200, 200);
        private Paint mPaint = new Paint();

        public Circle(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            mPaint.setColor(mColor);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2), mPaint);
        }

        public int getColor() {
            return mColor;
        }

        public void setColor(int mColor) {
            this.mColor = mColor;
        }
    }
}