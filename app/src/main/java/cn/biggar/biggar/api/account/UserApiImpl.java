package cn.biggar.biggar.api.account;

import android.content.Context;

import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.AccountBean;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.bean.UserBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/4
* 版 权：比格科技有限公司
*/
public class UserApiImpl implements IUserAPI {

    private Context context;

    public UserApiImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<UserBean> login(String username, String password, String registrationID, int state) {
        String url;
        if (state == LoginActivity.IS_BUSINESS) {
            url = BaseAPI.API_BUSINESS_LOGIN;//商家登录
        } else {
            url = BaseAPI.API_ACCOUNT_LOGIN;// 用户登录
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("regId", registrationID);
        params.put("device", "android");
      /*  return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<UserBean>(UserBean.class));*/
        // url = "http://www.biggar.cn/api.php/Api/login?username="+username+"&password=" + password;
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }

    @Override
    public Observable<UserBean> regist(String mobile, String nick, String password, String headImage, String imei, String system) {
        String url = BaseAPI.API_ACCOUNT_REGIEST;
        Map<String, String> params = new HashMap<>();
        params.put("E_Mobile", mobile);
        params.put("E_Name", nick);
        params.put("E_PWD", password);
        params.put("E_HeadImg", headImage);
        params.put("E_MobileIME", imei);
        params.put("E_MobileSYS", system);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }

    @Override
    public Observable<String> forgetPwd(String mobile, String password, String code) {
        String url = BaseAPI.API_ACCOUNT_FORGET_PWD;
        Map<String, String> params = new HashMap<>();
        params.put("E_Mobile", mobile);
        params.put("E_PWD", password);
        params.put("MobileCode", code);
        params.put("E_MobileIME", "");
        params.put("E_MobileSYS", "android");
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> getCode(String mobile, String type) {
        String url = BaseAPI.API_ACCOUNT_CODE_GET;
        Map<String, String> params = new HashMap<>();
        params.put("Mobile", mobile);
        params.put("R_PinTai", "app");
        params.put("M_CODE", type);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<UserBean> loginByThird(String platform, String openId, String taken, String info, String registrationID) {//绑定第三方
        String url = BaseAPI.API_ACCOUNT_THIRD_LOGIN + "?time=" + (new java.util.Date().getTime());
        Map<String, String> params = new HashMap<>();
        params.put("platform", platform);
        params.put("openId", openId);
        params.put("accessToken", taken);
        params.put("info", info);
        params.put("regId", registrationID);
        params.put("device", "android");


        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
//        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createGET(url));
//        return apiObserver.observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }

    @Override
    public Observable<String> updateUserAvart(String userId, String avartUrl) {
//        String url = BaseAPI.API_ACCOUNT_UPDATE_AVART+"?MemberID="+userId+"&HeadImgUrl="+avartUrl+"&type="+"HEAD";
        String url = BaseAPI.API_ACCOUNT_UPDATE_AVART;
        Map<String, String> params = new HashMap<>();

        params.put("MemberID", userId);
        params.put("HeadImgUrl", avartUrl);
        params.put("type", "HEAD");

        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
//        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createGET(url));
//        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> updataGuildImg(String userId, String avartUrl) {
        String url = BaseAPI.API_ACCOUNT_UPDATE_GUILD_IMG;
        Map<String, String> params = new HashMap<>();

        params.put("uid", userId);
        params.put("HeadImgUrl", avartUrl);
        params.put("type", "HEAD");

        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> bindThird(String userId, String platform, String openId, String token) {
        String url = BaseAPI.API_ACCOUNT_BIND_THIRD;
        Map<String, String> params = new HashMap<>();
        params.put("platform", platform);
        params.put("openId", openId);
        params.put("accessToken", token);
        params.put("userId", userId);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> unbindThird(String userId, String platform) {
        String url = BaseAPI.API_ACCOUNT_UN_BIND_THIRD;
        Map<String, String> params = new HashMap<>();
        params.put("platform", platform);
        params.put("uid", userId);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> comment(String id, String content, String type, String uid, String usernick) {
        String url = BaseAPI.API_ACCOUNT_COMMENT;
        Map<String, String> params = new HashMap<>();
        params.put("E_RelationID", id);
        params.put("E_Content", content);
        params.put("E_Type", type);
        params.put("E_UID", uid);
        params.put("E_NAME", usernick);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> concern(String id, String nickname, int type, String uid, String usernick) {
        String url = BaseAPI.API_ACCOUNT_CONCERN;
        Map<String, String> params = new HashMap<>();
        params.put("ID", id);
        params.put("TIT", nickname);
        params.put("TYPE", type + "");
        params.put("E_UID", uid);
        params.put("E_NAME", usernick);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> report(int type, String id) {
        String url = BaseAPI.GET_REPORT_URL + (type == 1 ? "MemberID" : "MemberID") + "=" + id;
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createGET(url));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> checkConcern(String id, int type, String userid, String checktype) {
        String url = BaseAPI.API_ACCOUNT_CHECK_CONCERN;
        Map<String, String> params = new HashMap<>();
        params.put("ID", id);
        params.put("TYPE", type + "");
        params.put("E_UID", userid);
        params.put("CHECK", checktype);

        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> songLi(String tmid, String id, String type, String oid, String uid) {
        String url = BaseAPI.API_SONGLI + "?TMID=" + tmid + "&" + "ID=" + id + "&" + "TYPE=" + type + "&" + "OID=" + oid + "&" + "E_UID=" + uid;

        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createGET(url));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> sendGift(String gift_num, String gift_id, String member, String cmember, String type, String OID) {
        String url = BaseAPI.API_SEND_GIFT;
        Map<String, String> params = new HashMap<>();
        params.put("gift_num", gift_num);
        params.put("gift_id", gift_id);
        params.put("member", member);
        params.put("cmember", cmember);
        params.put("type", type);
        params.put("OID", OID);
        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createPOST(url, params));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> updateUserInfo(String userId, HashMap<String, String> parameter) {
        StringBuffer url = new StringBuffer(BaseAPI.API_ACCOUNT_UPDATE_INFO);
//        Map<String, String> params = new HashMap<>();
//        params.put("ID", userId);
        url.append("?ID=" + userId);
        if (parameter.containsKey("E_Name")) {
            url.append("&E_Name=" + parameter.get("E_Name"));
//            params.put("E_Name", parameter.get("E_Name"));
        }
        if (parameter.containsKey("E_Signature")) {
            url.append("&E_Signature=" + parameter.get("E_Signature"));
//            params.put("E_Signature", parameter.get("E_Signature"));
        }
        if (parameter.containsKey("E_Sex")) {
            url.append("&E_Sex=" + parameter.get("E_Sex"));
//            params.put("E_Sex", parameter.get("E_Sex"));
        }
        if (parameter.containsKey("E_Birthday")) {
            url.append("&E_Birthday=" + parameter.get("E_Birthday"));
//            params.put("E_Birthday", parameter.get("E_Birthday"));
        }
        if (parameter.containsKey("E_Height")) {
            url.append("&E_Height=" + parameter.get("E_Height"));
//            params.put("E_Height", parameter.get("E_Height"));
        }
        if (parameter.containsKey("E_Marriage")) {
            url.append("&E_Marriage=" + parameter.get("E_Marriage"));
//            params.put("E_Marriage", parameter.get("E_Marriage"));
        }
        if (parameter.containsKey("E_Education")) {
            url.append("&E_Education=" + parameter.get("E_Education"));
//            params.put("E_Education", parameter.get("E_Education"));
        }
        if (parameter.containsKey("E_FamilyAdd")) {
            url.append("&E_FamilyAdd=" + parameter.get("E_FamilyAdd"));
//            params.put("E_FamilyAdd", parameter.get("E_FamilyAdd"));
        }
        if (parameter.containsKey("E_Adress")) {
            url.append("&E_Adress=" + parameter.get("E_Adress"));
//            params.put("E_Adress", parameter.get("E_Adress"));
        }
        if (parameter.containsKey("E_Tags")) {
            url.append("&E_Tags=" + parameter.get("E_Tags"));
//            params.put("E_Adress", parameter.get("E_Adress"));
        }
        if (parameter.containsKey("E_blog")) {
            url.append("&blog=" + parameter.get("E_blog"));
        }
        if (parameter.containsKey("E_platform")) {
            url.append("&platform=" + parameter.get("E_platform"));
        }
        if (parameter.containsKey("E_Weixin")) {
            url.append("&wechat=" + parameter.get("E_Weixin"));
        }
        if (parameter.containsKey("E_Constellation")) {
            url.append("&Conste=" + parameter.get("E_Constellation"));
        }
        if (parameter.containsKey("E_Wechatprice")) {
            url.append("&price=" + parameter.get("E_Wechatprice"));
        }

        APIObserver apiObserver = new APIObserver(context, ApiConnectionFactory.createGET(url.toString()));
        return apiObserver.observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<ArrayList<AccountBean>> queryAccountList(String Id) {
        String url = BaseAPI.API_ACCOUNT_LIST + "?" + "E_MembID=" + Id;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<AccountBean>(AccountBean.class));
    }

    @Override
    public Observable<String> bingdingAccount(String uid, String name, String pwd, String type, String content) {
        String url = BaseAPI.SET_ACCOUNT_INFO;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("AE_Type", type);
        params.put("AE_Account", name);
        params.put("AE_Pswd", pwd);
        params.put("AE_Name", content);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> updatePhone(String code, String userId, String phone) {
        String url = BaseAPI.GET_ACCOUNT_UPDATE_PHONE + "?" + "MobileCode=" + code + "&ID=" + userId + "&E_Mobile=" + phone;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> topsBindingPhone(String code, String mobile, String uid, String passWord) {
        String url = BaseAPI.API_ACCOUNT_BIND_THIRD_PHONE;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("code", code);
        params.put("mobile", mobile);
        params.put("password", passWord);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> businessBoundPhone(String username, String password, String mobile) {
        String url = BaseAPI.GET_BUSINESS_UPDATE_PHONE;
        Map<String, String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params.put("mobile",mobile);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> validationLogin(String id, String uid) {
        String url = BaseAPI.API_ACCOUNT_VALIDATION_LOGIN + "?" + "ID=" + id + "&E_MembID=" + uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> cancelConcern(String uid, String oid, int type) {
        String url = BaseAPI.GET_CANCEL_CONCERN + "?" + "uid=" + uid + "&conid=" + oid + "&typeid=" + type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> feedBack(String feedText, String phone, String email, String userId, String userName) {
        String url = BaseAPI.API_GET_FEED_BACK;
        Map<String, String> params = new HashMap<>();
        params.put("E_Content", feedText);
        params.put("E_Mail", email);
        params.put("E_Tel", phone);
        params.put("ID", userId);
        params.put("E_Name", userName);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url,params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> drawRedPacket(String mKeys, String uid,int state) {
        String url = null;
        Map<String, String> params = null;
        if (state ==2){

            url = BaseAPI.API_DRAW_RED_PACKET;
            params = new HashMap<>();
            params.put("uid",uid);
            params.put("Keys",mKeys);
        }else {
           url = BaseAPI.API_DRAW_RED_PACKET_1+"?rid="+mKeys+"&uid="+uid;
       }
       if (state == 2){
           return new APIObserver(context, ApiConnectionFactory.createPOST(url,params))
                   .observeOnMainThread(new BaseParser<String>(String.class));
       }else {
           return new APIObserver(context, ApiConnectionFactory.createGET(url))
                   .observeOnMainThread(new BaseParser<String>(String.class));
       }

    }

    @Override
    public Observable<String> checkCertificationStatus(String uid) {
        String url = BaseAPI.API_CHECK_STATUS + "?uid=" + uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> applyForTalent(String uid, String img, String label) {
        String url = BaseAPI.API_SUBMIT_AFT + "?uid=" + uid + "&img=" + img + "&worker=" + label;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> payAddWehatAccount(String uid, String tid, String price, String wechatAccount, String orderId, String type) {
        String url = BaseAPI.API_ADD_WECHAT_BIGGAR + "?uid=" + uid + "&toformid=" + tid + "&price=" + price + "&wechatnum=" + wechatAccount + "&pay_id=" + orderId + "&type=" + type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> confirmAddWeChat(String uid, String tid, String id) {
        String url = BaseAPI.API_CONFIRM_ADD_WECHAT;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("formid", tid);
        params.put("id", id);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> complaintWeChat(String uid, String oid, String content, String name) {
        String url = BaseAPI.API_WECHAT_COMPLAINT;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("objid", oid);
        params.put("content", content);
        params.put("name", name);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> getIsPayWeChatLookPower(String uid, String tid) {
        String url = BaseAPI.API_IS_HAS_LOOK_WECHAT_POWER;
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("formid", tid);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<ArrayList<BuyStarMoneyBean>> getBuyStarMoney() {
        String url = BaseAPI.API_STAR_MONEY_LIST;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BuyStarMoneyBean>(BuyStarMoneyBean.class));
    }


}
