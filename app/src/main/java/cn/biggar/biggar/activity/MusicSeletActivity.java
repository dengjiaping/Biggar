package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.fragment.MusicListFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by SUE on 2016/6/24 0024.
 */
public class MusicSeletActivity extends BiggarActivity {

    @BindView(R.id.bar_title_tv) TextView barTitleTv;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, MusicSeletActivity.class);
        return intent;
    }

    public static Intent startIntent(Context context, String selection) {
        Intent intent = new Intent(context, MusicSeletActivity.class);
        intent.putExtra("selection", selection);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_music_select;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        barTitleTv.setText("选择音乐");
        String selection = getIntent().getStringExtra("selection");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fl, MusicListFragment.getInstance(selection))
                .commit();
    }


    @OnClick(R.id.bar_back_view)
    public void onClick() {
        finish();
    }


}
