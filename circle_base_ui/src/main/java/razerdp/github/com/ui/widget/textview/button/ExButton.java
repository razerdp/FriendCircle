package razerdp.github.com.ui.widget.textview.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.textview.FixedOvalShape;
import razerdp.github.com.ui.widget.textview.FixedRectShape;
import razerdp.github.com.ui.widget.textview.FixedRoundRectShape;
import razerdp.github.com.ui.widget.textview.Type;


/**
 * Created by 大灯泡 on 2018/3/8.
 * <p>
 * 普益投通用button，囊括大多数的场景
 */
public class ExButton extends android.support.v7.widget.AppCompatButton implements Type {
    private static final String TAG = "ExButton";

    int normalTextColor = Color.WHITE;
    int pressedTextColor = UIHelper.getColor(R.color.btn_disable);
    int disableTextColor = UIHelper.getColor(R.color.btn_disable);

    int normalBackgroundColor = UIHelper.getColor(R.color.common_black);
    int pressedBackgroundColor;
    int disableBackgroundColor = UIHelper.getColor(R.color.btn_disable);

    boolean strokeMode;
    int strokeWidth = UIHelper.dipToPx(0.5f);

    int radius = UIHelper.dipToPx(8);
    int topLeftRadius = 0;
    int topRightRadius = 0;
    int bottomLeftRadius = 0;
    int bottomRightRadius = 0;

    int drawableHorizontalReduce = 0;
    int drawableVerticalReduce = 0;

    private int type = NORMAL;


    public ExButton(Context context) {
        this(context, null);
    }

    public ExButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public ExButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExButton);
        Drawable background = null;
        background = a.getDrawable(R.styleable.ExButton_android_background);
        if (background != null) {
            type = NORMAL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(background);
            } else {
                setBackgroundDrawable(background);
            }
            if (a.getDimensionPixelSize(R.styleable.ExButton_android_lineSpacingExtra, 0) == 0 &&
                    a.getFloat(R.styleable.ExButton_android_lineSpacingMultiplier, 0) == 0) {
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
        type = a.getInt(R.styleable.ExButton_type, ExButton.RECTANGE);
        normalTextColor = a.getColor(R.styleable.ExButton_android_textColor, normalTextColor);
        pressedTextColor = a.getColor(R.styleable.ExButton_textPressedColor, normalTextColor);
        disableTextColor = a.getColor(R.styleable.ExButton_textDisableColor, disableTextColor);

        normalBackgroundColor = a.getColor(R.styleable.ExButton_backgroundColor, normalBackgroundColor);
        pressedBackgroundColor = a.getColor(R.styleable.ExButton_backgroundPressedColor, brightnessColor(normalBackgroundColor, 1.1f));
        disableBackgroundColor = a.getColor(R.styleable.ExButton_backgroundDisableColor, disableBackgroundColor);

        drawableHorizontalReduce = a.getDimensionPixelOffset(R.styleable.ExButton_drawable_horizontal_reduce, drawableHorizontalReduce);
        drawableVerticalReduce = a.getDimensionPixelOffset(R.styleable.ExButton_drawable_vertical_reduce, drawableVerticalReduce);

        strokeMode = a.getBoolean(R.styleable.ExButton_strokeMode, false);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ExButton_stroke_Width, strokeWidth);

        radius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_radius, radius);
        topLeftRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_topLeftRadius, topLeftRadius);
        topRightRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_topRightRadius, topRightRadius);
        bottomLeftRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = a.getDimensionPixelOffset(R.styleable.ExButton_corner_bottomRightRadius, bottomRightRadius);
        if (a.getDimensionPixelSize(R.styleable.ExButton_android_lineSpacingExtra, 0) == 0 &&
                a.getFloat(R.styleable.ExButton_android_lineSpacingMultiplier, 0) == 0) {
            setLineSpacing(UIHelper.dipToPx(1.2f), 1.2f);
        }
        apply();

    }


    private int brightnessColor(int color, @FloatRange(from = 0f) float brightness) {
        float[] hslArray = new float[3];

        ColorUtils.colorToHSL(color, hslArray);
        hslArray[2] = hslArray[2] * brightness;

        return ColorUtils.HSLToColor(hslArray);
    }


    public void apply() {
        Shape shape = null;
        switch (type) {
            case ExButton.NORMAL:
            case ExButton.RECTANGE:
                shape = new FixedRectShape(strokeWidth, drawableHorizontalReduce, drawableVerticalReduce);
                break;
            case ExButton.ROUND:
                shape = new FixedOvalShape(strokeWidth, drawableHorizontalReduce, drawableVerticalReduce);
                break;
            case ExButton.ROUND_RECTANGE:
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
                shape = new FixedRoundRectShape(outerRect, strokeMode ? strokeWidth : 0, drawableHorizontalReduce, drawableVerticalReduce);
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

    @Override
    public void setTextColor(int color) {
        setNormalTextColor(color);
        super.setTextColor(color);
    }
}
