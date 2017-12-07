package cn.biggar.biggar.base;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/2/7.
 */

public abstract class BasePresenter<T> {
    public Context mContext;
    public T mView;
    private CompositeDisposable mCompositeDisposable;

    public void setView(T v) {
        this.mView = v;
        this.onStart();
    }

    public void onStart() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public void onStop() {

    }

    public void addDisposable(Disposable disposable) {
        if (disposable != null) {
            mCompositeDisposable.add(disposable);
        }
    }

    public void onDestroy() {
        clearDisposable();
    }

    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
