package cn.biggar.biggar.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.BuyStarActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.RedPacketDialog;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import fj.mtsortbutton.lib.Interface.IDynamicSore;
import me.dudu.livegiftview.CustomerAnimation;
import me.dudu.livegiftview.GiftController;
import me.dudu.livegiftview.GiftFrameLayout;
import me.dudu.livegiftview.GiftModel;

/**
 * Created by Chenwy on 2017/8/29.
 */

public class GiftListLayout extends RelativeLayout implements IDynamicSore, View.OnClickListener, MultiClickButton.OnMultiClickListener {
    private Context mContext;
    private LinearLayout llBottom;
    private DynamicSoreView dynamicSoreView;
    private SuperButton tvSendGift;
    private MultiClickButton multiClickButton;
    private GiftFrameLayout giftFrameLayout1;
    private GiftFrameLayout giftFrameLayout2;
    private RelativeLayout rlRedPacket;
    private TextView tvRecharge;
    private TextView tvRedPacketNum;
    private TextView balanceOfAaccount;
    private ImageView ivFullGift;

    private AnimatorSet animIn;
    private AnimatorSet animOut;

    private String uid;
    private List<GiftModel> mLiWuBeens;
    private Map<Integer, GiftItemAdapter> adapters = new HashMap<>();
    private GiftModel clickLiWuBean;
    private boolean isRequestGift;
    private UserBean mUserBean;
    private boolean isShow;

    private GiftController giftController = new GiftController();
    private String mMemberId;
    private String mVideoId;
    private List<CardBean> cardBeens;
    private OnGiftDataSendListener onGiftDataSendListener;
    private OnShowGuideListener onShowGuideListener;
    private boolean isMe;

    /**
     * 是否前两次获得红包
     */
    private boolean isFirstShowBig;

