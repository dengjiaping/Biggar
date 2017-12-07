package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.event.MusicSelectEvent;
import cn.biggar.biggar.fragment.MusicAlbumListFragment;
import cn.biggar.biggar.fragment.MusicFolderFragment;
import cn.biggar.biggar.fragment.MusicListFragment;
import cn.biggar.biggar.fragment.MusicSongerListFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

import per.sue.gear2.TestFragment;
import per.sue.gear2.widget.PagerSlidingTabStrip;


/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MoreMusicActivity extends BiggarActivity {

    private static final String[] CONTENT = new String[] { "歌曲", "歌手", "专辑", "文件夹" };



    @BindView(R.id.bar_title_tv) TextView barTitleTv;
    @BindView(R.id.pagerSlidingTab) PagerSlidingTabStrip tabpageIndicator;
    @BindView(R.id.viewPager) ViewPager viewPager;



    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, MoreMusicActivity.class);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_music;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        barTitleTv.setText("选择音乐");
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabpageIndicator.setViewPager(viewPager);
    }



    @Subscribe
    public void onEventMainThread(MusicSelectEvent event) {
        finish();
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @OnClick(R.id.bar_back_view)
    public void onClick() {
        finish();
    }



    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            fragment =  TestFragment.newInstance(CONTENT[position % CONTENT.length]);

            switch(position){
                case 0:fragment = MusicListFragment.getInstance();
                    break;
                case 1:fragment = MusicSongerListFragment.getInstance();
                    break;
                case 2:fragment = MusicAlbumListFragment.getInstance();
                    break;
                case 3:fragment = MusicFolderFragment.getInstance();
                    break;


            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}



