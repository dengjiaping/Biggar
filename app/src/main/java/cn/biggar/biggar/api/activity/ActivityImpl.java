package cn.biggar.biggar.api.activity;

import android.content.Context;

import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.ActivityBean;

import java.util.ArrayList;

import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/**
 * Created by mx on 2016/8/11
 * 活动
 */
public class ActivityImpl implements IActivityAPI {

    private Context context;

    public ActivityImpl(Context context) {
        this.context = context;
    }


    @Override
    public Observable<ArrayList<ActivityBean>> queryActivityRewardList(String brandId, int p, int pages) {
        String url = BaseAPI.API_ACTIVITY_REWARD_LIST + "?" + "p=" + p + "&" + "pages=" + pages;

        if(brandId!=null){
            url+="&" + "BrandID=" + brandId;
        }

        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<ActivityBean>(ActivityBean.class));
    }

    @Override
    public Observable<ArrayList<ActivityBean>> queryActivityMyJoinList(String brandId, String userID, int p, int pages) {
        String url = BaseAPI.API_ACTIVITY_MYJOIN_LIST + "?" + "p=" + p + "&" + "pages=" + pages+"&"+"E_UID="+userID;
        if(brandId!=null){
            url+="&" + "BrandID=" + brandId;
        }
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<ActivityBean>(ActivityBean.class));
    }
}
