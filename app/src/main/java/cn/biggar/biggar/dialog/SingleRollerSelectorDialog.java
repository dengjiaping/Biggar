package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.biggar.biggar.R;
import cn.biggar.biggar.view.LoopView.LoopView;
import cn.biggar.biggar.view.LoopView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import per.sue.gear2.utils.MetricsUtils;


/**
 * 滚动选择 器  单选
 */
public class SingleRollerSelectorDialog extends Dialog {
    private LoopView mLoopView;
    private Context mContext;
    private String mCurStr;
    private View mContentView;
    private TextView mTvRight;
    private List<String> mData=new ArrayList<>();
    private Callback callback;
    private RelativeLayout.LayoutParams layoutParams;

    public void setOnCallbcak(Callback callbcak) {
        this.callback = callbcak;
    }

    public SingleRollerSelectorDialog(Context context) {
        super(context, R.style.style_my_dialog);
        View contentView = View.inflate(context, R.layout.dialog_single_selector, null);
        this.mContext = context;
        this.mContentView = contentView;

        setContentView(contentView);
        initViews();
        initEvents();
    }

    private void initEvents() {
        mContentView.findViewById(R.id.view_gap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentView.findViewById(R.id.id_select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentView.findViewById(R.id.id_select_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    callback.callback(mCurStr);
                }
            }
        });

        //滚动监听
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurStr = mData.get(index);
            }
        });
    }

    private void initViews() {
        mTvRight= (TextView) mContentView.findViewById(R.id.tv_single_selector_right);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        RelativeLayout rootview = (RelativeLayout) mContentView.findViewById(R.id.rl_loopview);

        mLoopView = new LoopView(getContext());
        mLoopView.setViewPadding(10,0,10,0);
        //设置是否循环播放
        //loopView.setNotLoop();
        //设置原始数据
        mLoopView.setItems(mData);
        //设置初始位置
        mLoopView.setInitPosition(0);
        //设置字体大小
        mLoopView.setTextSize(18);

        rootview.addView(mLoopView, layoutParams);
    }

    @Override
    public void show() {
        if (isShowing()) return;
        super.show();
        int[] phoneWh = MetricsUtils.getScreenHight(getContext());
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (phoneWh[0]); // 设置宽度
        this.getWindow().setAttributes(lp);
    }


    /**
     * 设置数据
     * @param data
     */
    public void setDate(List<String> data){
        this.mData=data;
        mLoopView.setItems(mData);
        mLoopView.setInitPosition(0);
        try {
            mCurStr = mData.get(mLoopView.getSelectedItem());
        }catch (Exception e){
            mCurStr=mData.get(0);
        }
    }
    /***
     * 设置 右 提示文字
     * @param desc
     */
    public void setRightText(CharSequence desc) {
        mTvRight.setText(desc);
    }
    /**
     * 时间选择 器  回调类
     */
    public interface Callback {
         void callback(String str);
    }

}
