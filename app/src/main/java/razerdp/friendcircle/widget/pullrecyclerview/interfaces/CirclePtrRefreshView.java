package razerdp.friendcircle.widget.pullrecyclerview.interfaces;

/**
 * Created by 大灯泡 on 2016/10/29.
 * <p>
 * 刷新view
 */

public interface CirclePtrRefreshView {

    void onRefreshing();

    void onCompelete();
    /**
     * 开始下拉
     */
    void onStart(PtrState state, int positionForRefresh);

    /**
     * 移动中
     *
     * @param state              当前状态
     * @param positionForRefresh 刷新位置
     * @param offsetY            移动距离
     */
    void onMoved(PtrState state, int positionForRefresh, int offsetY);

    /**
     * 释放时
     */
    void onRelease(PtrState state, int positionForRefresh, int offsetY);

    /**
     * 回到原来的位置
     */
    void onReset(PtrState state, int positionForRefresh);

}
