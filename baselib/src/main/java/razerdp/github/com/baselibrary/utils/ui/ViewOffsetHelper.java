
package razerdp.github.com.baselibrary.utils.ui;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by 大灯泡 on 2017/02/28.
 * <p>
 * view偏移帮助类
 */
 public class ViewOffsetHelper {

    private final View mTarget;

    private int mOffsetTop;
    private int mOffsetLeft;

    public ViewOffsetHelper(View view) {
        mTarget = view;
    }

    /**
     * 不可见的情况下就没必要做offset了
     *
     * @return
     */
    private boolean checkHasTarget() {
        return mTarget != null && mTarget.getVisibility() == View.VISIBLE;
    }

    /**
     * offset绝对位置，其实就是记录上一次的值进行相减
     *
     * @param absoluteOffset
     * @return
     */
    public boolean absoluteOffsetTopAndBottom(int absoluteOffset) {
        if (checkHasTarget()) {
            if (mOffsetTop != absoluteOffset) {
                mOffsetTop = absoluteOffset;
                updateOffsets(true);
                return true;
            }
        }
        return false;
    }

    public void offsetTopAndBottom(int relativeOffset) {
        if (checkHasTarget()) {
            mOffsetTop += relativeOffset;
            updateOffsets(true);
        }
    }

    public boolean absoluteOffsetLeftAndRight(int absoluteOffset) {
        if (checkHasTarget()) {
            if (mOffsetLeft != absoluteOffset) {
                mOffsetLeft = absoluteOffset;
                updateOffsets(false);
                return true;
            }
        }
        return false;
    }

    public void offsetLeftAndRight(int relativeOffset) {
        if (checkHasTarget()) {
            mOffsetLeft += relativeOffset;
            updateOffsets(false);
        }
    }

    public int getTopAndBottomOffset() {
        return mOffsetTop;
    }

    public int getLeftAndRightOffset() {
        return mOffsetLeft;
    }


    private void updateOffsets(boolean isTopAndBottom) {
        if (isTopAndBottom) {
            ViewCompat.offsetTopAndBottom(mTarget, mOffsetTop - mTarget.getTop());
        }else {
            ViewCompat.offsetLeftAndRight(mTarget, mOffsetLeft - mTarget.getLeft());
        }
    }

}
