/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package per.sue.gear2.widget.nav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import per.sue.gear2.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class GearTabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            GearTabView tabView = (GearTabView)view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected, false);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final IcsLinearLayout mTabLayout;

    protected ViewPager mViewPager;
    private OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    protected int mScrollState;
    protected int mCurrentPage;
    protected float mPositionOffset;
    private TabViewStyle tabViewStyle = new TabViewStyle();

    private OnTabReselectedListener mTabReselectedListener;


    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    public GearTabPageIndicator(Context context) {
        this(context, null);
    }

    public GearTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new IcsLinearLayout(context, 0);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));

        DisplayMetrics dm = getResources().getDisplayMetrics();

        // scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        //tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();

        // get custom attrs
        a = context.obtainStyledAttributes(attrs, R.styleable.GearTabPageIndicator);

        indicatorColor = a.getColor(R.styleable.GearTabPageIndicator_gearIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.GearTabPageIndicator_gearUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.GearTabPageIndicator_gearDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearTabPaddingLeftRight, tabPadding);
        //tabBackgroundResId = a.getResourceId(R.styleable.GearTabPageIndicator_gearTabBackground, tabBackgroundResId);
        //shouldExpand = a.getBoolean(R.styleable.GearTabPageIndicator_gearShouldExpand, shouldExpand);
        // scrollOffset = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearScrollOffset, scrollOffset);
        // textAllCaps = a.getBoolean(R.styleable.GearTabPageIndicator_gearTextAllCaps, textAllCaps);
        tabTextSize = a.getDimensionPixelSize(R.styleable.GearTabPageIndicator_gearTextSize, tabTextSize);
        selectedTabTextColor = a.getColor(R.styleable.GearTabPageIndicator_gearsTextSelectColor, selectedTabTextColor);
        tabTextColor = a.getColor(R.styleable.GearTabPageIndicator_gearTextDefultColor, tabTextColor);
        //isSelectBold = a.getBoolean(R.styleable.GearTabPageIndicator_gearTextSelectBold, isSelectBold);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);


    }


    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    /**
     * 添加View
     * @param index
     * @param text
     * @param iconResId
     */
    private void addTab(int index, CharSequence text, int iconResId) {
        final GearTabView tabView = GearTabView.createTabView(getContext(), index, text, iconResId);
        tabView.mIndex = index;
        tabView.mMaxTabWidth = mMaxTabWidth;
        tabView.setFocusable(true);
        if(null != tabView.getTitleTextView()){
            TextView textView = tabView.getTitleTextView();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,tabTextSize);
            int[][] state = new int[][]{{ android.R.attr.state_selected,android.R.attr.state_selected}, {0, 1}};
            int[] colors = new int[]{selectedTabTextColor, tabTextColor };
            ColorStateList list = new ColorStateList(state,colors );
            textView.setTextColor(list);
        }

        tabView.setOnClickListener(mTabClickListener);
        tabView.setPadding(tabPadding,5,tabPadding,5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT, 1);
        layoutParams.gravity = Gravity.CENTER;
        mTabLayout.addView(tabView, layoutParams);
    }

    float lastPositionOffset;

    private boolean isShowLineIndicator = false;
    private boolean isUnderTabBottom =  true;
    private int tabCount;

    private int indicatorHeight = 4;
    private int underlineHeight = 1;
    private int dividerPadding = 12;
    private int tabPadding = 16;
    private int dividerWidth = 1;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF666666;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x00000000;
    private Paint rectPaint;
    private Paint dividerPaint;
    @Override
    protected void onDraw(Canvas canvas) {

        if(mViewPager == null)return;

        super.onDraw(canvas);
        onDrawLines(canvas);
    }

    protected void onDrawLines(Canvas canvas){

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();
        int underlineTop = 0;
        int underlineBotom = 0;
        int indicatorTop = 0;
        int indicatorBotom = 0;
        if (isShowLineIndicator) {
            if (isUnderTabBottom) {
                underlineTop = height - underlineHeight;
                underlineBotom = height;
                indicatorTop = height - indicatorHeight;
                indicatorBotom = height;
            } else {
                underlineTop = 0;
                underlineBotom = underlineHeight;
                indicatorTop = 0;
                indicatorBotom = indicatorHeight;
            }
        }
        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, underlineTop, mTabLayout.getWidth(), underlineBotom, rectPaint);

        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = mTabLayout.getChildAt(mCurrentPage);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (mPositionOffset > 0f && mCurrentPage < tabCount - 1) {

            View nextTab = mTabLayout.getChildAt(mCurrentPage + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (mPositionOffset * nextTabLeft + (1f - mPositionOffset) * lineLeft);
            lineRight = (mPositionOffset * nextTabRight + (1f - mPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, indicatorTop, lineRight, indicatorBotom, rectPaint);
        // draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = mTabLayout.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    public View getSelectTabView(){
        return mTabLayout.getChildAt(mCurrentPage);
    }

    public int getSelectPosition(){
        return mCurrentPage;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        lastPositionOffset = mPositionOffset;
        mPositionOffset = positionOffset;
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mPositionOffset = 0;
            lastPositionOffset = 0;
            invalidate();
        }
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        tabCount = adapter.getCount();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter)adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            //throw new IllegalStateException("ViewPager has not been bound.");
            return ;
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
        changeImageColorFilter(item);

    }

    private void changeImageColorFilter(int item){
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final boolean isSelected = (i == item);
            View view  =  mTabLayout.getChildAt(i);
            GearTabView tabView = (GearTabView)view;
            if(view instanceof GearTabView){
                if(null != tabView.getIconImageView()) {
                    if (isSelected) {
                        tabView.getIconImageView().setColorFilter(selectedTabTextColor);
                    } else {
                        tabView.getIconImageView().setColorFilter(tabTextColor);
                    }
                }
            }

        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }


    public void setShowUnderTabBottom(boolean showUnderTabBottom) {
        isShowLineIndicator = showUnderTabBottom;
    }
}
