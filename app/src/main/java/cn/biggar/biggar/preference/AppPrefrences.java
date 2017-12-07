package cn.biggar.biggar.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2015/12/29
* 版 权：比格科技有限公司
*/
public class AppPrefrences extends Preferences{


    private static AppPrefrences appPrefrences;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String SP_NAME = "app_info";
    //第一次启动
    private final String KEY_FIRST = "isFirst";
    //新手引导第一次
    private final String KEY_NEWBIE_GUIDE_FIRST = "isNewbie_guide_First";//
    private final String KEY_ACCOUNT_LIST = "account_list";
    private final String KEY_ACCOUNT_LAST = "account_last";
    private final String KEY_PASSWORD = "password";
    private final String KEY_TOKEN = "token";
    //商家token
    private final String KEY_MERCHANT_TOKEN = "key_merchant_token";
    private final String KEY_LOGIN_SOURCE = "key_login_source";
    private final String  KEY_CURRTIME = "currTime";
    private final String  KEY_SERVER_IP = "serverIp";
    private final String  KEY_SPLASH_BG = "cardTime";
    private final String  KEY_GIFT_BG = "giftTime";
    private final String KEY_ROLE="key_role";//角色 1 达人 2 商家 3 公会
    private Context context;

    private final String  KEY_DTNAMIC_IS_VISIBLE_LOCATION= "dynamic_isvisible_location";





    private  AppPrefrences(Context ctx) {
        this.sp = ctx.getSharedPreferences(SP_NAME, ctx.MODE_PRIVATE);
        context = ctx.getApplicationContext();
        this.editor = this.sp.edit();
    }

    public static synchronized AppPrefrences getInstance(Context ctx){

        if(appPrefrences == null){
            appPrefrences = new AppPrefrences(ctx);
        }
        return appPrefrences;
    }

    /**
     * 保存第一次运行flag;
     */
    public void setIsFirst(boolean isFirst) {
        this.editor.putBoolean(KEY_FIRST, isFirst);
        this.editor.commit();
    }

    /**
     * 获取是否是第一次运行Flag;
     * @return
     */
    public boolean isFirst() {

        return this.sp.getBoolean(KEY_FIRST, true) ;
    }

    /**
     * 保存第一次运行新手引导flag;
     */
    public void setIsNewbieGuideFirst(boolean isFirst) {
        this.editor.putBoolean(KEY_NEWBIE_GUIDE_FIRST, isFirst);
        this.editor.commit();
    }

    /**
     * 获取是否是第一次运行 新手引导Flag;
     * @return
     */
    public boolean isNewbieGuideFirst() {
//        return false; //现版本 默认不显示新手引导
        return this.sp.getBoolean(KEY_NEWBIE_GUIDE_FIRST, true) ;
    }

    public void addAccount(String  account) {
        ///this.editor.
        //this.editor.commit();
    }

    public void setLastAccount(String account){
        this.editor.putString(KEY_ACCOUNT_LAST, account);
        this.editor.commit();
    }

    public String getLastAccount(String defIP){
        return this.sp.getString(KEY_ACCOUNT_LAST, defIP);
    }

    public void setCardTime(String time){
        this.editor.putString(KEY_SPLASH_BG, time);
        this.editor.commit();
    }

    public String getCardTime(){
        return this.sp.getString(KEY_SPLASH_BG, null);
    }
    public void setGiftTime(String time){
        this.editor.putString(KEY_GIFT_BG, time);
        this.editor.commit();
    }
    public String getGiftTime(){
        return this.sp.getString(KEY_GIFT_BG, null);
    }
    public void setServerIP(String ip){
        this.editor.putString(KEY_SERVER_IP, ip);
        this.editor.commit();
    }

    public String getServerIP(String defIP){
        return this.sp.getString(KEY_SERVER_IP, defIP);
    }

    public void setToken(String token){
        this.editor.putString(KEY_TOKEN, token);
        this.editor.commit();
    }

    public String getToken(){
        return this.sp.getString(KEY_TOKEN, null);
    }



    /**
     * 商家token
     * @param token
     */
    public void setMerchantToken(String token){
        this.editor.putString(KEY_MERCHANT_TOKEN, token);
        this.editor.commit();
    }
    /**
     * 商家token
     */
    public String getMerchantToken(){
        return this.sp.getString(KEY_MERCHANT_TOKEN, null);

    }
//    public void setGiftList(ArrayList<LiWuBean> beans){
//        this.editor.putString(KEY_MERCHANT_TOKEN, token);
//        this.editor.commit();
//    }


    public String getmLoginSource() {
        return this.sp.getString(KEY_LOGIN_SOURCE,null);
    }

    public void setmLoginSource(String mLoginSource) {
        this.editor.putString(KEY_LOGIN_SOURCE, mLoginSource);
        this.editor.commit();
    }

    public void setCurrTime(long currTime){
        this.editor.putLong(KEY_CURRTIME, currTime);
        this.editor.commit();
    }

    public long getCurrTime(){
        return this.sp.getLong(KEY_CURRTIME, System.currentTimeMillis());
    }

    public boolean isLogined(){

        if(null == getUserBean(context))
            return false;

        if(TextUtils.isEmpty(getToken())){
            return false;
        }else {
            return true;
        }
    }

    public void setDynamicByLocation(boolean isVisible) {
        this.editor.putBoolean(KEY_DTNAMIC_IS_VISIBLE_LOCATION, isVisible);
        this.editor.commit();
    }

    public boolean isVisibleDynamicByLocation() {
        return this.sp.getBoolean(KEY_DTNAMIC_IS_VISIBLE_LOCATION, true);
    }

    /**
     * 保存选择 账号类型//角色 1 达人 2 商家 3 公会
     */
    public void setAccountType(int type) {
        this.editor.putInt(KEY_ROLE, type);
        this.editor.commit();
    }

    /**
     * 获取当前 账户类型//角色 1 达人 2 商家 3 公会
     * @return
     */
    public int getCurrentAccountType() {
        return this.sp.getInt(KEY_ROLE, 1) ;
    }

}


