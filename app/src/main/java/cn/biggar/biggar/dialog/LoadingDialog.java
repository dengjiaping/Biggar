package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import cn.biggar.biggar.R;


/**
 * Created by mx on 2016/8/23.
 * loading
 */
public class LoadingDialog extends Dialog  {
    private Context mContext;
    private View mContentView;
    private ImageView mIvLoad;
    RotateAnimation animation;
    public LoadingDialog(Context context) {
        super(context, R.style.style_loading_dialog);
        this.mContext = context;
        getWindow().setWindowAnimations(R.style.loading_anim); //设置窗口弹出动画
        this.mContentView = View.inflate(context, R.layout.dialog_loading, null);
        setContentView(mContentView);
        init();
    }

    private void init(){
        mIvLoad= (ImageView) mContentView.findViewById(R.id.dialog_loading_iv);
        animation = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_rotating);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mIvLoad.clearAnimation();
            }
        });
    }

    @Override
    public void show() {
        mIvLoad.setAnimation(animation);
        super.show();
    }
}