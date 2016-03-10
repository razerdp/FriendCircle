package razerdp.friendcircle.app.https.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 网络请求回调bean封装
 */
public class BaseResponse {
    //请求码
    private int status;
    //错误码
    private int errorCode;
    //请求类型，用于单activity多个请求的区分
    private int requestType;
    //请求回来的JSON字符串
    private String jsonStr;
    //错误信息
    private String errorMsg;
    //待用，可以存放解析后的JSON Array
    private ArrayList<Object> datas=new ArrayList<>();
    //存放解析后的数据
    private Object data;
    //是否展示dialog
    private boolean showDialog;

    private int start;
    private boolean hasMore;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<?> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }
}
