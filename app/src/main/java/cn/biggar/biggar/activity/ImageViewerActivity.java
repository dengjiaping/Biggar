package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;

/**
 * Created by mx on 2016/8/30.
 * 图片查看器
 */
public class ImageViewerActivity extends BiggarActivity {
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.discreteScrollView)
    DiscreteScrollView discreteScrollView;
    private List<String> mList;
    private int mIndex;
    private CommonPresenter presenter;
    private MyAdapter adapter;
    private String mUserId;
    private boolean isLocPic;

    public static Intent startIntent(Context context, List<String> urls, int index, boolean isShowDelete) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putStringArrayListExtra("urls", (ArrayList<String>) urls);
        intent.putExtra("index", index);
        intent.putExtra("isShowDelete", isShowDelete);
        return intent;
    }

    public static Intent startIntent(Context context, List<String> urls, int index, boolean isShowDelete, boolean isLocPic) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putStringArrayListExtra("urls", (ArrayList<String>) urls);
        intent.putExtra("index", index);
        intent.putExtra("isShowDelete", isShowDelete);
        intent.putExtra("isLocPic", isLocPic);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_image_viewer;
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        UserBean userBean = Preferences.getUserBean(this);
        mUserId = userBean == null ? "" : userBean.getId();
        mList = getIntent().getStringArrayListExtra("urls");
        mIndex = getIntent().getIntExtra("index", 0);
        isLocPic = getIntent().getBooleanExtra("isLocPic", false);
        boolean isShowDelete = getIntent().getBooleanExtra("isShowDelete", false);
        tvDelete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);

        presenter = new CommonPresenter(getActivity());
        adapter = new MyAdapter(mList);
        discreteScrollView.setAdapter(adapter);
        discreteScrollView.setOffscreenItems(mList.size());
        discreteScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                tvNum.setText(adapterPosition + 1 + "/" + mList.size());
            }
        });
        discreteScrollView.scrollToPosition(mIndex);
        tvNum.setText(mIndex + 1 + "/" + mList.size());
    }

    @OnClick({R.id.return_view_icon, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_view_icon:
                finishActivity();
                break;
            case R.id.tv_delete:
                deleteImage();
                break;
        }
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(@Nullable List<String> data) {
            super(R.layout.item_photo_view, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            PhotoView view = helper.getView(R.id.pv);
            view.enable();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
            if (!isLocPic){
                Glide.with(ImageViewerActivity.this).load(Utils.getRealUrl(item)).apply(new RequestOptions().fitCenter()).into(view);
            }else {
                Glide.with(ImageViewerActivity.this).load(item).apply(new RequestOptions().fitCenter()).into(view);
            }
        }
    }

    /**
     * 删除图片
     */
    private void deleteImage() {
        TipsDialog.newInstance("您确定删除此照片吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
            @Override
            public void onSure() {
                String url = mList.get(discreteScrollView.getCurrentItem());

                presenter.deleteImage(mUserId, url, new OnObjectListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                        ToastUtils.showNormal(result);
                        int currentItem = discreteScrollView.getCurrentItem();
                        mList.remove(currentItem);
                        adapter.setNewData(mList);
                        if (currentItem > 1) {
                            discreteScrollView.scrollToPosition(currentItem - 1);
                            tvNum.setText(currentItem + "/" + mList.size());
                        }

                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("urls", (ArrayList<String>) mList);
                        intent.setAction(Constants.KEY_DELETE_IMGE);
                        sendBroadcast(intent);

                        if (mList.size() == 0) {
                            finishActivity();
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        ToastUtils.showError(msg);
                    }
                });
            }

            @Override
            public void onCancel() {
            }
        }).setMargin(52).show(getSupportFragmentManager());
    }

    private void finishActivity(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getActivity().overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
    }
}
