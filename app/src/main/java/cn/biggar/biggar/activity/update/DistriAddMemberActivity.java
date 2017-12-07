package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
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
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/18.
 * 分销管理 --- 添加成员
 */

public class DistriAddMemberActivity extends BiggarListActivity<BasePresenter, FansListBean> {
    @BindView(R.id.et_search)
    EditText etSearch;

    private String uid;
    private String proId;
    private String searchName = "";

    @Override
    protected void initDataBefore() {
        HideUtil.init(this);
        showLoading();
        UserBean userBean = Preferences.getUserBean(this);

        if (null != userBean) {
            uid = userBean.getId();
        }
        proId = getIntent().getStringExtra("pro_id");

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

    private void addMember(final int position, final FansListBean fansListBean) {
        String url = BaseAPI.BASE_URL + "Brand/addMember";
        OkGo.<String>post(url)
                .params("uid", uid)
                .params("pro_id", proId)
                .params("fans_id", fansListBean.getID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        if (bgResponse.status == 1) {
                            fansListBean.setE_State("0");
                            notifyItemChanged(position);
                        } else {
                            String info = (String) bgResponse.info;
                            ToastUtils.showError(info);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showError("请求失败");
                    }
                });
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        String url = BaseAPI.GET_FANS_LIST + "?uid=" + uid
                + "&cid=" + uid
                + "&pro_id=" + proId
                + "&pages=" + Constants.PAGE_SIZE
                + "&p=" + curPage
                + "&name=" + searchName;
        OkGo.<List<FansListBean>>get(url)
                .execute(new JsonCallBack<List<FansListBean>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<List<FansListBean>> response) {
                        List<FansListBean> fansBeens = response.body();
                        if (isLoadMore) {
                            finishLoadMore(fansBeens);
                        } else {
                            finishRefresh(fansBeens);
                        }
                    }

                    @Override
                    public void onError(Response<List<FansListBean>> response) {
                        super.onError(response);
                        handleError();
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_distri_add_member;
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_distri_add_member;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, FansListBean fansListBean) {
        helper.addOnClickListener(R.id.tv_sq);
        SuperButton btnSq = helper.getView(R.id.tv_sq);
        String eState = fansListBean.getE_State();
        if (TextUtils.isEmpty(eState)) {
            btnSq.setShapeCornersRadius(9999)
                    .setShapeSolidColor(Color.parseColor("#1A83D4"))
                    .setUseShape();
            btnSq.setTextColor(Color.parseColor("#FFFFFF"));
            btnSq.setText("授权代理");
            btnSq.setEnabled(true);
        } else {
            if (eState.equals("0")) {
                btnSq.setShapeCornersRadius(0)
                        .setShapeSolidColor(ContextCompat.getColor(context, R.color.whites))
                        .setUseShape();
                btnSq.setTextColor(Color.parseColor("#666666"));
                btnSq.setText("等待同意");
                btnSq.setEnabled(false);
            } else if (eState.equals("1")) {
                btnSq.setShapeCornersRadius(0)
                        .setShapeSolidColor(ContextCompat.getColor(context, R.color.whites))
                        .setUseShape();
                btnSq.setTextColor(Color.parseColor("#666666"));
                btnSq.setText("已授权");
                btnSq.setEnabled(false);
            }
        }

        helper.setText(R.id.tv_name, fansListBean.getE_Name());
        Glide.with(this)
                .load(Utils.getRealUrl(fansListBean.getE_HeadImg()))
                .apply(new RequestOptions().centerCrop())
                .into((ImageView) helper.getView(R.id.iv_avatar));
    }

    @Override
    protected void onListItemClick(FansListBean data, int position) {

    }

    @Override
    protected void onListItemChildClick(int viewId, FansListBean data, int position) {
        if (viewId == R.id.tv_sq) {
            hideKeyboard();
            addMember(position, data);
        }
    }
}
