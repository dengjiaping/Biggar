package cn.biggar.biggar.view;


/**
 * ViewPager底部显示页面状态的控件
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.biggar.biggar.R;
import cn.biggar.biggar.utils.FileUtils;


public class ViewPagerStateView extends LinearLayout
{
    Context mContext;
    View[]  mViews;
    int  mNum;
    View mView;

    public ViewPagerStateView(Context context)
    {
        super(context);
    }

    public ViewPagerStateView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mContext = context;

        Init();
    }



    private void Init()
    {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_view_pager_state_view, this);
        //   mChildView = new ImageView(mContext);
    }

    public void SetViewNum(int num , int position)
    {
        this.mNum = num;
        this.removeAllViews();
        this.setGravity(Gravity.CENTER);
        mViews = new View[mNum];
        for(int i=0;i<mNum;i++)
        {
            mViews[i] = new ImageView(mContext);
            if(i == position)
            {
                mViews[i].setBackgroundResource(R.mipmap.icon_banner_true);
            }
            else
            {
                mViews[i].setBackgroundResource(R.mipmap.icon_banner_false);
            }

            LayoutParams lp = new LayoutParams(FileUtils.dip2px(mContext, 8), FileUtils.dip2px(mContext, 8));
            lp.setMargins(FileUtils.dip2px(mContext, 5), 0, 0, 0);
            mViews[i].setLayoutParams(lp);
            this.addView(mViews[i]);
        }

    }


    public void setCurState(int p)
    {
        for(int i=0;i<mNum;i++)
        {
            if(i == p)
            {
                mViews[i].setBackgroundResource(R.mipmap.icon_banner_true);
            }
            else
            {
                mViews[i].setBackgroundResource(R.mipmap.icon_banner_false);
            }
        }
    }




}

