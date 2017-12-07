package per.sue.gear2.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import per.sue.gear2.R;


public class ImageSimpleAdapter extends ArrayListAdapter<String>{


	private OnAdapterItemClickLiener mOnAdapterItemClickLiener;
	private int mImageHeight;
	private int mImageWidth;
    private int defultImageResId;
	private boolean isLoob;


	@Override
	public String getItem(int position) {
		return getList().get(position% getList().size());
	}

	@Override
	public int getCount() {
		return !isLoob?getList().size():Integer.MAX_VALUE;
	}

	public ImageSimpleAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		if(convertView == null) {
			holdView = new HoldView();
            LinearLayout linearLayout = new LinearLayout(getContext());
            ImageView imageView = new ImageView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT );

			if(mImageHeight != 0)params.height = mImageHeight;
			if(mImageWidth != 0)params.width = mImageWidth;

            linearLayout.addView(imageView, params);
			holdView.mImageView =  imageView;
			convertView  =linearLayout ;
			convertView.setTag(holdView);
		}else {
			holdView = (HoldView) convertView.getTag();
		}
		int resId  = R.drawable.gear_image_default;
		if(0 != defultImageResId)resId = defultImageResId;

			holdView.mImageView.setImageResource(resId);
			holdView.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holdView.mImageView.setOnClickListener(new  OnImageViewClickListener(position% getList().size()));

		String url= getList().get(position% getList().size());
	    if(null != url && !"".equals(url))
	    {
			Picasso.with(mContext).load(url).fit().centerCrop().into(holdView.mImageView);
	    }
		return convertView;
	}


	public static class HoldView
	{
		ImageView mImageView;
	}


	public class OnImageViewClickListener implements OnClickListener{

		private int position;

		public OnImageViewClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mOnAdapterItemClickLiener != null){

				mOnAdapterItemClickLiener.OnImageViewClick(position, getItem(position));
			}
		}

	}

	public void setOnAdapterItemClickLiener(OnAdapterItemClickLiener l){
		mOnAdapterItemClickLiener = l;

	}

	public interface OnAdapterItemClickLiener{

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
