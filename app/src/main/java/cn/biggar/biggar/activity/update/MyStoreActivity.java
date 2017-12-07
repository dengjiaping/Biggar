package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.fragment.update.DistributionBrandFragment;
import cn.biggar.biggar.fragment.update.DistributionManagerFragment;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/17.
 */

public class MyStoreActivity extends BiggarActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.fl_search)
    FrameLayout flSearch;
    @BindView(R.id.vp_store)
    ViewPager vpStore;
    @BindView(R.id.tl)
    SlidingTabLayout tl;

    private VpAdapter vpAdapter;
    private List<Fragment> fragments;
    private String[] mTitles = new String[]{"可分销品牌", "分销管理"};
    private int mCurPosition;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_store;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        HideUtil.init(this);
        tvTitle.setText("我的比格店");
        fragments = new ArrayList<>();
        fragments.add(DistributionBrandFragment.newInstance());
        fragments.add(DistributionManagerFragment.newInstance());
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpStore.setAdapter(vpAdapter);
        tl.setTabWidth(Utils.px2dip(this, Utils.getScreenWidth(this) / 2));
        tl.setViewPager(vpStore, mTitles);

        vpStore.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurPosition = position;
                hideSearch();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    hideKeyboard();
                    String searchName = etSearch.getText().toString().trim();
                    Fragment fragment = fragments.get(mCurPosition);
                    if (mCurPosition == 0) {
                        DistributionBrandFragment distributionBrandFragment = (DistributionBrandFragment) fragment;
                        distributionBrandFragment.startSearch(searchName);
                    } else {

                    }
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchName = etSearch.getText().toString().trim();
                Fragment fragment = fragments.get(mCurPosition);
                if (mCurPosition == 0) {
                    DistributionBrandFragment distributionBrandFragment = (DistributionBrandFragment) fragment;
                    distributionBrandFragment.setSearchName(searchName);
                } else {

                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.iv_search, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                showSearch();
                break;
            case R.id.tv_cancel:
                hideSearch();
                break;
        }
    }

    /**
     * 隐藏搜索
     */
    private void hideSearch() {
        etSearch.setText("");
        hideKeyboard();
        ivSearch.setVisibility(mCurPosition == 0 ? View.VISIBLE : View.INVISIBLE);
        flSearch.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.GONE);
    }

    /**
     * 显示搜索
     */
    private void showSearch() {
        tvCancel.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.GONE);
        flSearch.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        etSearch.requestFocus();
    }

    public void hideKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
