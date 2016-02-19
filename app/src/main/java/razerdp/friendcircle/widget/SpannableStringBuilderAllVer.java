package razerdp.friendcircle.widget;

import android.text.SpannableStringBuilder;

/**
 * Created by 大灯泡 on 2016/2/19.
 */
public class SpannableStringBuilderAllVer extends SpannableStringBuilder {
    public SpannableStringBuilderAllVer() {
        super("");
    }

    public SpannableStringBuilderAllVer(CharSequence text) {
        super(text, 0, text.length());
    }

    public SpannableStringBuilderAllVer(CharSequence text, int start, int end) {
        super(text, start, end);
    }

    public SpannableStringBuilderAllVer append(CharSequence text) {
        if (text == null) return this;
        int length = length();
        return (SpannableStringBuilderAllVer) replace(length, length, text, 0, text.length());
    }

    /** 该方法在原API里面只支持API21或者以上，这里抽取出来以适应低版本 */
    public SpannableStringBuilderAllVer append(CharSequence text, Object what, int flags) {
        if (text == null) return this;
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }
}
