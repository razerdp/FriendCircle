package razerdp.github.com.baselibrary.manager.localphoto;

/**
 * Created by 大灯泡 on 2017/3/23.
 */

public class LPException extends Exception {

    public LPException(String r) {
        this(r, null);
    }

    public LPException(String message, Exception cause) {
        super(message, cause);
    }
}
