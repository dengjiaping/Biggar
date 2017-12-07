package cn.biggar.biggar.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.utils.DateUtils;
import cn.biggar.biggar.utils.ToastUtils;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Chenwy on 2017/7/19.
 */

public class DialogDate extends BasePopupWindow {
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;

    //开始日期
    private DatePickerDialog startDateDialog;
    //结束日期
    private DatePickerDialog endDateDialog;

    private OnDateChooseListener onDateChooseListener;

    private Context context;

    public DialogDate(Activity context, OnDateChooseListener onDateChooseListener) {
        super(context);
        this.context = context.getApplicationContext();
        this.onDateChooseListener = onDateChooseListener;
        startDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String s = DateUtils.formatDate(year + "-" + month + "-" + dayOfMonth);
                setStartDate(s);
            }
        }, Integer.parseInt(DateUtils.getCurrentDate().split("-")[0])
                , Integer.parseInt(DateUtils.getCurrentDate().split("-")[1]) - 1
                , Integer.parseInt(DateUtils.getCurrentDate().split("-")[2]));

        endDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String s = DateUtils.formatDate(year + "-" + month + "-" + dayOfMonth);
                setEndDate(s);
            }
        }, Integer.parseInt(DateUtils.getCurrentDate().split("-")[0])
                , Integer.parseInt(DateUtils.getCurrentDate().split("-")[1]) - 1
                , Integer.parseInt(DateUtils.getCurrentDate().split("-")[2]));

    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        View rootView = createPopupById(R.layout.dialog_date);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public void setStartDate(String startDate) {
        tvStartDate.setText(startDate);
    }

    public void setEndDate(String endDate) {
        tvEndDate.setText(endDate);
    }

    @OnClick({R.id.ll_start_date, R.id.ll_end_date, R.id.iv_cancel, R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start_date:
                startDateDialog.show();
                break;
            case R.id.ll_end_date:
                endDateDialog.show();
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                onSure();
                break;
        }
    }

    private void onSure() {
        String startDate = tvStartDate.getText().toString().trim();
        String endDate = tvEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(startDate)) {
            ToastUtils.showError("请选择开始日期");
            return;
        }

        if (TextUtils.isEmpty(endDate)) {
            ToastUtils.showError("请选择结束日期");
            return;
        }

        if (!isEndDateBiggerStartDate(startDate, endDate)) {
            ToastUtils.showError("结束日期不能小于开始日期");
            return;
        }

        if (onDateChooseListener != null) {
            onDateChooseListener.onDateChoose(startDate, endDate);
        }


        dismiss();
    }

    private boolean isEndDateBiggerStartDate(String startDate, String endDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(startDate);//将字符串转换为date类型
            Date dt2 = df.parse(endDate);
            if (dt1.getTime() > dt2.getTime())//比较时间大小,如果dt1大于dt2
            {
                return false;
            }
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public interface OnDateChooseListener {
        void onDateChoose(String startDate, String endDate);
    }
}
