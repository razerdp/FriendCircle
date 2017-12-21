package razerdp.github.com.ui.util;

import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.utils.StringUtil;
import razerdp.github.com.ui.widget.span.UrlSpanEx;

/**
 * Created by 大灯泡 on 2017/12/21.
 * <p>
 * 多span样式util
 */
public class MultiSpanUtil {
    private MultiSpanUtil() {
    }

    public static MultiSpanOption create(@StringRes int source) {
        return create(StringUtil.getResourceString(source));
    }

    public static MultiSpanOption create(CharSequence source) {
        return new MultiSpanOption(source);
    }

    abstract static class BaseItemOption<T> {
        private MultiSpanOption mOption;
        private CharSequence keyWord;

        private BaseItemOption(@StringRes int keyWord, MultiSpanOption option) {
            this(StringUtil.getResourceString(keyWord), option);
        }

        private BaseItemOption(CharSequence keyWord, MultiSpanOption option) {
            this.mOption = option;
            this.keyWord = keyWord;
        }

        public T append(String keyWord) {
            return appendInternal(mOption, (T) this, keyWord);
        }

        abstract T appendInternal(MultiSpanOption mOption, T option, CharSequence keyWord);

        public String getKeyWord() {
            return keyWord.toString();
        }

        public SpannableStringBuilder getSpannableStringBuilder() {
            return getSpannableStringBuilder(-1);
        }

        public SpannableStringBuilder getSpannableStringBuilder(int defaultTextColor) {
            append(null);
            SpannableStringBuilder result = new SpannableStringBuilder(mOption.source);
            if (defaultTextColor != -1) {
                result.setSpan(new ForegroundColorSpan(defaultTextColor), 0, mOption.source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (TextUtils.isEmpty(mOption.source)) return result;
            final int sourceStrLen = mOption.source.length();
            final String source = mOption.sourceToString();

            for (ItemOption itemOption : mOption.mItemOptions) {
                if (itemOption.isMatchLast()) {
                    int index = source.lastIndexOf(itemOption.getKeyWord());

                    if (index < sourceStrLen && index >= 0) {
                        applySpan(result, itemOption.getKeyWord(), itemOption, index);
                    }
                } else {
                    int index = source.indexOf(itemOption.getKeyWord());
                    if (index >= 0) {
                        // 遍历关键字，当关键字没有的时候,跳出循环
                        while (index < sourceStrLen && index >= 0) {
                            applySpan(result, itemOption.getKeyWord(), itemOption, index);
                            index = source.indexOf(itemOption.getKeyWord(), index + itemOption.getKeyWord().length());
                        }
                    }
                }
            }
            return result;

        }

        public void into(TextView textView) {
            if (textView == null) return;
            textView.setText(getSpannableStringBuilder(textView.getCurrentTextColor()));
        }

        private void applySpan(SpannableStringBuilder spanBuilder, String keyWord, @NonNull ItemOption option, int index) {
            if (index <= -1 || spanBuilder == null || option == null) return;
            //color
            if (option.getTextColor() != -1) {
                spanBuilder.setSpan(new ForegroundColorSpan(option.getTextColor()), index, index + keyWord
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //texttype
            if (option.getTextType() != Typeface.DEFAULT) {
                spanBuilder.setSpan(new StyleSpan(option.getTextType().getStyle()), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //textsize
            if (option.getTextSize() != -1) {
                spanBuilder.setSpan(new AbsoluteSizeSpan(option.getTextSize()), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //underline
            if (option.isUnderLine()) {
                spanBuilder.setSpan(new UnderlineSpan(), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //urlSpan
            if (option.isUrl()) {
                spanBuilder.setSpan(new UrlSpanEx(keyWord, option.urlColor, option.onUrlClickListener), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
    }

    public static class MultiSpanOption {

        final CharSequence source;
        final List<ItemOption> mItemOptions;

        private MultiSpanOption(@StringRes int resid) {
            this(StringUtil.getResourceString(resid));
        }

        private MultiSpanOption(@StringRes int resid, Object... formats) {
            this(StringUtil.getResourceStringAndFormat(resid, formats));
        }

        private MultiSpanOption(CharSequence source) {
            this.source = source;
            mItemOptions = new ArrayList<>();
        }

        public ItemOption append(@StringRes int keyWord) {
            return appendInternal(null, StringUtil.getResourceString(keyWord));
        }

        public ItemOption append(String keyWord) {
            return appendInternal(null, keyWord);
        }

        ItemOption appendInternal(ItemOption option, CharSequence keyWord) {
            ItemOption result = new ItemOption(keyWord, this);
            if (option != null) {
                mItemOptions.add(option);
            }
            return result;
        }

        String sourceToString() {
            return source.toString();
        }

    }

    public static class ItemOption extends BaseItemOption<ItemOption> {
        private boolean matchLast = false;
        private int textSize = -1;
        private Typeface textType;
        private int textColor = -1;
        private boolean underLine = false;
        private boolean isUrl = false;
        private View.OnClickListener onUrlClickListener;
        private int urlColor = -1;


        public ItemOption(CharSequence keyWord, MultiSpanOption option) {
            super(keyWord, option);
        }

        @Override
        ItemOption appendInternal(MultiSpanOption mOption, ItemOption option, CharSequence keyWord) {
            return mOption.appendInternal(option, keyWord);
        }

        public boolean isMatchLast() {
            return matchLast;
        }

        public ItemOption matchLast(boolean matchLast) {
            this.matchLast = matchLast;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public ItemOption setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Typeface getTextType() {
            return textType;
        }

        public ItemOption setTextType(Typeface textType) {
            this.textType = textType;
            return this;
        }

        public int getTextColor() {
            return textColor;
        }

        public ItemOption setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public ItemOption setTextColorFromRes(@ColorRes int textColor) {
            return setTextColor(UIHelper.getResourceColor(textColor));
        }

        public boolean isUnderLine() {
            return underLine;
        }

        public ItemOption setUnderLine(boolean underLine) {
            this.underLine = underLine;
            return this;
        }

        public boolean isUrl() {
            return isUrl;
        }

        public ItemOption setUrl(boolean url) {
            return setUrl(url, null);
        }

        public ItemOption setUrl(boolean url, View.OnClickListener l) {
            return setUrl(url, -1, l);
        }

        public ItemOption setUrl(boolean url, int urlTextColor, View.OnClickListener l) {
            isUrl = url;
            urlColor = urlTextColor;
            onUrlClickListener = l;
            return this;
        }
    }


}
