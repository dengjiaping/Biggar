package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.event.GetTaskEvent;
import cn.biggar.biggar.fragment.update.MyTaskFragment;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/8/3.
 */

public class MyTaskActivity extends BiggarActivity {
    @BindView(R.id.tl)
    SlidingTabLayout tl;
    @BindView(R.id.vp)
    ViewPager vp;
    private String[] mTitles = new String[]{"待领取", "进行中", "已完结"};

    private VpAdapter vpAdapter;
    private List<Fragment> fragments;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_task;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fragments.add(MyTaskFragment.newInstance(i));
        }
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(vpAdapter);

        tl.setTabWidth(Utils.px2dip(this, Utils.getScreenWidth(this) / 3));
        tl.setViewPager(vp, mTitles);
    }

    static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public VpAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Subscribe
    public void getTask(GetTaskEvent getTaskEvent) {
        //跳转到进行中
        if (getTaskEvent.isJumpToLoading) {
            ((MyTaskFragment)fragments.get(1)).refreshToFirst();
            vp.setCurrentItem(1, false);
        }
        //不跳转到进行中
        else {
            ((MyTaskFragment)fragments.get(0)).removeItem();
        }
    }
}
