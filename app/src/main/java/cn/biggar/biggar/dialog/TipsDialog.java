package cn.biggar.biggar.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/9/14.
 */

public class TipsDialog extends BaseNiceDialog {
    private String title;
    private String leftText;
    private String rightText;
    private OnTipsOnClickListener onTipsOnClickListener;

    public static TipsDialog newInstance(String title) {
        TipsDialog tipsDialog = new TipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        tipsDialog.setArguments(bundle);
        return tipsDialog;
    }

    public static TipsDialog newInstance(String title, String leftText, String rightText) {
        TipsDialog tipsDialog = new TipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("leftText", leftText);
        bundle.putString("rightText", rightText);
        tipsDialog.setArguments(bundle);
        return tipsDialog;
    }

    public TipsDialog setOnTipsOnClickListener(OnTipsOnClickListener onTipsOnClickListener) {
        this.onTipsOnClickListener = onTipsOnClickListener;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_tips;
    }

    @Override
    public void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
        Bundle arguments = getArguments();
        if (null != arguments) {
            title = arguments.getString("title");
            leftText = arguments.getString("leftText");
            rightText = arguments.getString("rightText");
        }

        viewHolder.setOnClickListener(R.id.tv_left, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTipsOnClickListener != null) {
                    onTipsOnClickListener.onCancel();
                }
                dismiss();
            }
        });

        viewHolder.setOnClickListener(R.id.tv_right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTipsOnClickListener != null) {
                    onTipsOnClickListener.onSure();
                }
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(title)) {
            viewHolder.setText(R.id.tv_title, title);
        }

        if (!TextUtils.isEmpty(leftText)) {
            viewHolder.setText(R.id.tv_left, leftText);
        }

        if (!TextUtils.isEmpty(rightText)) {
            viewHolder.setText(R.id.tv_right, rightText);
        }
    }

    public interface OnTipsOnClickListener {
        void onSure();

        void onCancel();
    }
}
