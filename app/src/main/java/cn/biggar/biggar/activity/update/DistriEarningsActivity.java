package cn.biggar.biggar.activity.update;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.dialog.DialogDate;
import cn.biggar.biggar.fragment.update.DistriEarningFragment;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/7/17.
 * 分销管理 --- 我的收益
 */

public class DistriEarningsActivity extends BiggarActivity implements DialogDate.OnDateChooseListener {
    @BindView(R.id.tl)
    SlidingTabLayout stl;
    @BindView(R.id.vp_earning)
    ViewPager vpEarnings;

    private VpAdapter vpAdapter;
    private List<Fragment> fragments;
    private String[] mTitles = new String[]{"今天", "3天", "7天"};
    private DialogDate dialogDate;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_distri_earning;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        dialogDate = new DialogDate(getActivity(), this);

        fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fragments.add(DistriEarningFragment.newInstance(i));
        }
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpEarnings.setAdapter(vpAdapter);
        stl.setTabWidth(Utils.px2dip(this, Utils.getScreenWidth(this) / 3));
        stl.setViewPager(vpEarnings, mTitles);

        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDate.showPopupWindow();
            }
        });
    }

    @Override
    public void onDateChoose(String startDate, String endDate) {
        startActivity(new Intent(this,DistriEarningsDateActivity.class)
                .putExtra("startDate",startDate)
                .putExtra("endDate",endDate));
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
}
