package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yinglan.keyboard.HideUtil;

import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriMemberListBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Chenwy on 2017/7/17.
 * 分销管理 --- 成员列表
 */

public class DistriMemberListActivity extends BiggarListActivity<BasePresenter, DistriMemberListBean> {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.fl_search)
    FrameLayout flSearch;

    private String proId;
    private String bid;
    private String lv;
    private String userId;
    private String searchName;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_distri_member_list;
    }

    @Override
    protected void initDataBefore() {
        HideUtil.init(this);
        showLoading();
        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DistriMemberListActivity.this, DistriAddMemberActivity.class).putExtra("pro_id", proId));
            }
        });
        UserBean userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            userId = userBean.getId();
        }
        proId = getIntent().getStringExtra("pro_id");
        bid = getIntent().getStringExtra("bid");
        lv = getIntent().getStringExtra("lv");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchName = etSearch.getText().toString().trim();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    hideKeyboard();
                    searchName = etSearch.getText().toString().trim();
                    curPage = 1;
                    refreshData(false);
                }
                return false;
            }
        });
    }

    @Override
    protected void initDataAfter() {

    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        String url = BaseAPI.BASE_URL + "Brand/memberList?uid=" + userId
                + "&p=" + curPage
                + "&pages=" + Constants.PAGE_SIZE
                + "&bid=" + bid
                + "&lv=" + lv
                + "&pro_id=" + proId;
        if (!TextUtils.isEmpty(searchName)) {
            url += "&name=" + searchName;
        }

        OkGo.<BgResponse<List<DistriMemberListBean>>>get(url)
                .tag(this)
                .execute(new JsonCallBack<BgResponse<List<DistriMemberListBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<DistriMemberListBean>>> response) {
                        BgResponse<List<DistriMemberListBean>> listBgResponse = response.body();
                        List<DistriMemberListBean> distriMemberListBeens = listBgResponse.info;
                        if (isLoadMore) {
                            finishLoadMore(distriMemberListBeens);
                        } else {
                            finishRefresh(distriMemberListBeens);
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<DistriMemberListBean>>> response) {
                        super.onError(response);
                        handleError();
                    }
                });
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_distri_member_list;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, DistriMemberListBean item) {
        helper.setText(R.id.tv_name, item.E_Name)
                .setText(R.id.tv_day_money, "￥" + item.E_DayMoney)
                .setText(R.id.tv_sum_money, "￥" + item.E_SumMoney);

        final ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        Glide.with(this).load(Utils.getRealUrl(item.E_HeadImg))
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(Utils.dip2px(getApplicationContext(), 6), 0,
                                RoundedCornersTransformation.CornerType.LEFT))).into(ivAvatar);
    }

    @Override
    protected void onListItemClick(DistriMemberListBean data, int position) {
        startActivity(MyHomeActivity.startIntent(DistriMemberListActivity.this
                , data.E_MembID));
    }

    @Override
    protected void onListItemChildClick(int viewId, DistriMemberListBean data, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
