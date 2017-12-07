package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.AddressBean;
import cn.biggar.biggar.dialog.SelectorAddressDialog;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.AddressPersenter;
import cn.biggar.biggar.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.view.RootLayout;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.VerifyUtils;


/**
 * Created by 张炼 on 2016/8/30.
 * 新增地址
 */
public class AddToAddressActivity extends BiggarActivity implements SelectorAddressDialog.CallBackListener {
    String mState;
    AddressBean mBean;
    @BindView(R.id.edit_address_name)
    EditText editAddressName;
    @BindView(R.id.edit_address_phone)
    EditText editAddressPhone;
    @BindView(R.id.selector_address)
    TextView selectorAddress;
    @BindView(R.id.edit_detailed_address)
    EditText editDetailedAddress;
    @BindView(R.id.add_address_ok)
    LinearLayout addAddressOk;
    Context mContext;
    String mAddress;
    AddressPersenter presenter;
    @BindView(R.id.add_address_ok_text)
    TextView addAddressOkText;

    public static Intent statIntent(Context context, String state) {
        Intent intent = new Intent(context, AddToAddressActivity.class);
        intent.putExtra("state", state);

        context.startActivity(intent);
        return intent;
    }

    public static Intent statIntent(Context context, String state, AddressBean bean) {
        Intent intent = new Intent(context, AddToAddressActivity.class);
        intent.putExtra("state", state);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_addto_address;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
        mState = getIntent().getStringExtra("state");
        presenter = new AddressPersenter(mContext);
        if (mState.equals("edit")) {
            mBean = (AddressBean) getIntent().getSerializableExtra("bean");
            dataInit();
        } else {
            RootLayout.getInstance(this).setTitle("添加收货地址");
        }


    }

    private void dataInit() {
        editAddressName.setText(mBean.getE_Name());
        editAddressPhone.setText(mBean.getE_Mobile());

        selectorAddress.setText(mBean.getE_prov() + "," + mBean.getE_city() + "," + mBean.getE_dist());
        editDetailedAddress.setText(mBean.getE_Adress());
        RootLayout.getInstance(this).setTitle("编辑收货地址");
        mProv = mBean.getE_prov();
        mCity = mBean.getE_city();
        mDist = mBean.getE_dist();
    }

    @OnClick({R.id.selector_address, R.id.add_address_ok})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selector_address:
                SelectorAddressDialog dialog = new SelectorAddressDialog(mContext);
                dialog.setListener(this);
                dialog.showDialog();
                break;
            case R.id.add_address_ok:
                submmitAddress();
                break;
        }
    }

    private void submmitAddress() {
        String mUserName = editAddressName.getText().toString();
        String mPhoneCode = editAddressPhone.getText().toString();
        String mAddress = selectorAddress.getText().toString();
        String mDetailAddress = editDetailedAddress.getText().toString();

        if (vertifyInput(mUserName, mPhoneCode, mAddress, mDetailAddress)) {
            showLoading();
            if (mState.equals("edit")) {

                presenter.editAddress(mBean.getID(), mBean.getE_MemID(), mDetailAddress, mPhoneCode, mProv, mCity, mDist, mUserName, new OnObjectListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        dismissLoading();
                        ToastUtils.showNormal(result);
                        finish();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        dismissLoading();
                        ToastUtils.showError(msg);
                    }
                });
            } else {
                String mUserId = Preferences.getUserBean(mContext).getId();
                presenter.add_Address(mUserName, mUserId, mProv, mCity, mDist, mDetailAddress, mPhoneCode, new OnObjectListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        dismissLoading();
                        ToastUtils.showNormal(result);
                        finish();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        dismissLoading();
                        ToastUtils.showError(msg);
                    }
                });
            }
        }
    }

    private boolean vertifyInput(String mUserName, String mPhoneCode, String mAddress, String mDetailAddress) {
        boolean canSubmit = true;
        if (TextUtils.isEmpty(mUserName)) {
            editAddressName.setError("收件人不能为空。");
            editAddressName.requestFocus();
            canSubmit = false;
        } else if (!VerifyUtils.isMobileNo(mPhoneCode)) {
            editAddressPhone.setError("手机格式错误。");
            editAddressPhone.requestFocus();
            canSubmit = false;
        } else if (TextUtils.isEmpty(mPhoneCode)) {
            editAddressPhone.setError("手机号码不能为空。");
            editAddressPhone.requestFocus();
            canSubmit = false;
        } else if (TextUtils.isEmpty(mAddress)) {
            selectorAddress.setError("请选择地址。");
            selectorAddress.requestFocus();
            canSubmit = false;
        } else if (TextUtils.isEmpty(mDetailAddress)) {
            editDetailedAddress.setError("请输入详细地址。");
            editDetailedAddress.requestFocus();
            canSubmit = false;
        }
        return canSubmit;
    }

    private String mProv, mCity, mDist;

    @Override
    public void callBack(String data, String prov, String city, String dist) {
        mAddress = data;
        mProv = prov;
        mCity = city;
        mDist = dist;
        selectorAddress.setText(mAddress);
        editDetailedAddress.setText("");
    }
}
