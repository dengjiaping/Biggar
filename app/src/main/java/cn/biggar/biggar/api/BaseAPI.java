package cn.biggar.biggar.api;

/*
* 文件名：
* 描 述：\
* 作 者：苏昭强
* 时 间：2016/4/25
* 版 权：比格科技有限公司
*/
public class BaseAPI {
//    public static String BASE_URL = "http://192.168.2.104/api.php/";//测试服
//    public static String BASE_RES_URL = "http://192.168.2.104/";//测试服

//    public static String BASE_URL = "http://mp.biggar.cn/api.php/";//测试服
//    public static String BASE_RES_URL = "http://mp.biggar.cn/";//测试服

    public static String BASE_URL = "http://www.biggar.cn/api.php/";//正式服
    public static String BASE_RES_URL = "http://www.biggar.cn/";//正式服

    public static final String QINIU_HOST = "http://cdn.biggar.cn/";
    public static final String QINIU_TOKEN = BASE_RES_URL + "vendor/index.php";
    public static final String API_COMMON_VERSION = BASE_URL + "Api/api_updatecheck";//检测更新
    public static final String API_ACCOUNT_LOGIN = BASE_URL + "Api/login";//登陆接口
    public static final String API_BUSINESS_LOGIN = BASE_URL + "Business/business_login";//商家登陆接口
    public static final String API_ACCOUNT_REGIEST = BASE_URL + "Api/reg";//注册接口
    public static final String API_ACCOUNT_CODE_GET = BASE_RES_URL + "Member/GetMobileCode";//获取验证码
    public static final String API_ACCOUNT_FORGET_PWD = BASE_URL + "Api/mpassword";//忘记接口
    public static final String API_ACCOUNT_THIRD_LOGIN = BASE_URL + "Api/api_account";//第三方登录
    public static final String API_VIDEO_PUBLISH = BASE_URL + "Api/save_video";//视频发布
    public static final String API_VIDEO_TYPE = BASE_URL + "Api/videotype.html";//视频类别
    public static final String API_VIDEO_TAG = BASE_URL + "Api/besthot_tags?pages=100";//视频标签
    public static final String API_ACCOUNT_UPDATE_AVART = BASE_URL + "Api/headUpload";//更新头像
    public static final String API_ACCOUNT_UPDATE_GUILD_IMG = BASE_URL + "Guild/headUpdate";//公会头像
    public static final String API_ACCOUNT_BIND_THIRD = BASE_URL + "Api/api_account_beng";//第三放绑定
    public static final String API_ACCOUNT_BIND_THIRD_PHONE = BASE_URL + "Api/bindingMobile";//第三放绑定手机
    public static final String API_ACCOUNT_UN_BIND_THIRD = BASE_URL + "Api/Unbundling";//解除第三放绑定
    public static final String API_ACCOUNT_CONCER_LIST = BASE_URL + "Index/index_concern_json";//视频关注列表
    public static final String API_VIDEO_RECOMMEND_LIST = BASE_URL + "Index/index_json.html";//视频推荐列表
    public static final String API_BANNER_GET = BASE_URL + "Api/adv.html";//获取广告
    public static final String API_ACCOUNT_IMAGE = BASE_URL + "Api/member_head.html?ID=";//获取用户相片 头像
    public static final String API_USER_DETAILS = BASE_URL + "Index/member_info_json.html?ID=";//获取用户详情
    public static final String API_VIDEO_DETAILS = BASE_URL + "Index/play_json.html";//获取视频 图片详情
    public static final String API_ACCOUNT_COMMENT = BASE_URL + "Index/add_comment_jsons";//评论
    public static final String API_COMMENT_LIKE = BASE_URL + "Index/comment_click_json.html";//评论点赞
    public static final String API_ACCOUNT_CONCERN = BASE_URL + "Index/add_concern_json.html";//关注/收藏
    public static final String API_COMMENT_LIST = BASE_URL + "Index/comment_list_json.html";//评论列表
    public static final String API_ACCOUNT_CHECK_CONCERN = BASE_URL + "Index/check_concern_json.html";//验证 是否关注
    public static final String API_MEMBER_VIDEO = BASE_URL + "Index/member_video_json.html";//会员上传的视频
    public static final String API_GET_CARD = BASE_URL + "Card/index";//卡券
    public static final String API_LIWU_LIST = BASE_URL + "Gift/sysgift";//我的礼物列表
    public static final String API_MY_LIWU_LIST = BASE_URL + "Index/mygift_json.html";//我(TA)礼物列表
    public static final String API_SONGLI = BASE_URL + "Index/send_gift_json.html";//送礼物
    public static final String API_SEND_GIFT = BASE_URL + "Gift/givingGift";//新版送礼物
    public static final String API_DELVIDEO = BASE_URL + "Index/DelVideo";//删除发布的图片或者视频
    public static final String API_BRAND_CONCER_LIST = BASE_URL + "Brand/pinpai_follow_json.html";//品牌关注列表
    public static final String API_BRAND_RECOMMEND_LIST = BASE_URL + "Brand/pinpai_index_json.html";//品牌推荐列表
    public static final String API_BRAND_DETAILS = BASE_URL + "Brand/details_json.html";//品牌详情
    public static final String API_BRAND_FNAS_SHOW = BASE_URL + "Brand/pinpai_fansshow_json.html";//品牌粉丝秀
    public static final String API_ACTIVITY_REWARD_LIST = BASE_URL + "Activity/index_new_json.html";//活动悬赏列表
    public static final String API_ACTIVITY_SHOW_LIST = BASE_URL + "Activity/dotask_video_json.html";//活动秀场列表
    public static final String API_ACTIVITY_MYJOIN_LIST = BASE_URL + "Activity/brand_mytask_json.html";//活动秀场列表
    public static final String API_BRADN_LOGO = BASE_URL + "Api/brand_logo.html";//品牌LOGO
    public static final String API_DESIRE_LIST = BASE_URL + "Index/member_treasure_json.html";//愿望列表
    public static final String API_CARD_COUPONS = BASE_URL + "Index/card_treasure_img.html?IDKEYS=";//卡劵图片地址
    public static final String API_GET_LEBEL = BASE_URL + "Index/member_fornicated.html";//获取标签
    public static final String API_BRAND_GOODS_LIST = BASE_URL + "Brand/pinpai_mall_json.html";//品牌商品
    public static final String API_COMMIT_LABEL = BASE_URL + "Index/add_fornicated_json.html";//提交标签
    public static String GET_REPORT_URL = BASE_URL + "Index/Web_ReportVideo.html?"; //举报用户/视频
    public static String GET_ACHIEVE_DESIRE_URL = BASE_URL + "Index/member_dotreasure_json?"; //实现愿望
    public static final String API_ACCOUNT_UPDATE_INFO = BASE_URL + "User/myinfo.html";//修改资料
    public static String GET_ADDRESS_LIST = BASE_URL + "User/address_json.html"; //获得地址列表
    public static String GET_DELETE_ADDRESS = BASE_URL + "User/delect_add.html"; //删除地址
    public static String SET_DEFAULT_ADDRESS = BASE_URL + "User/default_add.html"; //设为默认地址
    public static String GET_EDIT_ADDRESS = BASE_URL + "User/edit_add.html"; //编辑地址
    public static String GET_ADD_ADDRESS = BASE_URL + "User/add_address.html"; //添加地址
    public static String GET_DEFAULT_ADDRESS = BASE_URL + "User/default_address.html"; //获得默认地址
    public static String API_ACCOUNT_LIST = BASE_URL + "User/userinfo.html"; //切换账号列表
    public static String SET_ACCOUNT_INFO = BASE_URL + "User/switchconfig.html";//录入 切换账号信息
    public static String API_ACCOUNT_VALIDATION_LOGIN = BASE_URL + "User/switch_merchats_gourl.html";//验证 其他端账号
    public static String GET_MY_CARD = BASE_URL + "User/MyCard.html";//我的卡券
    public static String GET_MY_FANS = BASE_URL + "User/MyFans.html";//我的粉丝列表
    public static String GET_PRESENTED = BASE_URL + "User/GiftFans.html";//赠送卡券
    public static String GET_MY_CASE = BASE_URL + "User/MyCase.html";//我的宝箱
    public static String GET_ACCOUNT_UPDATE_PHONE = BASE_URL + "User/edit_mobile.html";//修改手机号码
    public static String GET_BUSINESS_UPDATE_PHONE = BASE_URL + "Business/business_bound_phone";//商家绑定手机
    public static String GET_MY_BRAND_LIST = BASE_URL + "User/MyBrand.html";//我的品牌列表
    public static String GET_MY_BROKER_LIST = BASE_URL + "User/MyAgent.html";//我的经纪列表
    public static String GET_JOIN_GUILDS = BASE_URL + "User/red.html";//加入公会
    public static String GET_FANS_LIST = BASE_URL + "User/ToMyFans";//粉丝列表
    public static String GET_GUILD_DATA = BASE_URL + "Guild/GuildData";//公会接口
    public static String GET_FOCUS_LIST = BASE_URL + "User/MyConcern";//关注列表
    public static String GET_WISH_LIST = BASE_URL + "User/CardCoupon.html";//我的愿望列表
    public static String GET_BROKER_MEMBER_LIST = BASE_URL + "Guild/Members";//公会成员列表
    public static String GET_BROKER_LOGOUT = BASE_URL + "User/logout.html";//退出公会
    public static String GET_BRAND_CARD_WISH_HOT_LIST = BASE_URL + "User/HotBrand";//卡卷愿望 热门品牌列表
    public static String GET_BRAND_CARD_WISH_LIST = BASE_URL + "User/brand";//卡卷愿望 品牌列表
    public static String GET_BRAND_CARD_LIST = BASE_URL + "User/CardBrand";//品牌卡卷列表
    public static String GET_ACCOUNT_ADD_WISH = BASE_URL + "User/MakeWish";//添加愿望
    public static String GET_ACCOUNT_UPDATE_WISH = BASE_URL + "User/exit_Wish";//修改愿望
    public static String GET_JPUSH_MSG = BASE_URL + "Api/get_push_msg";//极光推送
    public static String GET_USER_RANKINGS = BASE_URL + "User/Rankings";//公会排名
    public static String GET_USER_INCOME_RANKINGS = BASE_URL + "User/IncomeRanking";//收入排名
    public static String GET_MEMBER_MEMBEREDIT = BASE_URL + "Guild/MemberEdit";//管理员修改公会资料
    public static String GET_MEMBER_RELEASE = BASE_URL + "Guild/Release";//管理员修改公会资料
    public static String GET_USER_LOG_NUM = BASE_URL + "User/log_num";//谁看过我
    public static String GET_SHARE_SUCCESS = BASE_URL + "Api/share_member_action.html";//分享成功
    public static String GET_CANCEL_CONCERN = BASE_URL + "User/DelFollow";//取消关注
    public static String API_GUILD_RECRUITING_LIST = BASE_URL + "Guild/Recruit";//公会招募列表
    public static String API_GUILD_MEMBEREXAMINE_LIST = BASE_URL + "Guild/MemberExamine";//公会成员审核列表
    public static String API_GUILD_MBERSTATE = BASE_URL + "Guild/MberState";//公会成员审核
    public static String API_GUILD_DISSOLUTION = BASE_URL + "Guild/Dissolution";//解散公会
    public static String API_GUILD_INVITE_JOIN = BASE_URL + "Guild/MemberTion";//邀请加入公会
    public static String API_GUILD_INVITE_MEMBER_LIST = BASE_URL + "Guild/UserMember";//公会邀请成员列表
    public static String API_GUILD_SET_ADMIN = BASE_URL + "Guild/SettingsAdmin";//设置管理员
    public static String API_GUILD_DEL_MEMBER = BASE_URL + "Guild/DelMember";//踢出成员
    public static String API_GET_SCREEN = BASE_URL + "Guild/Screen";//筛选条件获取
    public static String API_GET_ALL_NEW_MESSAGE = BASE_URL + "Mynews/Notice";//获取所有类型最新消息
    public static String API_GET_MESSAGE_LIST = BASE_URL + "Mynews/MessageClass";//获取消息列表
    public static String API_GET_CLEAR_MESSAGE = BASE_URL + "Mynews/DelMessage";//清除消息
    public static String API_GET_MESSAGE_NUMBER = BASE_URL + "Mynews/MessageNum";//消息数量
    public static String API_GET_REWARD_LIST = BASE_URL + "Guild/ClickScreen";//公会悬赏列表
    public static String API_GET_IS_HAVE_GUILD = BASE_URL + "Guild/YesGuild";//是否有公会
    public static String API_GET_GUILD_DETAILS = BASE_URL + "Income/GuildIncome";//公会后台 详情Details
    public static String API_GET_TASK_MANAGE_LIST = BASE_URL + "Income/TaskManage";//公会任务管理列表
    public static String API_GET_Income_MANAGE_LIST = BASE_URL + "Income/IncomeStting";//公会收入管理列表
    public static String API_GET_INVITATION_RECORD = BASE_URL + "Income/Record";//邀约记录
    public static String API_GET_USER_INCOME_DETAILS_LIST = BASE_URL + "Income/IncomeDetail";//用户收入明细
    public static String API_GET_USER_TASK_DETAILS_LIST = BASE_URL + "Income/TaskDetail";//用户任务详细
    public static String API_GET_SPILT_SETTING_LIST = BASE_URL + "Income/SpiltSettings";//分成设置
    public static String API_SET_PROPORTION = BASE_URL + "Income/Proportion";//设置分成比例
    public static String API_GET_INCOME_DETAILS_LIST = BASE_URL + "Income/GuildDetail";//收入明细列表
    public static String API_GET_JOIN_GUILD_SET = BASE_URL + "Mynews/MberState";//邀请加入公会 同意或拒绝
    public static String API_GET_CREATE_GUILD = BASE_URL + "Guild/addguild";//创建公会申请
    public static String API_GET_FEED_BACK = BASE_URL + "User/feedback.html";//意见反馈
    public static String API_GET_VERIFY_CODE = BASE_URL + "Guild/getmobileCode";//验证手机号验证码
    public static String API_GET_GUILD_CREATE_STATUS = BASE_URL + "Guild/auditPerson";//获取用户 申请创建公会  状态
    public static String API_GET_HALL_OF_FAME_LIST = BASE_URL + "User/celebrity";//名人堂列表
    public static String API_BUSINESS_HOME_DATA = BASE_URL + "Business/business_index";//商家首页数据
    public static String API_BUSINESS_REGISTER = BASE_URL + "Business/business_reg";//商家注册
    public static String API_EXPERT_LIBRARY_LIST = BASE_URL + "Celebrity/Upscreen";//达人库
    public static String API_HANG_LINK_LIST = BASE_URL + "Business/product";//挂链商品
    public static String API_MY_LINK_LIST = BASE_URL + "Business/videourl";//我的链接
    public static String API_MARKETING_CASE_LIST = BASE_URL + "Celebrity/index";//营销案例列表
    public static String API_NEW_TALENT_LIST = BASE_URL + "Celebrity/Master";//新晋达人列表
    public static String API_EXPERT_LIBRARY_LEABEL = BASE_URL + "Celebrity/tags";//达人库标签
    public static String API_PERSON_PHOTO_UPLOAD = BASE_URL + "Personnal/index";//相册上传
    public static String API_WEAL_LIST = BASE_URL + "Personnal/redcentre";//福利列表
    public static String API_DRAW_RED_PACKET_1 = BASE_URL + "Personnal/getreds";//领取红包
    public static String API_DRAW_RED_PACKET = BASE_URL + "Card/addCard";//领取红包
    public static String API_CHECK_STATUS = BASE_URL + "User/authtion";//检查比格认证状态
    public static String API_SUBMIT_AFT = BASE_URL + "User/Apply";//提交申请达人认证
    public static String API_TALENT_LABEL = BASE_URL + "User/Label";//达人认证标签
    public static String API_ADD_WECHAT_BIGGAR = BASE_URL + "User/weatch";//添加微信  比格币
    public static String API_WECHAT_BUYERS_ORDER_LIST = BASE_URL + "Wechat/record";//买家购买 微信订单记录
    public static String API_WECHAT_MY_ORDER_LIST = BASE_URL + "Wechat/myapply";//我购买 微信订单记录
    public static String API_CONFIRM_ADD_WECHAT = BASE_URL + "Wechat/add";//确认添加了微信
    public static String API_WECHAT_COMPLAINT = BASE_URL + "Wechat/report";//微信号 投诉
    public static String API_WECHAT_ACCOUNT_WECHAT_PAY = BASE_URL + "Wechat/GoWxWebPay";//微信号 微信支付
    public static String API_STAR_MONEY_WECHAT_PAY = BASE_URL + "Gift/GoWxWebPay";//星币 微信支付
    public static String API_IS_HAS_LOOK_WECHAT_POWER = BASE_URL + "Wechat/is_wechat";//微信号 是否有查看权力
    public static String API_DELETE_IMAGE = BASE_URL + "Personnal/delimgs";//删除图片
    public static String API_QUERY_BRAND_RED_PACKET = BASE_URL + "Brand/pinpai_card";//品牌 红包  查询
    public static String API_GET_BRAND_RED_PACKET = BASE_URL + "Brand/Oncetime";//品牌 红包领取
    public static String API_GUILD_TASK_LIST = BASE_URL + "Guild/GuildTask";//公会后台任务列表
    public static String API_GET_GUILD_TASK = BASE_URL + "Guild/ReceiveTask";//公会后台 接受任务
    public static String API_GET_IS_BUSINESS = BASE_URL + "Business/business_state";//是否是商家
    public static String API_SET_EDIT_CHAIN = BASE_URL + "Business/editChain";//选择挂链
    public static String API_SET_EDIT_LINK = BASE_URL + "Business/addurl";//编辑链接
    public static String API_VERIFY_PASS = BASE_URL + "Api/userMobile";//判断密码
    public static String API_OPEN_LINK = BASE_URL + "Business/videoUrlstatus ";//开通和关闭链接
    public static String API_BUSSION_CARD = BASE_URL + "Business/businessCard";//商家卡片
    public static String API_STAR_MONEY_LIST = BASE_URL + "Gift/rechargeConfig";//开通和关闭链接
    public static String API_GET_BLANCE = BASE_URL + "Gift/account";//获取余额

}






