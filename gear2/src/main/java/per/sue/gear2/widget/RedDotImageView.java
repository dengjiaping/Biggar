package per.sue.gear2.widget;/*
* 描 述：
* 作 者：ld
* 时 间：2016/2/22
* 版 权：睿思达科技有限公司
*/

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import per.sue.gear2.R;


public class RedDotImageView extends FrameLayout {
    private ImageView mImageView;
    private ImageView mRedDot;

    private int mSrc;

    public RedDotImageView(Context context) {
        super(context);
        init();
    }

    public RedDotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RedDotImageView);
        mSrc = a.getInt(R.styleable.RedDotImageView_src, 0);
        init();
    }

    private void init() {
        mImageView = new ImageView(getContext());
        mImageView.setImageResource(mSrc);
        mRedDot = new ImageView(getContext());
        if (mSrc != 0) {
            mRedDot.setImageResource(R.drawable.gear_tip_dot_bg);
        }
        mRedDot.setVisibility(GONE);
        addView(mImageView);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        addView(mRedDot, params);
    }

    public void setImageResource(int img) {
        mImageView.setImageResource(img);
    }


    public void showRedDot(boolean isShow) {
        if (isShow) {
            mRedDot.setVisibility(VISIBLE);
        } else {
            mRedDot.setVisibility(GONE);
        }
    }


}
