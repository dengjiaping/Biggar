package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yinglan.keyboard.HideUtil;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.update.SeachBean;
import cn.biggar.biggar.contract.SearchContract;
import cn.biggar.biggar.presenter.update.SearchPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class SearchActivity extends BiggarActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.tv_recommend_name1)
    TextView tvRecommendName1;
    @BindView(R.id.tv_recommend_name2)
    TextView tvRecommendName2;
    @BindView(R.id.tv_recommend_name3)
    TextView tvRecommendName3;
    @BindView(R.id.tv_recommend_name4)
    TextView tvRecommendName4;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.iv_content_picture1)
    ImageView ivContentPicture1;
    @BindView(R.id.iv_content_picture2)
    ImageView ivContentPicture2;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_recommend_avatar1)
    CircleImageView avatar1;
    @BindView(R.id.iv_recommend_avatar2)
    CircleImageView avatar2;
    @BindView(R.id.iv_recommend_avatar3)
    CircleImageView avatar3;
    @BindView(R.id.iv_recommend_avatar4)
    CircleImageView avatar4;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_plays1)
    TextView tvPlays1;
    @BindView(R.id.head1)
    CircleImageView head1;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.tv_plays2)
    TextView tvPlays2;
    @BindView(R.id.head2)
    CircleImageView head2;
    @BindView(R.id.name2)
    TextView name2;

    private int contentWidth;
    private int avatarWidth;
    private SeachBean mSeachBean;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        HideUtil.init(this);
        tvCancel.setVisibility(View.VISIBLE);
        etSearch.setHint("搜索网红/有价值的内容");
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    String searchName = etSearch.getText().toString().trim();

                    if (TextUtils.isEmpty(searchName)) {
                        ToastUtils.showError("请输入搜索关键字");
                        return false;
                    }
                    hideKeyboard();
                    startActivity(new Intent(SearchActivity.this, SearchResultActivity.class).putExtra("searchName", searchName));
                }
                return false;
            }
        });
        int screenWidth = Utils.getScreenWidth(this);
        contentWidth = (int) ((screenWidth - Utils.dip2px(this, 42)) / 2f);
        avatarWidth = (int) ((screenWidth - Utils.dip2px(this, 120)) / 4f);

        ivContentPicture1.getLayoutParams().width = contentWidth;
        ivContentPicture1.getLayoutParams().height = contentWidth;

        ivContentPicture2.getLayoutParams().width = contentWidth;
        ivContentPicture2.getLayoutParams().height = contentWidth;

        avatar1.getLayoutParams().width = avatarWidth;
        avatar1.getLayoutParams().height = avatarWidth;

        avatar2.getLayoutParams().width = avatarWidth;
        avatar2.getLayoutParams().height = avatarWidth;

        avatar3.getLayoutParams().width = avatarWidth;
        avatar3.getLayoutParams().height = avatarWidth;

        avatar4.getLayoutParams().width = avatarWidth;
        avatar4.getLayoutParams().height = avatarWidth;

        mPresenter.requestSearch();
    }


    @Override
    protected void onResume() {
        super.onResume();
        etSearch.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, 0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    protected int getKeyboardMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
    }

    @OnClick({R.id.tv_cancel, R.id.iv_recommend_avatar1, R.id.iv_recommend_avatar2, R.id.iv_recommend_avatar3, R.id.iv_recommend_avatar4
            , R.id.rl_content1, R.id.rl_content2,R.id.head1,R.id.head2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.iv_recommend_avatar1:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.member.get(0).ID));
                }
                break;
            case R.id.iv_recommend_avatar2:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.member.get(1).ID));
                }
                break;
            case R.id.iv_recommend_avatar3:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.member.get(2).ID));
                }
                break;
            case R.id.iv_recommend_avatar4:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.member.get(3).ID));
                }
                break;
            case R.id.rl_content1:
                if (mSeachBean != null) {
                    startActivity(ContentDetailActivity.startIntent(this, mSeachBean.video.get(0).ID, mSeachBean.video.get(0).E_TypeVal));
                }
                break;
            case R.id.rl_content2:
                if (mSeachBean != null) {
                    startActivity(ContentDetailActivity.startIntent(this, mSeachBean.video.get(1).ID, mSeachBean.video.get(1).E_TypeVal));
                }
                break;
            case R.id.head1:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.video.get(0).E_MemberID));
                }
                break;
            case R.id.head2:
                if (mSeachBean != null) {
                    startActivity(MyHomeActivity.startIntent(this, mSeachBean.video.get(1).E_MemberID));
                }
                break;
        }
    }

    @Override
    public void showError(String errMsg) {
        ToastUtils.showError(errMsg);
    }

    @Override
    public void showSearch(SeachBean seachBean) {
        mSeachBean = seachBean;
        List<SeachBean.Member> members = seachBean.member;
        List<SeachBean.Video> videos = seachBean.video;
        for (int i = 0; i < members.size(); i++) {
            SeachBean.Member member = members.get(i);
            switch (i) {
                case 0:
                    Glide.with(this).load(Utils.getRealUrl(member.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(avatar1);
                    tvRecommendName1.setText(member.E_Name);
                    break;
                case 1:
                    Glide.with(this).load(Utils.getRealUrl(member.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(avatar2);
                    tvRecommendName2.setText(member.E_Name);
                    break;
                case 2:
                    Glide.with(this).load(Utils.getRealUrl(member.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(avatar3);
                    tvRecommendName3.setText(member.E_Name);
                    break;
                case 3:
                    Glide.with(this).load(Utils.getRealUrl(member.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(avatar4);
                    tvRecommendName4.setText(member.E_Name);
                    break;
            }
        }

        for (int i = 0; i < videos.size(); i++) {
            SeachBean.Video video = videos.get(i);
            switch (i) {
                case 0:
                    Glide.with(this).load(Utils.getRealUrl(video.E_Img1)).apply(new RequestOptions().centerCrop()).into(ivContentPicture1);
                    Glide.with(this).load(Utils.getRealUrl(video.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(head1);
                    tvPlays1.setText(video.E_Plays);
                    tvContent1.setText(video.E_Title);
                    name1.setText(video.E_Name);
                    break;
                case 1:
                    Glide.with(this).load(Utils.getRealUrl(video.E_Img1)).apply(new RequestOptions().centerCrop()).into(ivContentPicture2);
                    Glide.with(this).load(Utils.getRealUrl(video.E_HeadImg)).apply(new RequestOptions().centerCrop()).into(head2);
                    tvPlays2.setText(video.E_Plays);
                    tvContent2.setText(video.E_Title);
                    name2.setText(video.E_Name);
                    break;
            }
        }
    }
}
