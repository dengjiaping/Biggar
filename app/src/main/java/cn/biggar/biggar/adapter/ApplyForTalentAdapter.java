package cn.biggar.biggar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.view.RecyclerAdapter.BaseRecyclerAdapter;
import cn.biggar.biggar.view.RecyclerAdapter.ViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import per.sue.gear2.utils.ImageFactory;
import per.sue.gear2.widget.RoundedImageView;

/**
 * Created by mx on 2016/12/13.
 * 申请比格认证
 */

public class ApplyForTalentAdapter extends BaseRecyclerAdapter<String> {
    private int w,h;

    public ApplyForTalentAdapter(Context context) {
        super(context, R.layout.item_apply_for_talent);
        w = 300;
        h = 300;
    }

    @Override
    public void convert(ViewHolder holder, final String s, int position) {
        final RoundedImageView imageView=holder.getView(R.id.item_aft_image);
        if (position==0){
            imageView.setImageResource(R.mipmap.person_add_img_icon);
            holder.setVisibility(R.id.item_aft_delete_tv, View.GONE);
            return;
        }
        holder.setVisibility(R.id.item_aft_delete_tv, View.VISIBLE);
        load(new File(s), imageView, w, h, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
                imageView.setImageBitmap( ImageFactory.ratio(s,w,h));
            }
        });
    }


    private void load(File target, ImageView iv, int w, int h, Callback callback) {
        RequestCreator creater;
        creater = Picasso.with(mContext).load(target);
        if(w>0&&h>0){
            creater.resize(w,h).centerCrop();
        }
        creater.config(Bitmap.Config.RGB_565);

        if (callback == null) {
            creater.into(iv);
        } else {
            creater.into(iv, callback);
        }
    }
}
