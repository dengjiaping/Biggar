package cn.biggar.biggar.api.userDetails;

import android.content.Context;

import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.AddressBean;
import cn.biggar.biggar.bean.BrokerBean;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.CheckVisitBean;
import cn.biggar.biggar.bean.DesireBean;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.GuildMemberBean;
import cn.biggar.biggar.bean.JPushBean;
import cn.biggar.biggar.bean.LabelBean;
import cn.biggar.biggar.bean.LiWuBean;
import cn.biggar.biggar.bean.MyWishBean;
import cn.biggar.biggar.bean.RedPacketBean;
import cn.biggar.biggar.bean.TheChestBean;
import cn.biggar.biggar.bean.UserBean;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/8.
 */
public class UserDetailsApiImpl implements IUserDetailsAPI {
    private Context mContext;

    public UserDetailsApiImpl(Context context) {
        mContext = context;
    }

    @Override
    public Observable<ArrayList<UserBean>> queryUserDetails(String tId, String uId) {
        String url = BaseAPI.API_USER_DETAILS + tId + "&E_UID=" + uId;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }

    @Override
    public Observable<ArrayList<DesireBean>> queryUserDesire(String userId, int pageNum, int pages) {
        String url = BaseAPI.API_DESIRE_LIST + "?p=" + pageNum + "&pages=" + pages + "&MID=" + userId;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<DesireBean>(DesireBean.class));
    }

    @Override
    public Observable<ArrayList<LabelBean>> queryUserLabel(String userId) {
        String url = BaseAPI.API_GET_LEBEL + "?pages=" + 100 + "&MID=" + userId + "&p=" + 1;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<LabelBean>(LabelBean.class));
    }

    @Override
    public Observable<String> commitLabel(String userId, String mId, String tagId, String e_name) {
        String url = BaseAPI.API_COMMIT_LABEL + "?E_UID=" + mId + "&MID=" + mId + "&TAGID=" + tagId + "&E_Name=" + e_name;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<ArrayList<AddressBean>> queryAddressList(String userId) {
        String url = BaseAPI.GET_ADDRESS_LIST;
        Map<String, String> params = new HashMap<String, String>();
        params.put("ID", userId);
        return new APIObserver(mContext, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<AddressBean>(AddressBean.class));
//        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<AddressBean>(AddressBean.class));
    }

    @Override
    public Observable<String> deleteAddress(String userId, String addressId) {
        String url = BaseAPI.GET_DELETE_ADDRESS + "?E_MemID=" + userId + "&ID=" + addressId;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> setDefaultAddress(String userId, String addressId) {
        String url = BaseAPI.SET_DEFAULT_ADDRESS + "?UID=" + userId + "&addID=" + addressId;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> editAddress(String Id, String memberID, String address, String mobil, String prov, String city, String dist, String custome) {
        String url = BaseAPI.GET_EDIT_ADDRESS;

        Map<String, String> params = new HashMap<>();
        params.put("ID", Id);
        params.put("address", address);
        params.put("mobil", mobil);
        params.put("prov", prov);
        params.put("city", city);
        params.put("dist", dist);
        params.put("custome", custome);
        params.put("memberID", memberID);

        return new APIObserver(mContext, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> add_Address(String custome, String memberID, String prov, String city, String dist, String address, String mobil) {
        String url = BaseAPI.GET_ADD_ADDRESS;
        Map<String, String> params = new HashMap<>();
        params.put("custome", custome);
        params.put("address", address);
        params.put("memberID", memberID);
        params.put("mobil", mobil);
        params.put("prov", prov);
        params.put("city", city);
        params.put("dist", dist);
        params.put("mobil", mobil);

        return new APIObserver(mContext, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<AddressBean> getDefaultAddress(String Id) {
        String url = BaseAPI.GET_DEFAULT_ADDRESS + "?ID=" + Id;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<AddressBean>(AddressBean.class));
    }

    @Override
    public Observable<TheChestBean> getMyTheChest(String uid, int pages, int p) {
        String url = BaseAPI.GET_MY_CASE + "?uid=" + uid + "&pages=" + pages + "&p=" + p;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<TheChestBean>(TheChestBean.class));

    }


    @Override
    public Observable<ArrayList<CardBean>> queryMyCard(String userId, int pageNum, int pages) {
        String url = BaseAPI.GET_MY_CARD + "?p=" + pageNum + "&pages=" + pages + "&ID=" + userId;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardBean>(CardBean.class));

    }

    @Override
    public Observable<ArrayList<UserBean>> queryMyFans(String userId, int pageNum, int pages, String searchName) {
        String url = BaseAPI.GET_MY_FANS + "?uid=" + userId + "&pages=" + pages + "&p=" + pageNum + "&E_Name=" + searchName;
//        String url = BaseAPI.GET_MY_FANS+"?uid="+userId+"&pages="+pages+"&p="+pageNum;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }


    @Override
    public Observable<ArrayList<FansListBean>> queryFansList(String userId, int pageNum, int pages) {
        String url = BaseAPI.GET_FANS_LIST + "?uid=" + userId + "&pages=" + pages + "&p=" + pageNum;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<FansListBean>(FansListBean.class));
    }


    @Override
    public Observable<String> presented(String cardid, String uid, String fromuser) {
        String url = BaseAPI.GET_PRESENTED + "?cardid=" + cardid + "&uid=" + uid + "&fromuser=" + fromuser;
        Map<String, String> params = new HashMap<>();
        params.put("cardid", cardid);
        params.put("uid", uid);
        params.put("fromuser", fromuser);

        return new APIObserver(mContext, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<ArrayList<BrokerBean>> queryBroker(String userid, String brokerName, int pages, int p) {
        String url = BaseAPI.GET_MY_BROKER_LIST + "?uid=" + userid + "&name=" + brokerName + "&pages=" + pages + "&p=" + p;
//        String url =  BaseAPI.GET_MY_BROKER_LIST+"?uid="+userid+"&name="+brokerName;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BrokerBean>(BrokerBean.class));
    }

    @Override
    public Observable<String> joinGuilds(String userid, String brindId, String content) {
        String url = BaseAPI.GET_JOIN_GUILDS + "?uid=" + userid + "&brind=" + brindId + "&E_Content" + content;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<String>(String.class));
    }


    @Override
    public Observable<ArrayList<BrokerBean>> querySkipJudge(String userid, String name) {
        String url = BaseAPI.GET_GUILD_DATA + "?uid=" + userid + "&bid=" + name;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url)).observeOnMainThread(new BaseParser<ArrayList<BrokerBean>>(BrokerBean.class));

    }

    @Override
    public Observable<ArrayList<FansListBean>> queryFocusList(String userId, String cid, int pageNum, int pages) {
        String url = BaseAPI.GET_FOCUS_LIST + "?uid=" + userId + "&cid=" + cid + "&pages=" + pages + "&p=" + pageNum;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<FansListBean>(FansListBean.class));
    }

    @Override
    public Observable<ArrayList<GuildMemberBean>> queryMembers(String userId, int pageNum, int pages, String guildId) {
        String url = BaseAPI.GET_BROKER_MEMBER_LIST + "?uid=" + userId + "&bid=" + guildId + "&pages=" + pages + "&p=" + pageNum;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<GuildMemberBean>(GuildMemberBean.class));
    }

    @Override
    public Observable<ArrayList<MyWishBean>> queryMyWishCard(String userId) {
        String url = BaseAPI.GET_WISH_LIST + "?uid=" + userId;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<MyWishBean>(MyWishBean.class));
    }

    @Override
    public Observable<String> logout(String bid, String userId) {
        String url = BaseAPI.GET_BROKER_LOGOUT + "?uid=" + userId + "&bid=" + bid;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> addWish(String userId, String wishCardId) {
        String url = BaseAPI.GET_ACCOUNT_ADD_WISH + "?uid=" + userId + "&cardid=" + wishCardId;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> updateWish(String userId, String oldCardId, String newCardId) {
        String url = BaseAPI.GET_ACCOUNT_UPDATE_WISH + "?uid=" + userId + "&cardid=" + oldCardId + "&bid=" + newCardId;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<JPushBean> getPushMsg(String id) {
        String url = BaseAPI.GET_JPUSH_MSG + "?cid=" + id;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<JPushBean>(JPushBean.class));
    }


    @Override
    public Observable<ArrayList<CheckVisitBean>> getCheckVisit(String id, int pageNum, int pages) {
        String url = BaseAPI.GET_USER_LOG_NUM + "?uid=" + id + "&pages=" + pages + "&p=" + pageNum;

        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<ArrayList<CheckVisitBean>>(CheckVisitBean.class));
    }

    @Override
    public Observable<String> uploadPhoto(String id, String imgs) {
        String url = BaseAPI.API_PERSON_PHOTO_UPLOAD + "?uid=" + id + "&img=" + imgs;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<RedPacketBean> getWealList(String id, int pageNum, int pages) {
        String url = BaseAPI.API_WEAL_LIST + "?uid=" + id + "&pages=" + pages + "&p=" + pageNum;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<RedPacketBean>(RedPacketBean.class));
    }

    @Override
    public Observable<Integer> getBalance(String id) {
        String url = BaseAPI.API_GET_BLANCE;
        Map<String, String> params = new HashMap<>();
        params.put("uid", id);
        return new APIObserver(mContext, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<Integer>(Integer.class));
    }

    @Override
    public Observable<ArrayList<LiWuBean>> getGiftList(String id) {
        String url = BaseAPI.API_LIWU_LIST + "?p=" + 0 + "&" + "pages=" + 100 + "&uid=" + id;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<LiWuBean>(LiWuBean.class));
    }

    @Override
    public Observable<CardBean> getCard(String type, String brand_id) {
        String url = BaseAPI.API_GET_CARD;
        if (type.equals("home")) {
            url = url + "?home=" + type;
        } else if (type.equals("brand")) {
            url = url + "?type=" + type + "&brand_id=" + brand_id;
        } else if (type.equals("gift")) {
            url = url + "?type=" + type + "&giftID=" + brand_id;
        }
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardBean>(CardBean.class));
    }

    @Override
    public Observable<CardBean> getGiftCard(String type, String id, String member_id) {
        String url = BaseAPI.API_GET_CARD + "?type=" + type + "&giftID=" + id + "&member_id=" + member_id;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardBean>(CardBean.class));
    }

    @Override
    public Observable<CardBean> getBrandCard(String type, String id, String uid) {
        String url = BaseAPI.API_GET_CARD + "?type=" + type + "&brandID=" + id + "&uid=" + uid;
        return new APIObserver(mContext, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CardBean>(CardBean.class));
    }
}
