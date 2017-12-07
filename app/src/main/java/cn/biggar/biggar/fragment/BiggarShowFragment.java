package cn.biggar.biggar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.adapter.update.FollowAdapter;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.LazyFragment;
import cn.biggar.biggar.bean.ConcersBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.MyHomeContract;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.update.MyHomePresenter;
import cn.biggar.biggar.utils.DensityUtil;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ItemDecoration.UniversalItemDecoration;
import cn.biggar.biggar.view.MulitStatusView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.view.scrollable.ScrollableHelper;
import per.sue.gear2.presenter.OnObjectListener;

/**
 * Created by Chenwy on 2017/5/25.
 */

public class BiggarShowFragment extends LazyFragment<MyHomePresenter> implements MyHomeContract.View, ScrollableHelper.ScrollableContainer {

    @BindView(R.id.rv_biggar_show_list)
    RecyclerView rvBiggarShowList;
    @BindView(R.id.multiStatusView)
    MulitStatusView multiStatusView;

    private int curPage = 1;
    private FollowAdapter mFollowAdapter;
    private UserBean userBean;
    private String userId;
    private CommonPresenter mCommonPresenter;
    private boolean isShowPlus = false;

    public static BiggarShowFragment getInstance(String mId) {
        BiggarShowFragment mConcersFragment = new BiggarShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mId", mId);
        mConcersFragment.setArguments(bundle);
        return mConcersFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.frg_follow;
    }

    @Override
    protected void lazyLoadData(Bundle savedInstanceState) {
        mCommonPresenter = new CommonPresenter(getActivity());
        userBean = Preferences.getUserBean(getContext());
        userId = (String) getArguments().get("mId");


        if (userBean!=null && userBean.getId() != null && userId != null
                && userBean.getId().equals(userId)){
            isShowPlus = true;
        }
        mFollowAdapter = new FollowAdapter(R.layout.item_my_home, new ArrayList<ConcersBean>(),isShowPlus);
        rvBiggarShowList.setLayoutManager(new LinearLayoutManager(mContext));
        rvBiggarShowList.setAdapter(mFollowAdapter);
        mFollowAdapter.setEnableLoadMore(true);
        mFollowAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.requestMembers(++curPage, Constants.PAGE_SIZE, userId);
            }
        }, rvBiggarShowList);

        rvBiggarShowList.addItemDecoration(new UniversalItemDecoration() {
            @Override
            public Decoration getItemOffsets(int position) {
                ColorDecoration decoration = new ColorDecoration();
                if (position < mFollowAdapter.getItemCount() - 1) {
                    decoration.bottom = DensityUtil.dp2px(mContext,12);
                }
                decoration.decorationColor = Color.TRANSPARENT;
                return decoration;
            }
        });

        rvBiggarShowList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ConcersBean bean = mFollowAdapter.getItem(position);
//                getActivity().startActivity(RedContentDetailsActivity.startIntent(getContext(), bean.getID(), bean.getE_TypeVal()));
                startActivity(new Intent(getActivity(), ContentDetailActivity.class).putExtra("id",bean.getID()).putExtra("typeVal",bean.getE_TypeVal()));
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                super.onItemChildClick(adapter, view, position);
                if (view.getId() == R.id.play_user_iv) {
                    getActivity().startActivity(MyHomeActivity.startIntent(getActivity(), mFollowAdapter.getItem(position).getE_MemberID()));
                }else if (view.getId() == R.id.iv_plus){
                    TipsDialog.newInstance("您确定要删除吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                        @Override
                        public void onSure() {
                            delete(mFollowAdapter.getItem(position), position);
                        }

                        @Override
                        public void onCancel() {
                        }
                    }).setMargin(52).show(getChildFragmentManager());
                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                super.onItemLongClick(adapter, view, position);
                if (!isShowPlus){
                    return;
                }
                TipsDialog.newInstance("您确定要删除吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                    @Override
                    public void onSure() {
                        delete(mFollowAdapter.getItem(position), position);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).setMargin(52).show(getChildFragmentManager());
            }
        });

        multiStatusView.getView(MulitStatusView.VIEW_STATE_ERROR).findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStatusView.setViewState(MulitStatusView.VIEW_STATE_LOADING);
                refresh();
            }
        });

        multiStatusView.getView(MulitStatusView.VIEW_STATE_EMPTY).findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStatusView.setViewState(MulitStatusView.VIEW_STATE_LOADING);
                refresh();
            }
        });

        refresh();
    }

    private void delete(ConcersBean bean, final int position) {
        mCommonPresenter.deleteRelease(bean.getE_MemberID(), bean.getID(), new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ToastUtils.showNormal(result);
                mFollowAdapter.remove(position);
                if (mFollowAdapter.getData().size() == 0) {
                    multiStatusView.setViewState(MulitStatusView.VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(msg);
            }
        });
    }


    public void refresh() {
        curPage = 1;
        mPresenter.requestMembers(curPage, Constants.PAGE_SIZE, userId);
    }

    @Override
    public void showError(String errMsg) {
        multiStatusView.setViewState(MulitStatusView.VIEW_STATE_ERROR);
    }

    @Override
    public void showFollows(List<ConcersBean> concersBeens) {
        multiStatusView.setViewState(concersBeens.size() > 0 ? MulitStatusView.VIEW_STATE_CONTENT : MulitStatusView.VIEW_STATE_EMPTY);
        mFollowAdapter.setNewData(concersBeens);
        mFollowAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void showMoreFollows(List<ConcersBean> concersBeens) {
        mFollowAdapter.addData(concersBeens);
        if (concersBeens.size() > 0) {
            mFollowAdapter.loadMoreComplete();
        } else {
            mFollowAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public View getScrollableView() {
        return rvBiggarShowList;
    }
}
