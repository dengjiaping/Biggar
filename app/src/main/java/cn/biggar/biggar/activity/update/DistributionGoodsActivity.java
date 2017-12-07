package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriGoodsBean;
import cn.biggar.biggar.dialog.DistriUpgradeDialog1;
import cn.biggar.biggar.dialog.DistriUpgradeDialog2;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/8/7.
 * 分销---商品列表
 */

public class DistributionGoodsActivity extends BiggarListActivity<BasePresenter, DistriGoodsBean> {
    private String brandId;
    private String brandName;
    private String typeId;
    private String userId;
    private UserBean userBean;

    private int clickPosition;

    @Override
    protected void initDataBefore() {
        showLoading();
        brandId = getIntent().getStringExtra("brand_id");
        brandName = getIntent().getStringExtra("brand_name");
        typeId = getIntent().getStringExtra("type_id");
        RootLayout.getInstance(this).setTitle(brandName);

        userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            userId = userBean.getId();
        }
    }

    @Override
    protected void initDataAfter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            userId = userBean.getId();
        }
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        String url = BaseAPI.BASE_URL + "Brand/commodity?brand_id=" + brandId
                + "&type_id=" + typeId
                + "&member_id=" + userId
                + "&p=" + curPage
                + "&pages=" + Constants.PAGE_SIZE;
        OkGo.<BgResponse<List<DistriGoodsBean>>>get(url)
                .execute(new JsonCallBack<BgResponse<List<DistriGoodsBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<BgResponse<List<DistriGoodsBean>>> response) {
                        BgResponse<List<DistriGoodsBean>> listBgResponse = response.body();
                        List<DistriGoodsBean> distriGoodsBeen = listBgResponse.info;
                        if (isLoadMore) {
                            finishLoadMore(distriGoodsBeen);
                        } else {
                            finishRefresh(distriGoodsBeen);
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<BgResponse<List<DistriGoodsBean>>> response) {
                        super.onError(response);
                        handleError();
                    }
                });
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_distri_goods;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, DistriGoodsBean item) {
        helper.addOnClickListener(R.id.tv_dl).addOnClickListener(R.id.tv_member);
        SuperButton tvSq = helper.getView(R.id.tv_dl);

        if (item.E_Lv.equals("1")) {
            tvSq.setText("A级代理");
        } else if (item.E_Lv.equals("2")) {
            tvSq.setText("B级代理");
        } else if (item.E_Lv.equals("3")) {
            tvSq.setText("C级代理");
        }

        if (!item.E_Lv.equals("1") && NumberUtils.parseInt(item.E_Price) >= NumberUtils.parseInt(item.E_OnePrice)
                && NumberUtils.parseInt(item.E_Count) >= NumberUtils.parseInt(item.E_Order)
//                && Integer.parseInt(item.E_ExtensionPrice) >= Integer.parseInt(item.E_tuiPrice)
//                && Integer.parseInt(item.E_MaxPrice) >= Integer.parseInt(item.E_MaxSale)
                ) {
            helper.setGone(R.id.iv_shape_hint, true);
        } else {
            helper.setGone(R.id.iv_shape_hint, false);
        }


        helper.setText(R.id.tv_goods_name, item.E_Name)
                .setText(R.id.tv_sell_price, "￥" + item.E_SellPrice)
                .setText(R.id.tv_fl2, item.E_Rate3 + "%");

        final ImageView ivGoodsImage = helper.getView(R.id.iv_goods_image);
        Glide.with(this)
                .asBitmap()
                .load(item.E_Img1)
                .apply(new RequestOptions().centerCrop())
                .into(new BitmapImageViewTarget(ivGoodsImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(Utils.dip2px(getApplicationContext(), 10));
                        ivGoodsImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @Override
    protected void onListItemClick(DistriGoodsBean data, int position) {
        String goodsId = data.ID;
        WebViewActivity.getInstance(getActivity(), BaseUrl.GET_GOODS_DETAILS_H5_URL
                + "?soure=g&version=" + Constants.WEB_VERSION
                + "&devices=android" + "&ID=" + goodsId + "&BID=" + brandId);
    }

    @Override
    protected void onListItemChildClick(int viewId, final DistriGoodsBean data, int position) {
        switch (viewId) {
            case R.id.tv_dl:
                clickPosition = position;
                if (data.E_Lv.equals("1")) {
                    ToastUtils.showNormal("已是当前商品最高级代理~");
                } else if (data.E_Lv.equals("2")) {

                    //没有认证
                    if (userBean.getE_VerWorker().equals("0")) {
                        DistriUpgradeDialog2 distriUpgradeDialog2 = new DistriUpgradeDialog2();
                        Bundle bundle = new Bundle();
                        bundle.putString("brandId", brandId);
                        bundle.putSerializable("distriGoodsBean", data);
                        distriUpgradeDialog2.setArguments(bundle);
                        distriUpgradeDialog2.show(getSupportFragmentManager());
                    }
                    //已经认证
                    else {
                        DistriUpgradeDialog1 distriUpgradeDialog1 = new DistriUpgradeDialog1();
                        Bundle bundle = new Bundle();
                        bundle.putString("brandId", brandId);
                        bundle.putSerializable("distriGoodsBean", data);
                        distriUpgradeDialog1.setArguments(bundle);
                        distriUpgradeDialog1.setOnUpgradeCallBack(new DistriUpgradeDialog1.OnUpgradeCallBack() {
                            @Override
                            public void onUpgrade() {
                                //升级变成审核中
                                data.E_State = "3";
                                notifyItemChanged(clickPosition);
                            }
                        });
                        distriUpgradeDialog1.show(getSupportFragmentManager());
                    }
                } else if (data.E_Lv.equals("3")) {
                    DistriUpgradeDialog1 distriUpgradeDialog1 = new DistriUpgradeDialog1();
                    Bundle bundle = new Bundle();
                    bundle.putString("brandId", brandId);
                    bundle.putSerializable("distriGoodsBean", data);
                    distriUpgradeDialog1.setArguments(bundle);
                    distriUpgradeDialog1.setOnUpgradeCallBack(new DistriUpgradeDialog1.OnUpgradeCallBack() {
                        @Override
                        public void onUpgrade() {
                            //升级变成审核中
                            data.E_State = "3";
                            notifyItemChanged(clickPosition);
                        }
                    });
                    distriUpgradeDialog1.show(getSupportFragmentManager());
                }
                break;
            case R.id.tv_member:
                startActivity(new Intent(getActivity(), DistriMemberListActivity.class)
                        .putExtra("pro_id", data.ID)
                        .putExtra("lv", data.E_Lv)
                        .putExtra("bid", brandId));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
