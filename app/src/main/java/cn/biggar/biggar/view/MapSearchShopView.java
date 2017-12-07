package cn.biggar.biggar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.utils.Utils;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Chenwy on 2017/11/16.
 */

public class MapSearchShopView extends RelativeLayout {
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.iv_shop_bg)
    ImageView ivShopBg;
    @BindView(R.id.iv_distance)
    ImageView ivDistance;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.ll_store_tilte)
    LinearLayout llStoreTitle;

    private MapShopClickListener mapShopCloseListener;
    private Context mContext;

    public void setMapShopCloseListener(MapShopClickListener mapShopCloseListener) {
        this.mapShopCloseListener = mapShopCloseListener;
    }

    public MapSearchShopView(Context context) {
        this(context, null);
    }

    public MapSearchShopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSearchShopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        ButterKnife.bind(LayoutInflater.from(context).inflate(R.layout.map_search_shop_view, this));
//        int w = Utils.getScreenWidth(context) - Utils.dip2px(context, 28);
//        int h = (int) (199f * w / 353f);
//        int storeTitleW = w - Utils.dip2px(context, 14);
//        int storeTitleH = (int) (47f * w / 339f);
//        rlBg.getLayoutParams().width = w;
//        rlBg.getLayoutParams().height = h;
//        llStoreTitle.getLayoutParams().width = storeTitleW;
//        llStoreTitle.getLayoutParams().height = storeTitleH;
    }

    @OnClick({R.id.iv_close, R.id.rl_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                hide();
                if (mapShopCloseListener != null) {
                    mapShopCloseListener.mapShopClose();
                }
                break;
            case R.id.rl_container:
                if (mapShopCloseListener != null) {
                    mapShopCloseListener.mapShopClick();
                }
                break;
        }
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void updateData(MapLocationBean.Arr mapLocationBean) {
        setVisibility(VISIBLE);
        Glide.with(mContext).load(Utils.getRealUrl(mapLocationBean.E_Img2))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(),new RoundedCornersTransformation(Utils.dip2px(mContext, 4), 0,
                        RoundedCornersTransformation.CornerType.BOTTOM))))
                .into(ivShopBg);
        tvStoreName.setText(mapLocationBean.E_Name);
        tvDistance.setText(mapLocationBean.distance + "km");
    }

    public interface MapShopClickListener {
        void mapShopClose();

        void mapShopClick();
    }
}
