package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.bean.JPushBean;

import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by zl on 2016/9/29.
 */
public class JPushPersenter extends AbsPresenter{


    private IUserDetailsAPI api;

    public JPushPersenter(Context context){

        api = DataApiFactory.getInstance().createIUserDetais(context);
    }
    public void getPushMsg(String Id, final OnObjectListener<JPushBean> listener){
        api.getPushMsg(Id).subscribe(new Subscriber<JPushBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0,e.getMessage());
            }

            @Override
            public void onNext(JPushBean jPushBean) {
                listener.onSuccess(jPushBean);
            }
        });
    }
}
