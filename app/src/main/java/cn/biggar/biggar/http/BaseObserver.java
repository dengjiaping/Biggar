package cn.biggar.biggar.http;


import android.content.Context;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/10/16.
 */

public abstract class BaseObserver<T> implements Observer<BgResponse<T>> {
    private Context mContext;

    public BaseObserver() {
    }

    public BaseObserver(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public abstract void onSubscribe(Disposable d);

    @Override
    public void onNext(BgResponse<T> t) {
        onHandleSuccess(t.info);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ResultException) {
            onHandleError(((ResultException) e).errorCode, ((ResultException) e).errorMsg);
        } else {
            onHandleError(-1, e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onHandleSuccess(T t);

    protected abstract void onHandleError(int code, String msg);
}
