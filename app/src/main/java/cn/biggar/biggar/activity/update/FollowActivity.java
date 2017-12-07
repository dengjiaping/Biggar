package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.FollowContract;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.FollowEvent;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.FollowPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/8/2.
 */

public class FollowActivity extends BiggarListActivity<FollowPresenter, FansListBean> implements FollowContract.View {
    private String mUserId, mUserName;
    private String mCid;
    private UserBean userBean;
    private int clickPosition;

    public static Intent startIntent(Context context, String cid) {
        Intent intent = new Intent(context, FollowActivity.class);
        intent.putExtra("cid", cid);
        return intent;
    }

    @Override
    protected void initDataBefore() {
        userBean = Preferences.getUserBean(this);
        mCid = getIntent().getStringExtra("cid");

        if (mCid == null) {
            mCid = "";
        }

        if (userBean != null) {
            mUserId = userBean.getId();
            mUserName = TextUtils.isEmpty(userBean.geteName()) ? mUserId : userBean.geteName();
        } else {
            mUserId = "";
            mUserName = "";
        }

        if (TextUtils.isEmpty(mUserId) || !mUserId.equals(mCid)) {
            RootLayout.getInstance(this).setTitle("Ta的关注");
        } else {
            RootLayout.getInstance(this).setTitle("关注");
        }
    }

