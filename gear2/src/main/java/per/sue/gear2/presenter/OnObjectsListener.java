package per.sue.gear2.presenter;

import java.util.List;

/**
 * Created by mx on 2016/8/12.
 * 返回结果  objects  集合
 */
public abstract class OnObjectsListener<T> {

    /**
     * 成功
     * @param result
     */
    public void onSuccess(List<T> result){}

    /**
     * 成功 并返回 请求ID
     * @param request
     * @param result
     */
    public  void onSuccessRequest(int request,List<T> result){}

    /**
     * 错误
     */
    public void onError(int code, String msg){}

    /**
     * 完成
     */
    public void onCompleted(){}

}
