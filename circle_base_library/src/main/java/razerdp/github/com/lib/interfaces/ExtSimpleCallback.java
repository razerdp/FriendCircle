package razerdp.github.com.lib.interfaces;

/**
 * Created by 大灯泡 on 2018/6/13.
 * 升级版simpleCallback
 */
public abstract class ExtSimpleCallback<T> implements SimpleCallback<T> {

    public void onStart() {
    }

    public void onError(int code,String errorMessage) {
    }

    public void onFinish() {
    }
}
