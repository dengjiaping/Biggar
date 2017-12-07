package cn.biggar.biggar.api.account;


import cn.biggar.biggar.bean.AccountBean;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.bean.UserBean;


import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/25
* 版 权：比格科技有限公司
*/
public interface IUserAPI {


    /**
     * 登陆
     * @param username 用户账号
     * @param password 用户密码
     * @return
     */
    Observable<UserBean> login(String username, String password, String registrationID, int state);

    /**
     * 注册
     * @param mobile 手机账号
     * @param nick 昵称
     * @param password 密码
     * @param 
     * @param imei IMEI
     * @param system 系统名称
     * @return
     */
    Observable<UserBean> regist(String mobile, String nick, String password, String headImage, String imei, String system);

    /**
     * 忘记密码
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    Observable<String> forgetPwd(String mobile, String password, String code);


    /**
     * 获取验证码
     * @param mobile 手机号码
     * @param type 默认为注册， 忘记密码： ForgetPassword  绑定手机 BindMobile
     * @return
     */
    Observable<String> getCode(String mobile, String type);


    /**
     * 第三方登陆
     * @param platform “QQ”， “WEIXIN”, "SINA"
     * @param openId
     * @param token
     * @return
     */
    Observable<UserBean> loginByThird(String platform, String openId, String token, String info, String registrationID);

    /**
     * 更新头像
     * @param userId
     * @param avartUrl
     * @return
     */
    Observable<String> updateUserAvart(String userId, String avartUrl);

    /**
     * 更新公会头像
     * @param userId
     * @param avartUrl
     * @return
     */
    Observable<String> updataGuildImg(String userId, String avartUrl);

    /**
     * 绑定第三方
     * @param userId
     * @param platform
     * @param openId
     * @param token
     * @return
     */
    Observable<String> bindThird(String userId, String platform, String openId, String token);
    /**
     * 绑定第三方
     * @param userId
     * @param platform
     * @return
     */
    Observable<String> unbindThird(String userId, String platform);



    /**
     * 评论
     * @param id 评论对像ID、如视频ID
     * @param content 内容
     * @param type 评论类型：如视频（VIDEO）、会员（MEMBER）、商品（SHOP）、通告（ACTIVITY）
     * @param uid userId
     * @param usernick user昵称
     * @return
     */
    Observable<String> comment(String id, String content, String type, String uid, String usernick);

    /**
     * 关注、收藏
     * @param id 关注对像ID、如会员ID
     * @param nickname 关注对像呢称、如会员呢称
     * @param type 关注类型：1:会员、2:品牌、3:产品、4视频、5主题、只可单选
     * @param uid userId
     * @param usernick user昵称
     * @return
     */
    Observable<String> concern(String id, String nickname, int type, String uid, String usernick);

    /**
     * 举报用户
     * @param type  1 用户  2 视频
     * @param id 用户ID
     * @return
     */
    Observable<String> report(int type, String id);

    /**
     *  验证是否 关注
     * @param id 关注对像ID、如会员ID、多个对像用|串起来、如: 10|12|30
     * @param type 关注类型：1:会员、2:品牌、3:产品、4视频、5主题、只可单选
     * @param userid
     * @param checktype 验证我关注的（ME）、验证关注我的（TA）、省略时默认为：ME
     */
    Observable<String> checkConcern(String id, int type, String userid, String checktype);


    /**
     *  送礼
     * @param tmid 收到礼物的会员ID、如视频所属用户
     * @param id 礼物ID
     * @param type 类型：视频（1）、任务（2）、商品（3）、品牌（4）、网红（5）默认：1
     * @param oid 类型对像ID：如视频ID
     * @param uid 登录用户ID
     * @return
     */
    Observable<String> songLi(String tmid, String id, String type, String oid, String uid);

    Observable<String> sendGift(String gift_num, String gift_id, String member, String cmember, String type, String OID);


    /**
     * 修改资料
     * @param userId 登录会员ID
     * @param parameter 参数
     * @return
     */
    Observable<String> updateUserInfo(String userId, HashMap<String, String> parameter);


    /**
     * 查询 用户 可切换账号列表
     * @param Id 用户E_MembID
     * @return
     */
    Observable<ArrayList<AccountBean>> queryAccountList(String Id);

    /**
     * 绑定 账号
     * @param name
     * @param pwd
     * @param type
     * @param content
     * @return
     */
    Observable<String> bingdingAccount(String uid, String name, String pwd, String type, String content);

    /**
     * 修改手机号码
     * @param code
     * @param userId
     * @param phone
     * @return
     */
    Observable<String> updatePhone(String code, String userId, String phone);

    Observable<String> topsBindingPhone(String code, String mobile, String uid, String passWord);

    /**
     * 商家绑定手机
     * @param username
     * @param password
     * @param mobile
     * @return
     */
    Observable<String> businessBoundPhone(String username, String password, String mobile);
    /**
     * 验证登录  其他端账号
     * @param id
     * @param uid
     * @return
     */
    Observable<String> validationLogin(String id, String uid);

    /**
     * 关注、收藏
     * @param oid 关注对像ID、如会员ID
     * @param type 类型 1:会员关注、2:品牌关注、3:其它关注
     * @param uid userId
     * @return
     */
    Observable<String> cancelConcern(String uid, String oid, int type);

    /**
     * 意见反馈
     * @param feedText
     * @param phone
     * @param email
     * @param userId
     * @return
     */
    Observable<String> feedBack(String feedText, String phone, String email, String userId, String userName);

    Observable<String> drawRedPacket(String key, String uid, int state);


    /**
     * 检查 认证状态
     * @param uid
     * @return
     */
    Observable<String> checkCertificationStatus(String uid);

    /**
     *  申请认证达人
     * @param uid
     * @param img ,分割
     * @param label 职业
     * @return
     */
    Observable<String> applyForTalent(String uid, String img, String label);


    /**
     * 添加微信
     * @param uid 我的ID
     * @param tid   他的ID
     * @param price 价格
     * @param wechatAccount 微信号
     * @return
     */
    Observable<String> payAddWehatAccount(String uid, String tid, String price, String wechatAccount, String orderId, String type);


    /**
     * 确认添加微信
     * @param uid 我的ID
     * @param tid   他的ID
     * @return
     */
    Observable<String> confirmAddWeChat(String uid, String tid, String id);

    /**
     * 添加微信号投诉
     * @param uid  举报人id
     * @param oid 举报对象id
     * @param str 举报内容
     * @param name 举报人姓名
     * @return
     */
    Observable<String> complaintWeChat(String uid, String oid, String str, String name);


    /**
     * 是否拥有 查看微信账户的权力
     * @param uid
     * @param tid
     * @return
     */
    Observable<String> getIsPayWeChatLookPower(String uid, String tid);

    /**
     * 星币列表
     * @param
     * @param
     * @return
     */
    Observable<ArrayList<BuyStarMoneyBean>> getBuyStarMoney();


}
