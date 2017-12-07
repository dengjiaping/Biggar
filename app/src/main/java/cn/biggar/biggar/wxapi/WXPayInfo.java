package cn.biggar.biggar.wxapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SUE on 2016/6/21 0021.
 */
public class WXPayInfo {

    @SerializedName("appid")
    private String appId;
    @SerializedName("nonce_str")
    private String noncestr;
    @SerializedName("package")
    private String packageName;
    @SerializedName("mch_id")
    private String partnerId;
    @SerializedName("prepay_id")
    private String prepayId;
    @SerializedName("timestamp")
    private String timeStamp;
    @SerializedName("sign")
    private String sign;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
