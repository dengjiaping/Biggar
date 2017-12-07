package cn.biggar.biggar.view.ItemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mx on 2016/12/5.
 * recyclerview  边距  适用于LinearLayoutManager
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;//水平方向
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;//垂直方向
    private int mOrientation;//方向
    private final int mSpace;//边距大小 px

    public SpaceItemDecoration(@LinearLayoutCompat.OrientationMode  int orientation, int space) {
        this.mOrientation = orientation;
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        final int lastPosition = state.getItemCount() - 1;//整个RecyclerView最后一个item的position
        final int current = parent.getChildLayoutPosition(view);//获取当前要进行布局的item的position
        if (current == -1) return;//holder出现异常时，可能为-1
        if (layoutManager instanceof LinearLayoutManager && !(layoutManager instanceof GridLayoutManager)) {//LinearLayoutManager
            if (mOrientation == LinearLayoutManager.VERTICAL) {//垂直
                outRect.set(0, 0, 0, mSpace);
                if (current == lastPosition) {//判断是否为最后一个item
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, 0, mSpace);
                }
            } else {//水平
                if (current == lastPosition) {//判断是否为最后一个item
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0,mSpace,  0);
                }
            }
        }
    }
}
