package cn.biggar.biggar.http;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/10/23.
 */

public abstract class BaseArrayObserver<T> implements Observer<List<T>> {
    @Override
    public abstract void onSubscribe(Disposable d);

    @Override
    public void onNext(List<T> ts) {
        onHandleSuccess(ts);
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

    protected abstract void onHandleSuccess(List<T> t);

    protected abstract void onHandleError(int code, String msg);
}
