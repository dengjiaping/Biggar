package cn.biggar.biggar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.view.SmartImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Update by Chenwy on 17/6/5.
 */
public class ImagePublicAdapter extends BaseAdapter {
    private List<String> images;
    private Context context;
    private RequestOptions options = new RequestOptions();

    public ImagePublicAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        options.centerCrop();
    }

    @Override
    public int getCount() {
        return images.size() < 9 ? images.size() + 1 : images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_image_publish, null);

        SmartImageView imageView = (SmartImageView) view.findViewById(R.id.iv_add);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);


        // 最后一个位置显示添加按钮
        if (images.size() < 9 && i == getCount() - 1) {
            ivDelete.setVisibility(View.GONE);
            imageView.setImageResource(R.mipmap.ic_image_add);
        } else {
            ivDelete.setVisibility(View.VISIBLE);
            Glide.with(context).load(images.get(i)).apply(options).into(imageView);
        }

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRemoveListener != null){
                    onRemoveListener.onRemove(i);
                }
            }
        });

        return view;
    }

    private OnRemoveListener onRemoveListener;

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public interface OnRemoveListener {
        void onRemove(int position);
    }
}
