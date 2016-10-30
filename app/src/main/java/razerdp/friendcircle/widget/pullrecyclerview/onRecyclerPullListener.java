package razerdp.friendcircle.widget.pullrecyclerview;

/**
 * Created by 大灯泡 on 2016/10/29.
 *
 * 下拉回调
 */

public interface onRecyclerPullListener {
    void onStart();
    void onMove(int offsetPos);
    void onFinish();
}
