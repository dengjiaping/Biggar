package cn.biggar.biggar.wxapi;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.biggar.biggar.app.Constants;
import rx.Subscriber;

/**
 * Created by SUE on 2016/6/21 0021.
 */
public class WXPayManager {
    private static WXPayManager ourInstance = new WXPayManager();

    public static WXPayManager getInstance() {
        return ourInstance;
    }

    private WXPayManager() {
    }

    public void payByWX(Context context, WXPayInfo wxPayInfo) {

        final IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, null);
        iwxapi.registerApp(Constants.APP_ID);
        PayReq mPayReq = new PayReq();


        mPayReq.appId = Constants.APP_ID;
        mPayReq.partnerId = Constants.MCH_ID;
        mPayReq.packageValue = "Sign=WXPay";
        mPayReq.nonceStr = wxPayInfo.getNoncestr();
        mPayReq.prepayId = wxPayInfo.getPrepayId();
        mPayReq.sign = wxPayInfo.getSign();
        mPayReq.timeStamp = wxPayInfo.getTimeStamp();

        iwxapi.sendReq(mPayReq);
    }

    public void payByWX(final Context context) {
        DataApiFactory.getInstance().createICommonAPI(context).loadWXPayInfo()
                .subscribe(new Subscriber<WXPayInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WXPayInfo wxPayInfo) {
                        payByWX(context, wxPayInfo);
                    }
                });


    }
}





