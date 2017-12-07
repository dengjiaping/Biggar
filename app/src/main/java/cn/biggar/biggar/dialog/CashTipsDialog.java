package cn.biggar.biggar.dialog;

import android.os.Bundle;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/9/22.
 */

public class CashTipsDialog extends BaseNiceDialog {
    private String content;

    public static CashTipsDialog newInstance(String content) {
        CashTipsDialog cashTipsDialog = new CashTipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        cashTipsDialog.setArguments(bundle);
        return cashTipsDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_cash_tips;
    }

    @Override
    public void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
        Bundle arguments = getArguments();
        if (null != arguments) {
            content = arguments.getString("content");
            viewHolder.setText(R.id.tv_content,content);
        }

        viewHolder.setOnClickListener(R.id.btn_sure, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
