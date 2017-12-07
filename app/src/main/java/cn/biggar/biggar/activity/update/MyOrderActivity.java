package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.VPAdaper;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.fragment.update.MyOrderFragment;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/11/20.
 */

public class MyOrderActivity extends BiggarActivity {
    @BindView(R.id.tl)
    SlidingTabLayout tl;
    @BindView(R.id.vp)
    ViewPager vp;
    private String[] mTitles = new String[]{"全部", "待付款", "待收货", "已完成", "已取消"};
    private List<Fragment> fragments;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        int tabWidth = Utils.px2dip(this, Utils.getScreenWidth(this) / 5);
        tl.setTabWidth(tabWidth);
        tl.setIndicatorWidth(tabWidth - 28);
        initFragments();
        vp.setAdapter(new VPAdaper(getSupportFragmentManager(), fragments));
        tl.setViewPager(vp, mTitles);
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(MyOrderFragment.newInstance(Constants.ORDER_ALL));
        fragments.add(MyOrderFragment.newInstance(Constants.ORDER_WAIT_PAY));
        fragments.add(MyOrderFragment.newInstance(Constants.ORDER_WAIT_TAKE));
        fragments.add(MyOrderFragment.newInstance(Constants.ORDER_FINISH));
        fragments.add(MyOrderFragment.newInstance(Constants.ORDER_CANCEL));
    }
}
