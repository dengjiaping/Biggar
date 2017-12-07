package cn.biggar.biggar.view.RecyclerAdapter;

import android.view.View;

/**
 * Created by mx on 2016/12/3.
 */
public interface OnItemClickListener<T> {
    void onClick(int position, View view, T item);
}