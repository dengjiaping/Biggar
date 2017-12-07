package cn.biggar.biggar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.preference.AppPrefrences;
import java.util.ArrayList;



/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/6
*/
public class GuideActivity extends BiggarActivity {
    private ViewPager mViewPage;
    private ImageView mPlayNow;
    private ArrayList<ImageView> mList;

    private int[] mImages={
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3,
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        initView();
        initEvents();
    }

    @Override
    protected void initImmersionBar() {
    }

    private void initView() {
        mPlayNow= (ImageView) findViewById(R.id.play_now_iv);
        mViewPage = (ViewPager) findViewById(R.id.view_pager);
        mViewPageAdapter adapter = new mViewPageAdapter();
        initData();
        mViewPage.setAdapter(adapter);
    }

    private void initData() {
        mList=new ArrayList<>();
        for(int i=0;i<mImages.length;i++){
            ImageView imageView=new ImageView(this);
            mList.add(imageView);
        }
        mPlayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.startIntent(getActivity()));
                AppPrefrences.getInstance(getApplication()).setIsFirst(false);
                finish();

            }
        });
    }
    private void initEvents(){
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPlayNow.setVisibility(position==mImages.length-1?View.VISIBLE: View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean isCanSwipeBack() {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mList){
            for(View view : mList){
                view.setBackgroundResource(0);
            }
        }
    }

    private class mViewPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view=mList.get(position);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setBackgroundResource(mImages[position]);
            view.setLayoutParams(params);
            container.addView(mList.get(position));
            return mList.get(position);
        }
    }


}
