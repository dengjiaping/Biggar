package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;

/**
 * Created by zl on 2016/11/23.
 * 关于我们
 */

public class AboutActivity extends BiggarActivity{
    public static void statrIntent(Context context){
        Intent intent = new Intent(context,AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {

    }
}
