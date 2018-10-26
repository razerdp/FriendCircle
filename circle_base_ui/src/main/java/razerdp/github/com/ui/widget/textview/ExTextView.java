package razerdp.github.com.ui.widget.textview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.v4.graphics.ColorUtils;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.span.SpannableStringBuilderCompat;

/**
 * Created by 大灯泡 on 2018/3/30.
 * <p>
 * 通用Textview
 */
public class ExTextView extends android.support.v7.widget.AppCompatTextView implements Type {
    private static final String TAG = "ExTextView";

    int normalTextColor = UIHelper.getColor(R.color.common_black);
    int pressedTextColor = normalTextColor;
    int disableTextColor = Color.GRAY;

    int normalBackgroundColor = Color.TRANSPARENT;
    int pressedBackgroundColor;
    int disableBackgroundColor = Color.TRANSPARENT;

    boolean strokeMode;
    int strokeWidth = UIHelper.dipToPx(0.5f);

    int radius = 0;
    int topLeftRadius = radius;
    int topRightRadius = radius;
    int bottomLeftRadius = radius;
    int bottomRightRadius = radius;

    private int type = Type.NORMAL;
    private boolean urlRegion;
    private OnUrlClickListener mOnUrlClickListener;


    //跳跃动画
    private int pointsLoopDuration = 1200;
    private int loadingPointsLength = 3;

    /*
     * 正则文本
     * ((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?|(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?
     * */
    private String pattern =
            "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?|(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    // 创建 Pattern 对象
    Pattern r = Pattern.compile(pattern);
    // 现在创建 matcher 对象
    Matcher m;
    //记录网址的list
    LinkedList<String> mStringList;
    //记录该网址所在位置的list
    LinkedList<UrlInfo> mUrlInfos;
    int flag = Spanned.SPAN_POINT_MARK;

    //跳跃动画
    private SpannableStringBuilder loadingBuilder;


    public ExTextView(Context context) {
        this(context, null);
    }

