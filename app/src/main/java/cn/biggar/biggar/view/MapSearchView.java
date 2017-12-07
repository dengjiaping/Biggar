package cn.biggar.biggar.view;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.update.MapSeachHistoryBean;
import cn.biggar.biggar.bean.update.MapSearchBean;
import cn.biggar.biggar.utils.ToastUtils;

/**
 * Created by Chenwy on 2017/11/13.
 */

public class MapSearchView extends RelativeLayout {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.rl_to_search)
    RelativeLayout rlToSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.fl_search_list)
    FrameLayout flSearchList;

    private OnSearchChangeListener onSearchChangeListener;
    private MyAdpter myAdpter;
    private List<MapSearchBean> suggestionInfos;
    private Context mContext;
    private boolean isClickItem;

    public boolean isClickItem() {
        return isClickItem;
    }

    public void setClickItem(boolean clickItem) {
        isClickItem = clickItem;
    }

    public void setOnSearchChangeListener(OnSearchChangeListener onSearchChangeListener) {
        this.onSearchChangeListener = onSearchChangeListener;
    }

    public MapSearchView(Context context) {
        this(context, null);
    }

    public MapSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        ButterKnife.bind(LayoutInflater.from(context).inflate(R.layout.map_search_view, this));
        suggestionInfos = new ArrayList<>();
        myAdpter = new MyAdpter(suggestionInfos);
        rvSearch.setLayoutManager(new LinearLayoutManager(context));
        rvSearch.setAdapter(myAdpter);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    //点击搜索要做的操作
                    String searchName = etSearch.getText().toString().trim();

                    if (TextUtils.isEmpty(searchName)){
                        ToastUtils.showNormal("请输入搜索关键字");
                        return false;
                    }

                    hideKeyboard();
                    if (onSearchChangeListener != null) {
                        onSearchChangeListener.startKeySearch(searchName);
                    }
                    suggestionInfos.clear();
                    myAdpter.setNewData(suggestionInfos);
                    flSearchList.setVisibility(GONE);
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                if (TextUtils.isEmpty(key)) {
                    if (llSearch.getVisibility() == VISIBLE){
                        showHistory();
                    }
                    return;
                }
                showSearch();
                if (onSearchChangeListener != null) {
                    onSearchChangeListener.onSearchChange(key);
                }
            }
        });

        myAdpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                hideKeyboard();
                if (onSearchChangeListener != null) {
                    setClickItem(true);
                    MapSearchBean item = myAdpter.getItem(position);
                    etSearch.setText(item.key);
                    etSearch.setSelection(item.key.length());
                    onSearchChangeListener.startSearch(item);
                    updateHistory(item);
                    suggestionInfos.clear();
                    myAdpter.setNewData(suggestionInfos);
                    flSearchList.setVisibility(GONE);
                }
            }
        });
    }

    public void hideKeyboard() {
        View view = ((Activity) mContext).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void updateData(List<MapSearchBean> datas) {
        suggestionInfos.clear();
        suggestionInfos.addAll(datas);
        myAdpter.setNewData(suggestionInfos);
    }

    /**
     * 更新历史搜索
     */
    public void updateHistory(MapSearchBean mapSearchBean) {
        List<MapSearchBean> mapSearchBeen = null;
        String history = SPUtils.getInstance().getString(Constants.MAP_SEARCH_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            MapSeachHistoryBean mapSeachHistoryBean = new Gson().fromJson(history, MapSeachHistoryBean.class);
            mapSearchBeen = mapSeachHistoryBean.mapSearchBeen;
        }

        if (mapSearchBeen == null) {
            mapSearchBeen = new ArrayList<>();
        }

        for (MapSearchBean searchBean : mapSearchBeen) {
            if (searchBean.key.equals(mapSearchBean.key)){
                return;
            }
        }

        if (mapSearchBeen.size() >= 5) {
            mapSearchBeen.remove(mapSearchBeen.get(mapSearchBeen.size() - 1));
        }

        mapSearchBeen.add(0,mapSearchBean);

        MapSeachHistoryBean mapSeachHistoryBean = new MapSeachHistoryBean();
        mapSeachHistoryBean.mapSearchBeen = mapSearchBeen;
        String s = new Gson().toJson(mapSeachHistoryBean);

        SPUtils.getInstance().put(Constants.MAP_SEARCH_HISTORY, s);
    }

    /**
     * 显示历史
     */
    private void showHistory() {
        List<MapSearchBean> mapSearchBeen = null;


        String history = SPUtils.getInstance().getString(Constants.MAP_SEARCH_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            MapSeachHistoryBean mapSeachHistoryBean = new Gson().fromJson(history, MapSeachHistoryBean.class);
            mapSearchBeen = mapSeachHistoryBean.mapSearchBeen;
        }

        if (mapSearchBeen == null || mapSearchBeen.size() == 0) {
            flSearchList.setVisibility(GONE);
            return;
        }

        flSearchList.setVisibility(VISIBLE);
        suggestionInfos.clear();
        suggestionInfos.addAll(mapSearchBeen);
        myAdpter.setNewData(suggestionInfos);
    }

    @OnClick({R.id.rl_to_search, R.id.tv_cancel})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.rl_to_search:
                rlToSearch.setVisibility(GONE);
                llSearch.setVisibility(VISIBLE);
                etSearch.requestFocus();
                etSearch.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm =
                                (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etSearch, 0);
                    }
                });
                showHistory();
                break;
            case R.id.tv_cancel:
                cancel();
                break;
        }
    }

    public void cancel(){
        hideKeyboard();
        rlToSearch.setVisibility(VISIBLE);
        hideSearch();
        etSearch.setText("");
        flSearchList.setVisibility(GONE);
    }

    public void showSearch() {
        llSearch.setVisibility(VISIBLE);
        flSearchList.setVisibility(VISIBLE);
    }

    public void hideSearch() {
        suggestionInfos.clear();
        myAdpter.setNewData(suggestionInfos);
        llSearch.setVisibility(GONE);
        flSearchList.setVisibility(GONE);
    }

    static class MyAdpter extends BaseQuickAdapter<MapSearchBean, BaseViewHolder> {

        public MyAdpter(List<MapSearchBean> data) {
            super(R.layout.item_map_search, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MapSearchBean item) {
            helper.setText(R.id.txt, item.key);
        }
    }

    public interface OnSearchChangeListener {
        void onSearchChange(String key);

        void startSearch(MapSearchBean data);

        void startKeySearch(String keyword);
    }
}
