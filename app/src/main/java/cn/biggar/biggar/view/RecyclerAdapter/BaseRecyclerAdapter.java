package cn.biggar.biggar.view.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mx on 2016/12/3.
 * RecyclerView  BaseAdapter
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    protected OnItemClickListener mOnItemClickListener;


    public BaseRecyclerAdapter(Context context, int layoutId)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = new ArrayList<T>();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 添加数据
     * @param datas
     */
    public void addDatas(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (datas==null)return;
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加一个数据
     * @param data
     */
    public void addData(T data){
        if (mDatas!=null){
            mDatas.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除指定 索引数据
     * @param position
     * @return
     */
    public T remove(int position){
        if (mDatas!=null){
            T t = mDatas.remove(position);
            notifyDataSetChanged();
            return t;
        }
        return null;
    }

    /**
     * 设置item 点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 绑定事件
     * @param vh   viewholder
     * @param item item
     */
    protected final void bindItemViewClickListener(final int position, ViewHolder vh, final T item) {
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position,view, item);
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder, T t,int position);

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mContext, mHeaderView, null);
        }
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        int pos = getRealPosition(viewHolder);
        convert(viewHolder, mDatas.get(pos),pos);
        bindItemViewClickListener(pos,viewHolder,mDatas.get(pos));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0&&holder.getItemViewType()==TYPE_HEADER) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }
    /**
     * 获取 指定索引  数据
     * @param position
     * @return
     */
    public T getData(int position){
        return mDatas.get(position);
    }
    /**
     * 返回集合
     * @return
     */
    public List<T> getData(){
        return mDatas;
    }

    /**
     * @return 集合数量
     */
    public int getCount(){
        return mDatas==null?0:mDatas.size();
    }
}