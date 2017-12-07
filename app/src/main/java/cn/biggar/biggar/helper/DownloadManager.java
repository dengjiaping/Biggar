package cn.biggar.biggar.helper;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.orhanobut.logger.Logger;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.ApkInfoBean;
import cn.biggar.biggar.fragment.UpdateDialogFragment;
import rx.Subscriber;

/**
 * Created by langgu on 16/5/19.
 */
public class DownloadManager {

    private static DownloadManager ourInstance = new DownloadManager();
    private boolean isRuning;

    private DownloadManager() {
    }

    public static synchronized DownloadManager getInstance() {
        return ourInstance;
    }


    public void checkUpdate(final FragmentActivity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkAPI(context);
            }
        });
    }


    private synchronized void checkAPI(final FragmentActivity context) {

        if (isRuning) return;

        isRuning = true;
        DataApiFactory.getInstance().createICommonAPI(context.getApplication()).checkApkVersion().subscribe(new Subscriber<ApkInfoBean>() {
            @Override
            public void onCompleted() {
                isRuning = false;
            }

            @Override
            public void onError(Throwable e) {
                isRuning = false;
            }

            @Override
            public void onNext(ApkInfoBean apkInfoBean) {
                isRuning = false;

                int versionCode = AppUtils.getAppVersionCode();
                Logger.e("versionCode - " + versionCode + "\napkInfoBean.getVersionCode() - " + apkInfoBean.getVersionCode());
                if (apkInfoBean.getVersionCode() > versionCode) {
                    UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(apkInfoBean);
                    updateDialogFragment.setCancelable(apkInfoBean.isMust().equals("0"));
                    updateDialogFragment.show(context.getSupportFragmentManager(), null);
                }

                //缓存的版本号
                int cacheVersionCode = SPUtils.getInstance().getInt(Constants.VERSION_CODE, 0);
                //当前版本号
                int curVersionCode = AppUtils.getAppVersionCode();

                //如果缓存的版本号为0，则表示app是全新安装的，需要显示礼物的引导
                if (cacheVersionCode == 0) {
                    Logger.e("全新安装的...");
                    SPUtils.getInstance().put(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE, "1");
                    SPUtils.getInstance().put(Constants.VERSION_CODE, curVersionCode);
                    return;
                }

                //如果当前版本号 > 缓存的版本号，则表示版本更新了，要判断后台配置是否要显示引导
                if (curVersionCode > cacheVersionCode) {
                    String isShouldShowGuide = SPUtils.getInstance().getString(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE);
                    //如果是否引导表示为空，则表示从来没有进入过引导，或者引导步骤没有走完，要显示引导
                    if (TextUtils.isEmpty(isShouldShowGuide)){
                        Logger.e("没有进过引导或者引导并没有完成...");
                        SPUtils.getInstance().put(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE, "1");
                    }
                    //判断后台配置是否要显示引导
                    else {
                        Logger.e("判断后台配置是否要显示引导...");
                        SPUtils.getInstance().put(Constants.IS_SHOULD_SHOW_REDPACKET_GUIDE, apkInfoBean.isLoad);
                    }
                    SPUtils.getInstance().put(Constants.VERSION_CODE, curVersionCode);
                }
            }
        });
    }
}
