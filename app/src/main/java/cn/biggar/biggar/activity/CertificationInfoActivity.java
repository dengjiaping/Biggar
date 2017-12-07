package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.TalentLabelBean;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.MultiStateView;
import cn.biggar.biggar.view.MultipleTextViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.view.RootLayout;
import per.sue.gear2.presenter.OnObjectsListener;

/**
 * Created by mx on 2016/12/14.
 * 认证信息 / 达人
 */

public class CertificationInfoActivity extends BiggarActivity {
    @BindView(R.id.ci_edit)
    EditText mEt;
    @BindView(R.id.mtv_label)
    MultipleTextViewGroup mMtvLabel;
    @BindView(R.id.multiStateView)
    MultiStateView mMSV;
    private CommonPresenter mCommonP;

    private List<String> mLabels; //认证标签

    private String mStrLabel;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, CertificationInfoActivity.class);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_certification_info;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStrLabel=mEt.getText().toString().trim();
                if (TextUtils.isEmpty(mStrLabel)){
                    ToastUtils.showError("请选取认证的标签");
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("label",mStrLabel);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void initData(){
        mLabels=new ArrayList<>();
        mCommonP=new CommonPresenter(this);
        getLabelData();
    }

    private void initEvent(){
        mMtvLabel.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
            @Override
            public void onMultipleTVItemClick(View view, int position) {
                mEt.setText(mLabels.get(position));
            }
        });

        mMSV.setOnMultiStateViewClickListener(new MultiStateView.OnMultiStateViewClickListener() {
            @Override
            public void onRetry(int Type) {
                getLabelData();
            }
        });
    }

    private void getLabelData(){
        mCommonP.getTalentLabelList("300", new OnObjectsListener<TalentLabelBean>() {
            @Override
            public void onSuccess(List<TalentLabelBean> result) {
                super.onSuccess(result);
                if (!Utils.ListIsEmpty(result)){
                    mMSV.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    mLabels.clear();
                    for(TalentLabelBean bean:result){
                        mLabels.add(bean.getE_Name());
                    }
                    mMtvLabel.setTextViews(mLabels);
                }else{
                    mMSV.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                mMSV.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }

}
