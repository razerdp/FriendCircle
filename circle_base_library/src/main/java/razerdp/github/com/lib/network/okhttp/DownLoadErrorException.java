package razerdp.github.com.lib.network.okhttp;

/**
 * Created by 大灯泡 on 2019/2/13.
 */
public class DownLoadErrorException extends Exception {
    int errorCode;

    public DownLoadErrorException(String message) {
        this(message, 0);
    }

    public DownLoadErrorException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
