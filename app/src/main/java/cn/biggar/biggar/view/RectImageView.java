package cn.biggar.biggar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import cn.biggar.biggar.view.roundedimageview.RoundedImageView2;

/**
 * Created by Chenwy on 2017/4/21.
 */

public class RectImageView extends RoundedImageView2 {
    public RectImageView(Context context) {
        this(context, null);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
