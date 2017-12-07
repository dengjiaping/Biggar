package cn.biggar.biggar.presenter.update;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.ConcersBean;
import cn.biggar.biggar.contract.MyHomeContract;
import cn.biggar.biggar.http.JsonCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;


/**
 * Created by Chenwy on 2017/5/26.
 */

public class MyHomePresenter extends MyHomeContract.Presenter {
    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(mContext);
    }

    @Override
    public void requestFollows(final int page, int pages, String uid) {
        String url = BaseAPI.API_ACCOUNT_CONCER_LIST + "?" + "p=" + page + "&" + "pages=" + pages + "&" + "uid=" + uid;
        OkGo.<List<ConcersBean>>get(url)
                .tag(mContext)
                .execute(new JsonCallBack<List<ConcersBean>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<List<ConcersBean>> response) {
                        List<ConcersBean> concersBeen = response.body();
                        if (page == 1) {
                            mView.showFollows(concersBeen);
                        } else {
                            mView.showMoreFollows(concersBeen);
                        }
                    }

                    @Override
                    public void onError(Response<List<ConcersBean>> response) {
                        super.onError(response);
                        mView.showError("error");
                    }
                });
    }

    @Override
    public void requestMembers(final int page, int pages, String uid) {
        String url = BaseAPI.API_MEMBER_VIDEO + "?" + "p=" + page + "&" + "pages=" + pages + "&" + "MID=" + uid;
        OkGo.<List<ConcersBean>>get(url)
                .tag(mContext)
                .execute(new JsonCallBack<List<ConcersBean>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<List<ConcersBean>> response) {
                        List<ConcersBean> concersBeen = response.body();
                        if (page == 1) {
                            mView.showFollows(concersBeen);
                        } else {
                            mView.showMoreFollows(concersBeen);
                        }
                    }

                    @Override
                    public void onError(Response<List<ConcersBean>> response) {
                        super.onError(response);
                        mView.showError("error");
                    }
                });
    }
}
