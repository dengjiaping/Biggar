package per.sue.gear2.presenter;

import android.content.Context;
import android.util.Log;


import per.sue.gear2.ui.IBaseView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by SUE on 2016/7/20 0020.
 */

public  abstract class ListPresenter<T> extends AbsPresenter  {

    protected boolean isRefresh;
    private boolean isWithLoad;
    protected int pageNum;
    private Subscription subscription;
    private Context context;
    private ListResultView<T> listResultView;
    private Observable observable;

    public ListPresenter(Context context, ListResultView<T> listResultView) {
        this.context = context;
        this.listResultView = listResultView;
    }

    public int getPageNum(){
        return pageNum;
    }

    public void refreshWithLoading() {
        isRefresh = true;
        isWithLoad = true;
        pageNum = 1;
//        listResultView.showLoading();
        query();
    }

    public void refresh() {
        isRefresh = true;
        isWithLoad = false;
        pageNum = 1;
        setObservable(getObservable());
        query();
    }

    public void loadMore() {
        isRefresh = false;
        isWithLoad = false;
        pageNum++;

        setObservable(getObservable());
        query();
    }

    public void cancelRequest() {
        if (null != subscription) subscription.unsubscribe();
    }

    public void query() {
        Log.d("MX","query - "+pageNum);
        if (null == observable) {
            return;
        }
        if (null != subscription) {
            subscription.unsubscribe();
        }
        subscription = observable.subscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {
                listResultView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listResultView.onError(-1, e.getMessage());
                if (isWithLoad) {
                    listResultView.onLoadFailed();
                }
            }

            @Override
            public void onNext(T recommendVideos) {
                if (isRefresh) {
                    listResultView.onSuccessRefresh(recommendVideos);
                } else {
                    listResultView.onSuccessLoadModre(recommendVideos);
                }

            }
        });
    }


    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public interface ListResultView<T> extends IBaseView {
        void onSuccessRefresh(T msg);

        void onSuccessLoadModre(T msg);
    }


    public abstract Observable getObservable();

}
