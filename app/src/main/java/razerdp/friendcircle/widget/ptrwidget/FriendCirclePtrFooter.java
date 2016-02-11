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
import android.widget.TextView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import razerdp.friendcircle.R;

/**
 * Created by 大灯泡 on 2016/2/9.
 * 加载更多布局
 */
public class FriendCirclePtrFooter extends RelativeLayout {
    private static final String TAG = "FriendCirclePtrFooter";

    private static final String LOAD_MORE = "加载更多";
    private static final String LOADING = "正在加载";
    private static final String LOAD_ALL = "已经全部加载";


    private ImageView mRotateIcon;
    private View rootView;
    private TextView loadingText;
    private RotateAnimation rotateAnimation;

    public FriendCirclePtrFooter(Context context) {
        this(context, null);
    }

    public FriendCirclePtrFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_ptr_footer, this, false);
        mRotateIcon = (ImageView) rootView.findViewById(R.id.friend_footer_loading);
        loadingText = (TextView) rootView.findViewById(R.id.friend_footer_load_state);
        addView(rootView);
        loadingText.setText(LOAD_MORE);

        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(600);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("不可以添加多个view噢");
        }
    }

    //=============================================================ptr:
    private PtrUIHandler mPtrUIHandler = new PtrUIHandler() {
        /**回到初始位置*/
        @Override
        public void onUIReset(PtrFrameLayout frame) {
            mRotateIcon.clearAnimation();
            loadingText.setText(LOAD_MORE);
        }

        /**离开初始位置*/
        @Override
        public void onUIRefreshPrepare(PtrFrameLayout frame) {

        }

        /**开始刷新动画*/
        @Override
        public void onUIRefreshBegin(PtrFrameLayout frame) {
            loadingText.setText(LOADING);
            if (mRotateIcon.getAnimation() != null) {
                mRotateIcon.clearAnimation();
            }
            mRotateIcon.startAnimation(rotateAnimation);
        }

        /**刷新完成*/
        @Override
        public void onUIRefreshComplete(PtrFrameLayout frame) {
            mRotateIcon.clearAnimation();
            loadingText.setText(LOAD_MORE);
        }

        /**位移更新重载*/
        @Override
        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        }
    };

    //=============================================================Getter/Setter

    public PtrUIHandler getPtrUIHandler() {
        return mPtrUIHandler;
    }

    public void setPtrUIHandler(PtrUIHandler ptrUIHandler) {
        mPtrUIHandler = ptrUIHandler;
    }

    public void setHasMore(boolean loadMore){
        if (loadMore){
            mRotateIcon.clearAnimation();
            mRotateIcon.setVisibility(VISIBLE);
        }else {
            mRotateIcon.clearAnimation();
            mRotateIcon.setVisibility(GONE);
            loadingText.setText(LOAD_ALL);
        }
    }
}
