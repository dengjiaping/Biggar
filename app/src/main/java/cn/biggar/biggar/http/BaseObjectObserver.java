package cn.biggar.biggar.http;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/10/23.
 */

public abstract class BaseObjectObserver<T> implements Observer<T> {
    @Override
    public abstract void onSubscribe(@NonNull Disposable d);

    @Override
    public void onNext(@NonNull T t) {
        onHandleSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
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
