package cn.biggar.biggar.adapter;

import android.support.v4.app.Fragment;

import cn.biggar.biggar.fragment.fragmentnavigator.FragmentNavigatorAdapter;
import cn.biggar.biggar.fragment.update.DiscoverFragment;
import cn.biggar.biggar.fragment.update.MapFragment;
import cn.biggar.biggar.fragment.update.MeFragment;

/**
 * Created by Chenwy on 2017/5/5.
 */

public class MainFragmentAdapter implements FragmentNavigatorAdapter {
    @Override
    public Fragment onCreateFragment(int i) {
        if (i == 0) {
            return MapFragment.getInstance();
        }
        if (i == 1) {
            return DiscoverFragment.getInstance();
        }
        return MeFragment.getInstance();
    }

    @Override
    public String getTag(int i) {
        if (i == 0) {
            return MapFragment.TAG;
        }
        if (i == 1) {
            return DiscoverFragment.TAG;
        }
        return MeFragment.TAG;

    }

    @Override
    public int getCount() {
        return 3;
    }
}