    /**
     * 大红包是否正在显示
     */
    private boolean isShowingBig;

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ImageView ivFullGift = (ImageView) msg.obj;
                    ivFullGift.setVisibility(GONE);
                    break;
            }
        }
    };

    public GiftListLayout(Context context) {
        this(context, null);
    }

    public GiftListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.gift_list_layout, this);
        mUserBean = Preferences.getUserBean(mContext);
        if (mUserBean != null) {
            uid = mUserBean.getId();
        }
        cardBeens = new ArrayList<>();
        initView();

        //appendGiftFrameLayout(礼物布局，礼物视图，动画效果，隐藏属性)
        giftController.appendGiftFrameLayout(giftFrameLayout1, new GiftViewHolder(), new CustomerAnimation(), true)
                .appendGiftFrameLayout(giftFrameLayout2, new GiftViewHolder(), new CustomerAnimation(), true);

        initAnim();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        setClickable(false);
    }

    private void initView() {
        ivFullGift = (ImageView) findViewById(R.id.iv_full_gift);
        giftFrameLayout1 = (GiftFrameLayout) findViewById(R.id.layout_gift_1);
        giftFrameLayout2 = (GiftFrameLayout) findViewById(R.id.layout_gift_2);
        rlRedPacket = (RelativeLayout) findViewById(R.id.rl_redpacket);
        rlRedPacket.setOnClickListener(this);
        tvRecharge = (TextView) findViewById(R.id.tv_recharge);
        tvRecharge.setOnClickListener(this);
        tvRedPacketNum = (TextView) findViewById(R.id.tv_red_packet_num);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        llBottom.setVisibility(GONE);
        llBottom.setClickable(false);
        ((RelativeLayout.LayoutParams) rlRedPacket.getLayoutParams()).bottomMargin = Utils.dip2px(mContext, 48);
        tvSendGift = (SuperButton) findViewById(R.id.tv_send_gift);
        tvSendGift.setOnClickListener(this);
        multiClickButton = (MultiClickButton) findViewById(R.id.multi_click_button);
        multiClickButton.setOnMultiClickListener(this);
        dynamicSoreView = (DynamicSoreView) findViewById(R.id.dynamicSoreView);
        dynamicSoreView.setiDynamicSore(this);
        balanceOfAaccount = (TextView) findViewById(R.id.balance_of_account);
        balanceOfAaccount.setText(mUserBean == null || TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points());
        mLiWuBeens = new ArrayList<>();
        dynamicSoreView.setGridView(R.layout.item_gift_list).init(mLiWuBeens);
    }

    private void initAnim() {
        float height = Utils.dip2px(mContext, 289);
        float packetHeight = Utils.dip2px(mContext, 68);

        ObjectAnimator moveIn = ObjectAnimator.ofFloat(llBottom, "translationY", height, 0f);
        animIn = new AnimatorSet();
        animIn.play(moveIn);
        animIn.setDuration(200);
        animIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setClickable(true);
                llBottom.setClickable(true);
                if (!isRequestGift) {
                    requestGift();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        ObjectAnimator moveOut = ObjectAnimator.ofFloat(llBottom, "translationY", 0f, height);
        animOut = new AnimatorSet();
        animOut.play(moveOut);
        animOut.setDuration(200);
        animOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setBackgroundResource(R.color.transparent);
                setClickable(false);
                llBottom.setClickable(false);
                llBottom.setVisibility(GONE);
                ((RelativeLayout.LayoutParams) rlRedPacket.getLayoutParams()).bottomMargin = Utils.dip2px(mContext, 48);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public boolean isShow() {
        return isShow;
    }

    public void show(boolean isMe) {
        Logger.e("show......");
        this.isMe = isMe;
        isShow = true;
        llBottom.setVisibility(VISIBLE);
        ((RelativeLayout.LayoutParams) rlRedPacket.getLayoutParams()).bottomMargin = 0;
        startShowAnim();
        int item = SPUtils.getInstance().getInt(Constants.CHOOSE_GIFT_ITEM, 0);
        dynamicSoreView.setCurItem(item);
    }

    public void setMemberId(String memberId) {
        this.mMemberId = memberId;
    }

    public void setVideoId(String videoId) {
        this.mVideoId = videoId;
    }

    /**
     * 请求礼物列表数据
     */
    private void requestGift() {
        if (onGiftDataSendListener != null) {
            onGiftDataSendListener.requestGiftList(uid);
        }
    }

    /**
     * 从服务器获取礼物数据成功
     *
     * @param liWuBean
     */
    public void showGiftDatas(List<GiftModel> liWuBean) {
        isRequestGift = true;

        String cacheGiftId = SPUtils.getInstance().getString(Constants.CHOOSE_GIFT_ID);
        if (!TextUtils.isEmpty(cacheGiftId)) {
            for (GiftModel giftModel : liWuBean) {
                if (giftModel.getID().equals(cacheGiftId)) {
                    giftModel.setCheck(true);
                    giftModel.setE_Num(1);
                    clickLiWuBean = giftModel;
                    break;
                }
            }
        } else {
            if (liWuBean.size() > 0) {
                GiftModel giftModel = liWuBean.get(0);
                giftModel.setCheck(true);
                giftModel.setE_Num(1);
                clickLiWuBean = giftModel;
            }
        }
        mLiWuBeens = liWuBean;
        dynamicSoreView.setGridView(R.layout.item_gift_list).init(mLiWuBeens);
        int item = SPUtils.getInstance().getInt(Constants.CHOOSE_GIFT_ITEM, 0);
        dynamicSoreView.setCurItem(item);
    }

    /**
     * 从服务器获取礼物数据失败
     */
    public void requestGiftFail() {
        isRequestGift = false;
        ToastUtils.showError("礼物获取失败");
    }

    public void hide() {
        Logger.e("hide......");
        isShow = false;
        startHideAnim();
        multiClickButton.reset();
    }

    private void startShowAnim() {
        animIn.cancel();
        animOut.cancel();
        animIn.start();
    }

    private void startHideAnim() {
        animIn.cancel();
        animOut.cancel();
        animOut.start();
    }

    @Override
    public void setGridView(View view, final int i, List list) {
        Logger.e("set GridView : " + i);
        RecyclerView rvGift = (RecyclerView) view.findViewById(R.id.rv_gift);
        final GiftItemAdapter giftItemAdapter = new GiftItemAdapter(R.layout.item_gift, list);
        giftItemAdapter.bindToRecyclerView(rvGift);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        rvGift.setLayoutManager(gridLayoutManager);
        rvGift.setAdapter(giftItemAdapter);
        giftItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.e("item click : " + position);

                GiftModel liWuBean = giftItemAdapter.getItem(position);
                if (clickLiWuBean != null && liWuBean != null && liWuBean.getID().equals(clickLiWuBean.getID())) {
                    return;
                }

                multiClickButton.reset();

                clearAllChoose();
                clickLiWuBean = liWuBean;
                SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ID, clickLiWuBean.getID());
                SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ITEM, i);
                clickLiWuBean.setE_Num(1);
                clickLiWuBean.setCheck(true);
                giftItemAdapter.notifyDataSetChanged();
            }
        });
        adapters.put(i, giftItemAdapter);

        if (i == 0 && list.size() > 0) {
            String isShouldShowGuide = SPUtils.getInstance().getString(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE);
            if (TextUtils.isEmpty(isShouldShowGuide) || isShouldShowGuide.equals("1")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onShowGuideListener != null){
                            onShowGuideListener.showGuide2(adapters.get(0).getViewByPosition(0, R.id.ll_bg),tvSendGift);
                        }
                    }
                }, 200);
            }
        }
    }

    /**
     * 清除所有选择
     */
    private void clearAllChoose() {
        for (Map.Entry<Integer, GiftItemAdapter> entry : adapters.entrySet()) {
            int p = entry.getKey();
            GiftItemAdapter giftItemAdapter = adapters.get(p);
            for (int i = 0; i < giftItemAdapter.getData().size(); i++) {
                GiftModel liWuBean = giftItemAdapter.getData().get(i);
                if (liWuBean.getE_Num() > 0) {
                    liWuBean.setE_Num(0);
                }

                if (liWuBean.isCheck()) {
                    liWuBean.setCheck(false);
                }
            }

            giftItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_gift:
                if (clickLiWuBean == null || clickLiWuBean.getE_Num() == 0) {
                    ToastUtils.showWarning("请选择您要送的礼物");
                    return;
                }

                if(isMe){
                    ToastUtils.showWarning("不能给自己送礼");
                    return;
                }

                if (!isEnoughMoneyToSengift()) {
                    ToastUtils.showWarning("星币不足，请先充值");
                    return;
                }


                tvSendGift.setVisibility(INVISIBLE);
                multiClickButton.setVisibility(VISIBLE);
                multiClickButton.start();
//                if (clickLiWuBean.getE_Combo() != null && clickLiWuBean.getE_Combo().equals("1")) {
//                    tvSendGift.setVisibility(INVISIBLE);
//                    multiClickButton.setVisibility(VISIBLE);
//                    multiClickButton.start();
//                } else {
//                    sendFullGift();
//                }
                break;
            case R.id.rl_redpacket:
                isFirstShowBig = false;
                showRedPacketPop(cardBeens.get(0));
                break;
            case R.id.tv_recharge:
                mContext.startActivity(new Intent(mContext, BuyStarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }

    private void pushClickPoints() {
        if (!clickLiWuBean.getE_KPoints().equals("0")) {
            int clickPoints = NumberUtils.parseInt(clickLiWuBean.getE_KPoints());
            String allPoints = mUserBean == null || TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points();
            int v = NumberUtils.parseInt(allPoints) + clickPoints;
            balanceOfAaccount.setText(v + "");
            mUserBean.setE_Points(v + "");
            Preferences.storeUserBean(getContext(), mUserBean);
        } else {
            addFreeGiftCount();
        }
    }

    private void removeClickPoints() {
        if (!clickLiWuBean.getE_KPoints().equals("0")) {
            int clickPoints = NumberUtils.parseInt(clickLiWuBean.getE_KPoints());
            String allPoints = mUserBean == null || TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points();
            int v = NumberUtils.parseInt(allPoints) - clickPoints;
            balanceOfAaccount.setText(v + "");
            mUserBean.setE_Points(v + "");
            Preferences.storeUserBean(getContext(), mUserBean);
        }
    }

    /**
     * 查询是否有红包
     *
     * @param cardBeens
     */
    private void queryWelfare(List<CardBean> cardBeens) {
        if (cardBeens.size() > 0) {
            //用户如果获得的红包次数>2，则放进小红包
            if (cardBeens.get(0).getE_redNum().equals("1")) {
                isFirstShowBig = false;
                if (rlRedPacket.getVisibility() == View.INVISIBLE) {
                    showRedPacketWithAnim(cardBeens);
                } else {
                    showRedPacket(cardBeens);
                }
            }
            //用户如果获得的红包次数<=2，则直接弹出拆红包框
            else {
                //如果没有显示大红包，则直接显示大红包
                if (!isShowingBig) {
                    cardBeens.add(cardBeens.get(0));
                    showRedPacketPop(cardBeens.get(cardBeens.size() - 1));
                    isFirstShowBig = true;
                }
                //如果大红包正在显示，则后面的直接放进小红包
                else {
                    showRedPacket(cardBeens);
                }
            }
        }
    }

    /**
     * 弹出大红包
     *
     * @param cardBean
     */
    private void showRedPacketPop(final CardBean cardBean) {
        new RedPacketDialog(mContext)
                .setCardBean(cardBean)
                .setUid(uid)
                .setOnPackGetListener(new RedPacketDialog.OnPackGetListener() {
                    @Override
                    public void OnPackGet(String cardId) {
                        cardBeens.remove(cardBean);
                        if (cardBeens == null || cardBeens.size() == 0) {
                            rlRedPacket.setVisibility(View.INVISIBLE);
                        } else {
                            rlRedPacket.setVisibility(View.VISIBLE);
                        }
                        tvRedPacketNum.setText(cardBeens.size() > 99 ? "99" : cardBeens.size() + "");
                        mUserBean = Preferences.getUserBean(getContext());
                        balanceOfAaccount.setText(TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points());
                    }

                    @Override
                    public void onDissmiss(boolean isGet) {
                        isShowingBig = false;
                        if (isFirstShowBig && !isGet) {
                            cardBeens.add(cardBean);
                        }

                        if (cardBeens == null || cardBeens.size() == 0) {
                            rlRedPacket.setVisibility(View.INVISIBLE);
                        } else {
                            rlRedPacket.setVisibility(View.VISIBLE);
                        }
                        tvRedPacketNum.setText(cardBeens.size() > 99 ? "99" : cardBeens.size() + "");
                    }
                })
                .openDissmissListener()
                .showDailog();
        isShowingBig = true;
    }

    public void showRedPacketWithAnim(List<CardBean> cbs) {
        showRedPacket(cbs);
        startSmallRedPacketAnimation(rlRedPacket);
    }

    /**
     * 小红包动画
     */
    private void startSmallRedPacketAnimation(View view) {
        ObjectAnimator animx = ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 1f);
        ObjectAnimator animy = ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.play(animx).with(animy);
        set.setDuration(500);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
    }

    public void showRedPacket(List<CardBean> cbs) {
        cardBeens.addAll(cbs);
        rlRedPacket.setVisibility(VISIBLE);
        tvRedPacketNum.setText(cardBeens.size() > 99 ? "99" : cardBeens.size() + "");
    }

    @Override
    public void onMultiClick(int clickNum) {
        //免费的
        if (clickLiWuBean.getE_KPoints().equals("0")) {
            sendFreeGift(clickNum);
        }
        //要星币
        else {
            sendMoneyGift(clickNum);
        }
    }

    /**
     * 全屏礼物
     */
    private void sendFullGift() {
        //免费的
        if (clickLiWuBean.getE_KPoints().equals("0")) {
            sendFreeGift(1);
        }
        //要星币
        else {
            sendMoneyGift(1);
        }
    }

    /**
     * 送免费的礼物
     */
    private void sendFreeGift(int clickNum) {
        int counts = clickLiWuBean.getCounts();
        if (counts <= 0) {
            ToastUtils.showError("今天" + clickLiWuBean.getE_Name() + "免费次数已用完");
            multiClickButton.reset();
            return;
        }
        clickLiWuBean.setSendGiftCount(clickNum);
        removeFreeGiftCount();
        sendGift();
    }

    /**
     * 送需要星币的礼物
     */
    private void sendMoneyGift(int clickNum) {
        if (!isEnoughMoneyToSengift()) {
            ToastUtils.showWarning("星币不足，请先充值");
            multiClickButton.reset();
            return;
        }
        clickLiWuBean.setSendGiftCount(clickNum);
        sendGift();
    }

    /**
     * 送礼
     */
    private void sendGift() {
//        if (clickLiWuBean.getE_Combo() != null && clickLiWuBean.getE_Combo().equals("1")) {
//            giftController.loadGift(clickLiWuBean);
//        } else {
//            ivFullGift.setVisibility(VISIBLE);
//            Glide.with(mContext).load(Utils.getRealUrl(clickLiWuBean.getE_Img2())).apply(new RequestOptions().centerCrop()).into(ivFullGift);
//            Message msg = mHandler.obtainMessage();
//            msg.obj = ivFullGift;
//            msg.what = 0;
//            mHandler.sendMessageDelayed(msg, 3000);
//        }
        giftController.loadGift(clickLiWuBean);
        //向服务器
        if (onGiftDataSendListener != null) {
            onGiftDataSendListener.sendGift(clickLiWuBean.getE_Num(), clickLiWuBean.getID(), mMemberId, uid, "1", mVideoId);
        }
        //扣钱
        removeClickPoints();
    }

    /**
     * 向服务器送礼请求成功
     */
    public void sendGiftSuccess(String isRed, List<CardBean> cardBeens) {
        queryWelfare(cardBeens);
        querySmallRedPacket(isRed);
    }

    private void querySmallRedPacket(String isRed) {
        if (clickLiWuBean.getIs_red().equals("0")) {
            return;
        }

        for (Map.Entry<Integer, GiftItemAdapter> entry : adapters.entrySet()) {
            int p = entry.getKey();
            GiftItemAdapter giftItemAdapter = adapters.get(p);
            for (int i = 0; i < giftItemAdapter.getData().size(); i++) {
                GiftModel liWuBean = giftItemAdapter.getData().get(i);
                if (liWuBean.getID().equals(clickLiWuBean.getID())) {
                    liWuBean.setIs_red(isRed);
                    giftItemAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    /**
     * 向服务器送礼请求失败
     */
    public void sendGiftFail() {
        pushClickPoints();
    }

    /**
     * 减少免费可用次数
     */
    private void removeFreeGiftCount() {
        int counts = clickLiWuBean.getCounts() - 1;
        clickLiWuBean.setCounts(counts);

        for (Map.Entry<Integer, GiftItemAdapter> entry : adapters.entrySet()) {
            int p = entry.getKey();
            GiftItemAdapter giftItemAdapter = adapters.get(p);
            for (int i = 0; i < giftItemAdapter.getData().size(); i++) {
                GiftModel liWuBean = giftItemAdapter.getData().get(i);
                if (clickLiWuBean.getID().equals(liWuBean.getID())) {
                    giftItemAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    /**
     * 增加免费可用次数
     */
    private void addFreeGiftCount() {
        int counts = clickLiWuBean.getCounts() + 1;
        clickLiWuBean.setCounts(counts);

        for (Map.Entry<Integer, GiftItemAdapter> entry : adapters.entrySet()) {
            int p = entry.getKey();
            GiftItemAdapter giftItemAdapter = adapters.get(p);
            for (int i = 0; i < giftItemAdapter.getData().size(); i++) {
                GiftModel liWuBean = giftItemAdapter.getData().get(i);
                if (clickLiWuBean.getID().equals(liWuBean.getID())) {
                    giftItemAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Override
    public void onMultiClickFinish() {
        multiClickButton.setVisibility(GONE);
        tvSendGift.setVisibility(VISIBLE);
    }

    /**
     * 是否有足够的钱送礼
     *
     * @return
     */
    private boolean isEnoughMoneyToSengift() {
        if (mUserBean == null) {
            return false;
        }
        String ePoints = TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points();
        int allMoney = Integer.parseInt(ePoints);
        int giftAllPrice = clickLiWuBean.getE_Num() * (Integer.parseInt(clickLiWuBean.getE_KPoints()));
        return allMoney >= giftAllPrice;
    }

    public void updateUserInfo(UserBean userBean) {
        mUserBean = userBean;
        if (mUserBean != null) {
            uid = mUserBean.getId();
        }
        giftController.updateUserBean();
        balanceOfAaccount.setText(mUserBean == null || TextUtils.isEmpty(mUserBean.getE_Points()) ? "0" : mUserBean.getE_Points());
    }


    static class GiftItemAdapter extends BaseQuickAdapter<GiftModel, BaseViewHolder> {
        private RequestOptions options = new RequestOptions();

        public GiftItemAdapter(int layoutResId, List<GiftModel> data) {
            super(layoutResId, data);
            options.skipMemoryCache(false).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        }

        @Override
        protected void convert(BaseViewHolder helper, GiftModel bean) {
            Glide.with(mContext).load(Utils.getRealUrl(bean.getE_Img1()))
                    .apply(options)
                    .into((ImageView) helper.getView(R.id.iv_gift_image));
            helper.setText(R.id.tv_gift_name, bean.getE_Name() == null ? "" : bean.getE_Name());
            if (bean.getE_KPoints().equals("0")) {
                if (bean.getCounts() == -1) {
                    bean.setCounts(0);
                }
                helper.setText(R.id.tv_gift_price, "免费" + bean.getCounts() + "次");
            } else {
                helper.setText(R.id.tv_gift_price, TextUtils.isEmpty(bean.getE_KPoints()) ? "" : bean.getE_KPoints() + "星币");
            }
            helper.setGone(R.id.iv_gift_frame, bean.isCheck());
            helper.setGone(R.id.tv_gift_combo, bean.getE_Combo() != null && bean.getE_Combo().equals("1"));
            helper.setGone(R.id.iv_small_redpacket, !TextUtils.isEmpty(bean.getIs_red()) && bean.getIs_red().equals("1"));
        }
    }

    public void setOnGiftDataSendListener(OnGiftDataSendListener onGiftDataSendListener) {
        this.onGiftDataSendListener = onGiftDataSendListener;
    }

    public void setOnShowGuideListener(OnShowGuideListener onShowGuideListener) {
        this.onShowGuideListener = onShowGuideListener;
    }

    public interface OnGiftDataSendListener {
        /**
         * 请求礼物数据
         */
        void requestGiftList(String uid);

        /**
         * 送礼请求
         */
        void sendGift(int giftNum, String giftId, String member, String cmember, String type, String OID);
    }

    public interface OnShowGuideListener{
        void showGuide2(View guide2View,View guide3View);
    }

    public void destory() {
        giftController.destroy();
    }
}
