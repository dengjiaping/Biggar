package per.sue.gear2.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import per.sue.gear2.R;

public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private OnScrollListener mOnScrollListener;
    private boolean isShowProgressBar;
    private OnWebChromeClientListener onWebChromeClientListener;

    @SuppressWarnings("deprecation")
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                dp2px(0), 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.gear_drawable_progress_bar);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        // support zoom
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(null != onWebChromeClientListener){
                onWebChromeClientListener.onReceivedTitle(view, title);
            }
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(isShowProgressBar){
                if (newProgress == 100 ) {
                    progressbar.setProgress(newProgress);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            progressbar.setVisibility(GONE);
                        }
                    }, 500);

                } else {
                    if (progressbar.getVisibility() == GONE) {
                        progressbar.setVisibility(VISIBLE);
                    }
                    progressbar.setProgress(newProgress);
                }
            }else{
                if (progressbar.getVisibility() == VISIBLE)
                progressbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        if(isShowProgressBar){
            LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressbar.setLayoutParams(lp);
        }

        super.onScrollChanged(l, t, oldl, oldt);
        if(null != mOnScrollListener){
            mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    private int dp2px(int valuePx) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valuePx, getContext().getResources().getDisplayMetrics());
    }



    public boolean isShowProgressBar() {
        return isShowProgressBar;
    }

    public void setIsShowProgressBar(boolean isShowProgressBar) {
        this.isShowProgressBar = isShowProgressBar;
    }

    public interface OnScrollListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt) ;
    }

    public interface  OnWebChromeClientListener{
         void onReceivedTitle(View view, String title);
    }

    public void setOnWebChromeClientListener(OnWebChromeClientListener onWebChromeClientListener) {
        this.onWebChromeClientListener = onWebChromeClientListener;
    }
}