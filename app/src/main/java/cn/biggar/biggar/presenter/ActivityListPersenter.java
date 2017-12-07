package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.activity.IActivityAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.ActivityBean;

import java.util.ArrayList;

import per.sue.gear2.presenter.ListPresenter;
import rx.Observable;

/**
 * Created by mx on 2016-08-08.
 * 通告列表
 */
public class ActivityListPersenter extends ListPresenter<ArrayList<ActivityBean>> {

    private IActivityAPI annunciateAPI;

    private String mBrandId;//品牌商ID、可省略、对应品牌商发布的任务

    private String mUserID; //我的参加时 用到
    private int mType; //查询类型 0 活动悬赏/品牌活动  2 我的参与

    public ActivityListPersenter(Context context, ListResultView<ArrayList<ActivityBean>> listResultView) {
        super(context, listResultView);
        this.annunciateAPI = DataApiFactory.getInstance().createIActivityAPI(context);
        setObservable(getObservable());
    }

    public void setBrandId(String brandId) {
        this.mBrandId = brandId;
        setObservable(getObservable());
    }

    public void setUserID(String userId){
        this.mUserID=userId;
        setObservable(getObservable());
    }
    public void setType(int type){
        this.mType=type;
        setObservable(getObservable());
    }

    public Observable getObservable() {
        Observable observable=null;
        switch (mType){
            case 0:
                observable = this.annunciateAPI.queryActivityRewardList(mBrandId,pageNum, Constants.PAGE_SIZE);
                break;
            case 2:
                observable = this.annunciateAPI.queryActivityMyJoinList(mBrandId,mUserID,pageNum,  Constants.PAGE_SIZE);
                break;
        }
        return observable;
    }

}
