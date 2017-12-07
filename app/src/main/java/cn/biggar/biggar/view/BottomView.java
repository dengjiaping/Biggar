package cn.biggar.biggar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;


/**
 * Created by Administrator on 2016/4/1.
 */
public class BottomView extends LinearLayout{
    Context mContext;
    View mView;
    TextView mText;
    ImageView mImage;
    boolean  IsSelect = false;
    int mBgDefault = 0;
    int mBgSelect = 0;
    int mTextColorDefault = 0;
    int mTextColorSelect = 0;
    int mIconDefault = 0;
    int mIconSelect = 0;
    String mTextStr = "";

    public int getmIconSelect() {
        return mIconSelect;
    }

    public void setmIconSelect(int mIconSelect) {
        this.mIconSelect = mIconSelect;
    }

    public int getmIconDefault() {
        return mIconDefault;
    }

    public void setmIconDefault(int mIconDefault) {
        this.mIconDefault = mIconDefault;

    }

    public BottomView(Context context) {
        super(context);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_bottom_view_item,this);
        mImage= (ImageView) mView.findViewById(R.id.img_select_bottom_view_icon);
        mText = (TextView) mView.findViewById(R.id.text_select_bottom_view_text);
        Init(attrs);
    }
    /**
     * 初始化
     */
    private void Init(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.BottomView);
        mBgDefault = a.getResourceId(R.styleable.BottomView_BottomView_bg_default , R.color.app_text_color_s);
        mBgSelect = a.getResourceId(R.styleable.BottomView_BottomView_bg_select , R.color.app_text_color_s);
        mTextColorDefault = a.getColor(R.styleable.BottomView_BottomView_text_color_default, mContext.getResources().getColor(R.color.app_text_color_s));
        mTextColorSelect = a.getColor(R.styleable.BottomView_BottomView_text_color_select, mContext.getResources().getColor(R.color.app_text_color_s));
        mTextStr = a.getString(R.styleable.BottomView_BottomView_text);
        mIconDefault = a.getResourceId(R.styleable.BottomView_BottomView_icon_default, R.color.app_text_color_s);
        mIconSelect = a.getResourceId(R.styleable.BottomView_BottomView_icon_select, R.color.app_text_color_s);
        mText.setText(mTextStr);
    }
    public void setText(String str){
        mText.setText(str);
    }
    /**
     * 设置选中项
     * @param state
     */
    public  void setItemSelect(boolean state)
    {
        IsSelect = state;
        if(IsSelect)
        {
            mImage.setImageResource(mIconSelect);
            mText.setTextColor(mTextColorSelect);
        }
        else
        {
            mImage.setImageResource(mIconDefault);
            mText.setTextColor(mTextColorDefault);
        }

    }

}
