package cn.biggar.biggar.view.pullableview;

import android.content.Context;
import android.util.AttributeSet;

import per.sue.gear2.widget.HeaderGridView;

/**
 * Created by mx on 2016/8/12.
 *  继承 添加头部底部 与 下拉刷新
 */
public class PullableHeaderFooterGridView extends HeaderGridView implements Pullable {

    public PullableHeaderFooterGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PullableHeaderFooterGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableHeaderFooterGridView(Context context) {
        super(context);
    }

    @Override
    public boolean canPullDown()
    {
        if (getCount() == 0)
        {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0)
        {
            // 滑到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        if (getCount() == 0)
        {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1))
        {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
