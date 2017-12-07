package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import cn.biggar.biggar.R;
import per.sue.gear2.utils.MetricsUtils;


/**
 * Created by mx on 2016/12/9.
 * 文本输入器
 */
public class EditInputDialog extends Dialog {
    private Context mContext;
    private View mContentView;
    private TextView mTvTitle;
    private EditText mEtContent;
    private Callback callback;
    private int mMaxLength=1000;
    public void setOnCallbcak(Callback callbcak) {
        this.callback = callbcak;
    }

    public EditInputDialog(Context context) {
        super(context, R.style.style_my_dialog);
        View contentView = View.inflate(context, R.layout.dialog_edit_input, null);
        this.mContext = context;
        this.mContentView = contentView;

        setContentView(contentView);
        initViews();
        initEvents();
    }

    private void initViews() {
        mEtContent= (EditText) mContentView.findViewById(R.id.dialog_edit_input_content_et);
        mTvTitle= (TextView) mContentView.findViewById(R.id.dialog_edit_input_title_tv);
        mEtContent.addTextChangedListener(new MyTextWatcher(mEtContent,"昵称不能超过20个字符"));
    }

    /**
     * title
     * @param title
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     * 默认值
     * @param def
     */
    public void setDefaultData(String def,String hint){
        mEtContent.setText(def);
        mEtContent.setHint(hint);
        mEtContent.setSelection(mEtContent.getText().length());
    }

    /**
     * 设置最大长度
     * @param length
     */
    public void setMaxLength(int length){
        this.mMaxLength=length;
    }

    private void initEvents() {
//        mContentView.findViewById(R.id.view_gap).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        mContentView.findViewById(R.id.dialog_edit_input_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dismiss();
            }
        });

        mContentView.findViewById(R.id.dialog_edit_input_confirm_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                dismiss();
                if (callback != null) {
                    callback.callback(mEtContent.getText().toString());
                }
            }
        });
    }

    class MyTextWatcher implements TextWatcher {
        EditText editText;
        private int mMaxLenth = 20;
        private int mCharCount;
        private String mToastText;
        private Toast mToast;
        MyTextWatcher(EditText editText,String mToastText){
            this.editText = editText;
            this.mToastText = mToastText;
        }
        /**
         * 避免多次重复弹出toast
         *
         * @param text
         */
        private void showToast(String text) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCharCount = before+count;
            if (mCharCount > mMaxLenth) {
                editText.setSelection(editText.length());
            }
            try {
                mCharCount = editText.getText().toString().getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mCharCount > mMaxLenth) {
                CharSequence subSequence = null;
                for (int i = 0; i < s.length(); i++) {
                    subSequence = s.subSequence(0, i);
                    try {

                        if (subSequence.toString().getBytes("GBK").length == mCharCount) {
                            editText.setText(subSequence.toString());
                            break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mToastText)) {
                    showToast(mToastText);
                }
                String androidVersion = android.os.Build.VERSION.RELEASE;
                if (androidVersion.charAt(0) >= '4') {
                    editText.setText(subSequence.toString());
                }
            }
        }
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
     * 时间选择 器  回调类
     */
    public interface Callback {
         void callback(String str);
    }

}
