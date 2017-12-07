package cn.biggar.biggar.bean.update;

import java.io.Serializable;

/**
 * Created by Chenwy on 2017/11/20.
 */

public class MyOrderBean implements Serializable {
    private static final long serialVersionUID = 5019548125380391526L;
    public int state;

    public MyOrderBean() {
    }

    public MyOrderBean(int state) {
        this.state = state;
    }
}
