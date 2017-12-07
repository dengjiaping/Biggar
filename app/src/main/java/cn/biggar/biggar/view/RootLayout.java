package cn.biggar.biggar.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/5/17.
 */

public class RootLayout extends LinearLayout {
    private final View mTitleBarView;
    private Context mContext;

    /**
     * 标题栏颜色
     */
    private int mTitleBarColor;

    /**
     * 标题文字
     */
    private String mTitleBarTitle;

    /**
     * 标题文字颜色
     */
    private int mTitleBarTitleColor;

    /**
     * 标题栏高度
     */
    private float mTitleBarHeight;

    /**
     * 标题栏左按钮
     */
    private int mTitleBarLeftIcon;

    /**
     * 标题栏左边第二个按钮
     */
    private int mTitleBarLeftSecondIcon;

    /**
     * 标题栏右按钮
     */
    private int mTitleBarRightIcon;

    /**
     * 标题栏右文本
     */
    private String mTitleBarRightText;

    /**
     * 标题栏右文本颜色
     */
    private int mTitleBarRightTextColor;

    /**
     * 是否显示左边第二个按钮
     */
    private boolean isShowLeftSecondIcon;

    /**
     * 是否显示右文本,默认不显示
     */
    private boolean isShowRightText;

    /**
     * 是否显示右图标,默认不显示
     */
    private boolean isShowRightIcon;

    /**
     * 是否默认关闭activity
     */
    private boolean isDefaultLeftFinish;

    /**
     * 是否显示分割线
     */
    private boolean isShowRootLine;


    private ImageView mIVLeft;
    private ImageView mIVLeftSecond;
    private TextView mTVTitle;
    private TextView mTVRight;
    private ImageView mIVRight;
    private View rootLine;


    public RootLayout(Context context) {
        this(context, null);
    }

    public RootLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RootLayout(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RootLayout);
        mTitleBarColor = t.getColor(R.styleable.RootLayout_titleBarColor, ContextCompat.getColor(context, R.color.whites));
        mTitleBarTitle = t.getString(R.styleable.RootLayout_titleBarTitle);
        mTitleBarTitleColor = t.getColor(R.styleable.RootLayout_titleBarTitleColor, ContextCompat.getColor(context, R.color.app_3));
        mTitleBarHeight = t.getDimension(R.styleable.RootLayout_titleBarHeight, getResources().getDimension(R.dimen.titleBarHeight));
        mTitleBarLeftIcon = t.getResourceId(R.styleable.RootLayout_titleBarLeftIcon, R.mipmap.arrow_333);
        mTitleBarLeftSecondIcon = t.getResourceId(R.styleable.RootLayout_titleBarLeftSecondIcon, R.mipmap.close_3x);
        mTitleBarRightIcon = t.getResourceId(R.styleable.RootLayout_titleBarRightIcon, 0);
        mTitleBarRightText = t.getString(R.styleable.RootLayout_titleBarRightText);
        mTitleBarRightTextColor = t.getColor(R.styleable.RootLayout_titleBarRightTextColor, ContextCompat.getColor(context, R.color.app_3));
        isShowLeftSecondIcon = t.getBoolean(R.styleable.RootLayout_isShowLeftSecondIcon, false);
        isShowRightText = t.getBoolean(R.styleable.RootLayout_isShowRightText, false);
        isShowRightIcon = t.getBoolean(R.styleable.RootLayout_isShowRightIcon, false);
        isDefaultLeftFinish = t.getBoolean(R.styleable.RootLayout_isDefaultLeftFinish, true);
        isShowRootLine = t.getBoolean(R.styleable.RootLayout_isShowRootLine, true);
        t.recycle();

        setFitsSystemWindows(true);
        setClipToPadding(true);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);

        ViewGroup.LayoutParams lpTitle = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) mTitleBarHeight);
        mTitleBarView = LayoutInflater.from(context).inflate(R.layout.base_titlebar, null, false);

        mTitleBarView.setBackgroundColor(mTitleBarColor);
        mIVLeft = (ImageView) mTitleBarView.findViewById(R.id.iv_left);
        mIVLeftSecond = (ImageView) mTitleBarView.findViewById(R.id.iv_left_second);
        mTVTitle = (TextView) mTitleBarView.findViewById(R.id.tv_title);
        mTVRight = (TextView) mTitleBarView.findViewById(R.id.tv_right);
        mIVRight = (ImageView) mTitleBarView.findViewById(R.id.iv_right);
        rootLine = mTitleBarView.findViewById(R.id.root_line);

        rootLine.setVisibility(isShowRootLine ? VISIBLE : GONE);

        mIVLeft.setImageResource(mTitleBarLeftIcon);

        mIVLeftSecond.setVisibility(isShowLeftSecondIcon ? VISIBLE : GONE);
        mIVLeftSecond.setImageResource(mTitleBarLeftSecondIcon);

        mTVTitle.setTextColor(mTitleBarTitleColor);
        mTVTitle.setMaxWidth((int) (Utils.getScreenWidth(context) * 0.65));
        if (!TextUtils.isEmpty(mTitleBarTitle)) {
            mTVTitle.setText(mTitleBarTitle);
        }
        mTVTitle.setSelected(true);

        mIVRight.setVisibility(isShowRightIcon ? VISIBLE : GONE);
        if (isShowRightIcon) {
            mIVRight.setImageResource(mTitleBarRightIcon);
        }

        mTVRight.setVisibility(isShowRightText ? VISIBLE : GONE);
        if (isShowRightText && !TextUtils.isEmpty(mTitleBarRightText)) {
            mTVRight.setText(mTitleBarRightText);
            mTVRight.setTextColor(mTitleBarRightTextColor);
        }

        addView(mTitleBarView, lpTitle);

        if (isDefaultLeftFinish) {
            mIVLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    ((Activity) context).finish();
                }
            });
        }
    }

    public View getTitleBarView() {
        return mTitleBarView;
    }

    public RootLayout setTitleMaxWidth(int maxWidth) {
        mTVTitle.setMaxWidth(maxWidth);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public RootLayout setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTVTitle.setVisibility(VISIBLE);
            mTVTitle.setText(title);
        }
        return this;
    }

    public RootLayout setRightTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTVRight.setVisibility(VISIBLE);
            mTVRight.setText(title);
        }
        return this;
    }

    public RootLayout setRightIcon(int rightIcon) {
        if (rightIcon > 0) {
            mIVRight.setVisibility(VISIBLE);
            mIVRight.setImageResource(rightIcon);
        }
        return this;
    }

    public RootLayout setOnLeftClick(OnClickListener l) {
        if (!isDefaultLeftFinish && mIVLeft.getVisibility() == VISIBLE) {
            mIVLeft.setOnClickListener(l);
        }
        return this;
    }

    public RootLayout setOnLeftSecondClick(OnClickListener l) {
        if (mIVLeftSecond.getVisibility() == VISIBLE) {
            mIVLeftSecond.setOnClickListener(l);
        }
        return this;
    }

    public RootLayout setOnRightClick(OnClickListener l) {
        if (mIVRight.getVisibility() == VISIBLE) {
            mIVRight.setOnClickListener(l);
            return this;
        }

        if (mTVRight.getVisibility() == VISIBLE) {
            mTVRight.setOnClickListener(l);
            return this;
        }

        return this;
    }

    public RootLayout hideKeyboard() {
        View view = ((Activity) mContext).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return this;
    }


    public static RootLayout getInstance(Activity context) {
        return (RootLayout) ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }
}
