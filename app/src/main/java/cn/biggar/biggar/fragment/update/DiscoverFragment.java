package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.activity.update.SearchActivity;
import cn.biggar.biggar.adapter.update.VPAdaper;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.event.LoginOutEvent;
import com.flyco.tablayout.SlidingTabLayout;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.view.ParentScrollViewPager;

/**
 * Created by Chenwy on 2017/5/25.
 */

public class DiscoverFragment extends BiggarFragment {
    public static String TAG = DiscoverFragment.class.getSimpleName();

    @BindView(R.id.tl)
    SlidingTabLayout tl;
    @BindView(R.id.page)
    ParentScrollViewPager page;

    private int mCurPosition = 0;
    private String[] mTitles = new String[]{"热门", "关注"};
    private List<Fragment> fragments;

    public static Fragment getInstance() {
        return new DiscoverFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.frg_discover;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        fragments.add(DiscoverChildFragment.getInstance(0));
        fragments.add(DiscoverChildFragment.getInstance(1));
        page.setAdapter(new VPAdaper(getChildFragmentManager(), fragments));

        page.setCurrentItem(0, false);
        tl.setViewPager(page, mTitles);
        page.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1 && !AppPrefrences.getInstance(getContext()).isLogined()) {
                    startActivity(LoginActivity.startIntent(getContext()));
                    page.setCurrentItem(mCurPosition);
                    return;
                }
                mCurPosition = position;
            }
        });
    }

    @OnClick({R.id.iv_xl_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_xl_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    public void refresh() {
        ((DiscoverChildFragment) fragments.get(mCurPosition)).goToFirstWithRefresh();
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Subscribe
    public void loginOut(LoginOutEvent loginOutEvent) {
        page.setCurrentItem(0);
    }
}
