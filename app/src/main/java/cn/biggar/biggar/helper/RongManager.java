package cn.biggar.biggar.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.biggar.biggar.event.ReceiverMsgEvent;
import cn.biggar.biggar.utils.MyExtensionModule;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.push.RongPushClient;

/**
 * Created by Chenwy on 2017/10/10.
 */

public class RongManager {
    private Context mContext;
    private Vibrator vibrator;

    public RongManager() {
    }

    private static class RongManagerHolder {
        private static final RongManager RONG_MANAGER = new RongManager();
    }

    public static RongManager getInstance() {
        return RongManagerHolder.RONG_MANAGER;
    }

    public void init(Context context) {
        mContext = context;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        RongPushClient.registerMiPush(mContext, "2882303761517553762", "5821755338762");
        RongPushClient.registerHWPush(mContext);
        RongIM.init(mContext);
        openReceiveMessageListener();
        setRongPlugin();
        Logger.e("融云初始化成功");
    }

    private void openReceiveMessageListener() {
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                //震动
                long[] pattern = new long[]{0, 300, 200, 300};
                vibrator.vibrate(pattern, -1);
                EventBus.getDefault().post(new ReceiverMsgEvent());
                return true;
            }
        });
    }

    private void setRongPlugin() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }

    public void startPrivateChat(String targetUserId, String title, String isNotFromNotification) {
        if (!TextUtils.isEmpty(targetUserId)) {
            if (RongContext.getInstance() == null) {
                throw new ExceptionInInitializerError("RongCloud SDK not init");
            } else {
                Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                        .appendQueryParameter("targetId", targetUserId)
                        .appendQueryParameter("title", title)
                        .appendQueryParameter("isNotFromNotification", isNotFromNotification)
                        .build();
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void startConversationList(Map<String, Boolean> supportedConversation, String isNotFromNotification) {
        if (mContext == null) {
            throw new IllegalArgumentException();
        } else if (RongContext.getInstance() == null) {
            throw new ExceptionInInitializerError("RongCloud SDK not init");
        } else {
            Uri.Builder builder = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendQueryParameter("isNotFromNotification", isNotFromNotification).appendPath("conversationlist");
            if (supportedConversation != null && supportedConversation.size() > 0) {
                Set keys = supportedConversation.keySet();
                Iterator var5 = keys.iterator();

                while (var5.hasNext()) {
                    String key = (String) var5.next();
                    builder.appendQueryParameter(key, ((Boolean) supportedConversation.get(key)).booleanValue() ? "true" : "false");
                }
            }

            Intent intent = new Intent("android.intent.action.VIEW", builder.build());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}
