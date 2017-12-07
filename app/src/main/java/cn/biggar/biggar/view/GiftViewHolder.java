package cn.biggar.biggar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import me.dudu.livegiftview.AbsGiftViewHolder;
import me.dudu.livegiftview.GiftModel;

/**
 * Created by Chenwy on 2017/8/31.
 */

public class GiftViewHolder extends AbsGiftViewHolder {
    private View mRootView;
    private ImageView ivGift;
    private GradientTextView tvGiftNum;
    private CircleImageView ivAvatar;
    private TextView tvName;
    private TextView tvGiftName;

    private String avatar;
    private String userName;
    private UserBean userBean;
    private Context context;

    @Override
    public View initGiftView(Context context) {
        this.context = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.gift_anim_layout, null);
        ivGift = (ImageView) mRootView.findViewById(R.id.iv_gift);
        tvGiftNum = (GradientTextView) mRootView.findViewById(R.id.tv_gift_num);
        ivAvatar = (CircleImageView) mRootView.findViewById(R.id.avatar);
        tvName = (TextView) mRootView.findViewById(R.id.tv_name);
        tvGiftName = (TextView) mRootView.findViewById(R.id.tv_gift_name);
        userBean = Preferences.getUserBean(context);
        if (userBean != null) {
            avatar = userBean.getE_HeadImg();
            userName = userBean.geteName();
        }
        return mRootView;
    }

    @Override
    public View getGiftContainerView() {
        return mRootView;
    }

    @Override
    public ImageView getGiftImageView() {
        return ivGift;
    }

    @Override
    public TextView getGiftNumberView() {
        return tvGiftNum;
    }


    @Override
    public void loadGiftModelToView(Context context, GiftModel giftModel) {
        if (giftModel == null) {
            return;
        }

        Glide.with(context).load(Utils.getRealUrl(giftModel.getE_Img1())).apply(new RequestOptions().centerCrop()).into(ivGift);

        Glide.with(context).load(Utils.getRealUrl(avatar)).apply(new RequestOptions().centerCrop()).into(ivAvatar);

        if (!TextUtils.isEmpty(userName)) {
            tvName.setText(userName);
        }

        if (!TextUtils.isEmpty(giftModel.getE_Name())) {
            tvGiftName.setText("送出" + giftModel.getE_Name());
        }
    }

    @Override
    public void updateUserBean() {
        userBean = Preferences.getUserBean(context);
        if (userBean != null) {
            avatar = userBean.getE_HeadImg();
            userName = userBean.geteName();
        }
    }
}
