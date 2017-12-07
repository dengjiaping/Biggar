package cn.biggar.biggar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import per.sue.gear2.widget.HorizontalListView;

/**
 * Created by mx on 2016/8/18.
 *  父容器 不会拦截 触摸事件的 横行listview
 */
public class MyHorizontalListView extends HorizontalListView{
    public MyHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
