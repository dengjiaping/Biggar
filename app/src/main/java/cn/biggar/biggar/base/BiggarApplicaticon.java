package cn.biggar.biggar.base;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import java.util.List;
import cn.biggar.biggar.helper.InitHelper;


public class BiggarApplicaticon extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        if (getProcessName() != null && getProcessName().equals(getPackageName())) {
            InitHelper.getInstance().initApp(this, this);
        }
    }

    private String getProcessName() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == android.os.Process.myPid()) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
