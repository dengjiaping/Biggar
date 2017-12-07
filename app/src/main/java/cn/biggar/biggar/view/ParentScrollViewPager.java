package cn.biggar.biggar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ParentScrollViewPager extends ViewPager {

    public ParentScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentScrollViewPager(Context context) {
        super(context);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
