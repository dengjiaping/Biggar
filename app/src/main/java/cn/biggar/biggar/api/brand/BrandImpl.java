package cn.biggar.biggar.api.brand;

import android.content.Context;

import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.BrandDetails;

import cn.biggar.biggar.bean.BrandListBean;
import cn.biggar.biggar.bean.CardWishBrandBean;
import cn.biggar.biggar.bean.GoodsBean;
import cn.biggar.biggar.bean.RedPacketBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.WishCardBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/**
 * Created by SUE on 2016/7/20 0020.
 */
public class BrandImpl implements IBrandAPI {

    private Context context;

    public BrandImpl(Context context) {
        this.context = context;
    }


    @Override
    public Observable<ArrayList<BrandBean>> queryRecommendBrandList(String type, String searchName, int p, int pages) {
        String url = BaseAPI.API_BRAND_RECOMMEND_LIST + "?" + "p=" + p + "&" + "pages=" + pages + "&" + "type=" + type;
        if (searchName!=null){
            url+="&name="+searchName;
        }
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BrandBean>(BrandBean.class));
    }

    @Override
    public Observable<ArrayList<BrandBean>> queryConcerBrandList(String userId,String type,String searchName,int flag,int p,int pages) {
        String url = BaseAPI.API_BRAND_CONCER_LIST + "?" + "p=" + p + "&" + "pages=" + pages + "&" + "MID=" + userId+ "&" + "type=" + type+"&flag="+flag;
        if (searchName!=null){
            url+="&name="+searchName;
        }
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BrandBean>(BrandBean.class));
    }

    @Override
    public Observable<BrandDetails> queryBrandDetails(String Id) {
        String url = BaseAPI.API_BRAND_DETAILS+"?"+"ID="+Id;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BrandDetails>(BrandDetails.class));
    }

    @Override
    public Observable<ArrayList<VideoImageBean>> queryFansShowList(String brandId, int p, int pages) {
        String url = BaseAPI.API_BRAND_FNAS_SHOW+"?"+"ID="+brandId+"&"+"p="+p+"&"+"pages="+pages;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoImageBean>(VideoImageBean.class));
    }

    @Override
    public Observable<ArrayList<GoodsBean>> queryBrandGoodsList(String brandId, String type, int p, int pages) {
        String url = BaseAPI.API_BRAND_GOODS_LIST + "?" + "ID=" + brandId + "&" + "p=" + p+ "&" + "pages=" + pages+ "&" + "type=" + type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<GoodsBean>(GoodsBean.class));
    }

    @Override
    public Observable<ArrayList<CardWishBrandBean>> queryCardWishHotBrandList(String uid) {
        String url = BaseAPI.GET_BRAND_CARD_WISH_HOT_LIST+"?"+"uid="+uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardWishBrandBean>(CardWishBrandBean.class));
    }

    @Override
    public Observable<ArrayList<CardWishBrandBean>> queryCardWishBrandList(String uid) {
        String url = BaseAPI.GET_BRAND_CARD_WISH_LIST+"?"+"uid="+uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardWishBrandBean>(CardWishBrandBean.class));
    }

    @Override
    public Observable<ArrayList<WishCardBean>> queryBrandCardList(String uid, String brandId) {
        String url = BaseAPI.GET_BRAND_CARD_LIST+"?"+"uid="+uid+"&brandid="+brandId;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<WishCardBean>(WishCardBean.class));
    }

    @Override
    public Observable<BrandListBean> queryBrandList(String uid, int p, int pages) {
        String url = BaseAPI.GET_MY_BRAND_LIST+"?"+"uid="+uid+"&p="+p+"&pages="+pages;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BrandListBean>(BrandListBean.class));
    }

    @Override
    public Observable<RedPacketBean> queryBrandRedPacket(String bid) {
        String url = BaseAPI.API_QUERY_BRAND_RED_PACKET;
        Map<String, String> params = new HashMap<>();
        params.put("bid", bid);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url,params))
                .observeOnMainThread(new BaseParser<RedPacketBean>(RedPacketBean.class));
    }

    @Override
    public Observable<String> getBrandRedPacket(String uid, String bid, String cid, String type) {
        String url = BaseAPI.API_GET_BRAND_RED_PACKET;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("bid", bid);
        params.put("cid", cid);
        params.put("tid", type);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url,params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }


}
