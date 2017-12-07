package cn.biggar.biggar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.bean.JPushBean;
import cn.biggar.biggar.presenter.JPushPersenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import per.sue.gear2.presenter.OnObjectListener;

/**
 * Created by mx on 2016/9/8.
 * 极光推送
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush_MyReceiver";
    private static String c_id;
    Context mContext;
    JPushPersenter pushPersenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            printBundle(bundle);
        }
        Log.i(TAG, "[MyReceiver] onReceive -" + intent.getAction() + ",extras:" + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.EXTRA_EXTRA.equals(intent.getAction())) {
            String regMessage = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] 接收Registration Id Message: " + regMessage);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int regNotifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + regNotifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //打开自定义的Activity
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            pushPersenter = new JPushPersenter(mContext);
            pushPersenter.getPushMsg(c_id, new OnObjectListener<JPushBean>() {
                @Override
                public void onSuccess(JPushBean result) {
                    super.onSuccess(result);

                    skip(result);

                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                }
            });
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            String regExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + regExtra);
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " 连接状态变化 " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] 未处理 - " + intent.getAction());
        }
    }

    /**
     * 跳转
     *
     * @param bean
     */
    private void skip(JPushBean bean) {
        if (bean != null) {
            int mTag = bean.c_tag;
                if (mTag==1){
                    Intent intent = ContentDetailActivity.startIntent(mContext, bean.c_tagvalue, "0");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }else if (mTag ==3){
                    MyHomeActivity.startIntent(mContext, bean.c_tagvalue, 1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }else if (mTag == 4){
                    Intent intent=ContentDetailActivity.startIntent(mContext, bean.c_tagvalue, "1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }else {
                    Intent intent = WebViewActivity.getIntent(mContext, bean.c_weburl+bean.c_tagvalue);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {

            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This MessageImpl has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();

                        c_id = json.optString(myKey);

                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get MessageImpl extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }

        return sb.toString();
    }

}
