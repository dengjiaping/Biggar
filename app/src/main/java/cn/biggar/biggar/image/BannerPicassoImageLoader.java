package cn.biggar.biggar.image;
import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;


/**
 * Created by Chenwy on 2017/5/15.
 */

public class BannerPicassoImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, final ImageView imageView) {
      Picasso.with(context).load((String) path).fit().centerCrop().into(imageView);
    }
}
