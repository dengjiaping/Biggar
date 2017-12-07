package cn.biggar.biggar.dialog;

import android.os.Bundle;
import android.view.View;
import com.allen.library.SuperButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ApplyForTalentActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriGoodsBean;
import cn.biggar.biggar.preference.Preferences;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/7/20.
 */

public class DistriUpgradeDialog2 extends BaseBottomDialog implements View.OnClickListener{
    @BindView(R.id.upgrade)
    SuperButton upgrade;

    private DistriGoodsBean distriGoodsBean;
    private String brandId;

    private String uid;
    private UserBean userBean;

    @Override
    public int getLayoutRes() {
        return R.layout.distri_upgrade_dialog2;
    }

    @Override
    public float getDimAmount() {
        return 0.3f;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        Bundle arguments = getArguments();
        distriGoodsBean = (DistriGoodsBean) arguments.getSerializable("distriGoodsBean");
        brandId = arguments.getString("brandId");
        upgrade.setOnClickListener(this);

        userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }

        upgrade.setText("我要认证");
    }

//    private void upgrade() {
//        String url = BaseAPI.BASE_URL + "Brand/upgrade";
//        OkGo.post(url)
//                .params("uid", uid)
//                .params("pro_id", distriGoodsBean.E_ProID)
//                .params("level", "1")
//                .params("brand_id", brandId)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
//                        int status = bgResponse.status;
//                        String info = (String) bgResponse.info;
//                        if (status == 1) {
//                            upgrade.setText("审核中");
//                            upgrade.setSolid(R.color.app_e5e5e5);
//                            upgrade.setEnabled(false);
//                        } else {
//                            ToastUtils.showError(info, getContext());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        ToastUtils.showError("请求失败", getContext());
//                    }
//                });
//    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.upgrade) {
            startActivityForResult(ApplyForTalentActivity.getInstance(getActivity(), ApplyForTalentActivity.MODE_AFT_4, uid), 1);
            dismiss();
//            if (userBean.getE_VerWorker().equals("0")){
//                startActivityForResult(ApplyForTalentActivity.getInstance(getActivity(), ApplyForTalentActivity.MODE_AFT_4, uid), 1);
//                dismiss();
//            }else {
//                upgrade();
//            }
        }
    }
}
