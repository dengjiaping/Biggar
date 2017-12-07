package cn.biggar.biggar.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zl on 2016/12/14.
 * 领取红包状态
 */

public class DrawRedPacketStateDialog {
    Context context;
    Dialog mDia;
    View view;
    String mText;
    @BindView(R.id.draw_red_state_text_tv)
    TextView mTextTv;
    @BindView(R.id.draw_red_state_ok_tv)
    TextView mButtonTv;
    OnExamineLinstener linstener;
    private String mButtonText;
    public DrawRedPacketStateDialog(Context context, String text,OnExamineLinstener linstener) {
        this.context = context;
        mText = text;
        this.linstener = linstener;
    }

    public void showDialog() {
        mDia = new AlertDialog.Builder(context, R.style.UploadImgDiaLog).create();
        view = LayoutInflater.from(context).inflate(R.layout.dialog_draw_red_state, null);
        ButterKnife.bind(this, view);
        mDia.show();
        mDia.setContentView(view);
        mDia.setCancelable(true);
        mDia.setCanceledOnTouchOutside(true);
        mTextTv.setText(mText);
        mButtonTv.setText(mButtonText);
    }

    /**
     * 设置提示内容
     * @param text
     */
    public void setButtonText(String text){
        this.mButtonText = text;
    }
    public void dissDialog(){
        mDia.dismiss();
    }
    @OnClick({R.id.draw_red_state_close_iv, R.id.draw_red_state_ok_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.draw_red_state_close_iv:
                mDia.dismiss();
                break;
            case R.id.draw_red_state_ok_tv:
                mDia.dismiss();
                linstener.onClickButton();
                break;
        }
    }
    public interface OnExamineLinstener{
        void onClickButton();
    }
}
