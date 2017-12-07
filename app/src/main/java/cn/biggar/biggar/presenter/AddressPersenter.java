package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.bean.AddressBean;

import java.util.ArrayList;

import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by 张炼 on 2016/8/30.
 * 地址P
 */
public class AddressPersenter extends AbsPresenter {
    Context mContext;
    private IUserDetailsAPI iUserDetailsAPI;

    public AddressPersenter(Context context) {
        mContext = context;

        iUserDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }

    /**
     * 根据用户Id查询地址列表
     *
     * @param userId
     */
    public void queryAddressList(String userId, final OnObjectListener<AddressBean> listener) {
        iUserDetailsAPI.queryAddressList(userId).subscribe(new Subscriber<ArrayList<AddressBean>>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<AddressBean> addressBeen) {
                listener.onSuccess(addressBeen);
            }
        });
    }

    /**
     * 删除地址
     *
     * @param userId
     * @param listener
     */
    public void deleteAddressList(String userId, String addressId, final OnObjectListener<String> listener) {
        iUserDetailsAPI.deleteAddress(userId, addressId).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 设为默认地址
     *
     * @param userId
     * @param addressId
     */
    public void setDefaultAddress(String userId, String addressId, final OnObjectListener<String> listener) {
        iUserDetailsAPI.setDefaultAddress(userId, addressId).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 编辑收货地址
     *
     * @param Id
     */
    public void editAddress(String Id, String memberID, String address, String mobil, String prov, String city, String dist, String custome
            , final OnObjectListener<String> listener) {
        iUserDetailsAPI.editAddress(Id,memberID,address,mobil,prov,city,dist,custome).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 添加地址
     *
     * @param custome 收件人
     * @param address 地址
     * @param mobil   手机号
     */
    public void add_Address(String custome, String memberID, String prov, String city, String dist, String address, String mobil, final OnObjectListener<String> listener) {
        iUserDetailsAPI.add_Address(custome, memberID,prov,city,dist,address, mobil).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 获得默认地址
     * @param id
     */
    public void getDefaultAddress(String id, final OnObjectListener<AddressBean> listener){
        iUserDetailsAPI.getDefaultAddress(id).subscribe(new Subscriber<AddressBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(AddressBean bean) {
                listener.onSuccess(bean);
            }
        });
    }
}
