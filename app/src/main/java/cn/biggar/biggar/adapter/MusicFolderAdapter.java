package cn.biggar.biggar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.FolderBean;

import per.sue.gear2.adapter.ArrayListAdapter;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicFolderAdapter extends ArrayListAdapter<FolderBean> {
    public MusicFolderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView  = LayoutInflater.from(getContext()).inflate(R.layout.item_muisc_folder_list, null);
        }
        TextView folderNameTV = (TextView)findViewById(convertView,  R.id.folder_name_tv);
        TextView folderDescribeTV =  (TextView)findViewById(convertView,  R.id.folder_describe_tv);
        TextView folderPath = (TextView) findViewById(convertView, R.id.folder_path_tv);
        FolderBean bean = getItem(position);
        folderNameTV.setText(bean.getFolderName());
        folderPath.setText(bean.getFolderPath());
        folderDescribeTV.setText(getContext().getString(R.string.music_label_music_count, bean.getFileCount()));

        return convertView;
    }
}
