package cn.biggar.biggar.helper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.lzy.okgo.OkGo;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.biggar.biggar.BuildConfig;
import cn.biggar.biggar.activity.MoreMusicActivity;
import cn.biggar.biggar.app.Constants;
import cn.jpush.android.api.JPushInterface;
import per.sue.gear2.net.ApiConnectionFactory;
import per.sue.gear2.utils.GearLog;

/**
 * Created by Chenwy on 2017/10/19.
 */

public class InitHelper {
    private Application mApp;
    private Context mContext;
    private boolean isInit;

    public InitHelper() {
    }

    private static class InitHelperHolder {
        private static final InitHelper INIT_HELPER = new InitHelper();
    }

    public static InitHelper getInstance() {
        return InitHelperHolder.INIT_HELPER;
    }

    public boolean isInit() {
        return isInit;
    }

    public void initApp(Application app, Context context) {
        mApp = app;
        mContext = context.getApplicationContext();
        RongManager.getInstance().init(mContext);
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        //工具类
        initUtils();
        //初始化一些礼物配置
        initGiftOption();
        //极光推送
        initJPush();
        //友盟
        initUmeng();
        //网络请求
        initNet();
        //趣拍
        initQuPai();
        //toast
        initToast();
        //错误日志收集
        initCrash();
        //二维码
        initQRCode();
        //百度地图
        initBaiduMap();

        isInit = true;
    }

    /**
     * 百度地图
     */
    private void initBaiduMap() {
        SDKInitializer.initialize(mContext);
    }

    /**
     * 工具类
     */
    private void initUtils() {
        GearLog.LOG_DEBUG = BuildConfig.DEBUG;
        Logger.addLogAdapter(new AndroidLogAdapter());
        Utils.init(mApp);
        Logger.e("工具类初始化成功");
    }

    /**
     * 初始化礼物一些配置
     */
    private void initGiftOption() {
        SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ID, "");
        SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ITEM, 0);
    }

    /**
     * 初始化极光PUSH
     */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mContext);
        Logger.e("极光推送初始化成功");
    }

    /**
     * 友盟
     */
    private void initUmeng() {
        Config.DEBUG = true;
        UMShareAPI.get(mContext);
        //微信
        PlatformConfig.setWeixin("wx28461decfd4646bf", "39d784e166985366c1ed8944e9ba6572");
        //新浪微博
        PlatformConfig.setSinaWeibo("2132023618", "6d68d2824d874d78c8d9f370a4881e40", "http://sns.whalecloud.com");
        //QQ
        PlatformConfig.setQQZone("101366788", "89e06567e078215990a900fb2c244a09");
        Logger.e("友盟初始化成功");
    }

    /**
     * 网络请求
     */
    private void initNet() {
        OkGo.getInstance().init(mApp);
        ApiConnectionFactory.getInstance().initialize(mContext);
    }

    /**
     * 错误日志收集
     */
    private void initCrash() {
        CrashReport.initCrashReport(mContext, "93531e9178", true);
    }


    private void initToast() {
        ToastHelper.getInstance().init(mContext);
    }

    /**
     * 二维码
     */
    private void initQRCode() {
        ZXingLibrary.initDisplayOpinion(mContext);
    }


    /**
     * 趣拍
     */
    private void initQuPai() {
        for (String str : new String[]{"gnustl_shared", "qupai-media-thirdparty", "qupai-media-jni"}) {
            System.loadLibrary(str);
        }

        QupaiService qupaiService = QupaiManager.getQupaiService(mContext);
        if (qupaiService != null) {
            Intent moreMusic = new Intent();
            //是否需要更多音乐页面--如果不需要填空
            moreMusic.setClass(mContext, MoreMusicActivity.class);
            QPHelper qpHelper = new QPHelper();
            qupaiService.initRecord(qpHelper.createVideoSessionCreateInfo(), qpHelper.createProjectOptions(), new QPUISettings());
            qupaiService.hasMroeMusic(moreMusic);
            if (qupaiService != null) {
                qupaiService.addMusic(0, "Big Up", "assets://Qupai/music/Big Up");
                qupaiService.addMusic(1, "Merry Christmas", "assets://Qupai/music/Merry Christmas");
                qupaiService.addMusic(2, "Mistletoe", "assets://Qupai/music/Mistletoe");
                qupaiService.addMusic(3, "Flashback", "assets://Qupai/music/Flashback");
                qupaiService.addMusic(4, "Cool", "assets://Qupai/music/Cool");
                qupaiService.addMusic(5, "Taste the Feeling", "assets://Qupai/music/Taste the Feeling");
                qupaiService.addMusic(6, "Free Fall", "assets://Qupai/music/Free Fall");
                qupaiService.addMusic(7, "This Is What You Came For", "assets://Qupai/music/This Is What You Came For");
                qupaiService.addMusic(8, "Network (Chase and Status Remix)", "assets://Qupai/music/Network");
                qupaiService.addMusic(9, "Sexy Back", "assets://Qupai/music/Sexy Back");
                qupaiService.addMusic(10, "California Gurls", "assets://Qupai/music/California Gurls");
                qupaiService.addMusic(11, "Party Rock Anthem", "assets://Qupai/music/Party Rock Anthem");
                qupaiService.addMusic(12, "Solo Dance", "assets://Qupai/music/Solo Dance");
                qupaiService.addMusic(13, "Ascension 2014", "assets://Qupai/music/Ascension 2014");
                qupaiService.addMusic(14, "Wicked Wonderland", "assets://Qupai/music/Wicked Wonderland");
                qupaiService.addMusic(15, "Panama", "assets://Qupai/music/Panama");
                qupaiService.addMusic(16, "Trouble", "assets://Qupai/music/Trouble");
                qupaiService.addMusic(17, "Stop", "assets://Qupai/music/Stop");
                qupaiService.addMusic(18, "Shake It Off", "assets://Qupai/music/Shake It Off");
                qupaiService.addMusic(19, "Nevada", "assets://Qupai/music/Nevada");
            }

            Logger.e("趣拍初始化成功...");
        }
    }
}
