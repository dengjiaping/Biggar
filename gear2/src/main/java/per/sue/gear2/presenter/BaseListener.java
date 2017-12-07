package per.sue.gear2.presenter;

/**
 * Created by mx on 2016/8/6.
 * 返回结果
 */
public interface BaseListener {
    /**
     * 错误
     */
    void onError(int code, String msg);

    /**
     * 完成
     */
    void onCompleted();
}
