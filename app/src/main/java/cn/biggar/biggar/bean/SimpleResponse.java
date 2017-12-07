package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by Chenwy on 2017/4/27.
 */

public class SimpleResponse implements Serializable {
    private static final long serialVersionUID = -1508859358996613651L;
    public int status;

    public BgResponse toBgResponse() {
        BgResponse bgResponse = new BgResponse();
        bgResponse.status = status;
        return bgResponse;
    }
}
