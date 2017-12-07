package cn.biggar.biggar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import java.io.File;
import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.ApkInfoBean;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by langgu on 16/5/20.
 */
public class UpdateDialogFragment extends DialogFragment {


    private ApkInfoBean apkInfoBean;
    private String isMust;


    public static UpdateDialogFragment newInstance(ApkInfoBean bean) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ApkInfoBean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        apkInfoBean = (ApkInfoBean) getArguments().getSerializable("ApkInfoBean");
        isMust = apkInfoBean.isMust();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_version, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cancelBtn.setVisibility(isMust.equals("0") ? View.VISIBLE : View.GONE);
        viewHolder.titleTv.setText("发现新版本" + apkInfoBean.getVersionName());
        viewHolder.sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.llUpdateControll.setVisibility(View.GONE);
                viewHolder.llUpdateing.setVisibility(View.VISIBLE);
                setCancelable(false);
                startDownLoad(viewHolder.numberProgressBar, apkInfoBean.getDownloadUrl(), apkInfoBean.getName());
            }
        });
        viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
//        double sizes = 0;
//        try {
//            int size = Integer.parseInt(apkInfoBean.getSize());
//            sizes = size / 1024.0 / 1024.0;
//        } catch (Exception e) {
//            LogUtils.e("MX", "UpdateDialogFragment e:" + e.getMessage());
//        }
        viewHolder.logTv.setText(new StringBuilder().append(apkInfoBean.getUpdateLog()));
        return dialog;
    }

    private void startDownLoad(final NumberProgressBar numberProgressBar, String url, String apkName) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/biggar/apk");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File apk = new File(dir, apkName + ".apk");
        if (apk.exists()) {
//            Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
//            installAPKIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (Build.VERSION.SDK_INT >= 24) {
//                Uri apkUri = FileProvider.getUriForFile(getActivity(), "cn.biggar.biggar.provider", apk);
//                installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                installAPKIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//            } else {
//                installAPKIntent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
//            }
//            startActivity(installAPKIntent);
//            dismiss();
//            return;
            apk.delete();
        }

        OkGo.<File>get(url)
                .execute(new FileCallback(dir.getAbsolutePath(), apkName + ".apk") {
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        numberProgressBar.setProgress((int) (progress.fraction * 100f));
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = response.body();
                        Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);
                        installAPKIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 24) {
                            Uri apkUri = FileProvider.getUriForFile(getActivity(), "cn.biggar.biggar.provider", file);
                            installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            installAPKIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        } else {
                            installAPKIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        }
                        startActivity(installAPKIntent);
                        dismiss();
                    }
                });
    }


    static class ViewHolder {
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.log_tv)
        TextView logTv;
        @BindView(R.id.sure_btn)
        Button sureBtn;
        @BindView(R.id.cancel_btn)
        Button cancelBtn;
        @BindView(R.id.number_progress_bar)
        NumberProgressBar numberProgressBar;
        @BindView(R.id.ll_update_controll)
        LinearLayout llUpdateControll;
        @BindView(R.id.ll_updateing)
        LinearLayout llUpdateing;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
