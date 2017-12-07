package cn.biggar.biggar.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import cn.biggar.biggar.R;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.utils.ToastUtils;

/**
 * Created by Chenwy on 2017/7/31.
 */

public class BiggarVideoPlayer extends StandardGSYVideoPlayer {
    private boolean isAutoPlay;
    private ImageView ivPlay;
    private ImageView ivThumb;
    private LinearLayout ll4GPlay;

    private RelativeLayout mPreviewLayout;
    private TextView mPreviewTime;
    private boolean mOpenPreView;
    private int mPreProgress = -2;
    private boolean mIsFromUser;
    private Context mContext;
    private String mUrl;

    public BiggarVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public BiggarVideoPlayer(Context context) {
        super(context);
    }

    public BiggarVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(final Context context) {
        super.init(context);
        mContext = context;
        ll4GPlay = (LinearLayout) findViewById(R.id.ll_4g_play);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPlayClickListener != null) {
                    onPlayClickListener.onPlayClick();
                }
            }
        });
        ll4GPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPlayClickListener != null) {
                    onPlayClickListener.onPlayClick();
                }
            }
        });
        ivThumb = (ImageView) findViewById(R.id.iv_thumb);
        mPreviewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        mPreviewTime = (TextView) findViewById(R.id.preview_time);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout;
    }

    public void autoPlay() {
        isAutoPlay = true;
        getCurrentPlayer().startPlayLogic();
    }

    public void handPlay() {
        isAutoPlay = false;
        getCurrentPlayer().startPlayLogic();
    }

    public void show4gPlay(){
        ivPlay.setVisibility(GONE);
        ll4GPlay.setVisibility(VISIBLE);
    }

    public void setmOpenPreView(boolean openPreView) {
        this.mOpenPreView = openPreView;
    }

    private void showThumbImageView() {
        ivThumb.setVisibility(VISIBLE);
    }

    private void hideThumbImageView() {
        ivThumb.setVisibility(GONE);
    }

    public void loadThumbImage(String url) {
        mUrl = url;
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .into(ivThumb);
    }

    @Override
    protected void resolveUIState(int state) {
        switch (state) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                showThumbImageView();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
                changeUiToPlayingShow();
                hideThumbImageView();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:
                if (isAutoPlay) {
                    changeUiToPlayingClear();
                } else {
                    changeUiToPlayingShow();
                }
                hideThumbImageView();
                startDismissControlViewTimer();
                ivPlay.setVisibility(GONE);
                ll4GPlay.setVisibility(GONE);
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToPauseShow();
                hideThumbImageView();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
                changeUiToError();
                hideThumbImageView();
                isAutoPlay = false;
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
                mPreviewLayout.setVisibility(GONE);
                dismissProgressDialog();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                showThumbImageView();
                isAutoPlay = false;
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
                mPreviewLayout.setVisibility(GONE);
                setViewShowState(mBottomContainer, INVISIBLE);
                cancelDismissControlViewTimer();
                dismissProgressDialog();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                hideThumbImageView();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        isAutoPlay = false;
        if (fromUser && mOpenPreView && getCurrentState() == CURRENT_STATE_PLAYING) {
            int time = progress * getDuration() / 100;
            showPreView(time);
            if (GSYVideoManager.instance().getMediaPlayer() != null
                    && mHadPlay && (mOpenPreView)) {
                mPreProgress = progress;
            }
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        if (mOpenPreView) {
            mIsFromUser = true;
            mPreviewLayout.setVisibility(VISIBLE);
            mPreProgress = -2;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOpenPreView) {
            if (mPreProgress >= 0) {
                seekBar.setProgress(mPreProgress);
            }
            super.onStopTrackingTouch(seekBar);
            mIsFromUser = false;
            mPreviewLayout.setVisibility(GONE);
        } else {
            super.onStopTrackingTouch(seekBar);
        }
    }

    @Override
    protected void setTextAndProgress(int secProgress) {
        if (mIsFromUser) {
            return;
        }
        super.setTextAndProgress(secProgress);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        BiggarVideoPlayer biggarVideoPlayer = (BiggarVideoPlayer) gsyBaseVideoPlayer;
        biggarVideoPlayer.mOpenPreView = mOpenPreView;
        biggarVideoPlayer.loadThumbImage(mUrl);
        biggarVideoPlayer.setOnPlayClickListener(onPlayClickListener);
        return gsyBaseVideoPlayer;
    }

    private void showPreView(int time) {
        mPreviewTime.setText(CommonUtil.stringForTime(time));
    }

    @Override
    protected void touchDoubleUp() {
    }

    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.mipmap.video_pause);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.mipmap.video_play);
            } else {
                imageView.setImageResource(R.mipmap.video_play);
            }
        }
    }

    /**
     * 退出全屏，全屏按钮
     *
     * @return
     */
    @Override
    public int getShrinkImageRes() {
        return R.mipmap.video_out_screen;
    }

    /**
     * 全屏，全屏按钮
     *
     * @return
     */
    @Override
    public int getEnlargeImageRes() {
        return R.mipmap.video_full_screen;
    }

    public int getRotate() {
        return mRotate;
    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        if (mCurrentState != CURRENT_STATE_ERROR && mCurrentState != CURRENT_STATE_AUTO_COMPLETE) {
            mPreviewLayout.setVisibility(VISIBLE);
            mPreviewTime.setText(seekTime + "/" + totalTime);
        }
    }

    @Override
    protected void dismissProgressDialog() {
        mPreviewLayout.setVisibility(GONE);
        mPreviewTime.setText("00:00");
    }

    public void rePlay() {
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                if (mIfCurrentIsFullscreen) {
                    Debuger.printfLog("onClickResumeFullscreen");
                    mVideoAllCallBack.onClickResumeFullscreen(mOriginUrl, mTitle, this);
                } else {
                    Debuger.printfLog("onClickResume");
                    mVideoAllCallBack.onClickResume(mOriginUrl, mTitle, this);
                }
            }
            try {
                GSYVideoManager.instance().getMediaPlayer().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setStateAndUi(CURRENT_STATE_PLAYING);
        }
    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
    }

    @Override
    protected void onBrightnessSlide(float percent) {
    }

    @Override
    protected void showBrightnessDialog(float percent) {
    }

    private OnPlayClickListener onPlayClickListener;

    public void setOnPlayClickListener(OnPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    public interface OnPlayClickListener {
        void onPlayClick();
    }
}
