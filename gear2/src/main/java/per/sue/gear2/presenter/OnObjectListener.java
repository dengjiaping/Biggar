package per.sue.gear2.presenter;

import java.util.List;

/**
 * Created by mx on 2016/8/12.
 * 返回结果  object
 */
public abstract class OnObjectListener<T>{

    /**
     * 成功
     * @param result
     */
    public void  onSuccess(T result){}

    /**
     * 成功返回List
     * @param results
     */
    public void onSuccess(List<T> results){}
    /**
     * 成功 并返回 请求ID
     * @param request
     * @param result
     */
    public  void onSuccessRequest(int request,T result){}

    /**
     * 错误
     */
    public void onError(int code, String msg){}

    /**
     * 完成
     */
    public void onCompleted(){}

}
