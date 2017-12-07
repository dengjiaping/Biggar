package cn.biggar.biggar.adapter;

import android.content.Context;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BaseAdapter;
import cn.biggar.biggar.base.ViewHolder;
import cn.biggar.biggar.bean.AddressBean;


import java.util.HashMap;


/**
 * Created by 张炼 on 2016/8/30.
 * 地址管理adapter
 */
public class AddressManageAdapter extends BaseAdapter<AddressBean> {
    public AddressManageAdapter(Context con) {
        super(con, R.layout.adapter_address_manage);
    }

    public void setListener(OnDeleteListener listener) {
        this.listener = listener;
    }

    OnDeleteListener listener;


    HashMap<String, Boolean> states = new HashMap<>();//用于记录每个RadioButton的状态，并保证只可选一个
    @Override
    public void convert(ViewHolder viewHolder, final AddressBean bean, final int position, View convertView) {
        boolean res = false;

        TextView mAddressName = viewHolder.getViewById(R.id.address_user_name);
        TextView mAddressPhone = viewHolder.getViewById(R.id.address_user_phone);
        TextView mAddressDetailed = viewHolder.getViewById(R.id.detailed_address);
        final RadioButton mRadio = viewHolder.getViewById(R.id.text_my_address_item_icon);
        TextView mDefaultText = viewHolder.getViewById(R.id.text_my_address_item_default);
        TextView mEditAddress = viewHolder.getViewById(R.id.edit_address_button);
        TextView mDeleteAddress = viewHolder.getViewById(R.id.delete_address_button);

        mAddressName.setText(bean.getE_Name());
        mAddressPhone.setText(bean.getE_Mobile());
        mAddressDetailed.setText(bean.getE_prov()+","+bean.getE_city()+","+bean.getE_dist()+","+bean.getE_Adress());
        mDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onDeleteAddress(position,bean.getID());
            }
        });
        mEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditAddress(bean);
            }
        });
        mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                if (states.get(String.valueOf(position)) == null) {
                    states.put(String.valueOf(0), false);
                }
                states.put(String.valueOf(position), mRadio.isChecked());
                AddressManageAdapter.this.notifyDataSetChanged();
            }
        });
        if (states.get(String.valueOf(position)) == null) {

            if (bean.getE_State().equals("0")) {
                res = false;
                mDefaultText.setText("设为默认");
            } else if (bean.getE_State().equals("1")) {
                res = true;
                mDefaultText.setText("默认地址");
            }

        }else if (states.get(String.valueOf(position)) == false) {
            res = false;
            mDefaultText.setText("设为默认");
        } else if (states.get(String.valueOf(position)) == true) {
            res = true;
            mDefaultText.setText("默认地址");
            listener.onSetDefaultAddress(bean.getID());
        }
        mRadio.setChecked(res);
    }
    public interface OnDeleteListener{
        void onDeleteAddress(int p, String addressId);
        void onSetDefaultAddress(String addressId);
        void onEditAddress(AddressBean bean);
    }
}
