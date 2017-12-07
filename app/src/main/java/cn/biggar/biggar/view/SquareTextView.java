package cn.biggar.biggar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mx on 2017/1/4.
 * 正方形的 textview
 */

public class SquareTextView extends TextView {

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = 0;

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = width;
        } else {
            size = height;
        }
        setMeasuredDimension(size, size);
    }
}
