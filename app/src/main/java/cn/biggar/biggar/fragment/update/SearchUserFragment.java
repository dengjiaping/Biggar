package cn.biggar.biggar.fragment.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.SearchUserContract;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.event.SearchEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.SearchUsePresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/9/7.
 */

public class SearchUserFragment extends BiggarListFragment<SearchUsePresenter, FansListBean> implements SearchUserContract.View {
    private String uid;
    private String uName;
    private String name;

    private int clickFolloePosition;
    private boolean isLoginSuccess;

    public static SearchUserFragment newInstance(String searchName) {
        SearchUserFragment searchUserFragment = new SearchUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchName", searchName);
        searchUserFragment.setArguments(bundle);
        return searchUserFragment;
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
            uName = userBean.geteName();
        }
    }

    @Override
    protected void initAfter() {

    }

    @Override
    public boolean isOpenEventBus() {
        return true;
    }

    @Subscribe
    public void loginSuess(LoginSucessEvent loginSucessEvent) {
        isLoginSuccess = true;
    }

    @Subscribe
    public void search(SearchEvent searchEvent) {
        name = searchEvent.searchName;
        if (searchEvent.currentItem == 0) {
            showLoading();
        }
        curPage = 1;
        mPresenter.requestUser("1", uid, Constants.PAGE_SIZE, curPage, name);
    }

    public void setSearchName(String searchName) {
        name = searchName;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
            uName = userBean.geteName();
        }
        if (isLoginSuccess) {
            curPage = 1;
            mPresenter.requestUser("1", uid, Constants.PAGE_SIZE, curPage, name);
        }
    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        if (isLoadMore) {
            mPresenter.loadMoreUser("1", uid, Constants.PAGE_SIZE, curPage, name);
        } else {
            mPresenter.requestUser("1", uid, Constants.PAGE_SIZE, curPage, name);
        }
    }


    @Override
    protected int itemLayout() {
        return R.layout.item_myfriend;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, FansListBean data) {
        Glide.with(mContext).load(Utils.getRealUrl(data.getE_HeadImg()))
                .apply(new RequestOptions().centerCrop()).into((CircleImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, data.getE_Name()).setText(R.id.tv_sign, TextUtils.isEmpty(data.getE_Signature()) ? "TA很懒，什么都没有留下~" : data.getE_Signature())
                .addOnClickListener(R.id.iv_tochat);

        if (TextUtils.isEmpty(uid)) {
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

    @Override
    protected void onListItemClick(FansListBean data, int position) {
        startActivity(MyHomeActivity.startIntent(mContext, data.getID()));
    }

    @Override
    protected void onListItemChildClick(int viewId, final FansListBean data, int position) {
        if (viewId == R.id.iv_tochat) {
            if (TextUtils.isEmpty(uid)) {
                startActivity(LoginActivity.startIntent(getActivity()));
                return;
            }

            if (uid.equals(data.getID())) {
                ToastUtils.showWarning("不能关注自己");
                return;
            }

            clickFolloePosition = position;
            int concern = data.getIsConcern();
            switch (concern) {
                //单关注
                case 0:
                    //已相互关注
                case 1:
                    TipsDialog.newInstance("您确定不再关注" + data.getE_Name() + "了吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                        @Override
                        public void onSure() {
                            showLoading();
                            mPresenter.cancelFollow(uid, data.getID());
                        }

                        @Override
                        public void onCancel() {
                        }
                    }).setMargin(52).show(getChildFragmentManager());
                    break;
                //单粉丝
                case 2:
                    //相互未关注
                case 3:
                    showLoading();
                    mPresenter.addFollow(data.getID()
                            , data.getE_Name(), 1
                            , uid, uName);
                    break;
            }
        }
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showUsers(List<FansListBean> userBeen) {
        finishRefresh(userBeen);
    }

    @Override
    public void showMoreUsers(List<FansListBean> userBeen) {
        finishLoadMore(userBeen);
    }

    @Override
    public void addFollowSuccess() {
        dismissLoading();
        FansListBean item = getItem(clickFolloePosition);
        int isConcern = item.getIsConcern();
        switch (isConcern) {
            //单粉丝变相互关注
            case 2:
                item.setIsConcern(1);
                notifyItemChanged(clickFolloePosition);
                break;
            //相互未关注 变单关注
            case 3:
                item.setIsConcern(0);
                notifyItemChanged(clickFolloePosition);
                break;
        }
    }

    @Override
    public void addFollowError(String errorMsg) {
        dismissLoading();
        ToastUtils.showError(errorMsg);
    }

    @Override
    public void canccelFollowSuccess() {
        dismissLoading();
        FansListBean item = getItem(clickFolloePosition);
        int isConcern = item.getIsConcern();
        switch (isConcern) {
            //单关注 变相互未关注
            case 0:
                item.setIsConcern(3);
                notifyItemChanged(clickFolloePosition);
                break;
            //相互关注 变粉丝单关注
            case 1:
                item.setIsConcern(2);
                notifyItemChanged(clickFolloePosition);
                break;
        }
    }

    @Override
    public void canccelFollowError(String errorMsg) {
        dismissLoading();
        ToastUtils.showError(errorMsg);
    }
}
