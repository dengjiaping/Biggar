package cn.biggar.biggar.api.userDetails;

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

import rx.Observable;

/**
 * Created by 张炼 on 2016/8/8.
 */
public interface IUserDetailsAPI {
    /**
     * 根据用户ID查询用户信息
     *
     * @param
     * @return
     */
    Observable<ArrayList<UserBean>> queryUserDetails(String tId, String uId);

    /**
     * 根据用户ID查询用户愿望
     *
     * @param userId
     * @return
     */
    Observable<ArrayList<DesireBean>> queryUserDesire(String userId, int pageNum, int pages);

    /**
     * 获取标签
     *
     * @param userId
     * @return
     */
    Observable<ArrayList<LabelBean>> queryUserLabel(String userId);

    /**
     * 提交标签
     *
     * @param userId 登录会员ID
     * @param mId    被打标签的会员ID
     * @param tagId  标签ID、多个用|分开、可为空
     * @param e_name 录入新标签、可为空
     * @return
     */
    Observable<String> commitLabel(String userId, String mId, String tagId, String e_name);

    /**
     * 查询地址列表
     *
     * @param userId 用户ID
     * @return
     */
    Observable<ArrayList<AddressBean>> queryAddressList(String userId);

    /**
     * 删除地址
     *
     * @param userId
     * @param addressId
     * @return
     */
    Observable<String> deleteAddress(String userId, String addressId);

    /**
     * 设为默认地址
     *
     * @param userId
     * @param addressId
     * @return
     */
    Observable<String> setDefaultAddress(String userId, String addressId);

    /**
     * 编辑收货地址
     */
    Observable<String> editAddress(String Id, String memberID, String address, String mobil, String prov, String city, String dist, String custome);

    /**
     * 新增收货地址
     *
     * @param custome
     * @param address
     * @param mobil
     * @return
     */
    Observable<String> add_Address(String custome, String memberID, String prov, String city, String dist, String address, String mobil);

    /**
     * 获得默认地址
     *
     * @param Id
     * @return
     */
    Observable<AddressBean> getDefaultAddress(String Id);

    /**
     * 我的宝箱
     *
     * @param uid
     * @param pages
     * @param p
     * @return
     */
    Observable<TheChestBean> getMyTheChest(String uid, int pages, int p);

    /**
     * 获得我的卡卷列表
     *
     * @return
     */
    Observable<ArrayList<CardBean>> queryMyCard(String userId, int pageNum, int pages);

    /**
     * 获得用户粉丝列表
     *
     * @param userId
     * @param pageNum
     * @param pages
     * @param searchName
     * @return
     */
    Observable<ArrayList<UserBean>> queryMyFans(String userId, int pageNum, int pages, String searchName);

    /**
     * 获得用户粉丝列表
     *
     * @param userId
     * @param pageNum
     * @param pages
     * @return
     */
    Observable<ArrayList<FansListBean>> queryFansList(String userId, int pageNum, int pages);

    /**
     * 赠送卡券
     *
     * @param cardid   卡券Id
     * @param uid      用户Id
     * @param fromuser 被赠送人Id
     * @return
     */
    Observable<String> presented(String cardid, String uid, String fromuser);

    /**
     * 经纪列表
     *
     * @return
     */
    Observable<ArrayList<BrokerBean>> queryBroker(String userid, String brokerName, int pages, int p);

    /**
     * 加入公会
     *
     * @param userid
     * @param brindId 公会ID
     * @return
     */
    Observable<String> joinGuilds(String userid, String brindId, String content);

    /**
     * 我的经纪跳转判断
     *
     * @param userid
     * @return
     */
    Observable<ArrayList<BrokerBean>> querySkipJudge(String userid, String bid);

    /**
     * 获得用户关注列表
     *
     * @param userId
     * @param pageNum
     * @param pages
     * @return
     */
    Observable<ArrayList<FansListBean>> queryFocusList(String userId, String cid, int pageNum, int pages);


    /**
     * 获得成员列表
     *
     * @param userId
     * @param pageNum
     * @param pages
     * @return
     */
    Observable<ArrayList<GuildMemberBean>> queryMembers(String userId, int pageNum, int pages, String guildId);

    /**
     * 获得我的愿望卡卷列表
     *
     * @return
     */
    Observable<ArrayList<MyWishBean>> queryMyWishCard(String userId);

    /**
     * 退出公会
     *
     * @param userId
     * @return
     */
    Observable<String> logout(String bid, String userId);

    /**
     * 添加愿望
     *
     * @param userId
     * @param wishCardId 愿望卡卷ID
     * @return
     */
    Observable<String> addWish(String userId, String wishCardId);

    /**
     * 修改愿望
     *
     * @param userId
     * @param oldCardId
     * @param newCardId
     * @return
     */
    Observable<String> updateWish(String userId, String oldCardId, String newCardId);

    /**
     * 获取推送消息
     *
     * @param id
     * @return
     */
    Observable<JPushBean> getPushMsg(String id);


    /**
     * 谁看过我
     *
     * @param id
     * @return
     */
    Observable<ArrayList<CheckVisitBean>> getCheckVisit(String id, int pageNum, int pages);

    /**
     * 相册上传
     *
     * @param id
     * @param imgs
     * @return
     */
    Observable<String> uploadPhoto(String id, String imgs);

    Observable<RedPacketBean> getWealList(String id, int pageNum, int pages);

    /**
     * 获取用户余额
     *
     * @param id
     * @return
     */
    Observable<Integer> getBalance(String id);

    /**
     * 获取礼物列表
     *
     * @param id
     * @return
     */
    Observable<ArrayList<LiWuBean>> getGiftList(String id);

    /**
     * 获取卡券
     *
     * @param
     * @return
     */
    Observable<CardBean> getCard(String type, String id);

    /**
     * 获取卡券
     *
     * @param
     * @return
     */
    Observable<CardBean> getGiftCard(String type, String id, String member_id);

    /**
     * 获取卡券
     *
     * @param type
     * @param id
     * @param uid
     * @return
     */
    Observable<CardBean> getBrandCard(String type, String id, String uid);
}
