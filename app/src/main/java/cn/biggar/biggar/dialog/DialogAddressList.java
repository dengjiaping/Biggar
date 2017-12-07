package cn.biggar.biggar.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.Serializable;
import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.AddAddressActivity;
import cn.biggar.biggar.bean.update.AddressListBean;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/11/27.
 */

public class DialogAddressList extends BaseBottomDialog {
    private RecyclerView rvAddress;
    private MyAdapter adapter;

    private List<AddressListBean> addresses;

    public static DialogAddressList newInstance(List<AddressListBean> addresses) {
        DialogAddressList dialogAddressList = new DialogAddressList();
        Bundle bundle = new Bundle();
        bundle.putSerializable("addresses", (Serializable) addresses);
        dialogAddressList.setArguments(bundle);
        return dialogAddressList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_address_list;
    }

    @Override
    public void bindView(View v) {
        addresses = (List<AddressListBean>) getArguments().getSerializable("addresses");
        v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        v.findViewById(R.id.btn_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddAddressActivity.class));
            }
        });
        rvAddress = (RecyclerView) v.findViewById(R.id.rv_address);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(addresses);
        rvAddress.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                updateCheck(position);
            }
        });
    }

    private void updateCheck(int position) {
        List<AddressListBean> datas = adapter.getData();
        AddressListBean addressListBean = adapter.getItem(position);
        for (AddressListBean data : datas) {
            if (data.id.equals(addressListBean.id)) {
                data.isCheck = true;
            } else {
                data.isCheck = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    static class MyAdapter extends BaseQuickAdapter<AddressListBean, BaseViewHolder> {

        public MyAdapter(@Nullable List<AddressListBean> data) {
            super(R.layout.item_address_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AddressListBean item) {
            helper.setImageResource(R.id.iv_check, item.isCheck ? R.mipmap.mall_checked_3x : R.mipmap.mall_uncheck_3x);
        }
    }
}
