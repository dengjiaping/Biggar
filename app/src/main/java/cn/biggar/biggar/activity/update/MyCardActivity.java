package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.BrandSpaceActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.MyCardBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import cn.biggar.biggar.view.GrayLoadMoreView;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/6/8.
 * 我的卡包
 */

public class MyCardActivity extends BiggarListActivity<BasePresenter, MyCardBean> {
    private String userId;

    @Override
    protected void initDataBefore() {
        userId = Preferences.getUserBean(this).getId();
        RootLayout.getInstance(this).setTitle("我的卡包");
    }

    @Override
    protected void initDataAfter() {
        setListBackGroundColor(R.color.app_e5e5e5);
    }

    @Override
    public LoadMoreView getLoadView() {
        return new GrayLoadMoreView();
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        String url = BaseAPI.BASE_URL + "User/mycard_json?uid=" + userId + "&p=" + curPage + "&pages=" + Constants.PAGE_SIZE;
        OkGo.<BgResponse<List<MyCardBean>>>get(url)
                .execute(new JsonCallBack<BgResponse<List<MyCardBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<MyCardBean>>> response) {
                        BgResponse<List<MyCardBean>> listBgResponse = response.body();
                        List<MyCardBean> infos = listBgResponse.info;
                        if (isLoadMore) {
                            finishLoadMore(infos);
                        } else {
                            finishRefresh(infos);
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<MyCardBean>>> response) {
                        super.onError(response);
                        dismissLoading();
                        handleError();
                    }
                });
    }

    @Override
    protected int itemLayout() {
        return R.layout.itrm_my_card;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, MyCardBean item) {
        TextView tvCardType = helper.getView(R.id.tv_card_type);
        TextView tvSpending = helper.getView(R.id.tv_spending);
        String eCardType = item.E_CardType;
        String eCardType2 = item.E_CardType2;
        String eMoney = item.E_Money;

        if (eCardType.equals("1")) {
            if (eCardType2.equals("1")) {
                tvCardType.setText(eMoney + "元");
                tvSpending.setText(" (满" + item.E_Spending + "元可用)");
                tvSpending.setVisibility(View.VISIBLE);
            } else if (eCardType2.equals("2")) {
                tvCardType.setText("直减 " + eMoney + " 元");
                tvSpending.setVisibility(View.GONE);
            } else if (eCardType2.equals("3")) {
                tvCardType.setText(eMoney + "折折扣券");
                tvSpending.setVisibility(View.GONE);
            }
        } else if (eCardType.equals("2")) {
            if (eCardType2.equals("3")) {
                tvCardType.setText("兑换券");
                tvSpending.setVisibility(View.GONE);
            }
        }

        helper.setText(R.id.tv_name, item.E_BrandCnName).setText(R.id.tv_date_line, "有效期至" + item.E_ExDate);
        Glide.with(this).load(item.E_Logo)
                .apply(new RequestOptions().centerCrop())
                .into((ImageView) helper.getView(R.id.iv_image));

        TextView tvCardUseState = helper.getView(R.id.tv_card_user_state);
        if (item.E_State.equals("-1")) {
            tvCardUseState.setText("已使用");
            tvCardUseState.setBackgroundResource(R.mipmap.card_in_use);
        } else {
            tvCardUseState.setText("马上使用");
            tvCardUseState.setBackgroundResource(R.mipmap.card_used);
        }
    }

    @Override
    protected void onListItemClick(MyCardBean data, int position) {
        startActivity(BrandSpaceActivity.startIntent(getActivity(), data.E_BrandID));
    }

    @Override
    protected void onListItemChildClick(int viewId, MyCardBean data, int position) {

    }
}
