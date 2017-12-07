package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import cn.biggar.biggar.R;
import cn.biggar.biggar.utils.TimeTransform;
import cn.biggar.biggar.view.LoopView.LoopView;
import cn.biggar.biggar.view.LoopView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import per.sue.gear2.utils.MetricsUtils;


/**
 * 滚动选择 器  日期
 */
public class DateSelectorDialog extends Dialog {
    private LoopView mLoopViewY, mLoopViewM, mLoopViewD;
    private Context mContext;
    private String mCurY, mCurM, mCurD;
    private View mContentView;
    private List<String> mDataY = new ArrayList<>();
    private List<String> mDataM = new ArrayList<>();
    private List<String> mDataD = new ArrayList<>();
    private Callback callback;
    private RelativeLayout.LayoutParams layoutParams;


    public void setOnCallbcak(Callback callbcak) {
        this.callback = callbcak;
    }

    public DateSelectorDialog(Context context) {
        super(context, R.style.style_my_dialog);
        View contentView = View.inflate(context, R.layout.dialog_date_selector, null);
        this.mContext = context;
        this.mContentView = contentView;

        setContentView(contentView);
        initViews();
        initData();
        initEvents();
    }

    private void initData() {
        loadDate();
    }

    private void initEvents() {
        mContentView.findViewById(R.id.view_gap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentView.findViewById(R.id.id_select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentView.findViewById(R.id.id_select_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    mCurD = mDataD.get(mLoopViewD.getSelectedItem());
                    Logger.d(" y - " + mCurY + " m - " + mCurM + " d - " + mCurD);
                    String strdate = mCurY + "-" + mCurM + "-" + mCurD;
                    Date date = new Date();
                    try {
                        date = TimeTransform.ConverToDateByYMD(strdate);
                    } catch (Exception e) {
                    }
                    callback.callback(date, strdate);
                }
            }
        });

        //滚动监听
        mLoopViewY.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurY = mDataY.get(index);
                Logger.d( "mCurY = " + mCurY);
            }
        });
        //滚动监听
        mLoopViewM.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurM = mDataM.get(index);
                Logger.d("mCurM = " + mCurM);
                newDdate();
            }
        });
        //滚动监听
        mLoopViewD.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurD = mDataD.get(index);
                Logger.d("mCurD = " + mCurD);
            }
        });
    }



    /**
     * 重新加载 日
     */
    private void newDdate() {
        int day = TimeTransform.getDaysByYearMonth(Integer.parseInt(mCurY), Integer.parseInt(mCurM));
        mDataD.clear();
        for (int i = 1; i <= day; i++) {
            mDataD.add(i < 10 ? "0" + i : i + "");
        }
        mLoopViewD.setItems(mDataD);
    }

    private void initViews() {
        mLoopViewY = (LoopView) mContentView.findViewById(R.id.loopview_y);
        mLoopViewM = (LoopView) mContentView.findViewById(R.id.loopview_m);
        mLoopViewD = (LoopView) mContentView.findViewById(R.id.loopview_d);
        mLoopViewY.setViewPadding(10, 0, 10, 0);
        mLoopViewM.setViewPadding(10, 0, 10, 0);
        mLoopViewD.setViewPadding(20, 0, 20, 0);
        //设置是否循环播放
        //loopView.setNotLoop();
        //设置初始位置
        mLoopViewY.setInitPosition(0);
        //设置字体大小
        mLoopViewY.setTextSize(18);
        //设置初始位置
        mLoopViewM.setInitPosition(0);
        //设置字体大小
        mLoopViewM.setTextSize(18);
        //设置初始位置
        mLoopViewD.setInitPosition(0);
        //设置字体大小
        mLoopViewD.setTextSize(18);

    }

    @Override
    public void show() {
        if (isShowing()) return;
        super.show();
        int[] phoneWh = MetricsUtils.getScreenHight(getContext());
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (phoneWh[0]); // 设置宽度
        this.getWindow().setAttributes(lp);
    }


    /**
     * 设置数据
     */
    public void loadDate() {
        int y = getCur(1);
        int m = getCur(2);
        int d = getCur(3);

        for (int i = 1960; i <= y; i++) {
            mDataY.add(i + "");
        }
        for (int i = 1; i <= m; i++) {
            mDataM.add(i < 10 ? "0" + i : i + "");
        }
        for (int i = 1; i <= d; i++) {
            mDataD.add(i < 10 ? "0" + i : i + "");
        }

        mLoopViewY.setItems(mDataY);
        mLoopViewM.setItems(mDataM);
        mLoopViewD.setItems(mDataD);

        mLoopViewY.setInitPosition(mDataY.size() - 1);
        mLoopViewM.setInitPosition(mDataM.size() - 1);
        mLoopViewD.setInitPosition(mDataD.size() - 1);

        mCurY = mDataY.get(mDataY.size() - 1);
        mCurM = mDataM.get(mDataM.size() - 1);
        mCurD = mDataD.get(mDataD.size() - 1);
    }
    /**
     * 设置 初始时间
     * @param date
     */
    public void setInitDate(String date) {
        try {
            setInitDate(TimeTransform.ConverToDateByYMD(date));
        } catch (Exception e) {
            Logger.e("setInitDate() - " + e.getMessage());
        }
    }

    /**
     * 设置 初始时间
     * @param date
     */
    public void setInitDate(Date date) {
        if(date==null)return;
        try {
            Calendar a = Calendar.getInstance();
            a.setTime(date);
            int y = a.get(Calendar.YEAR);
            int m = a.get(Calendar.MONTH) + 1;
            int d = a.get(Calendar.DATE);

            int indexY = mDataY.indexOf(y + "");
            int indexM = mDataM.indexOf(m < 10 ? "0" + m : m + "");

            mCurY = mDataY.get(indexY);
            mCurM = mDataM.get(indexM);
            newDdate();
            int indexD = mDataD.indexOf(d < 10 ? "0" + d : d + "");
            mCurD = mDataD.get(indexD);

            mLoopViewY.setInitPosition(indexY);
            mLoopViewM.setInitPosition(indexM);
            mLoopViewD.setInitPosition(indexD);
        }catch (Exception e){
            Logger.e("setInitDate() - e:"+e.getMessage());
        }
    }

    private int getCur(int state) {
        Calendar a = Calendar.getInstance();
        switch (state) {
            case 1:
                return a.get(Calendar.YEAR);
            case 2:
                return a.get(Calendar.MONTH) + 1;
            case 3:
                return a.get(Calendar.DATE);
        }
        return a.get(Calendar.DATE);
    }


    /**
     * 时间选择 器  回调类
     */
    public interface Callback {
        void callback(Date data, String str);
    }


}
