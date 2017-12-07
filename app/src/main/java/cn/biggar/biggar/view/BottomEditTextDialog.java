package cn.biggar.biggar.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/6/6.
 */

public class BottomEditTextDialog extends BaseBottomDialog {
    private EditText editText;
    private TextView myTextView;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_bottom_edit_text;
    }

    @Override
    public void bindView(View v) {
        editText = (EditText) v.findViewById(R.id.details_comments_edit);
        myTextView = (TextView) v.findViewById(R.id.details_comments_send);
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editText.setText(str1);
                    editText.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    myTextView.setText("确定");
                } else {
                    myTextView.setText("关闭");
                }
            }
        });
        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                String result = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(result)) {
                    if (onCommentSubmitListener != null) {
                        onCommentSubmitListener.onCommentSubmit(result);
                    }
                    editText.setText("");
                }
            }
        });
    }

    OnCommentSubmitListener onCommentSubmitListener;

    public void setOnCommentSubmitListener(OnCommentSubmitListener onCommentSubmitListener) {
        this.onCommentSubmitListener = onCommentSubmitListener;
    }

    public interface OnCommentSubmitListener {
        void onCommentSubmit(String commentContent);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
