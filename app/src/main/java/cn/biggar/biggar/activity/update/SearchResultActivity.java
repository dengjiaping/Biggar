package cn.biggar.biggar.activity.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.yinglan.keyboard.HideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.event.SearchEvent;
import cn.biggar.biggar.fragment.update.SearchContentFragment;
import cn.biggar.biggar.fragment.update.SearchGoodsFragment;
import cn.biggar.biggar.fragment.update.SearchUserFragment;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class SearchResultActivity extends BiggarActivity {
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tl)
    SlidingTabLayout tl;
    @BindView(R.id.vp_search)
    ViewPager vpSearch;
    @BindView(R.id.et_search)
    EditText etSearch;


    private String[] mTitles = new String[]{"用户", "内容", "商品"};
    private List<Fragment> fragments;
    private String searchName;
    private VpAdapter vpAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        HideUtil.init(this);
        etSearch.setHint("搜索网红/有价值的内容");
        searchName = getIntent().getExtras().getString("searchName");
        tvCancel.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(searchName)) {
            etSearch.setText(searchName);
            etSearch.setSelection(searchName.length());
        }

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    searchName = etSearch.getText().toString().trim();

                    if (TextUtils.isEmpty(searchName)) {
                        ToastUtils.showError("请输入搜索关键字");
                        return true;
                    }
                    hideKeyboard();
                    startSearch();
                }
                return false;
            }
        });

        fragments = new ArrayList<>();
        fragments.add(SearchUserFragment.newInstance(searchName));
        fragments.add(SearchContentFragment.newInstance(searchName));
        fragments.add(SearchGoodsFragment.newInstance(searchName));
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpSearch.setAdapter(vpAdapter);
        tl.setTabWidth(Utils.px2dip(this, Utils.getScreenWidth(this) / 3));
        tl.setViewPager(vpSearch, mTitles);
    }

    private void startSearch(){
        ((SearchUserFragment) fragments.get(0)).setSearchName(searchName);
        ((SearchContentFragment) fragments.get(1)).setSearchName(searchName);
        ((SearchGoodsFragment) fragments.get(2)).setSearchName(searchName);

        int currentItem = vpSearch.getCurrentItem();
        EventBus.getDefault().post(new SearchEvent(searchName,currentItem));
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

    @OnClick({R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                hideKeyboard();
                finish();
                break;
        }
    }
}
