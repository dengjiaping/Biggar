package cn.biggar.biggar.preference;

import android.content.Context;

import cn.biggar.biggar.bean.UserBean;


/**
 * Created by su on 2015/8/26.
 * 邮箱 125906374@qq.com
 */
public class Preferences extends AbsPreferences {

    private static final String PREFERENCES_ACCOUNT_USER_KEY = "cn.biggar.biggar.PREFERENCES_ACCOUNT_USER_KEY";

    /**
     * 存储登录用户信息
     * @param context
     * @param userBean
     */
    public static void storeUserBean(Context context, UserBean userBean){

        storeBaseObject(context , userBean,PREFERENCES_ACCOUNT_USER_KEY );
    }

    /**
     * 获取登录用户对象
     * @param context
     * @return
     */
    public static UserBean getUserBean(Context context){

        return (UserBean)getBaseObject(context,PREFERENCES_ACCOUNT_USER_KEY);
    }



}
