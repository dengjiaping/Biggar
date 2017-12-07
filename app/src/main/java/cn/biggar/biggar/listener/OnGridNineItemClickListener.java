package cn.biggar.biggar.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chenwy on 2017/11/9.
 */

public abstract class OnGridNineItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;

    public abstract void onItemClick(View view, int position);

    public abstract void onItemLongClick(View view, int position);

    public OnGridNineItemClickListener(final RecyclerView recyclerView) {
        mGestureDetector = new GestureDetector(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() { //这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                    //单击事件
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            onItemClick(childView, recyclerView.getChildLayoutPosition(childView));
                        } else {
                            onItemClick(null, -1);
                        }
                        return true;
                    }

                    //长按事件
                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            onItemLongClick(childView, recyclerView.getChildLayoutPosition(childView));
                        } else {
                            onItemLongClick(null, -1);
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //把事件交给GestureDetector处理
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
