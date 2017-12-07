package cn.biggar.biggar.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/8/29.
 */

public class MultiClickButton extends RelativeLayout {
    private RelativeLayout rlButton;
    private TextView tvTime;
    private CircleProgressView progressView;

    private AnimatorSet animatorSet;
    private static final int MAX_TIME = 30;
    private static final int MAX_PROGRESS = 30;
    private int time = MAX_TIME;
    private int progress = 0;
    private int clickNum = 0;
    private OnMultiClickListener onMultiClickListener;
    private boolean isStart;

    public MultiClickButton(Context context) {
        this(context, null);
    }

    public MultiClickButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiClickButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.multi_click_button, this);
        rlButton = (RelativeLayout) findViewById(R.id.rl_button);
        tvTime = (TextView) findViewById(R.id.tv_time);
        progressView = (CircleProgressView) findViewById(R.id.progress);
        progressView.setMaxProgress(MAX_PROGRESS);
        tvTime.setText(time + "");
        initAnim();
        rlButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }

    public void start(){
        isStart = true;
        startAnim();
        progress = 0;
        time = MAX_TIME;
        clickNum += 1;
        progressView.setProgress(progress);
        tvTime.setText(time + "");

        mHandler.removeMessages(1);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(1, 100);

        if (onMultiClickListener != null) {
            onMultiClickListener.onMultiClick(clickNum);
        }
    }

    public void setOnMultiClickListener(OnMultiClickListener onMultiClickListener) {
        this.onMultiClickListener = onMultiClickListener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time -= 1;
            progress += 1;
            tvTime.setText(time + "");
            progressView.setProgress(progress);

            if (time == 0) {
                reset();
            } else {
                mHandler.sendEmptyMessageDelayed(1, 100);
            }
        }
    };

    public void reset() {
        isStart = false;
        time = MAX_TIME;
        progress = 0;
        clickNum = 0;
        mHandler.removeMessages(1);
        mHandler.removeCallbacksAndMessages(null);
        if (onMultiClickListener != null) {
            onMultiClickListener.onMultiClickFinish();
        }
        progressView.setProgress(0);
        tvTime.setText("30");
    }

    private void initAnim() {
        animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlButton, "scaleX", 1f, 1.3f, 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rlButton, "scaleY", 1f, 1.3f, 1f, 1.2f, 1f);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
    }

    private void startAnim() {
        animatorSet.cancel();
        animatorSet.start();
    }

    public boolean isStart() {
        return isStart;
    }

    public interface OnMultiClickListener {
        void onMultiClick(int clickNum);

        void onMultiClickFinish();
    }
}
