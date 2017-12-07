package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import cn.biggar.biggar.R;
import per.sue.gear2.utils.MetricsUtils;

/**
 * Created by mx on 2016/9/5.
 * 普通提示 dialog
 */
public class OrdinaryPromptDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private View mContentView;
    private TextView mTvTitle,mTvConfirm;
    private ImageView mIvClose;

    public OrdinaryPromptDialog(Context context) {
        this(context,true);
    }

    /**
     * @param context
     * @param isCancelable 是否可取消  true： 点击dialog外 消失 显示close按钮    反之
     */
    public OrdinaryPromptDialog(Context context,boolean isCancelable) {
        super(context, R.style.style_my_dialog);
        this.mContext = context;
        this.mContentView = View.inflate(context, R.layout.dialog_ordinary_prompt, null);
        setContentView(mContentView);
        initView();
        initEvent();

        this.setCancelable(isCancelable);
        mIvClose.setVisibility(isCancelable?View.VISIBLE:View.GONE);
    }


    private void initView() {
        mIvClose = (ImageView) mContentView.findViewById(R.id.dialog_ordinary_prompt_close_iv);
        mTvTitle = (TextView) mContentView.findViewById(R.id.dialog_ordinary_prompt_title_tv);
        mTvConfirm = (TextView) mContentView.findViewById(R.id.dialog_ordinary_prompt_confirm_tv);

    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setBottomText(String text){
        mTvConfirm.setText(text);
    }

    private void initEvent() {
        mIvClose.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        int[] phoneWh = MetricsUtils.getScreenHight(mContext);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (phoneWh[0]); // 设置宽度
        lp.height = (int) (phoneWh[1]); // 设置宽度
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_ordinary_prompt_close_iv:
                dismiss();
                break;
            case R.id.dialog_ordinary_prompt_confirm_tv:
                dismiss();
                if(listener!=null){
                    listener.onClick(v);
                }
                break;
        }
    }

    private View.OnClickListener listener;
    public void setOnSelectedListener(View.OnClickListener listener){
        this.listener=listener;
    }
}

