package cn.biggar.biggar.activity.update;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;
import com.app.hubert.library.OnGuideChangedListener;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ImageViewerActivity;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.adapter.update.CommentListAdapter;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.ContentBean;
import cn.biggar.biggar.contract.ContentContract;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.image.BannerPicassoImageLoader;
import cn.biggar.biggar.listener.OnHeaderViewOnclickListener;
import cn.biggar.biggar.listener.VideoSampleListener;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.ContentPresenter;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.BiggarVideoPlayer;
import cn.biggar.biggar.view.BottomEditTextDialog;
import cn.biggar.biggar.view.GiftListLayout;
import cn.biggar.biggar.view.WhiteLoadMoreView;
import me.dudu.livegiftview.GiftModel;

/**
 * Created by Chenwy on 2017/8/25.
 */

public class ContentDetailActivity extends BiggarActivity<ContentPresenter> implements ContentContract.View, GiftListLayout.OnGiftDataSendListener
        , GiftListLayout.OnShowGuideListener, OnHeaderViewOnclickListener {
    @BindView(R.id.video_player)
    BiggarVideoPlayer videoPlayer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.gift_list_layout)
    GiftListLayout giftListLayout;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R.id.tv_share_num)
    TextView tvShareNum;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.send_gift_tips)
    TextView sendGiftTips;

    /**
     * 图片或视频的用户的id
     */
    private String contentId;

    /**
     * 登录的用户信息
     */
    private UserBean userBean;
    /**
     * 登录的用户id
     */
    private String mUserID;

    /**
     * 登录的用户名
     */
    private String mUserName;

    /**
     * 全屏工具类
     */
    private OrientationUtils orientationUtils;

    /**
     * 视频宽度
     */
    private int videoWidth;
    /**
     * 视频高度
     */
    private int videoheight;

    /**
     * 图片还是视频
     */
    private String typeVal;

    /**
     * 评论弹框
     */
    private BottomEditTextDialog bottomEditTextDialog;

    /**
     * 是否登录返回
     */
    private boolean isFromLogin;

    /**
     * 从页面返回
     */
    private boolean isRestart;

    private HeaderViewHolder headerViewHolder;
    private CommentListAdapter commentListAdapter;
    private int curPage = 1;
    private ContentBean mContentBean;
    private List<String> images;
    private boolean isLoadMoreFail;
    private ShareDialog shareDialog;
    private Controller guide1;
    private boolean isGuide1Show;
    private boolean isGuide2Show;
    private boolean isBackHideGuide;
    private boolean isGuide3Show;
    private Controller guide2;
    private Controller guide3;
    private ObjectAnimator animator;

    /**
     * @param context
     * @param id      视频Id
     * @param type    0视频  1图片
     * @return
     */
    public static Intent startIntent(Context context, String id, String type) {
        Intent intent = new Intent(context, ContentDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("typeVal", type);
        return intent;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_content_detail;
    }

    @Override
    protected void initImmersionBar() {
        //沉浸式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.transparent)
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                .init();
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        contentId = getIntent().getStringExtra("id");
        typeVal = getIntent().getStringExtra("typeVal");
        shareDialog = new ShareDialog();
        getUserInfo();
        giftListLayout.setOnGiftDataSendListener(this);
        giftListLayout.setOnShowGuideListener(this);
        bottomEditTextDialog = new BottomEditTextDialog();
        bottomEditTextDialog.setOnCommentSubmitListener(new BottomEditTextDialog.OnCommentSubmitListener() {
            @Override
            public void onCommentSubmit(String commentContent) {
                mPresenter.comment(contentId, commentContent, "VIDEO", mUserID, mUserName);
            }
        });
        commentListAdapter = new CommentListAdapter(new ArrayList<ContentBean.CommentList>());
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(commentListAdapter);
        commentListAdapter.setEnableLoadMore(true);
        commentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadMoreFail) {
                    curPage += 1;
                }
                mPresenter.loadMoreComments(contentId, mUserID, Constants.PAGE_SIZE, curPage);
            }
        }, rvContent);
        commentListAdapter.setLoadMoreView(new WhiteLoadMoreView());
        commentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.avatar_iv:
                        startActivity(MyHomeActivity.startIntent(getActivity(), commentListAdapter.getItem(position).E_MemberID));
                        break;
                    case R.id.like_ll:
                        if (mUserID == null) {
                            startActivity(LoginActivity.startIntent(ContentDetailActivity.this));
                            return;
                        }
                        mPresenter.like(position, commentListAdapter.getItem(position).ID, mUserID);
                        break;
                }
            }
        });
        initHeaderView();
        initEmptyView();

        if (typeVal.equals("0")) {
            //视频播放器
            initVideo();
        } else {
            //图片轮播
            initImage();
        }

        //获取数据
        showLoading();
        requestData();
    }

    private void startSendGiftTipsAnim() {
        sendGiftTips.setVisibility(View.VISIBLE);
        animator = ObjectAnimator.ofFloat(sendGiftTips, "translationY", 0, -50, 0, -50, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(2500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    private void releaseGiftTipsAnim() {
        if (animator != null) {
            animator.cancel();
            sendGiftTips.setVisibility(View.GONE);
            animator = null;
        }
    }

    /**
     * 初始化头部内容布局
     */
    private void initHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header_content_detail, (ViewGroup) rvContent.getParent(), false);
        commentListAdapter.addHeaderView(headerView);
        headerViewHolder = new HeaderViewHolder(this, headerView);
        headerViewHolder.setOnHeaderViewOnclickListener(this);
    }

    /**
     * 初始化空布局
     */
    private void initEmptyView() {
        commentListAdapter.setEmptyView(R.layout.empty_comment);
        commentListAdapter.setHeaderAndEmpty(true);
        commentListAdapter.getEmptyView().findViewById(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomEditTextDialog.show(getSupportFragmentManager());
            }
        });
    }

    private void showGuide() {
        String isShouldShowGuide = SPUtils.getInstance().getString(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE);
        if (userBean != null && (TextUtils.isEmpty(isShouldShowGuide) || isShouldShowGuide.equals("1"))) {
            guide1 = NewbieGuide.with(this)
                    .setOnGuideChangedListener(new OnGuideChangedListener() {
                        @Override
                        public void onShowed(Controller controller) {
                            isGuide1Show = true;
                        }

                        @Override
                        public void onRemoved(Controller controller) {
                            releaseGiftTipsAnim();
                            isGuide1Show = false;
                            if (!isBackHideGuide) {
                                boolean isMe = false;
                                if (userBean != null && mContentBean != null && userBean.getId().equals(mContentBean.E_MemberID)) {
                                    isMe = true;
                                }
                                giftListLayout.show(isMe);
                            }
                            guide1 = null;
                        }
                    })
                    .setLabel("guide1")
                    .addHighLight(findViewById(R.id.iv_send_gift), HighLight.Type.OVAL)
                    .setLayoutRes(R.layout.view_guide1)
                    .alwaysShow(true)
                    .show();
        }
    }

    /**
     * 图片轮播
     */
    private void initImage() {
        images = new ArrayList<>();
        banner.setVisibility(View.VISIBLE);
        videoWidth = Utils.getScreenWidth(this);
        videoheight = (int) (videoWidth * 210f / 375f);
        rlVideo.getLayoutParams().width = videoWidth;
        rlVideo.getLayoutParams().height = videoheight;
        banner.setImageLoader(new BannerPicassoImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(ImageViewerActivity.startIntent(getActivity(), images
                        , position, false));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });
    }

    /**
     * 视频播放器
     */
    private void initVideo() {
        videoPlayer.setVisibility(View.VISIBLE);
        videoWidth = Utils.getScreenWidth(this);
        videoheight = (int) (videoWidth * 210f / 375f);
        rlVideo.getLayoutParams().width = videoWidth;
        rlVideo.getLayoutParams().height = videoheight;

        //播放器自带返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        orientationUtils.setEnable(false);
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setRotateViewAuto(false);
        videoPlayer.setLockLand(false);
        videoPlayer.setShowFullAnimation(false);
        videoPlayer.setmOpenPreView(true);
        videoPlayer.setDismissControlTime(5000);

        //如果是4G网络
        if (NetworkUtils.is4G()) {
            videoPlayer.show4gPlay();
        }

        //设置全屏按键功能
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVideoWidth = GSYVideoManager.instance().getCurrentVideoWidth();
                int currentVideoHeight = GSYVideoManager.instance().getCurrentVideoHeight();
                int rotate = videoPlayer.getRotate();
                Logger.e(currentVideoWidth + "*" + currentVideoHeight);
                if (rotate == 0 && currentVideoWidth > currentVideoHeight) {
                    orientationUtils.resolveByClick();
                }
                videoPlayer.startWindowFullscreen(getActivity(), true, true);
                ivBack.setVisibility(View.GONE);
            }
        });
        videoPlayer.setOnPlayClickListener(new BiggarVideoPlayer.OnPlayClickListener() {
            @Override
            public void onPlayClick() {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showError("无可使用网络");
                } else {
                    videoPlayer.handPlay();
                }
            }
        });
        videoPlayer.setStandardVideoAllCallBack(new VideoSampleListener() {
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
                ivBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
                StandardGSYVideoPlayer.backFromWindowFull(ContentDetailActivity.this);
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                ToastUtils.showError("视频出错或不存在");
            }
        });
    }

    /**
     * 获取缓存用户信息
     */
    private void getUserInfo() {
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            userBean = Preferences.getUserBean(getApplication());
            mUserID = userBean.getId();
            mUserName = userBean.geteName();
        } else {
            mUserID = mUserName = null;
        }
    }

    private void requestData() {
        mPresenter.requestContents(contentId, mUserID);
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Subscribe
    public void loginSuccess(LoginSucessEvent loginSucessEvent) {
        isFromLogin = true;
    }

    @Override
    public void onBackPressed() {

        if (guide1 != null && isGuide1Show) {
            isBackHideGuide = true;
            guide1.remove();
            return;
        }

        if (guide2 != null && isGuide2Show) {
            isBackHideGuide = true;
            guide2.remove();
            return;
        }

        if (guide3 != null && isGuide3Show) {
            isBackHideGuide = true;
            guide3.remove();
            return;
        }

        if (giftListLayout.isShow()) {
            giftListLayout.hide();
            return;
        }

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        giftListLayout.updateUserInfo(userBean);
        if (isFromLogin) {
            mPresenter.requestContents(contentId, mUserID);
        }
        if (isRestart) {
            videoPlayer.rePlay();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    @Override
    public void onDestroy() {
        headerViewHolder.destory();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        releaseGiftTipsAnim();
        giftListLayout.destory();
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.ll_send_gift, R.id.ll_comment, R.id.ll_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_send_gift:
                if (TextUtils.isEmpty(mUserID)) {
                    startActivity(LoginActivity.startIntent(this));
                    return;
                }
                if (mContentBean != null) {
                    if (mContentBean.E_MemberID.equals(mUserID)) {
                        ToastUtils.showWarning("不能给自己送礼");
                        return;
                    }
                    giftListLayout.show(false);
                    releaseGiftTipsAnim();
                }
                break;
            case R.id.ll_comment:
                showComment();
                break;
            case R.id.ll_share:
                showShareDialog();
                break;
        }
    }

    /**
     * 转发分享
     */
    private void showShareDialog() {
        if (mContentBean == null) {
            ToastUtils.showError("数据获取失败，请稍后再试~");
            return;
        }

        shareDialog.show(getSupportFragmentManager(), mContentBean.E_ShareTitle, mContentBean.E_ShareContent
                , mContentBean.E_HeadImg, mContentBean.E_RATE_SHAREURL, typeVal
                , mContentBean.E_MemberID == null ? "" : mContentBean.E_MemberID, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.showNormal("分享成功");
                        mPresenter.shareSuccess(contentId, "VIDEO");
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

    /**
     * 弹出评论框
     */
    private void showComment() {
        if (mUserID == null) {
            startActivity(LoginActivity.startIntent(this));
        } else {
            bottomEditTextDialog.show(getSupportFragmentManager());
        }
    }

    /**
     * 请求内容数据失败
     *
     * @param errMsg
     */
    @Override
    public void showError(String errMsg) {
        dismissLoading();
        ToastUtils.showError(errMsg);
    }

    /**
     * 接口返回数据
     *
     * @param contentBean
     */
    @Override
    public void showContents(ContentBean contentBean) {
        dismissLoading();
        isLoadMoreFail = false;
        mContentBean = contentBean;

        if (userBean != null) {
            showGuide();
        }

        //头部数据
        showHeaderData(contentBean);
        //礼物
        giftListLayout.setMemberId(contentBean.E_MemberID);
        giftListLayout.setVideoId(contentId);
        if (mContentBean.red_data != null && mContentBean.red_data.size() > 0) {
            giftListLayout.showRedPacketWithAnim(mContentBean.red_data);
        }
        if (!TextUtils.isEmpty(contentBean.is_Float) && contentBean.is_Float.equals("1")) {
            startSendGiftTipsAnim();
        }
        //评论数
        tvCommentNum.setText(contentBean.E_Comments);
        //分享数
        tvShareNum.setText(contentBean.E_Share);
        //评论
        List<ContentBean.CommentList> commentlists = contentBean.commentlist;
        headerViewHolder.tvCommentTips.setVisibility(commentlists == null || commentlists.size() == 0 ? View.GONE : View.VISIBLE);
        commentListAdapter.setNewData(commentlists);
        if (commentlists.size() < Constants.PAGE_SIZE) {
            commentListAdapter.loadMoreEnd(true);
        } else {
            commentListAdapter.loadMoreComplete();
        }
        //视频
        if (typeVal.equals("0")) {
            showVideo(contentBean);
        }
        //图片
        else {
            showPictures(contentBean);
        }
    }

    /**
     * 视频数据
     *
     * @param contentBean
     */
    private void showVideo(ContentBean contentBean) {
        videoPlayer.loadThumbImage(Utils.getRealUrl(contentBean.E_Img2));
        videoPlayer.setUp(contentBean.E_Path, true, null, "");
        if (NetworkUtils.isWifiConnected()) {
            videoPlayer.autoPlay();
        }
    }


    private void showPictures(ContentBean contentBean) {
        images = contentBean.E_Img3;
        banner.releaseBanner();
        banner.setImages(images);
        banner.start();
    }

    /**
     * 显示头部数据
     */
    private void showHeaderData(ContentBean contentBean) {
        //vip
        headerViewHolder.ivVip.setVisibility(contentBean.E_VerWorker != null && contentBean.E_VerWorker.equals("1") ? View.VISIBLE : View.GONE);
        //头像
        Glide.with(this).load(Utils.getRealUrl(contentBean.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivAvatar);
        //名字
        headerViewHolder.tvName.setText(contentBean.E_CreateUser);
        //时间
        headerViewHolder.tvCreateTime.setText(contentBean.E_CreateDate);
        //浏览
        headerViewHolder.tvSee.setText(contentBean.E_Plays);
        //内容
        headerViewHolder.tvContent.setText(contentBean.E_Content);
        //关联商品
        if (contentBean.goods_data == null || contentBean.goods_data.size() == 0) {
            headerViewHolder.llLinkGoods.setVisibility(View.GONE);
        } else {
            headerViewHolder.llLinkGoods.setVisibility(View.VISIBLE);
            ContentBean.GoodsData goodsData = contentBean.goods_data.get(0);
            Glide.with(this).load(Utils.getRealUrl(goodsData.E_Img1)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivLinkGoods);
            headerViewHolder.tvLinkGoodsName.setText(goodsData.E_Name == null ? "" : goodsData.E_Name);
            headerViewHolder.tvLinkGoodsBrand.setText(goodsData.E_BrandCnName == null ? "" : goodsData.E_BrandCnName);
            headerViewHolder.tvLinkGoodsPrice.setText("￥" + goodsData.E_SellPrice);
        }

        //粉丝贡献
        if (contentBean.member_list.size() == 0) {
            headerViewHolder.llEmptyGive.setVisibility(View.VISIBLE);
            headerViewHolder.llFansGive.setVisibility(View.GONE);
        } else {
            headerViewHolder.llEmptyGive.setVisibility(View.GONE);
            headerViewHolder.llFansGive.setVisibility(View.VISIBLE);
            List<ContentBean.MemberList> memberLists = contentBean.member_list;
            int size = memberLists.size();
            switch (size) {
                case 1:
                    headerViewHolder.llNo1.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo2.setVisibility(View.INVISIBLE);
                    headerViewHolder.llNo3.setVisibility(View.INVISIBLE);
                    headerViewHolder.llNo4.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(0).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo1Avatar);
                    headerViewHolder.tvNo1Content.setText(memberLists.get(0).E_Name);
                    headerViewHolder.tvNo1Num.setText(memberLists.get(0).E_Points);
                    break;
                case 2:
                    headerViewHolder.llNo1.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo2.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo3.setVisibility(View.INVISIBLE);
                    headerViewHolder.llNo4.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(0).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo1Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(1).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo2Avatar);
                    headerViewHolder.tvNo1Content.setText(memberLists.get(0).E_Name);
                    headerViewHolder.tvNo1Num.setText(memberLists.get(0).E_Points);
                    headerViewHolder.tvNo2Content.setText(memberLists.get(1).E_Name);
                    headerViewHolder.tvNo2Num.setText(memberLists.get(1).E_Points);
                    break;
                case 3:
                    headerViewHolder.llNo1.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo2.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo3.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo4.setVisibility(View.INVISIBLE);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(0).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo1Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(1).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo2Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(2).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo3Avatar);
                    headerViewHolder.tvNo1Content.setText(memberLists.get(0).E_Name);
                    headerViewHolder.tvNo1Num.setText(memberLists.get(0).E_Points);
                    headerViewHolder.tvNo2Content.setText(memberLists.get(1).E_Name);
                    headerViewHolder.tvNo2Num.setText(memberLists.get(1).E_Points);
                    headerViewHolder.tvNo3Content.setText(memberLists.get(2).E_Name);
                    headerViewHolder.tvNo3Num.setText(memberLists.get(2).E_Points);
                    break;
                case 4:
                    headerViewHolder.llNo1.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo2.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo3.setVisibility(View.VISIBLE);
                    headerViewHolder.llNo4.setVisibility(View.VISIBLE);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(0).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo1Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(1).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo2Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(2).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo3Avatar);
                    Glide.with(this).load(Utils.getRealUrl(memberLists.get(3).E_HeadImg)).apply(new RequestOptions().centerCrop()).into(headerViewHolder.ivNo4Avatar);
                    headerViewHolder.tvNo1Content.setText(memberLists.get(0).E_Name);
                    headerViewHolder.tvNo1Num.setText(memberLists.get(0).E_Points);
                    headerViewHolder.tvNo2Content.setText(memberLists.get(1).E_Name);
                    headerViewHolder.tvNo2Num.setText(memberLists.get(1).E_Points);
                    headerViewHolder.tvNo3Content.setText(memberLists.get(2).E_Name);
                    headerViewHolder.tvNo3Num.setText(memberLists.get(2).E_Points);
                    headerViewHolder.tvNo4Content.setText(memberLists.get(3).E_Name);
                    headerViewHolder.tvNo4Num.setText(memberLists.get(3).E_Points);
                    break;
            }
        }
        //关注状态
        String eIsConcern = contentBean.E_IsConcern;
        int isConcern = NumberUtils.parseInt(eIsConcern);
        if (TextUtils.isEmpty(mUserID)) {
            headerViewHolder.ivFollow.setImageResource(R.mipmap.attention_3x);
        } else {
            switch (isConcern) {
                //单关注
                case 0:
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.followed_new_3x);
                    break;
                //已相互关注
                case 1:
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.a_together_3x);
                    break;
                //单粉丝
                case 2:
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.attention_3x);
                    break;
                //相互未关注
                case 3:
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.attention_3x);
                    break;
            }
        }
    }

    /**
     * 加载更多评论失败
     *
     * @param errorMsg
     */
    @Override
    public void loadMoreCommentError(String errorMsg) {
        isLoadMoreFail = true;
        commentListAdapter.loadMoreFail();
    }

    /**
     * 增加一条评论
     *
     * @param commentLists
     */
    @Override
    public void addComments(List<ContentBean.CommentList> commentLists) {
        isLoadMoreFail = false;
        commentListAdapter.addData(commentLists);
        if (commentLists.size() < Constants.PAGE_SIZE) {
            commentListAdapter.loadMoreEnd(true);
        } else {
            commentListAdapter.loadMoreComplete();
        }
        headerViewHolder.tvCommentTips.setVisibility(View.VISIBLE);
    }

    /**
     * 添加关注成功
     */
    @Override
    public void addFollowSuccess() {
        dismissLoading();
        if (mContentBean != null) {
            String eIsConcern = mContentBean.E_IsConcern;
            int isConcern = NumberUtils.parseInt(eIsConcern);
            switch (isConcern) {
                //单粉丝变相互关注
                case 2:
                    mContentBean.E_IsConcern = "1";
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.a_together_3x);
                    break;
                //相互未关注 变单关注
                case 3:
                    mContentBean.E_IsConcern = "0";
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.followed_new_3x);
                    break;
            }
        }
    }

    /**
     * 添加关注失败
     *
     * @param msg
     */
    @Override
    public void addFollowError(String msg) {
        dismissLoading();
        ToastUtils.showError(msg);
    }

    /**
     * 取消关注成功
     */
    @Override
    public void canccelFollowSuccess() {
        dismissLoading();
        if (mContentBean != null) {
            String eIsConcern = mContentBean.E_IsConcern;
            int isConcern = NumberUtils.parseInt(eIsConcern);
            switch (isConcern) {
                //单关注 变相互未关注
                case 0:
                    mContentBean.E_IsConcern = "3";
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.attention_3x);
                    break;
                //相互关注 变粉丝单关注
                case 1:
                    mContentBean.E_IsConcern = "2";
                    headerViewHolder.ivFollow.setImageResource(R.mipmap.attention_3x);
                    break;
            }
        }
    }

    /**
     * 评论成功
     *
     * @param commentBean
     */
    @Override
    public void commentSuccess(ContentBean.CommentList commentBean) {
        commentListAdapter.addData(0, commentBean);
        if (mContentBean != null) {
            String eComments = mContentBean.E_Comments;
            boolean isNumber = NumberUtils.isNumeric(eComments);
            if (isNumber) {
                int newComments = NumberUtils.parseInt(eComments) + 1;
                mContentBean.E_Comments = newComments + "";
                tvCommentNum.setText(newComments > 9999 ? "1万" : newComments + "");
            }
        }
    }

    /**
     * 评论失败
     *
     * @param errorMsg
     */
    @Override
    public void commentError(String errorMsg) {
        ToastUtils.showError(errorMsg);
    }

    /**
     * 点赞或取消点赞结果
     *
     * @param position
     * @param clicks
     * @param fabulous
     */
    @Override
    public void likeResult(int position, String clicks, String fabulous) {
        Logger.e("position ===> " + position);
        if (clicks.equals("-1")) {
            return;
        }
        ContentBean.CommentList item = commentListAdapter.getItem(position);
        item.E_Clicks = clicks;
        item.E_Fabulous = fabulous;
        commentListAdapter.notifyItemChanged(position + 1);
    }

    /**
     * 显示礼物列表数据
     *
     * @param liWuBeen
     */
    @Override
    public void showGiftList(List<GiftModel> liWuBeen) {
        giftListLayout.showGiftDatas(liWuBeen);
    }

    /**
     * 获取礼物列表数据失败
     */
    @Override
    public void requestGiftFail() {
        giftListLayout.requestGiftFail();
    }

    /**
     * 送礼成功
     *
     * @param cardBeens
     */
    @Override
    public void sendGiftSuccess(String isRed, List<CardBean> cardBeens) {
        giftListLayout.sendGiftSuccess(isRed, cardBeens);
    }

    /**
     * 送礼失败
     */
    @Override
    public void sendGiftFail() {
        giftListLayout.sendGiftFail();
    }

    /**
     * 取消关注失败
     *
     * @param msg
     */
    @Override
    public void canccelFollowError(String msg) {
        dismissLoading();
        ToastUtils.showError(msg);
    }

    /**
     * 请求礼物列表数据
     *
     * @param uid
     */
    @Override
    public void requestGiftList(String uid) {
        if (mContentBean != null) {
            mPresenter.requestGift(mContentBean.is_Float, uid);
        }
    }

    /**
     * 送礼请求
     *
     * @param giftNum
     * @param giftId
     * @param member
     * @param cmember
     * @param type
     * @param OID
     */
    @Override
    public void sendGift(int giftNum, String giftId, String member, String cmember, String type, String OID) {
        mPresenter.sendGift(giftNum, giftId, member, cmember, type, OID);
    }

    @Override
    public void onAvatarClick() {
        if (mContentBean != null) {
            startActivity(MyHomeActivity.startIntent(this, mContentBean.E_MemberID));
        }
    }

    @Override
    public void onNo1AvatarClick() {
        if (mContentBean != null) {
            startActivity(MyHomeActivity.startIntent(this
                    , mContentBean.member_list.get(0).E_CMemberID));
        }
    }

    @Override
    public void onNo2AvatarClick() {
        if (mContentBean != null) {
            startActivity(MyHomeActivity.startIntent(this
                    , mContentBean.member_list.get(1).E_CMemberID));
        }
    }

    @Override
    public void onNo3AvatarClick() {
        if (mContentBean != null) {
            startActivity(MyHomeActivity.startIntent(this
                    , mContentBean.member_list.get(2).E_CMemberID));
        }
    }

    @Override
    public void onNo4AvatarClick() {
        if (mContentBean != null) {
            startActivity(MyHomeActivity.startIntent(this
                    , mContentBean.member_list.get(3).E_CMemberID));
        }
    }

    @Override
    public void onLinkGoodsClick() {
        if (mContentBean != null && mContentBean.goods_data != null && mContentBean.goods_data.size() > 0) {
            WebViewActivity.getInstance(this, BaseUrl.GET_GOODS_DETAILS_H5_URL +
                    "?soure=g&version=" + Constants.WEB_VERSION
                    + "&devices=android"
                    + "&ID=" + mContentBean.goods_data.get(0).ID
                    + "&BID=" + contentId
                    + "&SUID=" + mContentBean.E_MemberID);
        }
    }

    @Override
    public void onFollowClick() {
        if (TextUtils.isEmpty(mUserID)) {
            startActivity(LoginActivity.startIntent(this));
            return;
        }

        if (mContentBean != null) {

            if (mUserID.equals(mContentBean.E_MemberID)) {
                ToastUtils.showWarning("不能关注自己");
                return;
            }

            String eIsConcern = mContentBean.E_IsConcern;
            int isConcern = NumberUtils.parseInt(eIsConcern);
            switch (isConcern) {
                //单关注
                case 0:
                    //已相互关注
                case 1:
                    TipsDialog.newInstance("您确定不再关注" + mContentBean.E_CreateUser + "了吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                        @Override
                        public void onSure() {
                            showLoading();
                            mPresenter.cancelFollow(mUserID, mContentBean.E_MemberID);
                        }

                        @Override
                        public void onCancel() {
                        }
                    }).setMargin(52).show(getSupportFragmentManager());
                    break;
                //单粉丝
                case 2:
                    //相互未关注
                case 3:
                    showLoading();
                    mPresenter.addFollow(mContentBean.E_MemberID
                            , mContentBean.E_CreateUser, 1
                            , mUserID, mUserName);
                    break;
            }
        }
    }

    @Override
    public void showGuide2(View guide2View, final View guide3View) {
        guide2 = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        isGuide2Show = true;
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        isGuide2Show = false;
                        if (!isBackHideGuide) {
                            showGuide3(guide3View);
                        }
                        guide2 = null;
                    }
                })
                .setLabel("guide2")
                .addHighLight(guide2View, HighLight.Type.RECTANGLE)
                .setLayoutRes(R.layout.view_guide2)
                .alwaysShow(true)
                .show();
    }

    private void showGuide3(View view) {
        guide3 = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        isGuide3Show = true;
                        SPUtils.getInstance().put(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE, "0");
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        isGuide3Show = false;
                        guide3 = null;
                    }
                })
                .setLabel("guide3")
                .addHighLight(view, HighLight.Type.RECTANGLE)
                .setLayoutRes(R.layout.view_guide3)
                .alwaysShow(true)
                .show();
    }

    /**
     * hewderview holder
     */
    static class HeaderViewHolder {
        private Unbinder headerBind;
        private Context context;

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_username)
        TextView tvName;
        @BindView(R.id.tv_createtime)
        TextView tvCreateTime;
        @BindView(R.id.tv_see)
        TextView tvSee;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_follow)
        ImageView ivFollow;
        @BindView(R.id.ll_link_goods)
        LinearLayout llLinkGoods;
        @BindView(R.id.iv_link_goods)
        ImageView ivLinkGoods;
        @BindView(R.id.tv_link_goods_name)
        TextView tvLinkGoodsName;
        @BindView(R.id.tv_link_goods_brand)
        TextView tvLinkGoodsBrand;
        @BindView(R.id.tv_link_goods_price)
        TextView tvLinkGoodsPrice;
        @BindView(R.id.ll_fans_give)
        LinearLayout llFansGive;
        @BindView(R.id.iv_no_1_avatar)
        ImageView ivNo1Avatar;
        @BindView(R.id.iv_no_2_avatar)
        ImageView ivNo2Avatar;
        @BindView(R.id.iv_no_3_avatar)
        ImageView ivNo3Avatar;
        @BindView(R.id.iv_no_4_avatar)
        ImageView ivNo4Avatar;
        @BindView(R.id.tv_no_1_content)
        TextView tvNo1Content;
        @BindView(R.id.tv_no_2_content)
        TextView tvNo2Content;
        @BindView(R.id.tv_no_3_content)
        TextView tvNo3Content;
        @BindView(R.id.tv_no_4_content)
        TextView tvNo4Content;
        @BindView(R.id.tv_no_1_num)
        TextView tvNo1Num;
        @BindView(R.id.tv_no_2_num)
        TextView tvNo2Num;
        @BindView(R.id.tv_no_3_num)
        TextView tvNo3Num;
        @BindView(R.id.tv_no_4_num)
        TextView tvNo4Num;
        @BindView(R.id.iv_vip)
        ImageView ivVip;
        @BindView(R.id.ll_fans_give_no_1)
        LinearLayout llNo1;
        @BindView(R.id.ll_fans_give_no_2)
        LinearLayout llNo2;
        @BindView(R.id.ll_fans_give_no_3)
        LinearLayout llNo3;
        @BindView(R.id.ll_fans_give_no_4)
        LinearLayout llNo4;
        @BindView(R.id.ll_empty_give)
        LinearLayout llEmptyGive;
        @BindView(R.id.tv_comment_tip)
        TextView tvCommentTips;

        private OnHeaderViewOnclickListener onHeaderViewOnclickListener;

        public void setOnHeaderViewOnclickListener(OnHeaderViewOnclickListener onHeaderViewOnclickListener) {
            this.onHeaderViewOnclickListener = onHeaderViewOnclickListener;
        }

        @OnClick({R.id.iv_avatar, R.id.ll_link_goods, R.id.iv_follow, R.id.iv_no_1_avatar, R.id.iv_no_2_avatar, R.id.iv_no_3_avatar, R.id.iv_no_4_avatar})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.iv_avatar:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onAvatarClick();
                    }
                    break;
                case R.id.ll_link_goods:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onLinkGoodsClick();
                    }
                    break;
                case R.id.iv_follow:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onFollowClick();
                    }
                    break;
                case R.id.iv_no_1_avatar:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onNo1AvatarClick();
                    }
                    break;
                case R.id.iv_no_2_avatar:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onNo2AvatarClick();
                    }
                    break;
                case R.id.iv_no_3_avatar:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onNo3AvatarClick();
                    }
                    break;
                case R.id.iv_no_4_avatar:
                    if (onHeaderViewOnclickListener != null) {
                        onHeaderViewOnclickListener.onNo4AvatarClick();
                    }
                    break;
            }
        }


        public HeaderViewHolder(Context context, View headerView) {
            this.context = context.getApplicationContext();
            headerBind = ButterKnife.bind(this, headerView);
            int screenWidth = Utils.getScreenWidth(this.context);
            int marginCount = Utils.dip2px(this.context, 28 * 5);
            int ivNoWidth = (screenWidth - marginCount) / 4;
            ivNo1Avatar.getLayoutParams().width = ivNoWidth;
            ivNo1Avatar.getLayoutParams().height = ivNoWidth;
            ivNo2Avatar.getLayoutParams().width = ivNoWidth;
            ivNo2Avatar.getLayoutParams().height = ivNoWidth;
            ivNo3Avatar.getLayoutParams().width = ivNoWidth;
            ivNo3Avatar.getLayoutParams().height = ivNoWidth;
            ivNo4Avatar.getLayoutParams().width = ivNoWidth;
            ivNo4Avatar.getLayoutParams().height = ivNoWidth;
        }

        public void destory() {
            headerBind.unbind();
        }
    }
}
