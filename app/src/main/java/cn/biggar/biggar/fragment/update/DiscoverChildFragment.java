package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.BannerBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.contract.BiggarShowListContract;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.image.BannerPicassoImageLoader;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.BiggarShowListPresenter;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.GridNineLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/6/12.
 */

public class DiscoverChildFragment extends BiggarListFragment<BiggarShowListPresenter, VideoImageBean> implements BiggarShowListContract.View {
    private int width2;
    private int height2;
    private RequestOptions options;
    private int mPosition;
    private String uid;
    private Banner banner;

    public static DiscoverChildFragment getInstance(int position) {
        DiscoverChildFragment discoverChildFragment = new DiscoverChildFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        discoverChildFragment.setArguments(b);
        return discoverChildFragment;
    }

    @Override
    public void showError(String errMsg) {
        dismissLoading();
        handleError(errMsg);
    }

    @Override
    public void showResultList(List<VideoImageBean> videoImageBeen) {
        dismissLoading();
        finishRefresh(videoImageBeen);
    }

    @Override
    public void showMoreResultList(List<VideoImageBean> videoImageBeen) {
        dismissLoading();
        finishLoadMore(videoImageBeen);
    }

    @Override
    public void showBanners(final List<BannerBean> banners) {
        List<String> images = new ArrayList<>();
        for (BannerBean bannerBeen : banners) {
            images.add(Utils.getRealUrl(bannerBeen.getE_Img1()));
        }
        banner.setImages(images);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerBean bannerBean = banners.get(position);
                String h5Url = bannerBean.getE_URL();
                if (h5Url.contains("?")) {
                    h5Url = h5Url + "&soure=homeAdv&version=" + Constants.WEB_VERSION;
                } else {
                    h5Url = h5Url + "?soure=homeAdv&version=" + Constants.WEB_VERSION;
                }
                if (!h5Url.startsWith("http")) {
                    if (h5Url.startsWith("/")) h5Url = h5Url.substring(1);
                    h5Url = BaseAPI.BASE_RES_URL + h5Url;
                }
                WebViewActivity.getInstance(getActivity(), h5Url, true);
            }
        });
        banner.releaseBanner();
        banner.start();
    }

    @Override
    protected void initDataBefore() {
        mPosition = getArguments().getInt("position");
        UserBean userBean = Preferences.getUserBean(mContext);
        uid = userBean == null ? "" : userBean.getId();

        float ratio = 375F / 175F;
        width2 = Utils.getScreenWidth(getContext()) - Utils.dip2px(getContext(), 16);
        height2 = (int) (width2 / ratio);
        options = new RequestOptions();
        options.centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    protected void initAfter() {
        if (mPosition == 0) {
            initBanner();
        }
    }

    private void initBanner() {
        View headerBanner = getHeaderView(R.layout.header_home_banner);
        banner = (Banner) headerBanner.findViewById(R.id.banner);
        int screenWidth = Utils.getScreenWidth(getContext());
        float ratio = 375F / 175F;
        int height = (int) (screenWidth / ratio);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = height;
        banner.setLayoutParams(layoutParams);
        banner.setImageLoader(new BannerPicassoImageLoader());
    }

    public void goToFirstWithRefresh() {
        toTopWithRefresh();
    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        if (!isLoadMore) {
            switch (mPosition) {
                case 0:
                    mPresenter.requestHotList(curPage, Constants.PAGE_SIZE);
                    break;
                case 1:
                    mPresenter.requestFollowList(curPage, Constants.PAGE_SIZE, uid);
                    break;
            }
        } else {
            switch (mPosition) {
                case 0:
                    mPresenter.loadMoreHotList(curPage, Constants.PAGE_SIZE);
                    break;
                case 1:
                    mPresenter.loadMoreFollowList(curPage, Constants.PAGE_SIZE, uid);
                    break;
            }
        }
    }

    public boolean isOpenEventBus() {
        return true;
    }

    @Subscribe
    public void loginSuccess(LoginSucessEvent loginSucessEvent) {
        uid = loginSucessEvent.userBean.getId();
        if (mPosition == 1) {
            Logger.e("登录成功啦hahahahahaha...");
            curPage = 1;
            refreshData(false);
        }
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_discover_child;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, VideoImageBean item) {
//        if (helper.getLayoutPosition() > 1 && helper.getLayoutPosition()%2==0){
//            item.setE_TypeVal("2");
//        }
        helper.setGone(R.id.line,
                (mPosition == 0 && helper.getLayoutPosition() > 1)
                        || (mPosition == 1 && helper.getLayoutPosition() > 0));
        String eTypeVal = item.getE_TypeVal();
        GridNineLayout gridNineLayout = helper.getView(R.id.grid_nine_layout);
        FrameLayout flSpecial = helper.getView(R.id.fl_special);
        LinearLayout llImageVideo = helper.getView(R.id.ll_image_video);
        //专题
        if (eTypeVal.equals("2")) {
            gridNineLayout.setVisibility(View.GONE);
            flSpecial.setVisibility(View.VISIBLE);
            llImageVideo.setVisibility(View.GONE);
            final ImageView ivSepcial = helper.getView(R.id.iv_special);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flSpecial.getLayoutParams();
            layoutParams.width = width2;
            layoutParams.height = height2;
            flSpecial.setLayoutParams(layoutParams);
            Glide.with(mContext).asBitmap().load(Utils.getRealUrl(item.getE_Img1()))
                    .apply(options)
                    .into(new BitmapImageViewTarget(ivSepcial) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.
                                            create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(Utils.dip2px(mContext, 6));
                            ivSepcial.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            gridNineLayout.setVisibility(View.VISIBLE);
            flSpecial.setVisibility(View.GONE);
            llImageVideo.setVisibility(View.VISIBLE);
            gridNineLayout.setDatas(item);

            helper.setText(R.id.play_username_tv, item.getE_CreateUser())//用户名
                    .setText(R.id.play_browsenumber_tv, item.getE_Plays())//浏览数
                    .setText(R.id.tv_gift_num, item.getE_giftNum())//礼物数
                    .setText(R.id.tv_comment_num, item.getE_Comments())//评论数
                    .setText(R.id.tv_share_num, item.getE_Share())//分享数
                    .setText(R.id.play_title, item.getE_Content())//内容
                    .setText(R.id.tv_time, item.getE_CreateDate());//发布日期
            //头像
            Glide.with(mContext).asBitmap().load(Utils.getRealUrl(item.getE_HeadImg()))
                    .apply(options)
                    .into((CircleImageView) helper.getView(R.id.play_user_iv));

            //头像点击
            helper.addOnClickListener(R.id.play_user_iv);
            helper.addOnClickListener(R.id.ll_link_goods);
            helper.setGone(R.id.ll_link_goods, item.goods_data != null);
            if (item.goods_data != null) {
                VideoImageBean.GoodsData goodsData = item.goods_data;
                Glide.with(mContext).load(Utils.getRealUrl(goodsData.E_Img1))
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.iv_link_goods));
                helper.setText(R.id.tv_link_goods_name, goodsData.E_Name)
                        .setText(R.id.tv_link_goods_brand, goodsData.E_BrandCnName)
                        .setText(R.id.tv_link_goods_price, "￥" + goodsData.E_SellPrice);
            }
        }
    }

    @Override
    protected void onListItemClick(VideoImageBean data, int position) {
        if (data.getE_TypeVal().equals("2")) {
            WebViewActivity.getInstance(getActivity(), data.getE_GoUrl());
        } else {
            startActivity(new Intent(mActivity, ContentDetailActivity.class)
                    .putExtra("id", data.getID())
                    .putExtra("typeVal", data.getE_TypeVal()));
        }
    }

    @Override
    protected void onListItemChildClick(int viewId, VideoImageBean data, int position) {
        if (viewId == R.id.play_user_iv) {
            startActivity(MyHomeActivity.startIntent(getActivity(), data.getE_MemberID()));
        }else if (viewId == R.id.ll_link_goods){
            VideoImageBean.GoodsData goodsData = data.goods_data;
            if (null != goodsData){
                WebViewActivity.getInstance(mActivity, BaseUrl.GET_GOODS_DETAILS_H5_URL +
                        "?soure=g&version=" + Constants.WEB_VERSION
                        + "&devices=android"
                        + "&ID=" + goodsData.ID
                        + "&BID=" + data.getID()
                        + "&SUID=" + data.getE_MemberID());
            }
        }
    }
}
