package razerdp.friendcircle.widget.commentwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import razerdp.friendcircle.api.data.model.CommentInfo;
import razerdp.friendcircle.widget.SpannableStringBuilderAllVer;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论控件
 */
public class CommentWidget extends TextView {
    private static final String TAG = "CommentWidget";
    //用户名颜色
    private int textColor = 0xff517fae;
    private static final int textSize = 14;

    private int key;
    private SpannableStringBuilderAllVer mSpannableStringBuilderAllVer;

    public CommentWidget(Context context) {
        this(context, null);
    }

    public CommentWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
    }

    public void setCommentText(CommentInfo info) {
        if (info == null) return;
        boolean hasContent = false;
        //根据hashCode判断内容是否一致
        if (key == 0) {
            key = info.hashCode();
        }
        else {
            hasContent = (key == info.hashCode());
        }
        if (!hasContent) {
            key = info.hashCode();
            setText("");
            setTag(info);
            createCommentStringBuilder(info);
        }
        else {
            try {
                setText(mSpannableStringBuilderAllVer);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e(TAG, "虽然在下觉得不可能会有这个情况，但还是捕捉下吧，万一被打脸呢。。。");
            }
        }
    }

    private void createCommentStringBuilder(@NonNull CommentInfo info) {
        String content = ": " + info.content + "\0";
        if (mSpannableStringBuilderAllVer == null) {
            mSpannableStringBuilderAllVer = new SpannableStringBuilderAllVer();
            boolean isApply = (info.userB == null);
            // 用户B为空，证明是一条原创评论
            if (info.userA != null && isApply) {
                CommentClick userA = new CommentClick.Builder(getContext(), info.userA).build();
                mSpannableStringBuilderAllVer.append(info.userA.nick, userA, 0);
                mSpannableStringBuilderAllVer.append(content);
            }
            else if (info.userA != null && !isApply) {
                //用户A，B不空，证明是回复评论
                CommentClick userA = new CommentClick.Builder(getContext(), info.userA).build();
                mSpannableStringBuilderAllVer.append(info.userA.nick, userA, 0);
                mSpannableStringBuilderAllVer.append("回复");
                CommentClick userB = new CommentClick.Builder(getContext(), info.userA).build();
                mSpannableStringBuilderAllVer.append(info.userA.nick, userB, 0);
                mSpannableStringBuilderAllVer.append(content);
            }
        }
        setText(mSpannableStringBuilderAllVer);
    }

    public CommentInfo getData() throws ClassCastException {
        return (CommentInfo) getTag();
    }
}
