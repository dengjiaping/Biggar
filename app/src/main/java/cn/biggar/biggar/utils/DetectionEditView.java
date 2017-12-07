package cn.biggar.biggar.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;

/**
 * Created by zl on 2017/2/28.
 * 监听editView
 */

public class DetectionEditView implements TextWatcher {
    EditText editText;
    TextView view;
    Context context;
    public DetectionEditView(Context context,EditText editText, TextView view) {
        this.editText = editText;
        this.view = view;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editText.getText().length() > 0) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_rounded_corners_red_big));
            view.setTextColor(context.getResources().getColor(R.color.whites));
            view.setClickable(true);
        } else {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_rad_border_gray_white));
            view.setTextColor(context.getResources().getColor(R.color.app_text_color_g));
            view.setClickable(false);
        }
    }
}
