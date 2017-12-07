package cn.biggar.biggar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.biggar.biggar.R;

/**
 * https://github.com/Kennyc1012/MultiStateView
 * Created by mx on 2016/2/22.
 */
public class MultiStateView extends FrameLayout implements View.OnClickListener{

    public static final int VIEW_STATE_CONTENT = 0;

    public static final int VIEW_STATE_ERROR = 1;

    public static final int VIEW_STATE_EMPTY = 2;

    public static final int VIEW_STATE_LOADING = 3;



    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_CONTENT, VIEW_STATE_ERROR, VIEW_STATE_EMPTY, VIEW_STATE_LOADING})
    public @interface ViewState {
    }

    private LayoutInflater mInflater;

    private View mContentView;

    private View mLoadingView;

    private View mErrorView;

    private View mEmptyView;

    private int mErrorViewLayoutId;
    private int mEmptyViewLayoutId;

    @ViewState
    private int mViewState = VIEW_STATE_CONTENT;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView);

        int loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }

        int emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
        if (emptyViewResId > -1) {
            mEmptyViewLayoutId=emptyViewResId;
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }

        int errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
        if (errorViewResId > -1) {
            mErrorViewLayoutId=errorViewResId;
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
        }

        int viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, VIEW_STATE_CONTENT);

        switch (viewState) {
            case VIEW_STATE_CONTENT:
                mViewState = VIEW_STATE_CONTENT;
                break;

            case VIEW_STATE_ERROR:
                mViewState = VIEW_STATE_ERROR;
                break;

            case VIEW_STATE_EMPTY:
                mViewState = VIEW_STATE_EMPTY;
                break;

            case VIEW_STATE_LOADING:
                mViewState = VIEW_STATE_LOADING;
                break;
        }

        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView();
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }


    @Nullable
    public View getView(@ViewState int state) {
        switch (state) {
            case VIEW_STATE_LOADING:
                return mLoadingView;

            case VIEW_STATE_CONTENT:
                return mContentView;

            case VIEW_STATE_EMPTY:
                return mEmptyView;

            case VIEW_STATE_ERROR:
                return mErrorView;

            default:
                return null;
        }
    }

    @ViewState
    public int getViewState() {
        return mViewState;
    }


    public void setViewState(@ViewState int state) {
        if (state != mViewState) {
            mViewState = state;
            setView();
        }
    }


    private void setView() {
        switch (mViewState) {
            case VIEW_STATE_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }

                mLoadingView.setVisibility(View.VISIBLE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }

                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error View");
                }

                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;

            case VIEW_STATE_CONTENT:
            default:
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw new NullPointerException("Content View");
                }

                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Checks if the given {@link View} is valid for the Content View
     *
     * @param view The {@link View} to check
     * @return
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }


    public void setViewForState(View view, @ViewState int state, boolean switchToState) {
        switch (state) {
            case VIEW_STATE_LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case VIEW_STATE_EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;

            case VIEW_STATE_ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;

            case VIEW_STATE_CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if (switchToState) setViewState(state);
    }


    public void setViewForState(View view, @ViewState int state) {
        setViewForState(view, state, false);
    }


    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }


    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
        setViewForState(layoutRes, state, false);
    }


    /**
     * 进一步 封装   方便代码量      要求 所有 view  重试按钮 ID = retry
     * @param listener
     */
    public  void setOnMultiStateViewClickListener(OnMultiStateViewClickListener listener){
        if(mErrorView!=null){
            View v=  mErrorView.findViewById(R.id.retry);
            if(v!=null) {
                v.setTag(MultiStateView.VIEW_STATE_ERROR);
                v.setOnClickListener(this);
            }
        }
        if(mEmptyView!=null){
            View v=  mEmptyView.findViewById(R.id.retry);
            if(v!=null) {
                v.setTag(MultiStateView.VIEW_STATE_EMPTY);
                v.setOnClickListener(this);
            }
        }
        this.listener=listener;
    }
    private  OnMultiStateViewClickListener listener;
    public interface OnMultiStateViewClickListener{
        void onRetry(int Type);  //类型
    }
    private Handler handler=new Handler();
    @Override
    public void onClick(final View v) {
        if(listener!=null) {
            setViewState(MultiStateView.VIEW_STATE_LOADING);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (v.getTag()!=null&&(int)v.getTag()==MultiStateView.VIEW_STATE_EMPTY) {
                        listener.onRetry(MultiStateView.VIEW_STATE_EMPTY);
                    } else if (v.getTag()!=null&&(int)v.getTag()==MultiStateView.VIEW_STATE_ERROR){
                        listener.onRetry(MultiStateView.VIEW_STATE_ERROR);
                    }
                }
            },200);
        }
    }


    /**
     * 设置 提示文字  根据 状态
     * @param state
     * @param hint
     */
    public void setHintTextByState(int state,String hint){

        if(hint==null)return;
        TextView textView=null;
        switch (state){
            case VIEW_STATE_ERROR:
                if(mErrorView!=null&&(mErrorViewLayoutId==R.layout.layout_error_view||mErrorViewLayoutId==R.layout.layout_error_view_1)){
                    textView= (TextView) mErrorView.findViewById(R.id.tv_error_info);
                }
                break;
            case VIEW_STATE_EMPTY:
                if(mEmptyView!=null&&(mEmptyViewLayoutId==R.layout.layout_empty_view||mErrorViewLayoutId==R.layout.layout_empty_view_1)){
                    textView= (TextView) mEmptyView.findViewById(R.id.tv_empty_info);
                }
                break;
        }
        if(textView!=null){
            textView.setText(hint);
        }
    }

    /**
     * 设置 提示文字 color 根据 状态
     * @param state
     */
    public void setHintTextColorByState(int state,int color){
        TextView textView1=null;
        TextView textView2=null;
        switch (state){
            case VIEW_STATE_ERROR:
                if(mErrorView!=null&&(mErrorViewLayoutId==R.layout.layout_error_view||mErrorViewLayoutId==R.layout.layout_error_view_1)){
                    textView1= (TextView) mErrorView.findViewById(R.id.tv_error_info);
                    textView2= (TextView) mErrorView.findViewById(R.id.retry);
                }
                break;
            case VIEW_STATE_EMPTY:
                if(mEmptyView!=null&&(mEmptyViewLayoutId==R.layout.layout_empty_view||mErrorViewLayoutId==R.layout.layout_empty_view_1)){
                    textView1= (TextView) mEmptyView.findViewById(R.id.tv_empty_info);
                    textView2= (TextView) mErrorView.findViewById(R.id.retry);
                }
                break;
        }
        if(textView1!=null){
            textView1.setTextColor(getContext().getResources().getColor(color));
        }
        if(textView2!=null){
            textView2.setTextColor(getContext().getResources().getColor(color));
        }
    }

    public void setHintTextColorByState(int state,int color,int image,String hintText){
        TextView textView1=null;
        TextView textView2=null;
        ImageView imageView=null;
        switch (state){
            case VIEW_STATE_ERROR:
                if(mErrorView!=null&&(mErrorViewLayoutId==R.layout.layout_error_view||mErrorViewLayoutId==R.layout.layout_error_view_1)){
                    textView1= (TextView) mErrorView.findViewById(R.id.tv_error_info);
                    textView2= (TextView) mErrorView.findViewById(R.id.retry);
                    imageView= (ImageView) mErrorView.findViewById(R.id.iv_error_img);
                }
                break;
            case VIEW_STATE_EMPTY:
                if(mEmptyView!=null&&(mEmptyViewLayoutId==R.layout.layout_empty_view||mErrorViewLayoutId==R.layout.layout_empty_view_1)){
                    textView1= (TextView) mEmptyView.findViewById(R.id.tv_empty_info);
                    textView2= (TextView) mErrorView.findViewById(R.id.retry);
                    imageView= (ImageView) mErrorView.findViewById(R.id.iv_empty_img);
                }
                break;
        }
        if (imageView!=null){
//            imageView.setVisibility(View.GONE);
            imageView.setImageDrawable(getContext().getResources().getDrawable(image));
        }
        if(textView1!=null){
            textView1.setTextColor(getContext().getResources().getColor(color));
            textView1.setText(hintText);
        }
        if(textView2!=null){
            textView2.setVisibility(View.GONE);
        }
    }
    /**
     * 隐藏 错误view 或 空数据view 的 icon
     * @param state
     */
    public void setHideIconByState(int state){
        ImageView imageView=null;
        switch (state){
            case VIEW_STATE_ERROR:
                if(mErrorView!=null&&(mErrorViewLayoutId==R.layout.layout_error_view||mErrorViewLayoutId==R.layout.layout_error_view_1)){
                    imageView= (ImageView) mErrorView.findViewById(R.id.iv_error_img);
                }
                break;
            case VIEW_STATE_EMPTY:
                if(mEmptyView!=null&&(mEmptyViewLayoutId==R.layout.layout_empty_view||mErrorViewLayoutId==R.layout.layout_empty_view_1)){
                    imageView= (ImageView) mEmptyView.findViewById(R.id.iv_empty_img);
                }
                break;
        }
        if (imageView!=null){
            imageView.setVisibility(View.GONE);
        }
    }


}