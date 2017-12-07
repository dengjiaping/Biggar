package cn.biggar.biggar.api;

import android.content.Context;

import cn.biggar.biggar.api.account.IUserAPI;
import cn.biggar.biggar.api.account.UserApiImpl;
import cn.biggar.biggar.api.activity.ActivityImpl;
import cn.biggar.biggar.api.activity.IActivityAPI;
import cn.biggar.biggar.api.brand.BrandImpl;
import cn.biggar.biggar.api.brand.IBrandAPI;
import cn.biggar.biggar.api.common.CommonApiImpl;
import cn.biggar.biggar.api.common.ICommonAPI;
import cn.biggar.biggar.api.order.IOrderAPI;
import cn.biggar.biggar.api.order.OrderApiImpl;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.api.userDetails.UserDetailsApiImpl;
import cn.biggar.biggar.api.video.IVideoAPI;
import cn.biggar.biggar.api.video.VideoApiImpl;


/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/4
* 版 权：比格科技有限公司
*/
public class DataApiFactory {

    private static DataApiFactory ourInstance = new DataApiFactory();

    public static DataApiFactory getInstance() {
        return ourInstance;
    }

    private DataApiFactory() {
    }

    public IUserAPI createIUserAPI(Context context) {
        return new UserApiImpl(context);
    }

    public IVideoAPI createIVideoAPI(Context context) {
        return new VideoApiImpl(context);
    }

    public IActivityAPI createIActivityAPI(Context context) {
        return new ActivityImpl(context);
    }

    public ICommonAPI createICommonAPI(Context context) {
        return new CommonApiImpl(context);
    }

    public IBrandAPI createIBrandAPI(Context context) {
        return new BrandImpl(context);
    }

    public IUserDetailsAPI createIUserDetais(Context context) {
        return new UserDetailsApiImpl(context);
    }

    public IOrderAPI createIOrderAPI(Context context) {
        return new OrderApiImpl(context);
    }


}
