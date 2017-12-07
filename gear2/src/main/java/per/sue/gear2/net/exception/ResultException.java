package per.sue.gear2.net.exception;

/**
 * Created by mx on 2016/12/14.
 * 网络 返回异常
 */

public class ResultException extends Throwable{
    public static int DEF_RESULT_ERROR_CODE=-1024;//ResultException 默认错误码  -1024
    public int code=DEF_RESULT_ERROR_CODE;

    public int getCode() {
        return code;
    }

    public ResultException() {
        super();
    }

    public ResultException(String msg) {
        super(msg);
    }

    public ResultException(int code, String msg) {
        super(msg);
        this.code=code;
    }

    public ResultException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResultException(Throwable cause) {
        super(cause);
    }


    /**
     * 根据  Throwable 生成  ResultException
     * @param cause
     * @return
     */
    public static  ResultException createResultException(Throwable cause){
        ResultException exception;
        if (cause instanceof ResultException){
            exception= (ResultException) cause;
        }else{
            exception=new ResultException(DEF_RESULT_ERROR_CODE,cause.getMessage());
        }
        return exception;
    }
}
