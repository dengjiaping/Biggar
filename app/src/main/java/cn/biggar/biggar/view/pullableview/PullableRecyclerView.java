package cn.biggar.biggar.view.pullableview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class PullableRecyclerView extends RecyclerView implements Pullable {

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        LayoutManager layoutManager = getLayoutManager();
        int firstItemPosition=-1;
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager1 = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
//            int lastItemPosition = linearManager1.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            firstItemPosition = linearManager1.findFirstVisibleItemPosition();
        }else if (layoutManager instanceof GridLayoutManager){
            GridLayoutManager linearManager2 = (GridLayoutManager) layoutManager;
            firstItemPosition=linearManager2.findFirstVisibleItemPosition();
        }

        if (getChildCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (firstItemPosition==0&&getChildAt(0).getTop() >= 0) {
            // 滑到顶部了
            return true;
        } else if(getChildAt(0).getTop() >= 0){
            // 滑到顶部了
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (getChildCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }
        return isSlideToBottom();
    }

    public boolean isSlideToBottom() {
        if (computeVerticalScrollExtent() + computeVerticalScrollOffset()
                >= computeVerticalScrollRange())
            return true;
        return false;
    }

}
