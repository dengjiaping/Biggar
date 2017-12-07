package cn.biggar.biggar.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.event.PaySuccessEvent;
import cn.biggar.biggar.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;



public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//		  	api = WXAPIFactory.createWXAPI(this, "wxb28681563b0f6874");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(final BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode+" errStr = "+resp.toString());
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(resp.errCode == 0){
						ToastUtils.showNormal("支付成功");
						EventBus.getDefault().post(new PaySuccessEvent(true));
					}else{
						if (resp.errCode == -2){
							ToastUtils.showNormal("取消支付");
						}else {
							ToastUtils.showError("支付失败");
						}
							EventBus.getDefault().post(new PaySuccessEvent(false));
					}
					finish();
				}
			});



		}
	}
}