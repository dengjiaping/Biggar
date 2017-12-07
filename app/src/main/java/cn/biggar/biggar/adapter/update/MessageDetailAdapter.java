package cn.biggar.biggar.adapter.update;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.MessageBean;
import cn.biggar.biggar.utils.DensityUtil;
import cn.biggar.biggar.utils.Utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/22.
 */

public class MessageDetailAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    private String tid;

    public MessageDetailAdapter(String tid, List<MessageBean> data) {
        super(R.layout.item_message_detail, data);
        this.tid = tid;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.fl_date).setPadding(0, DensityUtil.dp2px(mContext, 28), 0, 0);
        } else {
            helper.getView(R.id.fl_date).setPadding(0, 0, 0, 0);
        }

        helper.setText(R.id.tv_date, item.getE_CreateDate())
                .addOnClickListener(R.id.tv_agree)
                .addOnClickListener(R.id.tv_refuse);

        if (!TextUtils.isEmpty(tid)) {
            if (tid.equals("1")) {
                helper.setImageResource(R.id.iv_logo, R.mipmap.system_news_3x);
            } else if (tid.equals("2")) {
                helper.setImageResource(R.id.iv_logo, R.mipmap.official_news_3x);
            }
//            else if (tid.equals("3")) {
//                helper.setImageResource(R.id.iv_logo, R.mipmap.forum_news_3x);
//            }
        }


        LinearLayout llContent = helper.getView(R.id.ll_content);
        if ((item.getE_RelaType().equals("15") && item.getE_AgentState().equals("0")) || item.getE_RelaType().equals("5")){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.rightMargin = Utils.dip2px(mContext,20);
            llContent.setLayoutParams(params);
        }else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.rightMargin = Utils.dip2px(mContext,20);
            llContent.setLayoutParams(params);
        }

        helper.setGone(R.id.ll_apply, item.getE_RelaType().equals("15") && item.getE_AgentState().equals("0"));

        //代理邀请
        if (item.getE_RelaType().equals("15")) {
            helper.setGone(R.id.iv_goods,false);
            String agentState = item.getE_AgentState();

            //0就是等待同意或拒绝
            if (agentState.equals("0")) {
                String name = item.getE_Message();
                String a = "邀请你成为Ta的代理商，是否同意？";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //1就是同意了对方的代理
            else if (agentState.equals("1")) {
                String a = "你同意了";
                String name = item.getE_Message();
                String b = "的代理邀请";
                SpannableString spannableString = new SpannableString(a + name + b);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
                spannableString.setSpan(colorSpan, a.length(), a.length()+name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //2就是拒绝
            else if (agentState.equals("2")) {
                String a = "你拒绝了";
                String name = item.getE_Message();
                String b = "的代理邀请";
                SpannableString spannableString = new SpannableString(a + name + b);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
                spannableString.setSpan(colorSpan, a.length(), a.length()+name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //3就是对方同意了我给他的代理
            else if (agentState.equals("3")) {
                String name = item.getE_Message();
                String a = "同意了你的代理邀请";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //4就是拒绝我给他的代理
            else if (agentState.equals("4")) {
                String name = item.getE_Message();
                String a = "拒绝了你的代理邀请";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
        }
        //收到礼物
        else if(item.getE_RelaType().equals("5")){
            if (Integer.parseInt(item.getE_ObjID()) > 0){
                helper.setGone(R.id.iv_goods,true);
                Glide.with(mContext).load(Utils.getRealUrl(item.getE_VideoImg()))
                        .apply(new RequestOptions().centerCrop())
                        .into((ImageView) helper.getView(R.id.iv_goods));
            }else {
                helper.setGone(R.id.iv_goods,false);
            }
            helper.setText(R.id.tv_content, item.getE_Message());
        } else{
            helper.setGone(R.id.iv_goods,false);
            helper.setText(R.id.tv_content, item.getE_Message());
        }
    }
}
