package cn.biggar.biggar.fragment.fragmentnavigator;

import android.support.v4.app.Fragment;

/**
 * Created by Chenwy.
 */
public interface FragmentNavigatorAdapter {

    public Fragment onCreateFragment(int position);

    public String getTag(int position);

    public int getCount();
}