    @Override
    protected void initDataAfter() {

    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        mPresenter.requestFollows(mUserId, mCid, curPage, Constants.PAGE_SIZE, isLoadMore);
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_myfriend;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, FansListBean data) {
        Glide.with(this).load(Utils.getRealUrl(data.getE_HeadImg()))
                .apply(new RequestOptions().centerCrop()).into((CircleImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, data.getE_Name()).setText(R.id.tv_sign, TextUtils.isEmpty(data.getE_Signature()) ? "TA很懒，什么都没有留下~" : data.getE_Signature())
                .addOnClickListener(R.id.iv_tochat);

        if (TextUtils.isEmpty(mUserId)) {
            helper.setImageResource(R.id.iv_tochat, R.mipmap.attention_3x);
        } else {
            int concern = data.getIsConcern();
            switch (concern) {
                //已关注网红
                case 0:
                    helper.setImageResource(R.id.iv_tochat, R.mipmap.followed_new_3x);
                    break;
                //已相互关注
                case 1:
                    helper.setImageResource(R.id.iv_tochat, R.mipmap.a_together_3x);
                    break;
                //网红已关注您
                case 2:
                    helper.setImageResource(R.id.iv_tochat, R.mipmap.attention_3x);
                    break;
                //相互未关注
                case 3:
                    helper.setImageResource(R.id.iv_tochat, R.mipmap.attention_3x);
                    break;
            }
        }

        helper.setGone(R.id.iv_vip, !TextUtils.isEmpty(data.getE_VerWorker()) && data.getE_VerWorker().equals("1"));
    }

    @Subscribe
    public void loginCallBack(LoginSucessEvent loginSucessEvent) {
        userBean = loginSucessEvent.userBean;

        if (userBean != null) {
            mUserId = userBean.getId();
            mUserName = TextUtils.isEmpty(userBean.geteName()) ? mUserId : userBean.geteName();
        } else {
            mUserId = "";
            mUserName = "";
        }

        curPage = 1;
        refreshData(false);
    }


    @Subscribe
    public void followCallback(FollowEvent followEvent) {
        FansListBean itemData = getItemData(clickPosition);
        int concern = itemData.getIsConcern();
        if (userBean != null && !TextUtils.isEmpty(mUserId)) {
            switch (concern) {
                //已关注网红
                case 0:
                    if (followEvent.isFollow) {
                        itemData.setIsConcern(0);
                    } else {
                        itemData.setIsConcern(3);
                    }
                    break;
                //已相互关注
                case 1:
                    if (followEvent.isFollow) {
                        itemData.setIsConcern(1);
                    } else {
                        itemData.setIsConcern(2);
                    }
                    break;
                //网红已关注您
                case 2:
                    if (followEvent.isFollow) {
                        itemData.setIsConcern(1);
                    } else {
                        itemData.setIsConcern(2);
                    }
                    break;
                //相互未关注
                case 3:
                    if (followEvent.isFollow) {
                        itemData.setIsConcern(0);
                    } else {
                        itemData.setIsConcern(3);
                    }
                    break;
            }
            notifyItemChanged(clickPosition);
        }
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Override
    protected void onListItemClick(FansListBean data, int position) {
        clickPosition = position;
        startActivity(MyHomeActivity.startIntent(getActivity(), data.getID()));
    }

    @Override
    protected void onListItemChildClick(int viewId, FansListBean data, int position) {
        switch (viewId) {
            case R.id.iv_tochat:
                if (userBean == null || TextUtils.isEmpty(mUserId)) {
                    startActivity(LoginActivity.startIntent(FollowActivity.this));
                    return;
                }

                if (mUserId.equals(data.getID())) {
                    ToastUtils.showError("不能关注自己");
                    return;
                }

                clickPosition = position;
                int concern = data.getIsConcern();

                if (concern == 0 || concern == 1) {
                    showCacnelDialog(data, position);
                    return;
                }

                addConcern(data);
                break;
        }
    }

    /**
     * 取消关注 对话框
     */
    private void showCacnelDialog(final FansListBean fansListBean, final int position) {
        TipsDialog.newInstance("您确定不再关注" + fansListBean.getE_Name() + "了吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
            @Override
            public void onSure() {
                cancelConcern(fansListBean);
            }

            @Override
            public void onCancel() {
            }
        }).setMargin(52).show(getSupportFragmentManager());
    }

    /**
     * 取消关注
     */
    private void cancelConcern(final FansListBean fansList) {
        showLoading();
        mPresenter.cancelFollow(mUserId, fansList.getID());
    }

    /**
     * 加关注
     *
     * @param fansListBean
     */
    private void addConcern(final FansListBean fansListBean) {
        showLoading();
        mPresenter.addFollow(fansListBean.getID(), fansListBean.getE_Name(), 1, mUserId, mUserName);
    }

    /**
     * 更新 关注人数
     *
     * @param state ture+  false -
     */
    private void updateConcernNumber(boolean state) {
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            UserBean mUserData = Preferences.getUserBean(this);
            int count = Utils.parseInt(mUserData.getE_Concern_ta());
            if (state) {
                count++;
            } else {
                count--;
                count = count < 0 ? 0 : count;
            }
            mUserData.setE_Concern_ta(count + "");
            Preferences.storeUserBean(getActivity(), mUserData);
        }
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showFollows(List<FansListBean> fansListBeen) {
        finishRefresh(fansListBeen);
    }

    @Override
    public void showMoreFollows(List<FansListBean> fansListBeen) {
        finishLoadMore(fansListBeen);
    }

    @Override
    public void addFollowSuccess() {
        dismissLoading();
        FansListBean fansListBean = getItemData(clickPosition);
        int concern = fansListBean.getIsConcern();
        if (concern == 2) {
            fansListBean.setIsConcern(1);
        } else if (concern == 3) {
            fansListBean.setIsConcern(0);
        }
        notifyItemChanged(clickPosition);
        updateConcernNumber(true);
        ToastUtils.showNormal("关注成功");
    }

    @Override
    public void addFollowError(String errorMsg) {
        dismissLoading();
        ToastUtils.showNormal("关注失败");
    }

    @Override
    public void canccelFollowSuccess() {
        ToastUtils.showNormal("取消关注成功");
        dismissLoading();
        FansListBean fansListBean = getItemData(clickPosition);
        int concern = fansListBean.getIsConcern();
        //单关注 变成相互未关注
        if (concern == 0) {
            fansListBean.setIsConcern(3);
        }
        //相互关注 变成单关注
        else if (concern == 1) {
            fansListBean.setIsConcern(2);
        }
        notifyItemChanged(clickPosition);
        updateConcernNumber(false);
    }

    @Override
    public void canccelFollowError(String errorMsg) {
        dismissLoading();
        ToastUtils.showError("取消关注失败");
    }
}
