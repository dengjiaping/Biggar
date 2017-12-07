package cn.biggar.biggar.presenter.update;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.XbBean;
import cn.biggar.biggar.contract.StarRecordContract;
import cn.biggar.biggar.http.JsonCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;


/**
 * Created by Chenwy on 2017/6/1.
 */

public class StarRecordPresenter extends StarRecordContract.Presenter {
    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(mContext);
    }

    @Override
    public void requestDatas(String uid, String type, String ctype, String getType, int p, int pages) {
        String url = BaseAPI.BASE_URL + "User/screen";
        OkGo.<BgResponse<List<XbBean>>>get(url)
                .params("p", p)
                .params("pages", pages)
                .params("uid", uid)
                .params("type", type)
                .params("ctype", ctype)
                .params("GetType", getType)
                .execute(new JsonCallBack<BgResponse<List<XbBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<XbBean>>> response) {
                        BgResponse<List<XbBean>> listBgResponse = response.body();
                        mView.showResults(listBgResponse.info);
                    }

                    @Override
                    public void onError(Response<BgResponse<List<XbBean>>> response) {
                        super.onError(response);
                        mView.showError("");
                    }
                });
    }

    @Override
    public void requestMoreDatas(String uid, String type, String ctype, String getType, int p, int pages) {
        String url = BaseAPI.BASE_URL + "User/screen";
        OkGo.<BgResponse<List<XbBean>>>get(url)
                .params("p", p)
                .params("pages", pages)
                .params("uid", uid)
                .params("type", type)
                .params("ctype", ctype)
                .params("GetType", getType)
                .execute(new JsonCallBack<BgResponse<List<XbBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<XbBean>>> response) {
                        BgResponse<List<XbBean>> listBgResponse = response.body();
                        mView.showMoreResults(listBgResponse.info);
                    }

                    @Override
                    public void onError(Response<BgResponse<List<XbBean>>> response) {
                        super.onError(response);
                        mView.showError("");
                    }
                });
    }
}
