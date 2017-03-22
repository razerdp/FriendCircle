package razerdp.github.com.net.base;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 封装bmob的请求
 */

public abstract class BaseRequestClient<T> {
    private int requestType;
    private boolean showDialog;
    private OnResponseListener<T> onResponseListener;

    public void execute() {
        execute(false);
    }

    public void execute(boolean showDialog) {
        this.showDialog = showDialog;
        onResponseStart(requestType);
        executeInternal(requestType, showDialog);
    }

    protected abstract void executeInternal(final int requestType, final boolean showDialog);

    protected void onResponseStart(int requestType) {
        if (onResponseListener != null) {
            onResponseListener.onStart(requestType);
        }
    }

    protected void onResponseSuccess(T response, int requestType) {
        if (onResponseListener != null) {
            onResponseListener.onSuccess(response, requestType);
        }
    }

    protected void onResponseError(BmobException e, int requestType) {
        if (onResponseListener != null) {
            onResponseListener.onError(e, requestType);
        }
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public OnResponseListener<T> getOnResponseListener() {
        return onResponseListener;
    }

    public BaseRequestClient setOnResponseListener(OnResponseListener<T> onResponseListener) {
        this.onResponseListener = onResponseListener;
        return this;
    }
}
