package cn.biggar.biggar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;

/**
 * Created by Chenwy on 2017/11/7.
 */

public class MarkerInfoWindow extends RelativeLayout {
    private TextView tvInfo;

    public MarkerInfoWindow(Context context) {
        this(context, null);
    }

    public MarkerInfoWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkerInfoWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.marker_info_window,this);
        tvInfo = (TextView) findViewById(R.id.tv_info);
    }

    public void setInfo(String info) {
        tvInfo.setText(info);
    }
}
