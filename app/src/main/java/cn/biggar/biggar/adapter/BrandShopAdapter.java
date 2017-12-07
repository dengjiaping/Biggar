package cn.biggar.biggar.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BaseAdapter;
import cn.biggar.biggar.bean.GoodsBean;
import cn.biggar.biggar.utils.Utils;


/**
 * Created by mx on 2016/7/21.
 * 商品
 */
public class BrandShopAdapter extends BaseAdapter<GoodsBean> {


    public BrandShopAdapter(Context context,int layoutId) {
        super(context,layoutId);
    }

    @Override
    public void convert(cn.biggar.biggar.base.ViewHolder viewHolder, GoodsBean goodsBean, int position, View convertView) {
        viewHolder.setText(R.id.name_tv,goodsBean.getE_Name());
        viewHolder.setText(R.id.content_tv,goodsBean.getE_ContentMin());

        TextView textView=viewHolder.getViewById(R.id.price_old_tv);
        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        textView.setText("￥"+ (TextUtils.isEmpty(goodsBean.getE_PriceTag())?"0":goodsBean.getE_PriceTag()));

        TextView textViewNew=viewHolder.getViewById(R.id.price_tv);

        textViewNew.setText("￥"+ (TextUtils.isEmpty(goodsBean.getE_SellPrice())?"0":goodsBean.getE_SellPrice()));

        if (goodsBean.getE_SellPrice()!=null&&goodsBean.getE_PriceTag()!=null){
            if (goodsBean.getE_SellPrice().equals(goodsBean.getE_PriceTag())){
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
                textViewNew.setLayoutParams(layoutParams);
                textView.setVisibility(View.GONE);
            }else if (goodsBean.getE_SellPrice().equals("0.00")){
                textView.setVisibility(View.GONE);
                textViewNew.setVisibility(View.GONE);
            }else{
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                textViewNew.setLayoutParams(layoutParams);
                textView.setVisibility(View.VISIBLE);
            }
        }

        ImageView img=viewHolder.getViewById(R.id.image_iv);
        Picasso.with(mCon)
                .load(Utils.getRealUrl(goodsBean.getE_Img1()))
                .fit()
                .centerCrop()
                .into(img);
    }


}
