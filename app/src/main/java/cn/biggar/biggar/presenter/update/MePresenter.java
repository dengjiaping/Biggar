package cn.biggar.biggar.presenter.update;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.MeContract;
import cn.biggar.biggar.http.JsonCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;


/**
 * Created by Chenwy on 2017/5/31.
 */

public class MePresenter extends MeContract.Presenter {
    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(mContext);
    }

    @Override
    public void requestMeData(String tid, String uid) {
        String url = BaseAPI.API_USER_DETAILS + tid + "&E_UID=" + uid;
        OkGo.<List<UserBean>>get(url)
                .tag(mContext)
                .execute(new JsonCallBack<List<UserBean>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<List<UserBean>> response) {
                        List<UserBean> userBeen = response.body();
                        if (userBeen != null && userBeen.size() > 0) {
                            mView.showMeData(userBeen.get(0));
                        }
                    }



                    @Override
                    public void onError(Response<List<UserBean>> response) {
                        super.onError(response);
                        mView.showError("");
                    }
                });
    }
}
