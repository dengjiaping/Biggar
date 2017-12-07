package cn.biggar.biggar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.biggar.biggar.dialog.TipsDialog;

/**
 * Created by Chenwy on 2017/10/18.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TipsDialog.newInstance("启动成功").show(getSupportFragmentManager());
    }
}
