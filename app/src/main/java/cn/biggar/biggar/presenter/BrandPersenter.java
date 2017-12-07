package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.brand.IBrandAPI;
import cn.biggar.biggar.bean.BrandDetails;
import cn.biggar.biggar.bean.RedPacketBean;

import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by mx on 2016/8/13.
 * 品牌
 */
public class BrandPersenter extends AbsPresenter {
    private Context context;
    private IBrandAPI brandAPI;

    public BrandPersenter(Context context) {
        this.context = context;
        this.brandAPI = DataApiFactory.getInstance().createIBrandAPI(context);
    }

    /**
     * 查询品牌详情
     * @param ID
     * @param listener
     */
    public void queryBrandDetails(String ID, final OnObjectListener<BrandDetails> listener) {
        brandAPI.queryBrandDetails(ID).subscribe(new Subscriber<BrandDetails>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(BrandDetails brandBean) {
                listener.onSuccess(brandBean);
            }
        });
    }

    /**
     * 查询 品牌的 福利 （红包）
     * @param bid
     * @param listener
     */
    public void queryBrandRedPacket(String bid, final OnObjectListener<RedPacketBean> listener){
        brandAPI.queryBrandRedPacket(bid).subscribe(new Subscriber<RedPacketBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception=ResultException.createResultException(e);
                listener.onError(exception.getCode(),e.getMessage());
            }

            @Override
            public void onNext(RedPacketBean redPacketBean) {
                listener.onSuccess(redPacketBean);
            }
        });
    }

    /**
     * 领取 品牌的 福利 （红包）
     * @param bid
     * @param listener
     */
    public void getBrandRedPacket(String uid, String bid, String cid, String type, final OnObjectListener<String> listener){
        brandAPI.getBrandRedPacket(uid,bid,cid,type).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception=ResultException.createResultException(e);
                listener.onError(exception.getCode(),e.getMessage());
            }

            @Override
            public void onNext(String str) {
                listener.onSuccess(str);
            }
        });
    }

}