package cn.biggar.biggar.app;

/**
 * Created by mx on 2016/8/9.
 * 常量 类
 */
public class Constants {
    public static final int PAGE_SIZE = 10;// list 每页默认加载数量
    public static final String WEB_VERSION = "1.1"; //web版本标识

    public static final String TMP_IMAGE_PATH = "/biggar/tmpImages"; //图片缓存路径
    public static final String TMP_QRCODE_IMAGE_PATH = "/biggar/tmpqrcodeImages"; //公会分享的二维码缓存路径
    public static final String TMP_PHOTO_ALBUM_IMAGE_PATH = "/biggar/photo_album"; //保存到相册


    public static final String KEY_SOCIRTY_INTRO = "1";//公会公司简介
    public static final String KEY_SOCIRTY_NOTICE = "2";//公会公告
    public static final String KEY_SOCIRTY_RECIURT = "3";//公会招募
    public static final String KEY_USER_HEAD = "userHead";//更新用户头像
    public static final String KEY_GUILD_HEAD = "guildHead";//更新公会头像


    public static final int UNIT_BIGGAR = 100;// 1RMB == 100比格币
    public static final String KEY_DELETE_IMGE = "5";//删除图片

    public static final String QQ_LOGIN = "QQ"; //qq登录
    public static final String WEIXIN_LOGIN = "WEIXIN"; //微信登录
    public static final String SINA_LOGIN = "SINA"; //微博登录
    public static final String BIGGAR_LOGIN = "BIGGAR"; //biggar登录

    public static final String RONG_APP_KEY = "x4vkb1qpx7kik";
    public static final String RONG_APP_SECRET = "3RRDV1RiETx";


    /*******************************************趣拍*****************************/
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static final String WATER_MARK_PATH = "assets://Qupai/watermark/biggar_logo.png";

    /**
     * 默认码率
     */
    public static final int DEFAULT_BITRATE = 1000 * 1000;

    /**
     * 默认时长，单位秒
     */
    public static final int DEFAULT_DURATION_LIMIT = 5 * 60;

    public static int lastMusicId = 10;

    public static int getMusicId() {
        lastMusicId += 1;
        return lastMusicId;
    }
    /*******************************************趣拍*****************************/


    /**********************************微信*************************************/
    // appid
    public static final String APP_ID = "wx28461decfd4646bf";
    // 商户号
    public static final String MCH_ID = "1415195202";
    /**********************************微信************************************/


    public static final String IS_SHOULD_SHOW_REDPACKET_GUIDE = "is_should_show_redpacket_guide";//是否需要显示红包引导
    public static final String VERSION_CODE = "versiom_code";//版本号
    public static final String RONG_TOKEN = "rong_token";//融云token
    public static final String CHOOSE_GIFT_ID = "choose_gift_id";//选中的礼物id
    public static final String CHOOSE_GIFT_ITEM = "choose_gift_item";//选中的item
    public static final String MAP_SEARCH_HISTORY = "map_search_history";

    public static final String SPLASH_IMG = "splash_img";//启动页图片


    public static final int CODE_NETWORK_ERROR = 0x101;

    public static final int ORDER_ALL = 0;
    public static final int ORDER_WAIT_PAY = 1;
    public static final int ORDER_WAIT_TAKE = 2;
    public static final int ORDER_FINISH = 3;
    public static final int ORDER_CANCEL = 4;

    public static final String CARD_TYPE1 = "1";
    public static final String CARD_TYPE2 = "2";
    public static final String CARD_TYPE3 = "3";
    public static final String CARD_TYPE4 = "4";
}
