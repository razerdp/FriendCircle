package razerdp.github.com.baselibrary.mvp;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * presenter基类接口
 */

public interface IBasePresenter<V extends IBaseView> {

    /**
     * 绑定view
     */
    IBasePresenter<V> bindView(V view);

    /**
     * 取消绑定
     */
    IBasePresenter<V> unbindView();

}
