package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.AddressManageAdapter;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.AddressBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.AddressPersenter;
import cn.biggar.biggar.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.widget.MyListView;

public class AddressManageActivity extends BiggarActivity implements AddressManageAdapter.OnDeleteListener {

    @BindView(R.id.add_address)
    LinearLayout addAddress;
    @BindView(R.id.address_listview)
    MyListView mListview;
    AddressPersenter presenter;
    Context mContext;
    UserBean mBean;
    @BindView(R.id.address_no_text)
    TextView addressNoText;
    AddressManageAdapter mAdapter;
    List<AddressBean> addressBean;
    public static String EDIT_ADDRESS_STATE = "edit";
    public static String ADD_ADDRESS_STATE = "add";

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, AddressManageActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryInit();
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
        queryInit();
    }

    private void queryInit() {
        presenter = new AddressPersenter(mContext);
        mBean = Preferences.getUserBean(mContext);
        presenter.queryAddressList(mBean.getId(), new OnObjectListener<AddressBean>() {
            @Override
            public void onSuccess(List<AddressBean> results) {
                super.onSuccess(results);

                addressBean = results;
                if (results != null && !results.isEmpty()) {
                   setAdapter();
                    mListview.setVisibility(View.VISIBLE);
                    addressNoText.setVisibility(View.GONE);
                } else {
                    addressNoText.setVisibility(View.VISIBLE);
                    mListview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void setAdapter() {

        addressNoText.setVisibility(View.GONE);
        mAdapter = new AddressManageAdapter(mContext);
        mAdapter.setListener(this);
        mAdapter.setData(addressBean);
        mListview.setAdapter(mAdapter);
    }

    @OnClick({R.id.add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address:
                AddToAddressActivity.statIntent(mContext,ADD_ADDRESS_STATE);
                break;
        }
    }

    @Override
    public void onDeleteAddress(final int p, String addressId) {
        showLoading();
        presenter.deleteAddressList(mBean.getId(), addressId, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                dismissLoading();
                ToastUtils.showError(result);
                addressBean.remove(p);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dismissLoading();
                ToastUtils.showError(msg);
            }
        });
    }

    @Override
    public void onSetDefaultAddress(String addressId) {
        presenter.setDefaultAddress(mBean.getId(), addressId, new OnObjectListener<String>() {

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(msg);
            }
        });
    }

    @Override
    public void onEditAddress(AddressBean bean) {
        AddToAddressActivity.statIntent(mContext,EDIT_ADDRESS_STATE,bean);
    }
}
