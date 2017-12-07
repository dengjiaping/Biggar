package cn.biggar.biggar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;


/**
 * Created by Chenwy on 2017/5/4.
 */

public class MainBottomBar extends RelativeLayout {
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.iv_mine_hint)
    ImageView ivMineHint;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    private Context mContext;

    public static final int ITEM_MAP = 0;
    public static final int ITEM_ACTIVITY = 1;
    public static final int ITEM_MINE = 2;

    public MainBottomBar(Context context) {
        this(context, null);
    }

    public MainBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.mainbottombar, this);
        ButterKnife.bind(this, rootView);
    }

    private OnItemChooseListener onItemChooseListener;

    public void setOnItemChooseListener(OnItemChooseListener onItemChooseListener) {
        this.onItemChooseListener = onItemChooseListener;
    }

    @OnClick({R.id.ll_home, R.id.ll_map, R.id.ll_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                setDefault();
                ivHome.setImageResource(R.mipmap.label_home_act_3x);
                if (onItemChooseListener != null) {
                    onItemChooseListener.onItemChoose(ITEM_ACTIVITY);
                }
                break;
            case R.id.ll_map:
                setDefault();
                ivMap.setImageResource(R.mipmap.label_map_act_3x);
                if (onItemChooseListener != null) {
                    onItemChooseListener.onItemChoose(ITEM_MAP);
                }
                break;
            case R.id.ll_mine:
                setDefault();
                ivMine.setImageResource(R.mipmap.label_user_act_3x);
                if (onItemChooseListener != null) {
                    onItemChooseListener.onItemChoose(ITEM_MINE);
                }
                break;
        }
    }

    public interface OnItemChooseListener {
        void onItemChoose(int position);
    }

    public void setMineHint(boolean isHint) {
        ivMineHint.setVisibility(isHint ? GONE : VISIBLE);
    }

    private void setDefault() {
        ivMap.setImageResource(R.mipmap.label_map_nor_3x);
        ivHome.setImageResource(R.mipmap.label_home_nor_3x);
        ivMine.setImageResource(R.mipmap.label_user_nor_3x);
    }
}
