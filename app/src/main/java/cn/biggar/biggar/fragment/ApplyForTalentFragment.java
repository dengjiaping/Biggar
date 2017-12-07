package cn.biggar.biggar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.EditInfoActivity;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.OrdinaryPromptDialog;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.UserPersenter;
import cn.biggar.biggar.utils.Utils;
import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.widget.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mx on 2016/12/12.
 * 申请认证比格达人  1
 */

public class ApplyForTalentFragment extends BiggarFragment {


    @BindView(R.id.aft_hint_tv)
    TextView mTvHint;
    @BindView(R.id.aft_header_iv)
    CircleImageView mIvHeader;
    @BindView(R.id.aft_apply_for_layout)
    LinearLayout mApplyLayout;
    private String mUserId;
    private int mMode;

    /**
     * @param mode 0 申请认证（个人资料进入）   1 成功   2申请认证 （非个人资料进入）
     * @return
     */
    public static ApplyForTalentFragment getInstance(int mode, String userId) {
        ApplyForTalentFragment fragment = new ApplyForTalentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_apply_for_talent;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mMode = getArguments().getInt("mode");
        mUserId = getArguments().getString("userId");
        if (mMode == 1) {
            mIvHeader.setVisibility(View.VISIBLE);
            mApplyLayout.setVisibility(View.GONE);
            mIvHeader.setUseDefaultStyle(false);
            mTvHint.setText("恭喜你，已经成为比格达人");
            if (AppPrefrences.getInstance(getActivity()).isLogined()) {
                UserBean userBean = Preferences.getUserBean(getActivity());
                if (userBean != null) {
                    Glide.with(getActivity())
                            .load(Utils.getRealUrl(userBean.getE_HeadImg()))
                            .apply(new RequestOptions().centerCrop().placeholder(R.mipmap.gear_image_default))
                            .into(mIvHeader);
                } else {
                    mIvHeader.setImageResource(R.mipmap.gear_image_default);
                }
            } else {
                mIvHeader.setImageResource(R.mipmap.gear_image_default);
            }
        }

    }

    @OnClick(R.id.aft_apply_for_tv)
    public void OnClick(View view) {
        getStatus();
    }


    /**
     * 单键 提示
     *
     * @param hint
     */
    private void showHintDialog(String hint, final String result) {
        OrdinaryPromptDialog dialog = new OrdinaryPromptDialog(getActivity(), false);
        dialog.setOnSelectedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result == null) {
                    //已在审核中
                    getActivity().finish();
                } else {
                    //已审核通过
                    Intent intent = new Intent();
                    intent.putExtra("label", result);
                    getActivity().setResult(-2, intent);
                    getActivity().finish();
                }
            }
        });

        dialog.setTitle(hint);
        dialog.show();
    }

    private void getStatus() {
        showLoading();
        new UserPersenter(getActivity()).checkCertificationStatus(mUserId, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                dismissLoading();
                showHintDialog("您之前的认证申请已通过审核！", result);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                if (code == 0) {//未认证过
                    if(mMode==0){
                        getActivity().setResult(RESULT_OK, new Intent());
                    }else if (mMode==2){
                        startActivity(EditInfoActivity.startIntent(getActivity(),EditInfoActivity.MODE_TALENT));
                    }
                    getActivity().finish();
                    dismissLoading();
                } else if (code == 2) {         //已在审核中
                    showHintDialog(msg, null);
                }else if (code == 3){
                    startActivity(EditInfoActivity.startIntent(getActivity(),EditInfoActivity.MODE_TALENT));
                }
                dismissLoading();
            }
        });
    }
}
