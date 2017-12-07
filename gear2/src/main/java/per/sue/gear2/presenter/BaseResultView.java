package per.sue.gear2.presenter;

import per.sue.gear2.ui.IBaseView;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/28
* 版 权：比格科技有限公司
*/
public interface BaseResultView<T> extends IBaseView {

    void onSuccess(T msg);

}
