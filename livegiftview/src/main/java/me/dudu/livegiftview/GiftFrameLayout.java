package me.dudu.livegiftview;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Author : zhouyx
 * Date   : 2017/8/2
 * Description : 礼物布局
 */
public class GiftFrameLayout extends FrameLayout {

    /**
     * 礼物停止连击动画后继续展示的时间
     */
    private static final int GIFT_DISMISS_TIME = 3000;
    /**
     * 礼物UI
     */
    private AbsGiftViewHolder mViewHolder;
    /**
     * 是否开启礼物动画隐藏模式（如果两个礼物动画同时显示，并且第一个优先结束，第二个礼物的位置会移动到第一个位置上去）
     */
    private boolean isHideMode = false;
    /**
     * 自定义动画的接口
     */
    private ICustomerAnimation mAnimation;
    /**
     * 默认动画的接口
     */
    private ICustomerAnimation mDefaultAnimation = new CustomerAnimation();
    private IGiftAnimationStatusListener mGiftAnimationListener;
    /**
     * item 显示位置
     */
    private int mIndex;
    /**
     * 礼物信息
     */
    private GiftModel mGiftModel;
    /**
     * 礼物连击数
     */
    private int mComboTotalCount;
    /**
     * 礼物正在显示，判断在播放礼物退出动画前的状态，在这期间可触发连击效果
     */
    private boolean isGiftShowing = false;
    /**
     * 礼物完全结束
     */
    private boolean isGiftCompletelyEnd = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mGiftAnimationListener != null) {
                        mGiftAnimationListener.dismiss(mIndex);
                    }
                    break;
            }
        }
    };

    public GiftFrameLayout(Context context) {
        this(context, null);
    }

    public GiftFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewHolder(AbsGiftViewHolder viewHolder) {
        mViewHolder = viewHolder;
        addView(viewHolder.initGiftView(getContext()));
    }

    public void setHideMode(boolean isHideMode) {
        this.isHideMode = isHideMode;
    }

    public void setGiftAnimationListener(IGiftAnimationStatusListener giftAnimationListener) {
        this.mGiftAnimationListener = giftAnimationListener;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setAnimation(ICustomerAnimation animation) {
        this.mAnimation = animation;
    }

    /**
     * 获取当前显示礼物id
     */
    public String getCurrentGiftId() {
        if (mGiftModel != null) {
            return mGiftModel.getID();
        }
        return null;
    }

    /**
     * 更新礼物数量
     */
    public void updateGiftCount(int count) {
        mComboTotalCount = count;
        mGiftModel.setSendGiftCount(mComboTotalCount);
        mViewHolder.getGiftNumberView().setText("x " + mComboTotalCount);
        startNumberComboAnimation();

        delayDismiss();
    }

    public void updateUserBean() {
        mViewHolder.updateUserBean();
    }

    public void delayDismiss() {
        mHandler.removeMessages(1);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(1, GIFT_DISMISS_TIME);
    }

    /**
     * 初始化礼物控件状态
     */
    public void initGiftLayout() {
        GiftAnimationUtil.createAlphaAnimator(this, 1.0f, 0f);
    }

    /**
     * 加载礼物到当前控件
     */
    public boolean loadGift(GiftModel giftModel) {
        if (giftModel == null) {
            return false;
        }
        mGiftModel = giftModel;

        mViewHolder.loadGiftModelToView(getContext(), mGiftModel);

        mComboTotalCount = mGiftModel.getSendGiftCount();
        mViewHolder.getGiftNumberView().setText("x " + mComboTotalCount);
        return true;
    }

    /**
     * 动画开始时回调
     */
    public void initGiftLayoutStatus() {
        setVisibility(View.VISIBLE);
        setAlpha(1f);
        isGiftShowing = true;
        isGiftCompletelyEnd = false;
    }

    public boolean isGiftShowing() {
        return isGiftShowing;
    }

    /**
     * 开始播放礼物进入动画
     */
    public AnimatorSet startGiftEnterAnimation() {
        if (mAnimation == null) {
            return mDefaultAnimation.giftEnterAnimation(this, mViewHolder);
        } else {
            return mAnimation.giftEnterAnimation(this, mViewHolder);
        }
    }

    /**
     * 开始播放连击动画
     */
    public AnimatorSet startNumberComboAnimation() {
        if (mAnimation == null) {
            return mDefaultAnimation.giftNumberComboAnimation(this, mViewHolder);
        } else {
            return mAnimation.giftNumberComboAnimation(this, mViewHolder);
        }
    }

    /**
     * 开始播放礼物退出动画
     */
    public AnimatorSet endGiftExitAnimation() {
        if (mAnimation == null) {
            return mDefaultAnimation.giftExitAnimation(this, mViewHolder);
        } else {
            return mAnimation.giftExitAnimation(this, mViewHolder);
        }
    }

    public boolean isGiftCompletelyEnd() {
        return isGiftCompletelyEnd;
    }

    /**
     * 销毁控件
     */
    public void destroy() {
        initGiftLayout();
        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;//这里要置位null，否则当前页面销毁时，正在执行的礼物动画会造成内存泄漏
        }
        mGiftAnimationListener = null;
    }

    public void hide() {
        initGiftLayout();
        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 设置动画结束后布局是否向上移动
     */
    public void setGiftExitAnimationEndVisibility(boolean hasGift) {
        if (isHideMode && hasGift) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.INVISIBLE);
        }
    }

    public void setGiftExitAnimationEndVisibility() {
        setVisibility(View.INVISIBLE);
    }

    /**
     * 停止继续产生连击效果
     */
    public void stopCombo() {
        mComboTotalCount = 1;
        isGiftShowing = false;
    }

    /**
     * 设置完全结束状态（当前礼物动画完全结束，可接收下一个礼物进行展示）
     */
    public void giftCompletelyEnd() {
        this.isGiftCompletelyEnd = true;
    }

    public interface IGiftAnimationStatusListener {
        void dismiss(int index);
    }

}
