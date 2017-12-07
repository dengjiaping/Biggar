package cn.biggar.biggar.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BaseAdapter;
import cn.biggar.biggar.base.ViewHolder;
import cn.biggar.biggar.utils.Utils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by zl on 2016/12/8.
 */

public class PersonPhotoAdapter extends BaseAdapter<String> {

    public PersonPhotoAdapter(Context con) {
        super(con, R.layout.item_person_photo_image);

    }

    @Override
    public void convert(ViewHolder viewHolder, String s, int position, View convertView) {
        ImageView imageView = viewHolder.getViewById(R.id.person_photo_image_hv);
        Glide.with(mCon).load(Utils.getRealUrl(s))
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(10, 0))).into(imageView);
    }
}
