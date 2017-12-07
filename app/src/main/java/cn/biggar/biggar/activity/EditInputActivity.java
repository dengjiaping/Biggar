package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.utils.ToastUtils;

import butterknife.BindView;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by mx on 2016/8/30.
 * 编辑输入
 */
public class EditInputActivity extends BiggarActivity {
    @BindView(R.id.edit_input_content_et)
    EditText mEtContent;
    @BindView(R.id.edit_input_complete_tv)
    TextView mTvComplete;

    private int mMaxLength;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, EditInfoActivity.class);
        return intent;
    }

    public static Intent startIntent(Context context, String def, String title, String hint, int maxlength) {
        Intent intent = new Intent(context, EditInputActivity.class);
        intent.putExtra("def", def);
        intent.putExtra("title", title);
        intent.putExtra("hint", hint);
        intent.putExtra("maxlength", maxlength);
        return intent;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_input;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        String def = getIntent().getStringExtra("def");
        String title = getIntent().getStringExtra("title");
        String hint = getIntent().getStringExtra("hint");
        mMaxLength = getIntent().getIntExtra("maxlength", 10000);

        RootLayout.getInstance(this).setTitle(TextUtils.isEmpty(title) ? "" : title);

        mEtContent.setHint(TextUtils.isEmpty(hint) ? "" : hint);
        mEtContent.setText(TextUtils.isEmpty(def) ? "" : def);
        mEtContent.setSelection(mEtContent.getText().length());

        mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", mEtContent.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mEtContent.addTextChangedListener(new MyTextWatcher());

    }

    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Editable editable = mEtContent.getText();
            int length = editable.length();//原字符串长度
            if (length > mMaxLength) {//如果原字符串长度大于最大长度
                int selectEndIndex = Selection.getSelectionEnd(editable);//getSelectionEnd：获取光标结束的索引值
                String str = editable.toString();//旧字符串
                String newStr = str.substring(0, mMaxLength);//截取新字符串
                mEtContent.setText(newStr);
                editable = mEtContent.getText();
                int newLength = editable.length();//新字符串长度
                if (selectEndIndex > newLength) {//如果光标结束的索引值超过新字符串长度
                    selectEndIndex = editable.length();
                    ToastUtils.showWarning("最多只能输入" + selectEndIndex + "个字哦~");
                }
                Selection.setSelection(editable, selectEndIndex);//设置新光标所在的位置
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
