package cn.biggar.biggar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Chenwy on 2017/11/27.
 * 取消内边距的TextView
 */

public class NoneFontPaddingTextView extends TextView {
    private boolean adjustTopForAscent = true;

    public NoneFontPaddingTextView(Context context) {
        super(context);
    }

    public NoneFontPaddingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoneFontPaddingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint.FontMetricsInt fontMetricsInt;

    @Override
    protected void onDraw(Canvas canvas) {
        if (adjustTopForAscent) {//设置是否remove间距，true为remove
            if (fontMetricsInt == null) {
                fontMetricsInt = new Paint.FontMetricsInt();
                getPaint().getFontMetricsInt(fontMetricsInt);
            }
            canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        }
        super.onDraw(canvas);
    }
}
