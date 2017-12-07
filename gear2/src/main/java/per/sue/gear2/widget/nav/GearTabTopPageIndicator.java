package per.sue.gear2.widget.nav;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by SUE on 2016/7/7 0007.
 */
public class GearTabTopPageIndicator extends GearTabPageIndicator {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private int selectBackgroundColor = -1;
    private int  borderRadius = 0;


    public GearTabTopPageIndicator(Context context) {
        super(context);
    }

    public GearTabTopPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDrawLines(Canvas canvas) {
        super.onDrawLines(canvas);
        if(selectBackgroundColor != -1){

            if (mViewPager == null) {
                return;
            }

            final int count = mViewPager.getAdapter().getCount();
            if (count == 0) {
                return;
            }
            final int paddingLeft = getPaddingLeft();
            final float pageWidth = (getWidth() - paddingLeft - getPaddingRight()) / (1f * count);
            final float left = paddingLeft + pageWidth * (mCurrentPage + mPositionOffset);
            final float right = left + pageWidth;
            final float top = getPaddingBottom()  ;
            final float bottom = getHeight() - getPaddingBottom();

             mPaint.setColor(selectBackgroundColor);
            RectF rectF=new RectF(left,top,right,bottom);

                borderRadius = getHeight() / 2;
                canvas.drawRoundRect(rectF, borderRadius , borderRadius , mPaint);

            if(mPositionOffset > 0 && mPositionOffset < 1){

            }else{
                if(mCurrentPage == count -1  ){
                    canvas.drawRect(left, top, right - getHeight(), bottom, mPaint);
                }else if(mCurrentPage == 0 ){
                    canvas.drawRect(left + getHeight(), top, right , bottom, mPaint);
                }
            }


        }

    }

    public void setSelectBackgroundColor(int selectBackgroundColor) {
        this.selectBackgroundColor = selectBackgroundColor;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }
}
