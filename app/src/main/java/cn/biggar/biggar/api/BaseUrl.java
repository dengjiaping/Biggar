package cn.biggar.biggar.api;

/**
 * 调用H5的页面Url
 */
public class BaseUrl {
//        public static String BASE_URL = "http://192.168.2.104/app.php/";//测试
//    public static String BASE_URL = "http://mp.biggar.cn/app.php/";//测试
    public static String BASE_URL = "http://www.biggar.cn/app.php/";//正式

    public static String ADV_ACTIVITY_URL = BASE_URL + "Star/srar_cover";
    public static String SHARE_BRAND_URL = BASE_URL + "brand/index.html?"; //品牌分享URL
    public static String GET_MY_ORDER_H5_URL = BASE_URL + "user/myorder.html"; //我的订单
    public static String GET_MY_INCOME_H5_URL = BASE_URL + "user/my_income.html"; //我的收入
    public static String GET_RIZ_ZOAWD_H5_URL = BASE_URL + "index/eden.html"; //比格乐园
    public static String GET_GOODS_DETAILS_H5_URL = BASE_URL + "product/details.html"; //商品详情
    public static String GET_ACTIVITY_DETAILS_H5_URL = BASE_URL + "activity/activity_details.html"; //活动详情
    public static String GET_BRAND_BOX_URL = BASE_URL + "Brand/box.html"; //打开品牌空间宝箱
    public static String GET_FEED_BACK_URL = BASE_URL + "user/feedback.html"; //意见反馈
    public static String GET_CHAT_URL = BASE_URL + "index/chat.html"; //聊天
    public static String GET_SEARCH = BASE_URL + "index/search.html"; //搜索
    public static String BUSINESS_CARD = BASE_URL + "brand/business_card.html?"; //奖品兑换
}
