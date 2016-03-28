/*
* This class comes from Github:
* https://github.com/LiesSu/AndroidExtenders/blob/master/app/src/main/java/com/liessu/extender/span/ClickableSpanEx.java
*
* @author:LiesSu
*
* Thanks for his help :)
*
*
* */
package razerdp.friendcircle.widget.span;

import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * A extensional class for ClickableSpan .
 *
 * ex:
 * new ClickableSpanEx(Color.BLUE,Color.GRAY) {
 * public void onClick(View widget) {
 * Toast.makeText(DemoApplication.getContext(),name,Toast.LENGTH_SHORT).show();
 * }
 * }
 * <p/>
 * Note : If you need to  indicator click event of  ClickableSpanEx  , you must set the TextView OnTouchListener
 * like this:
 * textView.setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
 * or
 * call {@link #onTouch(View, MotionEvent)} in your onTouch method
 */
public abstract class ClickableSpanEx extends ClickableSpan {
    private static final String TAG = "ClickableSpanEx";

    /**
     * foreground color , protected member
     **/
    protected int mForegroundColor = Color.BLUE;
    /**
     * background color  , protected member
     **/
    protected int mBackgroundColor = Color.TRANSPARENT;
    /**
     * Determine whether the background of ClickableSpanEx transparent
     **/
    protected boolean isBackgroundTransparent = true;

    public ClickableSpanEx() {}

    /**
     * New ClickableSpanEx instance .
     *
     * @param foregroundColor foreground color
     */
    public ClickableSpanEx(int foregroundColor) {
        this(foregroundColor, Color.TRANSPARENT);
    }

    /**
     * New ClickableSpanEx instance .
     *
     * @param foregroundColor foreground color
     * @param backgroundColor backgroundColor color
     */
    public ClickableSpanEx(int foregroundColor, int backgroundColor) {
        mForegroundColor = foregroundColor;
        mBackgroundColor = backgroundColor;
    }

    /**
     * If you implement OnTouchListener, call this one in the onTouch method .
     *
     * @param v The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about the event.
     * @return True if the listener has consumed the event , false otherwise.
     */
    public static boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (v instanceof TextView) {
            TextView widget = (TextView) v;
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                //* Return the text the TextView is displaying. If TextView.setText() was called with
                // * an argument of BufferType.SPANNABLE or BufferType.EDITABLE, you can cast
                // * the return value from this method to Spannable or Editable, respectively.
                // *
                //* Note: The content of the return value should not be modified. If you want
                //* a modifiable one, you should make your own copy first.
                Spannable buffer = (Spannable) widget.getText();
                ClickableSpanEx[] link = buffer.getSpans(off, off, ClickableSpanEx.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].setTransparent(true);
                        link[0].onClick(widget);
                        Selection.removeSelection(buffer);
                    }
                    else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                        link[0].setTransparent(false);
                    }else {
                        Selection.removeSelection(buffer);
                        link[0].setTransparent(true);
                    }
                    return true;
                }
                else {
                    Selection.removeSelection(buffer);
                }
            }
        else {
            Log.e(TAG, "ClickableSpanEx supports TextView only .");
        }
        return false;
    }

    /**
     * Determine whether the background of ClickableSpanEx transparent .
     *
     * @param isTransparent disable background color if true
     */
    public void setTransparent(boolean isTransparent) {
        this.isBackgroundTransparent = isTransparent;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mForegroundColor);
        ds.setUnderlineText(false);
        ds.bgColor = isBackgroundTransparent ? Color.TRANSPARENT : mBackgroundColor;
    }

    /**
     * A inner static  class implements OnTouchListener interface . You can use it to indicator click
     * event of  ClickableSpanEx .
     */
    public static class ClickableSpanSelector implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return ClickableSpanEx.onTouch(v, event);
        }
    }
}
