package cn.biggar.biggar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;


/**
 * Created by 张炼 on 2016/8/15.
 */
public class SelectorView extends LinearLayout {
    Context mContext;
    View mView;
    TextView mTextNum,mTextData;
    LinearLayout mLayout;
    boolean IsSelect = false;
    int mBgDefault = 0;
    int mBgSelect = 0;
    int mTextColorDefault = 0;
    int mTextColorSelect = 0;
    int mIconDefault = 0;
    int mIconSelect = 0;
    String mTextStr = "";

    public SelectorView(Context context) {
        super(context);
    }

    public SelectorView(Context context, AttributeSet attrs) {
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item,this);
        mTextData= (TextView) mView.findViewById(R.id.label_view_name);
        mTextNum = (TextView) mView.findViewById(R.id.label_view_num);
        mLayout = (LinearLayout) mView.findViewById(R.id.label_view);
        Init(attrs);
    }

    private void Init(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.BottomView);
        mBgDefault = a.getResourceId(R.styleable.BottomView_BottomView_bg_default , R.color.app_text_color_s);
        mBgSelect = a.getResourceId(R.styleable.BottomView_BottomView_bg_select , R.color.app_text_color_s);
        mTextColorDefault = a.getColor(R.styleable.BottomView_BottomView_text_color_default, mContext.getResources().getColor(R.color.app_text_color_s));
        mTextColorSelect = a.getColor(R.styleable.BottomView_BottomView_text_color_select, mContext.getResources().getColor(R.color.app_text_color_s));
        mTextStr = a.getString(R.styleable.BottomView_BottomView_text);
        mIconDefault = a.getResourceId(R.styleable.BottomView_BottomView_icon_default, R.color.app_text_color_s);
        mIconSelect = a.getResourceId(R.styleable.BottomView_BottomView_icon_select, R.color.app_text_color_s);
        mTextData.setText(mTextStr);
    }
    public void setTexts(String title,String num){
        mTextData.setText(title);
        mTextNum.setText(num);
    }
    /**
     * 设置选中项
     * @param state
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  void setItemSelect(boolean state)
    {

        IsSelect = state;
        if(IsSelect)
        {
            mLayout.setBackgroundResource(mIconSelect);
            mTextData.setTextColor(mTextColorSelect);
            mTextNum.setTextColor(mTextColorSelect);
        }
        else
        {
            mLayout.setBackgroundResource(mIconDefault);
            mTextData.setTextColor(mTextColorDefault);
            mTextNum.setTextColor(mTextColorDefault);
        }

    }
}
