package razerdp.friendcircle.widget.praisewidget;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by 大灯泡 on 2016/3/26.
 * 针对点赞显示列表控件的点击事件
 */

/*
* SOF:
* http://stackoverflow.com/questions/16792963/android-clickablespan-intercepts-the-click-event
* */
public class PraiseMovementMethod extends LinkMovementMethod {
    private static PraiseMovementMethod sInstance;
    private int color;
    private int defaultBgColor;

    private PraiseMovementMethod(int color, int defaultBgColor) {
        this.color = color;
        this.defaultBgColor = defaultBgColor;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            PraiseClick[] link = buffer.getSpans(off, off, PraiseClick.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].setClickBackgroundColor(defaultBgColor);
                    link[0].onClick(widget);
                }
                else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    link[0].setClickBackgroundColor(color);
                }
                else {
                    link[0].setClickBackgroundColor(defaultBgColor);
                }

                return true;
            }
            else {
                Selection.removeSelection(buffer);
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static PraiseMovementMethod getInstance(int color, int defaultBackgroungColor) {
        if (sInstance == null) {
            sInstance = new PraiseMovementMethod(color, defaultBackgroungColor);
        }
        return sInstance;
    }
}
