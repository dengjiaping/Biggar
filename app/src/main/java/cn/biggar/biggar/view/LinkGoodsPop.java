package cn.biggar.biggar.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.LinkGoodsAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.DistriGoodsBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.utils.ToastUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Chenwy on 2017/6/6.
 */

public class LinkGoodsPop extends BasePopupWindow {
    private Button btnSure;
    private RecyclerView rvLinkGoods;
    private LinkGoodsAdapter linkGoodsAdapter;
    private int cuaPage = 1;
    private String mBrandId;
    private String mTypeId;
    private String mUserId;
    private String mCurChooseGoodsId;
    private String mCurChooseGoodsImage;
    private String mCurChooseGoodsPricel;
    private String mCurChooseGoodsName;
    private String mRate;
    private Context mContext;

    public LinkGoodsPop(final Activity context) {
        super(context);
        mContext = context;
        btnSure = (Button) findViewById(R.id.btn_sure);
        rvLinkGoods = (RecyclerView) findViewById(R.id.rv_link_goods);
        linkGoodsAdapter = new LinkGoodsAdapter(new ArrayList<DistriGoodsBean>());
        rvLinkGoods.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        rvLinkGoods.setAdapter(linkGoodsAdapter);
        linkGoodsAdapter.setEnableLoadMore(true);

        linkGoodsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                cuaPage += 1;
                refreshData();
            }
        }, rvLinkGoods);
        linkGoodsAdapter.setEmptyView(R.layout.empty_view);
        linkGoodsAdapter.setLoadMoreView(new WhiteLoadMoreView());

        rvLinkGoods.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<DistriGoodsBean> datas = linkGoodsAdapter.getData();
                for (int i = 0; i < datas.size(); i++) {
                    linkGoodsAdapter.getItem(i).isCheck = false;
                }
                mCurChooseGoodsId = linkGoodsAdapter.getItem(position).ID;
                mCurChooseGoodsImage = linkGoodsAdapter.getItem(position).E_Img1;
                mCurChooseGoodsPricel = linkGoodsAdapter.getItem(position).E_SellPrice;
                mCurChooseGoodsName = linkGoodsAdapter.getItem(position).E_Name;
                mRate = linkGoodsAdapter.getItem(position).E_Rate3;
                linkGoodsAdapter.getItem(position).isCheck = true;
                linkGoodsAdapter.notifyDataSetChanged();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mCurChooseGoodsId)) {
                    ToastUtils.showWarning("请先选择一个商品");
                    return;
                }
                dismiss();
                if (onLinkGoodsChooseLinstener != null) {
                    onLinkGoodsChooseLinstener.onLinkGoodsChoose(mCurChooseGoodsId, mCurChooseGoodsImage
                            , mCurChooseGoodsPricel, mCurChooseGoodsName,mRate);
                }
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return getScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    protected Animation initExitAnimation() {
        return getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.cancel);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.pop_link_goods);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ll_link_goods_pop);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        refreshData();
    }

    @Override
    public void dismiss() {
        OkGo.getInstance().cancelTag(mContext);
        super.dismiss();
    }

    public void setData(String brandId, String typeId, String userId) {
        this.mTypeId = typeId;
        this.mUserId = userId;
        this.mBrandId = brandId;
        this.cuaPage = 1;
    }

    private void refreshData() {
        String url = BaseAPI.BASE_URL + "Brand/commodity?brand_id=" + mBrandId
                + "&type_id=" + mTypeId
                + "&member_id=" + mUserId
                + "&p=" + cuaPage
                + "&pages=" + Constants.PAGE_SIZE;
        OkGo.<BgResponse<List<DistriGoodsBean>>>get(url)
                .tag(mContext)
                .execute(new JsonCallBack<BgResponse<List<DistriGoodsBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<DistriGoodsBean>>> response) {
                        BgResponse<List<DistriGoodsBean>> listBgResponse = response.body();
                        List<DistriGoodsBean> distriGoodsBeen = listBgResponse.info;
                        if (cuaPage == 1) {
                            if (distriGoodsBeen.size() <= 0){
                                ((TextView)linkGoodsAdapter.getEmptyView().findViewById(R.id.tv_empty_info)).setText("暂无内容");
                            }
                            linkGoodsAdapter.setNewData(distriGoodsBeen);
                            linkGoodsAdapter.disableLoadMoreIfNotFullPage();
                            if (distriGoodsBeen.size() < Constants.PAGE_SIZE) {
                                linkGoodsAdapter.loadMoreEnd(true);
                            }
                        } else {
                            linkGoodsAdapter.addData(distriGoodsBeen);
                            if (distriGoodsBeen.size() < Constants.PAGE_SIZE) {
                                linkGoodsAdapter.loadMoreEnd(true);
                            } else {
                                linkGoodsAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<DistriGoodsBean>>> response) {
                        super.onError(response);
                        if (cuaPage == 1){
                            if (linkGoodsAdapter.getData().size() > 0){
                                ToastUtils.showError("请求失败，请检查网络");
                            }else {
                                ((TextView)linkGoodsAdapter.getEmptyView().findViewById(R.id.tv_empty_info)).setText("请求失败，请检查网络");
                            }
                        }else {
                            linkGoodsAdapter.loadMoreFail();
                        }
                    }
                });
    }

    private OnLinkGoodsChooseLinstener onLinkGoodsChooseLinstener;

    public void setOnLinkGoodsChooseLinstener(OnLinkGoodsChooseLinstener onLinkGoodsChooseLinstener) {
        this.onLinkGoodsChooseLinstener = onLinkGoodsChooseLinstener;
    }

    public interface OnLinkGoodsChooseLinstener {
        void onLinkGoodsChoose(String goodsId, String goodsImage, String goodsPrice, String goodsName,String rate);
    }
}
