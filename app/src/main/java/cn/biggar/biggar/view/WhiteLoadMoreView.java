package cn.biggar.biggar.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/7/25.
 */

public class WhiteLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
