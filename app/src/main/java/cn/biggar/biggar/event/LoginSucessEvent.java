package cn.biggar.biggar.event;


import cn.biggar.biggar.bean.UserBean;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/25
* 版 权：比格科技有限公司
*/
public class LoginSucessEvent {

    public UserBean userBean;

    public LoginSucessEvent(UserBean userBean) {
        this.userBean = userBean;
    }
}
