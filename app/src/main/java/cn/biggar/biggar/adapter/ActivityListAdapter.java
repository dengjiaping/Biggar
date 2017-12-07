package cn.biggar.biggar.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.base.BaseAdapter;
import cn.biggar.biggar.base.ViewHolder;
import cn.biggar.biggar.bean.ActivityBean;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mx on 2016/8/11.
 * 通告列表
 */
public class ActivityListAdapter extends BaseAdapter<ActivityBean> {
    private int mType;// 默认 0  普通通告列表   1 公会后台任务列表
    public ActivityListAdapter(Context con) {
        super(con, R.layout.item_annunciate);
    }

    public ActivityListAdapter(Context con,int type) {
        super(con, R.layout.item_annunciate);
        this.mType=type;
    }

    @Override
    public void convert(ViewHolder viewHolder, ActivityBean activityBean, final int position, View convertView){
        if (mType==1){//公会后台任务
            viewHolder.setVisibility(R.id.brand_logo2_iv,View.VISIBLE);
            viewHolder.setVisibility(R.id.brand_get_tv,View.VISIBLE);
            viewHolder.setOnClickListener(R.id.brand_get_tv, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        v.setTag(position);
                        mListener.onClick(v);
                    }
                }
            });
        }else {
            viewHolder.setVisibility(R.id.brand_logo2_iv,View.GONE);
            viewHolder.setVisibility(R.id.brand_get_tv,View.GONE);
        }

        viewHolder.setText(R.id.content_tv, activityBean.getE_Name());
        viewHolder.setText(R.id.brand_name_tv, activityBean.getE_CreateUser());
        viewHolder.setText(R.id.total_bonus,activityBean.getE_ExciseSumPoint() == null ? "0" : activityBean.getE_ExciseSumPoint());
        viewHolder.setText(R.id.places_tv,activityBean.getE_InOrderCount());
        viewHolder.setText(R.id.number_tv,activityBean.getE_Clicks()==null?"0":activityBean.getE_Clicks());
        if (activityBean.getE_UseType() == 1){
            viewHolder.setVisibility(R.id.brand_phone_img_icon,View.VISIBLE);
        }else if (activityBean.getE_UseType() == 2){
            viewHolder.setVisibility(R.id.brand_share_img_icon,View.VISIBLE);
        }else if (activityBean.getE_UseType() == 3){
            viewHolder.setVisibility(R.id.brand_phone_img_icon,View.VISIBLE);
            viewHolder.setVisibility(R.id.brand_share_img_icon,View.VISIBLE);
        }
        CircleImageView logo=viewHolder.getViewById(R.id.brand_logo_iv);
        Picasso.with(mCon).load(Utils.getRealUrl(BaseAPI.API_BRADN_LOGO+"?ID="+ activityBean.getE_BrandID()))
                .fit()
                .centerCrop()
                .into(logo);

        ImageView image=viewHolder.getViewById(R.id.annunciate_iv);
        Picasso.with(mCon).load(Utils.getRealUrl(activityBean.getE_Img3()))
                .fit()
                .centerCrop()
                .into(image);

        if (activityBean.getE_OpenUrl()==1) {
            viewHolder.setVisibility(R.id.bonus_layout,View.INVISIBLE);
            viewHolder.setVisibility(R.id.places_tv,View.INVISIBLE);
            viewHolder.setVisibility(R.id.v,View.INVISIBLE);
        } else {
            viewHolder.setVisibility(R.id.bonus_layout,View.VISIBLE);
            viewHolder.setVisibility(R.id.places_tv,View.VISIBLE);
            viewHolder.setVisibility(R.id.v,View.VISIBLE);
        }
    }

    private View.OnClickListener mListener;
    public void setOnItemChildsClickListener(View.OnClickListener listener){
        this.mListener=listener;

    }
}
