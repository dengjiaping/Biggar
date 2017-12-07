package cn.biggar.biggar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fj.mtsortbutton.lib.Interface.IDynamicSore;
import fj.mtsortbutton.lib.adapter.ViewPagerAdapter;

/**
 * Created by Chenwy on 2017/9/30.
 */

public class DynamicSoreView<T> extends LinearLayout {
    Context mContext;
    private ViewPager viewPager;
    private LinearLayout llIndicator;
    private int RadioSelect;
    private int RadioUnselected;
    private int distance;
    private int number;
    private Integer gridView;
    private int page;
    private List<T> dataList;
    List<View> listSoreView = new ArrayList();
    View soreView;
    private IDynamicSore iDynamicSore;

    public IDynamicSore getiDynamicSore() {
        return this.iDynamicSore;
    }

    public void setiDynamicSore(IDynamicSore iDynamicSore) {
        this.iDynamicSore = iDynamicSore;
    }

    public DynamicSoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(fj.mtsortbutton.lib.R.layout.anfq_sore_button, this, true);
        this.viewPager = (ViewPager) this.findViewById(fj.mtsortbutton.lib.R.id.viewPager);
        this.llIndicator = (LinearLayout) this.findViewById(fj.mtsortbutton.lib.R.id.llIndicator);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, fj.mtsortbutton.lib.R.styleable.DynamicSoreView);
        if (typedArray != null) {
            this.RadioSelect = typedArray.getResourceId(fj.mtsortbutton.lib.R.styleable.DynamicSoreView_SoreRadioSelect, fj.mtsortbutton.lib.R.drawable.radio_select);
            this.RadioUnselected = typedArray.getResourceId(fj.mtsortbutton.lib.R.styleable.DynamicSoreView_SoreRadioUnselected, fj.mtsortbutton.lib.R.drawable.radio_unselected);
            this.distance = typedArray.getInteger(fj.mtsortbutton.lib.R.styleable.DynamicSoreView_SoreDistance, 10);
            this.number = typedArray.getInteger(fj.mtsortbutton.lib.R.styleable.DynamicSoreView_SoreNumber, 8);
            typedArray.recycle();
        }

        this.gridView = Integer.valueOf(fj.mtsortbutton.lib.R.layout.viewpager_default);
    }

    private void initViewPager() {
        this.listSoreView = new ArrayList();
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");

        for (int i = 0; i < this.page; ++i) {
            this.soreView = layoutInflater.inflate(this.gridView.intValue(), (ViewGroup) null);
            if (this.iDynamicSore != null) {
                int total = this.dataList.size();
                ArrayList data;
                int size;
                if (i == this.page - 1) {
                    data = new ArrayList();

                    for (size = i * this.number; size < total; ++size) {
                        data.add(this.dataList.get(size));
                    }
                } else {
                    data = new ArrayList();
                    if (total < this.number) {
                        size = total;
                    } else {
                        size = (i + 1) * this.number;
                    }

                    for (int j = i * this.number; j < size; ++j) {
                        data.add(this.dataList.get(j));
                    }
                }

                this.iDynamicSore.setGridView(this.soreView, i, data);
            }

            this.listSoreView.add(this.soreView);
        }

        this.viewPager.setAdapter(new ViewPagerAdapter(this.listSoreView));
        this.initLinearLayout(this.viewPager, this.page, this.llIndicator);
    }

    private void initLinearLayout(ViewPager viewPager, int pageSize, LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        final ImageView[] imageViews = new ImageView[pageSize];

        for (int i = 0; i < pageSize; ++i) {
            ImageView image = new ImageView(this.mContext);
            imageViews[i] = image;
            if (i == 0) {
                image.setImageResource(this.RadioSelect);
            } else {
                image.setImageResource(this.RadioUnselected);
            }

            LayoutParams params = new LayoutParams(-2, -2);
            params.setMargins(this.distance, 0, this.distance, 0);
            linearLayout.addView(image, params);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int arg0) {
                for (int i = 0; i < imageViews.length; ++i) {
                    imageViews[arg0].setImageResource(DynamicSoreView.this.RadioSelect);
                    if (arg0 != i) {
                        imageViews[i].setImageResource(DynamicSoreView.this.RadioUnselected);
                    }
                }

            }
        });
    }

    public DynamicSoreView setGridView(Integer gridView) {
        this.gridView = gridView;
        return this;
    }

    public DynamicSoreView setNumColumns(GridView gridView) {
        gridView.setNumColumns(this.number / 2);
        return this;
    }

    public DynamicSoreView init(List<T> t) {
        this.dataList = t;
        this.page = (int) Math.ceil((double) t.size() / (double) this.number);
        this.initViewPager();
        return this;
    }

    public DynamicSoreView setCurItem(int curItem){
        this.viewPager.setCurrentItem(curItem);
        return this;
    }
}
