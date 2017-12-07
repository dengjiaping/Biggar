package cn.biggar.biggar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.MusicBean;

import per.sue.gear2.adapter.ArrayListAdapter;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicListAdapter extends ArrayListAdapter<MusicBean> {


    public MusicListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_muisc_list, null);
        }
        TextView musicNameTV = (TextView)findViewById(convertView, R.id.music_name_tv);
        TextView singerNameTV = (TextView)findViewById(convertView, R.id.singer_name_tv);

        MusicBean musicBean = getItem(position);
        musicNameTV.setText(musicBean.getTitle());
        singerNameTV.setText(musicBean.getArtist());

        return convertView;
    }
}
