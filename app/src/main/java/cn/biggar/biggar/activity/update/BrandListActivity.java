package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.VPAdaper;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.event.SearchBrandEvent;
import cn.biggar.biggar.fragment.update.BrandListFragment;

/**
 * Created by Chenwy on 2017/11/6.
 * 品牌
 */

public class BrandListActivity extends BiggarActivity {
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;
    @BindView(R.id.brand_view_pager)
    ViewPager brandViewPager;
    @BindView(R.id.brand_search_iv)
    ImageView mIvSearch;
    @BindView(R.id.brand_search_layout)
    LinearLayout mLayoutSearch;
    @BindView(R.id.brand_search_et)
    EditText mEtSearch;
    @BindView(R.id.brand_search_cancel_tv)
    TextView tvCancel;

    private String[] mTitles = {"品牌推荐", "关注品牌"};
    private List<Fragment> fragments;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_brand_list;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        fragments.add(BrandListFragment.newInstance(0));
        fragments.add(BrandListFragment.newInstance(1));

        tabLayout.setTabData(mTitles);
        brandViewPager.setAdapter(new VPAdaper(getSupportFragmentManager(), fragments));
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                brandViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        brandViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setCurrentTab(position);
                mLayoutSearch.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                mIvSearch.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.brand_search_iv, R.id.brand_search_cancel_tv, R.id.bar_back_view})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.brand_search_cancel_tv:
                mLayoutSearch.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                mIvSearch.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.brand_search_iv:
                mLayoutSearch.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                mIvSearch.setVisibility(View.GONE);
                tabLayout.setVisibility(View.INVISIBLE);
                mEtSearch.requestFocus();
                break;
            case R.id.bar_back_view:
                finish();
                break;
        }
    }


    /**
     * 搜索
     */
    private void search() {
        EventBus.getDefault().post(new SearchBrandEvent(brandViewPager.getCurrentItem(), mEtSearch.getText().toString().trim()));
    }
}
