package razerdp.friendcircle.widget.ptrwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import razerdp.friendcircle.R;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 下拉刷新的头头
 */
public class FriendCirclePtrFooter extends RelativeLayout{
    private static final String TAG = "FriendCirclePtrHeader";

    private ImageView mRotateIcon;
    private View rootView;

    public FriendCirclePtrFooter(Context context) {
        this(context,null);
    }

    public FriendCirclePtrFooter(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FriendCirclePtrFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FriendCirclePtrFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context) {
        rootView= LayoutInflater.from(context).inflate(R.layout.widget_ptr_header,this,false);
        mRotateIcon= (ImageView) rootView.findViewById(R.id.rotate_icon);
    }

    //=============================================================ptr:
    private PtrUIHandler mPtrUIHandler=new PtrUIHandler() {
        /**回到初始位置*/
        @Override
        public void onUIReset(PtrFrameLayout frame) {
            mRotateIcon.setRotation(0);

        }

        /**离开初始位置*/
        @Override
        public void onUIRefreshPrepare(PtrFrameLayout frame) {

        }

        /**开始刷新动画*/
        @Override
        public void onUIRefreshBegin(PtrFrameLayout frame) {
            RotateAnimation rotateAnimation = new RotateAnimation(mRotateIcon.getRotation(), 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(600);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            if (mRotateIcon.getAnimation()!=null){
                mRotateIcon.clearAnimation();
            }
            mRotateIcon.startAnimation(rotateAnimation);

        }

        /**刷新完成*/
        @Override
        public void onUIRefreshComplete(PtrFrameLayout frame) {

        }

        /**位移更新重载*/
        @Override
        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
            final int mOffsetToRefresh = frame.getOffsetToRefresh();
            final int currentPos = ptrIndicator.getCurrentPosY();
            final int lastPos = ptrIndicator.getLastPosY();

            if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
                //未到达刷新线
                if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                    mRotateIcon.setRotation(currentPos);
                }
            } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
                //到达或超过刷新线
                if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                    mRotateIcon.setRotation(currentPos);
                }
            }

        }
    };
}
