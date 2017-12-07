package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;

/**
 * Created by 张炼 on 2016/9/6.
 * 协议
 */
public class XieYiActivity extends BiggarActivity {
    Context mContext;
    Handler mHandler;
    StringBuilder mStringBuilder;
    @BindView(R.id.text_xie_yi_content)
    TextView mText;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, XieYiActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_xieyi;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String str = mStringBuilder.toString();
                mText.setText(str);
            }
        };

        new MyThread().start();
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            try {
                InputStream is = getResources().openRawResource(R.raw.xieyi);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String buffer;
                mStringBuilder = new StringBuilder();
                while ((buffer = br.readLine()) != null) {
                    mStringBuilder.append(buffer).append("\n");
                }
                Message msg = new Message();
                mHandler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
