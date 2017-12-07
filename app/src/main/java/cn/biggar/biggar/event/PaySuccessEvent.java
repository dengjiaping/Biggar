package cn.biggar.biggar.event;

/**
 * Created by mx on 2016/6/21.
 * 微信支付成功
 */
public class PaySuccessEvent {

    public boolean paySucess;

    public PaySuccessEvent(boolean paySucess) {
        this.paySucess = paySucess;
    }
}


