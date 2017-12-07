package cn.biggar.biggar.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cn.biggar.biggar.utils.Utils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import per.sue.gear2.R;
import per.sue.gear2.adapter.ArrayListAdapter;

/**
 * Update by Chenwy on 2017/4/21.
 */

public class RoundImageAdapter extends ArrayListAdapter<String> {
    private OnAdapterItemClickLiener mOnAdapterItemClickLiener;
    private int mImageHeight;
    private int mImageWidth;
    private int defultImageResId;
    private boolean isLoob;


    @Override
    public String getItem(int position) {
        return getList().get(position % getList().size());
    }

    @Override
    public int getCount() {
        return !isLoob ? getList().size() : Integer.MAX_VALUE;
    }

    public RoundImageAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            holdView = new HoldView();
            LinearLayout linearLayout = new LinearLayout(getContext());
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            if (mImageHeight != 0) params.height = mImageHeight;
            if (mImageWidth != 0) params.width = mImageWidth;

            linearLayout.addView(imageView, params);
            holdView.mImageView = imageView;
            convertView = linearLayout;
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        holdView.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holdView.mImageView.setOnClickListener(new OnImageViewClickListener(position % getList().size()));

        String url = getList().get(position % getList().size());
        if (null != url && !"".equals(url)) {
            Glide.with(mContext).load(url)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .transform(new RoundedCornersTransformation(Utils.dip2px(mContext, 4), 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(holdView.mImageView);
        }else {
            holdView.mImageView.setImageResource(R.drawable.gear_image_default);
        }
        return convertView;
    }


    public static class HoldView {
        ImageView mImageView;
    }


    public class OnImageViewClickListener implements View.OnClickListener {

        private int position;

        public OnImageViewClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (mOnAdapterItemClickLiener != null) {
                mOnAdapterItemClickLiener.OnImageViewClick(position, getItem(position));
            }
        }

    }

    public void setOnAdapterItemClickLiener(OnAdapterItemClickLiener l) {
        mOnAdapterItemClickLiener = l;

    }

    public interface OnAdapterItemClickLiener {

        public void OnImageViewClick(int position, String url);

    }


    public boolean isLoob() {
        return isLoob;
    }

    public void setIsLoob(boolean isLoob) {
        this.isLoob = isLoob;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.mImageHeight = imageHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.mImageWidth = imageWidth;
    }

    public void setDefultImageResId(int defultImageResId) {
        this.defultImageResId = defultImageResId;
    }
}
