package per.sue.gear2.widget.flow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.support.v4.view.ViewPager;

public class ViewFlowFixViewPager extends ViewFlow {
	
	 private ViewPager mPager;

	public ViewFlowFixViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ViewFlowFixViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	  public void setViewPager(ViewPager mViewPager) {
	        mPager = mViewPager;
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent ev) {
	        if (mPager != null)
	            switch (ev.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    mPager.requestDisallowInterceptTouchEvent(true);
	                    break;
	                case MotionEvent.ACTION_UP:
	                    mPager.requestDisallowInterceptTouchEvent(false);
	                    break;
	                case MotionEvent.ACTION_CANCEL:
	                    mPager.requestDisallowInterceptTouchEvent(false);
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    mPager.requestDisallowInterceptTouchEvent(true);
	                    break;
	            }
	        return super.onInterceptTouchEvent(ev);
	    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
		return super.dispatchTouchEvent(ev);
	}



}
