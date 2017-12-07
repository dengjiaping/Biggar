package per.sue.gear2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import per.sue.gear2.R;


public class CircleImageView extends ImageView {

	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;
	private int mBorderWidth = 10;
	private int mBorderColor = Color.parseColor("#f2f2f2");

	static {
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}

	public CircleImageView(Context paramContext) {
		super(paramContext);
	}

	public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleImageView);
		mBorderColor = a.getColor(R.styleable.CircleImageView_borderColor, mBorderColor);
		final int defalut = (int) (2 * paramContext.getResources().getDisplayMetrics().density + 0.5f);
		mBorderWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_borderWidth, defalut);
		a.recycle();
	}

	private boolean useDefaultStyle = true;

	public void setUseDefaultStyle(boolean useDefaultStyle) {
		this.useDefaultStyle = useDefaultStyle;
	}


	@Override
	protected void onDraw(Canvas paramCanvas) {
		if (useDefaultStyle) {
			super.onDraw(paramCanvas);
			return;
		}
		final Drawable localDrawable = getDrawable();
		if (localDrawable == null)
			return;
		if (localDrawable instanceof NinePatchDrawable) {
			return;
		}
		if (this.paint == null) {
			final Paint localPaint = new Paint();
			localPaint.setFilterBitmap(false);
			localPaint.setAntiAlias(true);
			localPaint.setXfermode(MASK_XFERMODE);
			this.paint = localPaint;
		}
		final int width = getWidth();
		final int height = getHeight();
		/** ����layer */
		int layer = paramCanvas.saveLayer(0.0F, 0.0F, width, height, null, 31);
		/** ����drawable�Ĵ�С */
		localDrawable.setBounds(0, 0, width, height);
		/** ��drawable�󶨵�bitmap(this.mask)���棨drawableֻ��ͨ��bitmap��ʾ��4�� */
		localDrawable.draw(paramCanvas);
		if ((this.mask == null) || (this.mask.isRecycled())) {
			this.mask = createOvalBitmap(width, height);
		}
		/** ��bitmap����canvas���� */
		paramCanvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
		/** ���������Ƶ�layer�� */
		paramCanvas.restoreToCount(layer);
		drawBorder(paramCanvas, width, height);
	}


	private void drawBorder(Canvas canvas, final int width, final int height) {
		if (mBorderWidth == 0) {
			return;
		}
		final Paint mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		/**
		 * ���x��view��ȵ�һ�� ���y��view�߶ȵ�һ�� �뾶r����Ϊ��view�Ŀ��-border��һ��
		 */
		canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);
		canvas = null;
	}

	/**
	 * ��ȡһ��bitmap��Ŀ������4����drawable;
	 * <p>
	 * �����bitmap����canvas������أ����������滭һ����Բ(��ʵҲ��һ��Բ����Ϊwidth=height)4�̶���ʾ����
	 *
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap createOvalBitmap(final int width, final int height) {
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		final int padding = (mBorderWidth - 3) > 0 ? mBorderWidth - 3 : 1;
		/**
		 * ������Բ�Ĵ�С(��Ϊ��Բ������߻��border��������غϵģ����ͼƬ����ߵ���ɫ����п�������ߵ�Ч������Ϊ���������Ӻã�
		 * �������padding px)
		 */
		RectF localRectF = new RectF(padding, padding, width - padding, height - padding);
		localCanvas.drawOval(localRectF, localPaint);
		return localBitmap;
	}
}
