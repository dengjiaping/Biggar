package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BrandDetails;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.fragment.BrandActivityFragment;
import cn.biggar.biggar.fragment.BrandShopFragment;
import cn.biggar.biggar.fragment.BrandWelfareFragment;
import cn.biggar.biggar.fragment.HeaderViewPagerFragment;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.BrandPersenter;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.NoScrollViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.view.RootLayout;
import cn.biggar.biggar.view.scrollable.ScrollableLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import per.sue.gear2.adapter.ImageSimpleAdapter;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.UnitUtils;
import per.sue.gear2.widget.flow.CircleFlowIndicator;
import per.sue.gear2.widget.flow.ViewFlowFixViewPager;
import per.sue.gear2.widget.nav.GearTabPageIndicator;
import per.sue.gear2.widget.nav.IconPagerAdapter;

/**
 * Created by mx on 2016/12/20.
 * 品牌空间
 */
public class BrandSpaceActivity extends BiggarActivity implements View.OnClickListener {
    @BindView(R.id.recommend_circle_indicator)
    CircleFlowIndicator mCircleIndicator;
    @BindView(R.id.brand_space_tab)
    GearTabPageIndicator brandSpaceTab;
    @BindView(R.id.brand_space_viewpager)
    NoScrollViewPager brandSpaceViewpager;
    @BindView(R.id.scrollable_Layout)
    ScrollableLayout scrollableLayout;
    @BindView(R.id.recommend_view_flow)
    ViewFlowFixViewPager mViewFlow;
    @BindView(R.id.brand_logo1_iv)
    CircleImageView mIvLogo;
    @BindView(R.id.brand_name1_tv)
    TextView mTvName;
    @BindView(R.id.brand_fans_tv)
    TextView mTvFans;
    @BindView(R.id.brand_v_iv)
    ImageView mIvV;
    @BindView(R.id.brand_tags_layout)
    LinearLayout mLayoutTags;
    @BindView(R.id.brand_edit_tag_iv)
    ImageView mIvEditTag;
    @BindView(R.id.brand_bottom_a_rl)
    RelativeLayout mBottomA;
    @BindView(R.id.brand_bottom_b_rl)
    RelativeLayout mBottomB;
    @BindView(R.id.brand_bottom_c_rl)
    RelativeLayout mBottomC;
    public List<HeaderViewPagerFragment> fragments;
    @BindView(R.id.brand_bottom_b_tv)
    TextView mBottomBTv;
    @BindView(R.id.brand_bottom_d_rl)
    RelativeLayout mBottomD;
    private CommonPresenter commonP;
    private BrandPersenter mBrandP;
    private BrandDetails mData;
    private String mID, mUserID, mUserName;
    private ShareDialog shareDialog;

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            UserBean userBean = Preferences.getUserBean(getApplication());
            mUserID = userBean.getId();
            mUserName = userBean.geteName();
        } else {
            mUserID = mUserName = null;
        }
    }

    public static Intent startIntent(Context context, String Id) {
        Intent intent = new Intent(context, BrandSpaceActivity.class);
        intent.putExtra("id", Id);
        return intent;
    }

    public static Intent startIntent(Context context, String Id, String userid) {
        Intent intent = new Intent(context, BrandSpaceActivity.class);
        intent.putExtra("id", Id);
        intent.putExtra("userid", userid);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_brand_space;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
//        showLoading();
        mID = getIntent().getStringExtra("id");
        getUserInfo();
        initViews();
        initData();
        initEvents();
        shareDialog = new ShareDialog();
    }

    public void initViews() {
        initViewPager();
    }

    public void initData() {
        commonP = new CommonPresenter(this);
        mBrandP = new BrandPersenter(this);
        queryBrand();
        UserBean userBean = Preferences.getUserBean(getApplication());
        if (userBean != null) {
            checkConcern(mID, 2, userBean.getId(), "ME");
        }
    }

    private void initEvents() {

        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
                if (mData != null) {
                    String shareUrl = BaseUrl.SHARE_BRAND_URL + "ID=" + mData.getID();
                    ArrayList<String> e_img4 = new ArrayList<>();
                    for (String s : mData.getE_Img4()) {
                        e_img4.add(BaseAPI.BASE_RES_URL + s);
                    }
                    if (e_img4.size() == 0) {
                        e_img4.add(BaseAPI.BASE_RES_URL + mData.getE_Logo());
                    }
                    share(BaseAPI.BASE_RES_URL + mData.getE_Logo(), e_img4, shareUrl);
                }
            }
        });

        brandSpaceViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper().setCurrentScrollableContainer(fragments.get(position));
            }
        });

        mBottomA.setOnClickListener(this);
        mBottomB.setOnClickListener(this);
        mBottomC.setOnClickListener(this);
        mBottomD.setOnClickListener(this);
        mIvLogo.setOnClickListener(this);
        Log.d("MX", "init events");
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(BrandShopFragment.getInstance(mID));
        fragments.add(BrandActivityFragment.getInstance(mID));
        fragments.add(BrandWelfareFragment.getInstance(mID));//BrandWelfareFragment
        brandSpaceViewpager.setAdapter(new BrandPagerAdapter(getSupportFragmentManager()));
        brandSpaceTab.setViewPager(brandSpaceViewpager);
        scrollableLayout.getHelper().setCurrentScrollableContainer(fragments.get(0));
    }


    /**
     * 获取 商品
     */

    private void queryBrand() {
        mBrandP.queryBrandDetails(mID, new OnObjectListener<BrandDetails>() {
            @Override
            public void onSuccess(BrandDetails result) {
                dismissLoading();
                mData = result;
                loadData();
            }

            @Override
            public void onError(int code, String msg) {
                dismissLoading();
                ToastUtils.showError(getString(R.string.str_load_error));
                finish();
            }

        });
    }

    /**
     * 加载数据
     */

    private void loadData() {
        try {
            Glide.with(this).load(BaseAPI.BASE_RES_URL + mData.getE_Logo())
                    .apply(new RequestOptions().centerCrop())
                    .into(mIvLogo);

            mTvName.setText(mData.getE_BrandCnName());
            RootLayout.getInstance(this).setTitle(mData.getE_BrandCnName());
            mTvFans.setText("  " + mData.getE_Concerns());
            mIvV.setVisibility(TextUtils.isEmpty(mData.getE_Authen()) ? View.GONE : mData.getE_Authen().equals("1") ? View.VISIBLE : View.GONE);
//            mTvLike.setText("关注 " + mData.getE_BConcerns() + "");
//            mTvBiggarIndex.setText("比格指数 " + mData.getE_Points() + "");
            loadShufflingImgs();
            loadTags();

        } catch (Exception e) {
        }
    }

    /**
     * 加载 标签
     */

    private void loadTags() {
        if (mData.getE_AuTags() == null) return;
        if (mData.getE_AuTags().equals("")) return;
        String[] tags = mData.getE_AuTags().split(",");
        int size = tags.length > 3 ? 3 : tags.length;
        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == 1) {
                layoutParams.setMargins(UnitUtils.dip2px_new(this, 8), 0, UnitUtils.dip2px_new(this, 8), 0);
            }
            mLayoutTags.addView(createTag(tags[i]), layoutParams);
        }
    }

    /**
     * 创建 text-tag
     *
     * @param tag
     * @return
     */

    private TextView createTag(String tag) {
        TextView textView = new TextView(this);
        textView.setText(tag);
        textView.setBackgroundResource(R.drawable.shape_rounded_border_theme);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setMaxLines(1);
        textView.setPadding(UnitUtils.dip2px_new(this, 7), UnitUtils.dip2px_new(this, 1), UnitUtils.dip2px_new(this, 7), UnitUtils.dip2px_new(this, 1));
        return textView;
    }

    /**
     * 加载 顶部轮播图
     */

    private void loadShufflingImgs() {
        try {
            ArrayList<String> imagslist = (ArrayList<String>) mData.getE_Img2();
            if (imagslist == null || (imagslist != null && imagslist.isEmpty())) {
                imagslist.add("");
            }
            for (int i = 0; i < imagslist.size(); i++) {
                imagslist.set(i, Utils.getRealUrl(imagslist.get(i)));
            }
            ImageSimpleAdapter imageSimpleAdapter = new ImageSimpleAdapter(getActivity());
            imageSimpleAdapter.setOnAdapterItemClickLiener(new ImageSimpleAdapter.OnAdapterItemClickLiener() {
                @Override
                public void OnImageViewClick(int position, String url) {
                    // Toast.makeText(getActivity().getApplicationContext(), "position=" + position + "  url=" + url, Toast.LENGTH_SHORT).show();
                }
            });
            imageSimpleAdapter.setDefultImageResId(R.drawable.gear_image_default);
            imageSimpleAdapter.setList(imagslist);
            imageSimpleAdapter.setIsLoob(true);
            mViewFlow.setAdapter(imageSimpleAdapter);
            mViewFlow.setmSideBuffer(imagslist.size()); // 实际图片张数， 我的ImageAdapter实际图片张数为3
            mViewFlow.setFlowIndicator(mCircleIndicator);
            mViewFlow.setTimeSpan(4500);
            //viewFlow.setFillColor(getResources().getColor(r.color.app_green_l));
            mViewFlow.setSelection(imagslist.size()); // 设置初始位置
            mViewFlow.startAutoFlowTimer(); // 启动自动播放
            mViewFlow.requestLayout();// 重新计算该控制在父布局中的位置
        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.brand_bottom_a_rl:
                if (mUserID == null) {
                    startActivity(LoginActivity.startIntent(this));
                    return;
                }
                WebViewActivity.getInstance(BrandSpaceActivity.this, BaseUrl.GET_CHAT_URL + "?soure=chat&version=" + Constants.WEB_VERSION + "&devices=android" + "&ID=" + mID + "&TYPE=2");
                break;
            case R.id.brand_bottom_b_rl:
                if (mData == null) return;
                if (mBottomBTv.getTag() != null) {
                    if ((boolean) mBottomBTv.getTag()) {
                        showCacnelDialog(mData.getID(), mUserID, mData.getE_Name(), 2);
                    } else {
                        concern(mData.getID(), mData.getE_Name(), 2, mUserID, mUserName);
                    }
                } else {
                    concern(mData.getID(), mData.getE_Name(), 2, mUserID, mUserName);
                }
                break;
            case R.id.brand_bottom_c_rl:
                showPopup(mBottomC);
                break;
            case R.id.brand_bottom_d_rl:
                BusinessCardActivity.startIntent(getActivity(), mData.getID());
                break;
            case R.id.brand_logo1_iv:
                BusinessCardActivity.startIntent(getActivity(), mData.getID());
                break;
        }
    }


    private void showPopup(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.popup_brand_menu, null);

        TextView textView2 = (TextView) contentView.findViewById(R.id.tv_popup_brand_feedback);

        final PopupWindow mPopup = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.getInstance(BrandSpaceActivity.this, BaseUrl.GET_FEED_BACK_URL + "?soure=feedback&version=" + Constants.WEB_VERSION);
                mPopup.dismiss();
            }
        });

        mPopup.setBackgroundDrawable(new BitmapDrawable());
        mPopup.setOutsideTouchable(true);
        mPopup.showAsDropDown(view, 0, 3);
    }

    /**
     * 取消关注 对话框
     */

    private void showCacnelDialog(final String id, final String userId, String userName, final int type) {
        TipsDialog.newInstance("您确定不再关注" + userName + "了吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
            @Override
            public void onSure() {
                cancelConcern(id, userId, type);
            }

            @Override
            public void onCancel() {
            }
        }).setMargin(52).show(getSupportFragmentManager());
    }

    /**
     * 关注
     */

    private void concern(String id, String nick, int type, String userId, String username) {
        if (userId == null) {
            startActivity(LoginActivity.startIntent(this));
            return;
        }

        if (userId.equals(id)) {
            ToastUtils.showWarning("不能关注自己");
            return;
        }

        commonP.concern(id, nick, type, userId, username, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ToastUtils.showNormal("关注成功");
                mBottomBTv.setText("已关注");
                mBottomBTv.setTag(true);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(getString(R.string.str_operation_error));
            }
        });
    }

    /**
     * 取消关注
     *
     * @param id
     * @param uid
     * @param type
     */

    private void cancelConcern(String id, String uid, int type) {
        if (uid == null) {
            startActivity(LoginActivity.startIntent(this));
            return;
        }
        commonP.cancelConcern(uid, id, type, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ToastUtils.showNormal("取消成功");
                mBottomBTv.setText("关注");
                mBottomBTv.setTag(false);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(getString(R.string.str_operation_error));
            }
        });
    }

    /**
     * 检查是否关注
     */

    private void checkConcern(String id, int type, String userId, String checkType) {
        commonP.checkConcern(id, type, userId, checkType, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                mBottomBTv.setText("已关注");
                mBottomBTv.setTag(true);
            }
        });
    }


    /**
     * 分享
     *
     * @param imageUrl
     * @param targetUrl
     */

    public void share(String imageUrl, ArrayList<String> images, String targetUrl) {
        String content = TextUtils.isEmpty(mData.getE_BrandCnName()) ? getString(R.string.share_default_title) : mData.getE_BrandCnName();
        shareDialog.show(getSupportFragmentManager(), content, content, imageUrl, images, targetUrl, ShareDialog.SHARE_TYPE_4, "", new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showError("分享失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("取消分享");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    public class BrandPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

        private final int[] ICON_ARR = new int[]{
                R.mipmap.home,
                R.mipmap.flag_f,
                R.mipmap.bag_gray,
        };

        private final String[] TITLE_ARR = new String[]{
                getString(R.string.brand_nav_label_home),
                getString(R.string.brand_nav_label_activity),
                getString(R.string.str_welfare)
        };

        public BrandPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            return ICON_ARR[index];
        }

        @Override
        public int getCount() {
            return TITLE_ARR.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE_ARR[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}



