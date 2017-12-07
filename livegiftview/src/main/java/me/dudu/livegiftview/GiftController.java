package me.dudu.livegiftview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Author : zhouyx
 * Date   : 2017/8/2
 * Description : 礼物控制器
 */
public class GiftController {

    private static final String TAG = GiftController.class.getSimpleName();

    /**
     * 礼物队列
     */
    private ArrayList<GiftModel> mGiftQueue = new ArrayList<>();
    /**
     * 礼物控件集合
     */
    private SparseArray<GiftFrameLayout> mGiftLayoutList = new SparseArray<>();

    /**
     * 监听礼物状态
     */
    private GiftFrameLayout.IGiftAnimationStatusListener mLeftGiftAnimationStatusListener = new GiftFrameLayout.IGiftAnimationStatusListener() {
        @Override
        public void dismiss(int index) {
            giftExit(mGiftLayoutList.get(index), index);
        }
    };

    /**
     * 添加一个礼物控件
     *
     * @param frameLayout 礼物控件
     * @param holder      自定义礼物视图样式
     * @param animation   自定义礼物动画
     * @param hideMode    礼物列表展示完成后是否向上移动隐藏
     * @return
     */
    public GiftController appendGiftFrameLayout(@NonNull GiftFrameLayout frameLayout, AbsGiftViewHolder holder, ICustomerAnimation animation, boolean hideMode) {
        int size = mGiftLayoutList.size();
        frameLayout.setIndex(size);
        frameLayout.initGiftLayout();
        frameLayout.setHideMode(hideMode);
        frameLayout.setViewHolder(holder);
        frameLayout.setAnimation(animation);
        frameLayout.setGiftAnimationListener(mLeftGiftAnimationStatusListener);
        mGiftLayoutList.append(size, frameLayout);
        return this;
    }

    /**
     * 加入礼物
     */
    public void loadGift(GiftModel giftModel) {
        loadGift(giftModel, true);
    }

    /**
     * 加入礼物，具有实时连击效果
     *
     * @param gift
     * @param supportCombo 是否支持实时连击，如果为true：支持，否则不支持
     */
    public void loadGift(GiftModel gift, boolean supportCombo) {
        if (mGiftQueue == null) {
            return;
        }
        if (supportCombo) {
            for (int i = 0; i < mGiftLayoutList.size(); i++) {
                GiftFrameLayout giftFrameLayout = mGiftLayoutList.get(i);
                //判断礼物控件是否正在显示，如果没显示的话，则不执行连击动画,添加到队列中，并执行首次显示动画
                if (!giftFrameLayout.isGiftShowing()) {
                    continue;
                }
                //如果已经显示的话，则直接执行连击动画
                if (giftFrameLayout.getCurrentGiftId().equals(gift.getID())) {
                    giftFrameLayout.updateGiftCount(gift.getSendGiftCount());
                    return;
                }
            }
        }
        //如果没显示的话，则不执行连击动画,添加到队列中，并执行首次显示动画
        addGiftToQueue(gift, supportCombo);
    }

    public void updateUserBean() {
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            GiftFrameLayout giftFrameLayout = mGiftLayoutList.get(i);
            giftFrameLayout.updateUserBean();
        }
    }

    /**
     * 添加一个礼物到礼物队列中
     */
    private void addGiftToQueue(final GiftModel gift, final boolean supportCombo) {
        if (mGiftQueue == null) {
            return;
        }
        //如果队列中还没有礼物，则直接添加并显示
        if (mGiftQueue.size() == 0) {
            mGiftQueue.add(gift);
            showNextGift();
            return;
        }
        //如果对列中已经有礼物，则遍历队列
        if (supportCombo) {
            boolean addFlag = false;
            for (GiftModel model : mGiftQueue) {
                if (model.getID().equals(gift.getID())) {
                    model.setSendGiftCount(gift.getSendGiftCount());
                    addFlag = true;
                    break;
                }
            }
            //如果在现有的集合中不存在同一礼物就加入到现有集合中
            if (!addFlag) {
                mGiftQueue.add(gift);
            }
        } else {
            mGiftQueue.add(gift);
        }
    }

    public int getGiftQueueNum() {
        return mGiftLayoutList.size();
    }

    /**
     * 取出队列中的下一个礼物并显示
     */
    private synchronized void showNextGift() {
        if (isEmpty()) {
            return;
        }
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            GiftFrameLayout giftFrameLayout = mGiftLayoutList.get(i);
            if (!giftFrameLayout.isGiftShowing() && giftFrameLayout.isGiftCompletelyEnd()) {
                boolean hasGift = giftFrameLayout.loadGift(getNextGift());
                if (hasGift) {
                    giftFrameLayout.startGiftEnterAnimation();
                    giftFrameLayout.delayDismiss();
                }
            }
        }
    }

    /**
     * 取出队列中的下一个礼物
     */
    private synchronized GiftModel getNextGift() {
        GiftModel gift = null;
        if (mGiftQueue.size() != 0) {
            gift = mGiftQueue.get(0);
            mGiftQueue.remove(0);
        }
        return gift;
    }

    /**
     * 获取正在展示礼物的个数
     */
    public int getShowingGiftFrameLayoutCount() {
        int count = 0;
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            GiftFrameLayout giftFrameLayout = mGiftLayoutList.get(i);
            if (giftFrameLayout.isGiftShowing()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 更新控件状态并播放礼物退出动画,再取队列下一个进行处理
     */
    private void giftExit(final GiftFrameLayout giftFrameLayout, final int index) {
        //动画结束，这时不能触发连击动画
        giftFrameLayout.stopCombo();
        showLog("giftExit: 动画结束");
        AnimatorSet animatorSet = giftFrameLayout.endGiftExitAnimation();
        if (animatorSet != null) {
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    showLog("礼物动画dismiss: index = " + index);
                    //动画完全结束
                    giftFrameLayout.giftCompletelyEnd();
                    giftFrameLayout.setGiftExitAnimationEndVisibility();
                    //再取队列下一个进行处理
                    showNextGift();
                }
            });
        }
    }

    /**
     * 销毁控件
     */
    public synchronized void destroy() {
        if (mGiftQueue != null) {
            mGiftQueue.clear();
        }
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            GiftFrameLayout giftFrameLayout = mGiftLayoutList.get(i);
            if (giftFrameLayout != null) {
                giftFrameLayout.destroy();
            }
        }
        if (mGiftLayoutList != null) {
            mGiftLayoutList.clear();
        }
    }

    /**
     * 礼物是否为空
     */
    public synchronized boolean isEmpty() {
        return mGiftQueue == null || mGiftQueue.size() == 0;
    }

    private void showLog(String message) {
        Log.i(TAG, message);
    }

}
