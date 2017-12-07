package cn.biggar.biggar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.MusicContainer;

import per.sue.gear2.adapter.ArrayListAdapter;
import per.sue.gear2.widget.CircleImageView;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicContainerAdapter extends ArrayListAdapter<MusicContainer> {
    public MusicContainerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView  = LayoutInflater.from(getContext()).inflate(R.layout.item_muisc_container_list, null);
        }
        TextView folderNameTV = (TextView)findViewById(convertView,  R.id.fiels_name_tv);
        TextView folderDescribeTV =  (TextView)findViewById(convertView,  R.id.files_describe_tv);
        CircleImageView imageView = (CircleImageView) findViewById(convertView, R.id.files_icon_iv);
        imageView.setUseDefaultStyle(false);
        MusicContainer bean = getItem(position);
        folderNameTV.setText(bean.getTitle());
        folderDescribeTV.setText(bean.getSubhead());
        if(null != bean.getAlbumArtURI()){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), bean.getAlbumArtURI());
                imageView.setImageBitmap(bitmap);
            } catch (Exception exception) {
                exception.printStackTrace();
                // log error
            }
        }

        return convertView;
    }
}
