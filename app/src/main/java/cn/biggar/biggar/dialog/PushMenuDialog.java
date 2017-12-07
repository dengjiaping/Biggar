package cn.biggar.biggar.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/7/13.
 */

public class PushMenuDialog extends BaseBottomDialog {
    public static final int WAY_TAKE_PHOTO = 0;
    public static final int WAY_ABLUM = 1;
    public static final int WAY_RECORD = 2;
    @Override
    public int getLayoutRes() {
        return R.layout.pop_push_menu;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }

    @OnClick({R.id.ll_record, R.id.ll_album, R.id.ll_take_photo, R.id.iv_dissmiss})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_record:
                if (onPushWayChooseCallBck !=null){
                    onPushWayChooseCallBck.onPushWayChoose(WAY_RECORD);
                }
                dismiss();
                break;
            case R.id.ll_album:
                if (onPushWayChooseCallBck !=null){
                    onPushWayChooseCallBck.onPushWayChoose(WAY_ABLUM);
                }
                dismiss();
                break;
            case R.id.ll_take_photo:
                if (onPushWayChooseCallBck !=null){
                    onPushWayChooseCallBck.onPushWayChoose(WAY_TAKE_PHOTO);
                }
                dismiss();
                break;
            case R.id.iv_dissmiss:
                dismiss();
                break;
        }
    }

    public void setOnPushWayChooseCallBck(OnPushWayChooseCallBck onPushWayChooseCallBck) {
        this.onPushWayChooseCallBck = onPushWayChooseCallBck;
    }

    private OnPushWayChooseCallBck onPushWayChooseCallBck;
    public interface OnPushWayChooseCallBck {
        void onPushWayChoose(int wayChoose);
    }
}
