package razerdp.friendcircle.utils;

/**
 * Created by 大灯泡 on 2016/2/10.
 * 利用线程制作出的插值器
 */
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * @desc 平滑滚动线程，用于递归调用自己来实现某个视图的平滑滚动
 * */
public class SmoothChangeThread implements Runnable {
    //需要操控的视图
    private View v = null;
    //原Y坐标
    private int fromY = 0;
    //目标Y坐标
    private int toY = 0;
    //动画执行时间（毫秒）
    private long durtion = 0;
    //帧率
    private int fps = 60;
    //间隔时间（毫秒），间隔时间 = 1000 / 帧率
    private int interval = 0;
    //启动时间，-1 表示尚未启动
    private long startTime = -1;
    //减速插值器
    private static Interpolator mInterpolator = null;
    private OnSmoothResultChangeListener mListener;

    public static SmoothChangeThread CreateLinearInterpolator(View v, int fromY, int toY, long durtion, int fps){
        mInterpolator=new LinearInterpolator();
        return new SmoothChangeThread(v,fromY,toY,durtion,fps);
    }
    public static SmoothChangeThread CreateDecelerateInterpolator(View v, int fromY, int toY, long durtion, int fps){
        mInterpolator=new DecelerateInterpolator();
        return new SmoothChangeThread(v,fromY,toY,durtion,fps);
    }
    public static SmoothChangeThread CreateAccelerateDecelerateInterpolator(View v, int fromY, int toY, long durtion, int fps){
        mInterpolator=new AccelerateDecelerateInterpolator();
        return new SmoothChangeThread(v,fromY,toY,durtion,fps);
    }

    /**
     *
     * @param v view
     * @param fromY 原始数据
     * @param toY 目标数据
     * @param durtion 持续时间
     * @param fps 帧数
     */
    private SmoothChangeThread(View v, int fromY, int toY, long durtion, int fps) {
        this.v = v;
        this.fromY = fromY;
        this.toY = toY;
        this.durtion = durtion;
        this.fps = fps;
        this.interval = 1000 / this.fps;
    }

    @Override
    public void run() {
        //先判断是否是第一次启动，是第一次启动就记录下启动的时间戳，该值仅此一次赋值
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        //得到当前这个瞬间的时间戳
        long currentTime = System.currentTimeMillis();
        //放大倍数，为了扩大除法计算的浮点精度
        int enlargement = 1000;
        //算出当前这个瞬间运行到整个动画时间的百分之多少
        float rate = (currentTime - startTime) * enlargement / durtion;
        //这个比率不可能在 0 - 1 之间，放大了之后即是 0 - 1000 之间
        rate = Math.max(Math.min(rate, 1000),0);
        //将动画的进度通过插值器得出响应的比率，乘以起始与目标坐标得出当前这个瞬间，视图应该滚动的距离。
        int changeDistance = Math.round((fromY - toY) * mInterpolator.getInterpolation(rate / enlargement));
        int currentY = fromY - changeDistance;
        if (mListener!=null){
            mListener.onSmoothResultChange(currentY);
        }

        if (currentY != toY) {
            v.postDelayed(this, this.interval);
        }
        else {
            return;
        }
    }

    public void stop() {
        v.removeCallbacks(this);
        startTime=-1;
    }

    public OnSmoothResultChangeListener getOnSmoothResultChangeListener() {
        return mListener;
    }

    public void setOnSmoothResultChangeListener(OnSmoothResultChangeListener listener) {
        mListener = listener;
    }

    public interface OnSmoothResultChangeListener{
        void onSmoothResultChange(int result);
    }
}
