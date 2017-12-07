package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.contract.SearchContentContract;
import cn.biggar.biggar.event.SearchEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.SearchContentPresenter;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.GridNineLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/9/7.
 */

public class SearchContentFragment extends BiggarListFragment<SearchContentPresenter, VideoImageBean> implements SearchContentContract.View {
    private String uid;
    private String name;
    private int width;
    private int height;
    private int width2;
    private int height2;
    private RequestOptions options;

    public static SearchContentFragment newInstance(String searchName) {
        SearchContentFragment searchContentFragment = new SearchContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchName", searchName);
        searchContentFragment.setArguments(bundle);
        return searchContentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("searchName");
    }

    @Override
    protected void initDataBefore() {
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }
        height = width = Utils.getScreenWidth(getContext());
        float ratio = 375F / 175F;
        width2 = Utils.getScreenWidth(getContext()) - Utils.dip2px(getContext(), 16);
        height2 = (int) (width2 / ratio);
        options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.gear_image_default)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    protected void initAfter() {

    }

    @Override
    public boolean isOpenEventBus() {
        return true;
    }

    @Subscribe
    public void search(SearchEvent searchEvent) {
        name = searchEvent.searchName;
        if (searchEvent.currentItem == 1) {
            showLoading();
        }
        curPage = 1;
        mPresenter.requestContent("2", uid, Constants.PAGE_SIZE, curPage, name);
    }

    public void setSearchName(String searchName) {
        name = searchName;
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        if (!isLoadMore) {
            mPresenter.requestContent("2", uid, Constants.PAGE_SIZE, curPage, name);
        } else {
            mPresenter.loadMoreContent("2", uid, Constants.PAGE_SIZE, curPage, name);
        }
    }


    @Override
    protected int itemLayout() {
        return R.layout.item_discover_child;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, VideoImageBean item) {
        helper.setGone(R.id.line,
                helper.getLayoutPosition() > 1
                        || helper.getLayoutPosition() > 0);
        String eTypeVal = item.getE_TypeVal();
        GridNineLayout gridNineLayout = helper.getView(R.id.grid_nine_layout);
        FrameLayout flSpecial = helper.getView(R.id.fl_special);
        LinearLayout llImageVideo = helper.getView(R.id.ll_image_video);
        //专题
        if (eTypeVal.equals("2")) {
            gridNineLayout.setVisibility(View.GONE);
            flSpecial.setVisibility(View.VISIBLE);
            llImageVideo.setVisibility(View.GONE);
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
        } else if (viewId == R.id.ll_link_goods) {
            VideoImageBean.GoodsData goodsData = data.goods_data;
            if (null != goodsData) {
                WebViewActivity.getInstance(mContext, BaseUrl.GET_GOODS_DETAILS_H5_URL +
                        "?soure=g&version=" + Constants.WEB_VERSION
                        + "&devices=android"
                        + "&ID=" + goodsData.ID
                        + "&BID=" + data.getID()
                        + "&SUID=" + data.getE_MemberID());
            }
        }
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showContents(List<VideoImageBean> videoImageBeen) {
        finishRefresh(videoImageBeen);
    }

    @Override
    public void showMoreContents(List<VideoImageBean> videoImageBeen) {
        finishLoadMore(videoImageBeen);
    }
}