    public ExTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ExTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExTextView);
        Drawable background = null;
        background = a.getDrawable(R.styleable.ExTextView_android_background);
        if (background != null) {
            type = Type.NORMAL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(background);
            } else {
                setBackgroundDrawable(background);
            }
            if (a.getDimensionPixelSize(R.styleable.ExTextView_android_lineSpacingExtra, 0) == 0 &&
                    a.getFloat(R.styleable.ExTextView_android_lineSpacingMultiplier, 0) == 0) {
                setLineSpacing(UIHelper.dipToPx(1.2f), 1.2f);
            }
            a.recycle();
            return;
        } else {
            applyAttrs(context, a);
        }
        a.recycle();

    }

    void applyAttrs(Context context, TypedArray a) {
        type = a.getInt(R.styleable.ExTextView_type, Type.RECTANGE);
        normalTextColor = a.getColor(R.styleable.ExTextView_android_textColor, normalTextColor);
        pressedTextColor = a.getColor(R.styleable.ExTextView_textPressedColor, normalTextColor);
        disableTextColor = a.getColor(R.styleable.ExTextView_textDisableColor, disableTextColor);

        normalBackgroundColor = a.getColor(R.styleable.ExTextView_backgroundColor, normalBackgroundColor);
        pressedBackgroundColor = a.getColor(R.styleable.ExTextView_backgroundPressedColor, brightnessColor(normalBackgroundColor, 1.1f));
        disableBackgroundColor = a.getColor(R.styleable.ExTextView_backgroundDisableColor, disableBackgroundColor);

        strokeMode = a.getBoolean(R.styleable.ExTextView_strokeMode, false);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ExTextView_stroke_Width, strokeWidth);

        radius = a.getDimensionPixelOffset(R.styleable.ExTextView_corner_radius, radius);
        topLeftRadius = a.getDimensionPixelOffset(R.styleable.ExTextView_corner_topLeftRadius, topLeftRadius);
        topRightRadius = a.getDimensionPixelOffset(R.styleable.ExTextView_corner_topRightRadius, topRightRadius);
        bottomLeftRadius = a.getDimensionPixelOffset(R.styleable.ExTextView_corner_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = a.getDimensionPixelOffset(R.styleable.ExTextView_corner_bottomRightRadius, bottomRightRadius);

        urlRegion = a.getBoolean(R.styleable.ExTextView_urlRegion, false);
        if (a.getDimensionPixelSize(R.styleable.ExTextView_android_lineSpacingExtra, 0) == 0 &&
                a.getFloat(R.styleable.ExTextView_android_lineSpacingMultiplier, 0) == 0) {
            setLineSpacing(UIHelper.dipToPx(1.2f), 1.2f);
        }
        apply();

    }


    private int brightnessColor(int color, @FloatRange(from = 0f) float brightness) {
        if (color == Color.TRANSPARENT) return color;
        float[] hslArray = new float[3];

        ColorUtils.colorToHSL(color, hslArray);
        hslArray[2] = hslArray[2] * brightness;

        return ColorUtils.HSLToColor(hslArray);
    }


    public void apply() {
        Shape shape = null;
        switch (type) {
            case Type.NORMAL:
            case Type.RECTANGE:
                shape = new RectShape();
                break;
            case Type.ROUND:
                shape = new OvalShape();
                break;
            case Type.ROUND_RECTANGE:
                float[] outerRect;
                if (radius != 0) {
                    outerRect = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
                } else {
                    outerRect = new float[]{topLeftRadius,
                            topLeftRadius,
                            topRightRadius,
                            topRightRadius,
                            bottomLeftRadius,
                            bottomLeftRadius,
                            bottomRightRadius,
                            bottomRightRadius};
                }
                shape = new FixedRoundRectShape(outerRect, strokeMode ? strokeWidth : 0, 0, 0);
                break;
        }

        ShapeDrawable normalDrawable = new ShapeDrawable(shape);
        ShapeDrawable pressedDrawable = new ShapeDrawable(shape);
        ShapeDrawable disableDrawable = new ShapeDrawable(shape);

        applyPaint(normalDrawable, normalBackgroundColor);
        applyPaint(pressedDrawable, pressedBackgroundColor);
        applyPaint(disableDrawable, disableBackgroundColor);


        StateListDrawable backgroundStateListDrawable = new StateListDrawable();

        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        backgroundStateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disableDrawable);
        backgroundStateListDrawable.addState(new int[]{}, normalDrawable);

        int[][] textColorState = new int[4][];
        textColorState[0] = new int[]{android.R.attr.state_pressed};
        textColorState[1] = new int[]{android.R.attr.state_focused};
        textColorState[2] = new int[]{-android.R.attr.state_enabled};
        textColorState[3] = new int[]{};

        int[] textColors = {pressedTextColor, pressedTextColor, disableTextColor, normalTextColor};


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backgroundStateListDrawable);
        } else {
            setBackgroundDrawable(backgroundStateListDrawable);
        }
        setTextColor(new ColorStateList(textColorState, textColors));
    }

    private void applyPaint(ShapeDrawable drawable, int color) {
        if (drawable == null || drawable.getPaint() == null) return;
        Paint paint = drawable.getPaint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(color);
        if (strokeMode) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
        } else {
            paint.setStyle(Paint.Style.FILL);
        }
        applyPadding(drawable);
    }

    private void applyPadding(ShapeDrawable drawable) {
        if (drawable == null) return;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        if (type == Type.ROUND_RECTANGE) {
            paddingLeft += strokeWidth / 2;
            paddingTop += strokeWidth / 2;
            paddingRight += strokeWidth / 2;
            paddingBottom += strokeWidth / 2;
        }
        drawable.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (urlRegion) {
            text = recognUrl(text);
            this.setMovementMethod(LinkMovementMethod.getInstance());
        }
        super.setText(text, type);
    }

    private SpannableStringBuilderCompat recognUrl(CharSequence text) {
        mStringList.clear();
        mUrlInfos.clear();

        CharSequence contextText;
        CharSequence clickText;
        text = text == null ? "" : text;
        //以下用于拼接本来存在的spanText
        SpannableStringBuilderCompat span = new SpannableStringBuilderCompat(text);
        ClickableSpan[] clickableSpans = span.getSpans(0, text.length(), ClickableSpan.class);
        if (clickableSpans.length > 0) {
            int start = 0;
            int end = 0;
            for (int i = 0; i < clickableSpans.length; i++) {
                start = span.getSpanStart(clickableSpans[0]);
                end = span.getSpanEnd(clickableSpans[i]);
            }
            //可点击文本后面的内容页
            contextText = text.subSequence(end, text.length());
            //可点击文本
            clickText = text.subSequence(start,
                    end);
        } else {
            contextText = text;
            clickText = null;
        }
        m = r.matcher(contextText);
        //匹配成功
        while (m.find()) {
            //得到网址数
            UrlInfo info = new UrlInfo();
            info.start = m.start();
            info.end = m.end();
            mStringList.add(m.group());
            mUrlInfos.add(info);
        }
        return jointText(clickText, contextText);
    }

    /**
     * 拼接文本
     */
    private SpannableStringBuilderCompat jointText(CharSequence clickSpanText,
                                                   CharSequence contentText) {
        SpannableStringBuilderCompat spanBuilder;
        if (clickSpanText != null) {
            spanBuilder = new SpannableStringBuilderCompat(clickSpanText);
        } else {
            spanBuilder = new SpannableStringBuilderCompat();
        }
        if (mStringList.size() > 0) {
            //只有一个网址
            if (mStringList.size() == 1) {
                String preStr = contentText.toString().substring(0, mUrlInfos.get(0).start);
                spanBuilder.append(preStr);
                String url = mStringList.get(0);
                spanBuilder.append(url, new URLClick(url), flag);
                String nextStr = contentText.toString().substring(mUrlInfos.get(0).end);
                spanBuilder.append(nextStr);
            } else {
                //有多个网址
                for (int i = 0; i < mStringList.size(); i++) {
                    if (i == 0) {
                        //拼接第1个span的前面文本
                        String headStr =
                                contentText.toString().substring(0, mUrlInfos.get(0).start);
                        spanBuilder.append(headStr);
                    }
                    if (i == mStringList.size() - 1) {
                        //拼接最后一个span的后面的文本
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)),
                                flag);
                        String footStr = contentText.toString().substring(mUrlInfos.get(i).end);
                        spanBuilder.append(footStr);
                    }
                    if (i != mStringList.size() - 1) {
                        //拼接两两span之间的文本
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)), flag);
                        String betweenStr = contentText.toString()
                                .substring(mUrlInfos.get(i).end,
                                        mUrlInfos.get(i + 1).start);
                        spanBuilder.append(betweenStr);
                    }
                }
            }
        } else {
            spanBuilder.append(contentText);
        }

        return spanBuilder;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
    }

    public int getDisableTextColor() {
        return disableTextColor;
    }

    public void setDisableTextColor(int disableTextColor) {
        this.disableTextColor = disableTextColor;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public void setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        pressedBackgroundColor = brightnessColor(normalBackgroundColor, 1.1f);
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(int pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public int getDisableBackgroundColor() {
        return disableBackgroundColor;
    }

    public void setDisableBackgroundColor(int disableBackgroundColor) {
        this.disableBackgroundColor = disableBackgroundColor;
    }

    public boolean isStrokeMode() {
        return strokeMode;
    }

    public void setStrokeMode(boolean strokeMode) {
        this.strokeMode = strokeMode;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public void setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public void setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public void setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }

    public void setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isUrlRegion() {
        return urlRegion;
    }

    public void setUrlRegion(boolean urlRegion) {
        this.urlRegion = urlRegion;
    }

    @Override
    public void setTextColor(int color) {
        setNormalTextColor(color);
        super.setTextColor(color);
    }

    private static class UrlInfo {
        public int start;
        public int end;
    }

    private class URLClick extends ClickableSpan {
        private String text;

        public URLClick(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View widget) {
            if (mOnUrlClickListener != null) {
                if (mOnUrlClickListener.onUrlClickListener(text)) return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(text);
            intent.setData(content_url);
            getContext().startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(0xff517fae);
            ds.setUnderlineText(false);
        }
    }

    public interface OnUrlClickListener {
        boolean onUrlClickListener(String url);
    }


    public void setLoadingText() {
        setLoadingText("...");
    }

    public void setLoadingText(CharSequence text) {
        setLoadingText(text, 1500);
    }

    public void setLoadingText(CharSequence text, int loopDuration) {
        if (loadingBuilder == null) {
            loadingBuilder = new SpannableStringBuilder(text);
        }
        loadingBuilder.clear();
        loadingBuilder.clearSpans();
        loadingBuilder.append(text);
        final int length = loadingBuilder.length();
        //延迟
        int delay = (int) ((loopDuration / length) * 0.5);
        for (int i = 0; i < length; i++) {
            loadingBuilder.setSpan(new LoadingPointSpan(delay * i, loopDuration, 0.4f),
                    i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(loadingBuilder);
    }


    /**
     * textview小点点span，内封动画
     */
    class LoadingPointSpan extends SuperscriptSpan {
        private int delay;
        private int duration;
        private float maxOffsetRatio;//最高弹跳高度百分比
        private ValueAnimator mValueAnimator;
        private int curOffset;

        private boolean reCreateAnimate;

        public LoadingPointSpan() {
        }

        public LoadingPointSpan(int delay, int duration, float maxOffsetRatio) {
            this.delay = delay;
            this.duration = duration;
            this.maxOffsetRatio = maxOffsetRatio;
        }


        public int getDelay() {
            return delay;
        }

        public LoadingPointSpan setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public int getDuration() {
            return duration;
        }

        public LoadingPointSpan setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public float getMaxOffsetRatio() {
            return maxOffsetRatio;
        }

        public LoadingPointSpan setMaxOffsetRatio(float maxOffsetRatio) {
            this.maxOffsetRatio = maxOffsetRatio;
            return this;
        }


        /**
         * 更改baseline,.来改变位置
         *
         * @param tp
         */
        @Override
        public void updateDrawState(TextPaint tp) {
            initAnimate(tp.ascent());
            tp.baselineShift = curOffset;
        }


        /**
         * @param textAscent 文字高度
         */
        private void initAnimate(float textAscent) {
            if (!reCreateAnimate && mValueAnimator != null) return;
            if (mValueAnimator != null) mValueAnimator.cancel();
            curOffset = 0;
            maxOffsetRatio = Math.max(0, Math.min(1.0f, maxOffsetRatio));
            int maxOffset = (int) (textAscent * maxOffsetRatio);
            mValueAnimator = ValueAnimator.ofInt(0, maxOffset);
            mValueAnimator.setDuration(duration);
            mValueAnimator.setStartDelay(delay);
            mValueAnimator.setInterpolator(new PointInterpolator(maxOffsetRatio));
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    curOffset = (int) animation.getAnimatedValue();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (isAttachedToWindow()) {
                            invalidate();
                        }
                    } else {
                        if (getParent() != null) {
                            invalidate();
                        }
                    }
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    curOffset = 0;
                }
            });
            mValueAnimator.start();
        }

        public void stop() {
            Log.i(TAG, "stop: ");
            if (mValueAnimator != null) {
                mValueAnimator.cancel();
                mValueAnimator.removeAllUpdateListeners();
                mValueAnimator.removeAllListeners();
            }
            mValueAnimator = null;
        }

        /**
         * 时间限制在0~maxOffsetRatio
         */
        private class PointInterpolator implements TimeInterpolator {

            private final float maxOffsetRatio;

            public PointInterpolator(float animatedRange) {
                maxOffsetRatio = Math.abs(animatedRange);
            }

            @Override
            public float getInterpolation(float input) {
                if (input > maxOffsetRatio) {
                    return 0f;
                }
                double radians = (input / maxOffsetRatio) * Math.PI;
                return (float) Math.sin(radians);
            }

        }
    }

}
