package per.sue.gear2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import per.sue.gear2.R;


/**
 * Created by mx on 2016/8/25.
 * 半圆角 imageview
 */
@SuppressWarnings("UnusedDeclaration")
public class HalfRoundedImageView extends ImageView {
    private Paint paint;

    public HalfRoundedImageView(Context context) {
        this(context, null);
    }

    public HalfRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        getAttrs(context,attrs);
    }

    private int radius;
    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getRoundBitmap(bitmap);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取半圆角矩形图片方法
     * @param bitmap
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, radius, radius, paint);
        canvas.drawRect(0f,bitmap.getHeight()-radius, bitmap.getWidth(), bitmap.getHeight(),paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void getAttrs(Context context,AttributeSet attrs){
        if(attrs==null)return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
        radius = ta.getInteger(R.styleable.RoundedImageView_radius_, 14);
        ta.recycle();
    }
}