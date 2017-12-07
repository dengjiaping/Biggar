package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yinglan.keyboard.HideUtil;

import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.MyFriendContract;
import cn.biggar.biggar.helper.RongManager;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.MyFriendPresenter;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class MyFriendActivity extends BiggarListActivity<MyFriendPresenter, FansListBean> implements MyFriendContract.View {

    @BindView(R.id.et_search)
    EditText etSearch;
    private String mUserId;
    private String mCid;
    private UserBean userBean;
    private String keyName = "";

    @Override
    protected void initDataBefore() {
        RootLayout.getInstance(this).setTitle("我的好友");
        HideUtil.init(this);
        userBean = Preferences.getUserBean(this);
        mUserId = mCid = userBean.getId();
    }

    @Override
    protected void initDataAfter() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    hideKeyboard();
                    keyName = etSearch.getText().toString().trim();
                    curPage = 1;
                    showLoading();
                    mPresenter.requestFriend(mUserId, mCid, keyName, Constants.PAGE_SIZE, curPage);
                }
                return false;
            }
        });
    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        if (isLoadMore) {
            mPresenter.loadMoreFriend(mUserId, mCid, keyName, Constants.PAGE_SIZE, curPage);
        } else {
            mPresenter.requestFriend(mUserId, mCid, keyName, Constants.PAGE_SIZE, curPage);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_friend;
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_myfriend;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, FansListBean fansListBean) {
        Glide.with(this).load(Utils.getRealUrl(fansListBean.getE_HeadImg()))
                .apply(new RequestOptions().centerCrop())
                .into((CircleImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, fansListBean.getE_Name() == null ? "" : fansListBean.getE_Name())
                .setText(R.id.tv_sign, TextUtils.isEmpty(fansListBean.getE_Tags()) ? "TA很懒，什么都没有留下~" : fansListBean.getE_Tags())
                .addOnClickListener(R.id.iv_tochat);
        helper.setGone(R.id.iv_vip,!TextUtils.isEmpty(fansListBean.getE_VerWorker()) && fansListBean.getE_VerWorker().equals("1"));
    }

    @Override
    protected void onListItemClick(FansListBean data, int position) {
        startActivity(MyHomeActivity.startIntent(this, data.getID()));
    }

    @Override
    protected void onListItemChildClick(int viewId, final FansListBean data, int position) {
        if (viewId == R.id.iv_tochat) {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {
                    return new UserInfo(data.getID(), data.getE_Name(), Uri.parse(Utils.getRealUrl(data.getE_HeadImg())));
                }
            }, true);
            RongManager.getInstance().startPrivateChat(data.getID(), data.getE_Name(),"1");
        }
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void requestFriendFinish(List<FansListBean> fansListBeens) {
        finishRefresh(fansListBeens);
    }


    @Override
    public void loadMoreFriendFinish(List<FansListBean> fansListBeens) {
        finishLoadMore(fansListBeens);
    }
}
