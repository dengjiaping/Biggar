package cn.biggar.biggar.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.event.PaySuccessEvent;
import cn.biggar.biggar.event.PublishVideoSucessEvent;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.UpdataHeadImgPresenter;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;
import cn.biggar.biggar.wxapi.WXPayInfo;
import cn.biggar.biggar.wxapi.WXPayManager;
import cn.biggar.biggar.utils.ImageUtil;
import cn.biggar.biggar.utils.ToastUtils;
import per.sue.gear2.utils.BitmapUtils;
import per.sue.gear2.utils.ImageFactory;
import per.sue.gear2.widget.ProgressWebView;
import rx.Subscriber;

public class WebViewActivity extends BiggarActivity {

    @BindView(R.id.web_view)
    ProgressWebView webView;
    @BindView(R.id.load_iv)
    ImageView loadIv;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.ll_titlebar)
//    LinearLayout llTitleBar;

    private String baseUrl;
    UpdataHeadImgPresenter presenter;
    private String mUserID;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;

    private String mPrepayId;//微信预支付ID
    private boolean isShowTitleBar;
    private boolean isFromSplash;
    private RootLayout rootLayout;

    public static Intent jumpFromSplash(Context mContext, String url) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isFromSplash", true);
        intent.putExtra("isShowTitleBar", true);
        mContext.startActivity(intent);
        return intent;
    }

    public static Intent getInstance(Context mContext, String url) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
        return intent;
    }

    public static Intent getInstance(Context mContext, String url, boolean isShowTitleBar) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isShowTitleBar", isShowTitleBar);
        mContext.startActivity(intent);
        return intent;
    }

    public static Intent getIntent(Context mContext, String url) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        return intent;
    }


    public static Intent getInstance(Context mContext, String url, int state) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("state", state);
        mContext.startActivity(intent);
        return intent;
    }

    public static Intent getInstance(Context mContext, String url, int state, String content) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("state", state);
        intent.putExtra("content", content);
        mContext.startActivity(intent);
        return intent;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            UserBean userBean = Preferences.getUserBean(getApplication());
            mUserID = userBean.getId();
        } else {
            mUserID = null;
        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //标题栏
        rootLayout = RootLayout.getInstance(this);
        baseUrl = getIntent().getStringExtra("url");
        isShowTitleBar = getIntent().getBooleanExtra("isShowTitleBar", false);
        isFromSplash = getIntent().getBooleanExtra("isFromSplash", false);
        if (!isShowTitleBar) {
            rootLayout.getTitleBarView().setVisibility(View.GONE);
        } else {
            rootLayout
                    .setTitleMaxWidth((int) (Utils.getScreenWidth(getApplicationContext()) * 0.54))
                    .setOnLeftClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    })
                    .setOnLeftSecondClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isFromSplash) {
                                startActivity(MainActivity.startIntent(WebViewActivity.this));
                            }
                            finish();
                        }
                    })
                    .setOnRightClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            webView.loadUrl("javascript:shopToShare()");
                        }
                    });
        }

        initWebViews();
        webView.loadUrl(baseUrl);
        presenter = new UpdataHeadImgPresenter(getActivity());
    }

    private void initWebViews() {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//启用js
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//js和android交互
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setAppCacheEnabled(true); //设置H5的缓存打开,默认关闭
        settings.setUseWideViewPort(true);//设置webview自适应屏幕大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//设置，可能的话使所有列的宽度不超过屏幕宽度
        settings.setLoadWithOverviewMode(true);//设置webview自适应屏幕大小
        settings.setDomStorageEnabled(true);//设置可以使用localStorage
        settings.setSupportZoom(false);//关闭zoom按钮
        settings.setBuiltInZoomControls(false);//关闭zoom
        webView.setWebViewClient(new H5WebViewClient());
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        //  支持js(必要)
        webView.getSettings().setJavaScriptEnabled(true);
        //  添加js对象(必要)
        webView.addJavascriptInterface(new JsOperation(getActivity()), "client");


        //android webView 调用系统相册和相机
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mSourceIntent = ImageUtil.choosePicture();
                startActivityForResult(mSourceIntent, FILECHOOSER_RESULTCODE);
                mUploadMessage = uploadMsg;
            }

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mSourceIntent = ImageUtil.choosePicture();
                startActivityForResult(mSourceIntent, FILECHOOSER_RESULTCODE);
                mUploadMessage = uploadMsg;
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mSourceIntent = ImageUtil.choosePicture();
                startActivityForResult(mSourceIntent, FILECHOOSER_RESULTCODE);
                mUploadMessage = uploadMsg;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (isShowTitleBar) {
                    rootLayout.setTitle(title);
                }
            }
        });


    }

    private Intent mSourceIntent;
    private Uri imageUri;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Biggar");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        WebViewActivity.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

    }

    public class H5WebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                if (loadingLayout.getVisibility() == View.VISIBLE) {
                    loadingLayout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.gear_hide_in));
                    loadingLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            } else {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != FILECHOOSER_RESULTCODE
                    || mUploadCallbackAboveL == null) {
                return;
            }

            Uri[] results = null;

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    results = new Uri[]{imageUri};
                } else {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();

                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    Uri uri = Uri.parse(dataString);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //适配手机，有些手机上传图片会将图片旋转一定的角度
                    if (BitmapUtils.readPictureDegree(dataString) != 0) {

                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), ImageFactory.ratio(BitmapUtils.rotaingImageView(BitmapUtils.readPictureDegree(dataString), bitmap), 200, 200), null, null));
                        results = new Uri[]{uri};
                    } else {
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), ImageFactory.ratio(bitmap, 400, 400), null, null));
                        results = new Uri[]{uri};
                    }

                }
            }
            if (results != null) {
                mUploadCallbackAboveL.onReceiveValue(results);
                mUploadCallbackAboveL = null;
            } else {
                results = new Uri[]{imageUri};
                mUploadCallbackAboveL.onReceiveValue(results);
                mUploadCallbackAboveL = null;
            }
        } else {
            switch (requestCode) {
                case FILECHOOSER_RESULTCODE:
                case REQUEST_CODE_PICK_IMAGE:

                    try {
                        if (mUploadMessage == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.w("aa", "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri;
                        if (BitmapUtils.readPictureDegree(sourcePath) != 0) {
                            uri = Uri.parse(sourcePath);
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapUtils.rotaingImageView(BitmapUtils.readPictureDegree(sourcePath), bitmap), null, null));

                        } else {
                            uri = Uri.fromFile(new File(sourcePath));
                        }
                        mUploadMessage.onReceiveValue(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void load(String jsMethod) {
        webView.loadUrl(jsMethod);
    }


    private SHARE_MEDIA platform;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (null == map) return;
            showProgressDialog("正在正在绑定...");
            String userId = Preferences.getUserBean(WebViewActivity.this).getId();
            String openId = map.get("openid");
            if (openId == null) {
                openId = map.get("uid");
            }
            String accessToken = map.get("access_token");
            DataApiFactory.getInstance().createIUserAPI(WebViewActivity.this).bindThird(userId, share_media.name(), openId, accessToken)
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {


                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showError("绑定失败");
                            dismissProgressDialog();
                        }

                        @Override
                        public void onNext(String s) {
                            dismissProgressDialog();
                            ToastUtils.showNormal("绑定成功");
                            WebViewActivity.this.load("javascript: reloadF5()");
                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showError(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    public void bindQQ() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UMShareAPI.get(WebViewActivity.this).deleteOauth(getActivity(), platform, umAuthListener);
                platform = SHARE_MEDIA.QQ;
                UMShareAPI.get(WebViewActivity.this).doOauthVerify(getActivity(), platform, umAuthListener);
            }
        });
    }


    public void bindWeixin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UMShareAPI.get(WebViewActivity.this).deleteOauth(getActivity(), platform, umAuthListener);
                platform = SHARE_MEDIA.WEIXIN;
                UMShareAPI.get(WebViewActivity.this).doOauthVerify(getActivity(), platform, umAuthListener);
            }
        });

    }

    public void bindSina() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UMShareAPI.get(WebViewActivity.this).deleteOauth(getActivity(), platform, umAuthListener);
                platform = SHARE_MEDIA.SINA;
                UMShareAPI.get(WebViewActivity.this).doOauthVerify(getActivity(), platform, umAuthListener);
            }
        });
    }

    protected ProgressDialog progressDialog;

    public void showProgressDialog(String tip) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
        }
        progressDialog.setMessage(tip);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Subscribe
    public void onEventMainThread(PaySuccessEvent event) {
        if (event != null) {
            if (webView != null && event.paySucess) {
                webView.loadUrl("javascript:GoWxPaySuccse('" + mPrepayId + "')");
            }
        }
    }

    private void search(String searchStr) {
        hideKeyboard();
        String params = "'1','10','','" + searchStr + "'";
        webView.loadUrl("javascript:myorder_data(" + params + ")");
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(PublishVideoSucessEvent event) {
        //H5 跳转 拍摄视频   成功后刷新
        if (event != null && event.videoBean != null && webView != null) {
            webView.reload();
        }
    }

    /**
     * 拍摄视频
     */
    private void startRecordVideo() {
        if (!AppPrefrences.getInstance(getApplication()).isLogined()) {
            getActivity().startActivity(LoginActivity.startIntent(getActivity()));
        } else {
        }
    }

    private void showShareDialog(String title, String imageUrl, String content, String createUser, String targetUrl) {
        new ShareDialog().show(getSupportFragmentManager(), title, content, imageUrl, targetUrl, ShareDialog.SHARE_TYPE_3, "", new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showError("分享失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("取消分享");
            }
        });
    }

    class JsOperation {

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        private Activity activity;
        final IWXAPI msgApi;

        public JsOperation(Activity activity) {
            this.activity = activity;
            msgApi = WXAPIFactory.createWXAPI(activity, null);

        }

        /**
         * 拍视频或图片
         */
        @JavascriptInterface
        public void openVideo() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() instanceof WebViewActivity) {
                        startRecordVideo();
                    }
                }
            });
        }


        /**
         * 跳转登陆页面
         */
        @JavascriptInterface
        public void toLogin() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(LoginActivity.startIntent(getActivity()));
                }
            });
        }

        /**
         * 跳转活动悬赏
         */
        @JavascriptInterface
        public void toNotice() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        /**
         * 保存商家 token
         */
        @JavascriptInterface
        public void saveData(final String token) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppPrefrences.getInstance(getApplication()).setMerchantToken(token);
                }
            });
        }

        /**
         * 商家退出  即清除token
         */
        @JavascriptInterface
        public void merchantOut() {
            AppPrefrences.getInstance(getApplication()).setMerchantToken("");
        }

        /**
         * 返回商家 token
         */
        @JavascriptInterface
        public String getData() {
            String token = AppPrefrences.getInstance(getApplication()).getMerchantToken();
            return token;
        }

        /**
         * 商家  切换到 达人账户
         */
        @JavascriptInterface
        public void selectAccouView() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppPrefrences.getInstance(getActivity()).setAccountType(1);
                    startActivity(MainActivity.startIntent(getActivity()));
                    getActivity().finish();
                }
            });
        }


        /**
         * 返回上一页
         */
        @JavascriptInterface
        public void backToView() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFromSplash) {
                        startActivity(MainActivity.startIntent(WebViewActivity.this));
                    }
                    finish();
                }
            });
        }

        /**
         * 跳个人主页
         *
         * @param userId 用户ID
         */
        @JavascriptInterface
        public void toUserView(final String userId) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(MyHomeActivity.startIntent(getActivity(), userId));
                }
            });
        }


        /**
         * 跳转至品牌空间
         *
         * @param brandId 品牌ID
         */
        @JavascriptInterface
        public void toBrandView(final String brandId) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(BrandSpaceActivity.startIntent(getActivity(), brandId));
                }
            });
        }


        /**
         * 跳转至品牌空间  （应该不用）
         *
         * @param userId  用户ID
         * @param brandId 品牌ID
         */
        @JavascriptInterface
        public void toBrandView(final String userId, final String brandId) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(BrandSpaceActivity.startIntent(getActivity(), userId, brandId));
                }
            });
        }

        /**
         * 跳转是视频详情页 （应该不用）
         *
         * @param userId  用户Id
         * @param videoId 视频Id
         * @param type    视频或者图片类型
         */
        @JavascriptInterface
        public void toVideoView(final String userId, final String videoId, final String type) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(ContentDetailActivity.startIntent(getActivity(), videoId, type));
                }
            });
        }

        /**
         * 跳转是视频详情页
         *
         * @param videoId 视频Id
         * @param type    视频或者图片类型
         */
        @JavascriptInterface
        public void toVideoView(final String videoId, final String type) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().startActivity(ContentDetailActivity.startIntent(getActivity(), videoId, type));
                }
            });
        }

        /**
         * 退出登陆
         *
         * @param
         */
        @JavascriptInterface
        public void loginout() {
            AppPrefrences.getInstance(getActivity()).setToken("");
            Preferences.storeUserBean(getActivity(), null);
            MobclickAgent.onProfileSignOff();
            ToastUtils.showNormal("退出成功");
            this.getActivity().finish();
        }


        /**
         * 微信支付
         *
         * @param signature
         */
        @JavascriptInterface
        public void payByWXNew(String signature) {
            WXPayInfo wxPayInfo = new Gson().fromJson(signature, WXPayInfo.class);
            mPrepayId = wxPayInfo.getPrepayId();
            WXPayManager.getInstance().payByWX(getActivity(), wxPayInfo);
        }


        @JavascriptInterface
        public String getUserId() {
            return mUserID;
        }

        @JavascriptInterface
        public void bindWeiXin() {

            if (getActivity() instanceof WebViewActivity) {
                ((WebViewActivity) getActivity()).bindWeixin();
            }
        }

        @JavascriptInterface
        public void bindQQ() {

            if (getActivity() instanceof WebViewActivity) {
                ((WebViewActivity) getActivity()).bindQQ();
            }
        }

        @JavascriptInterface
        public void bindSina() {
            if (getActivity() instanceof WebViewActivity) {
                ((WebViewActivity) getActivity()).bindSina();
            }
        }


        /**
         * 分享
         */
        @JavascriptInterface
        public void shareTextImageTarget(final String title, final String content, final String imgUrl, final String shareUrl) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showShareDialog(title, imgUrl, content, "", shareUrl);
                }
            });
        }

        /**
         * 分享
         */
        @JavascriptInterface
        public void shareTextImageTarget(final String title, final String content, final String imgUrl, final String shareUrl, final String state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showShareDialog(title, imgUrl, content, "", shareUrl);
                }
            });
        }

        /**
         * 分享 前  参数获取
         */
        @JavascriptInterface
        public void shareOidType(String oid, String type) {
        }

        /**
         * 上传头像  弹出对话框
         *
         * @param witdth
         * @param height
         */
        @JavascriptInterface
        public void toTakePicture(final int witdth, final int height) {
        }

        @JavascriptInterface
        public void setNavTitle(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isShowTitleBar) {
                        rootLayout.setTitle(title);
                    }
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        if (isFromSplash) {
            startActivity(MainActivity.startIntent(this));
        }
        super.onBackPressed();
    }
}
