/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package razerdp.github.com.ui.widget.imageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import razerdp.github.com.baseuilib.R;


public class RoundedImageView extends ImageView {

    public static final String TAG = "RoundedImageView";

    public static final int DEFAULT_RADIUS = 0;
    public static final int DEFAULT_BORDER = 0;

    private int mCornerRadius;
    private int mBorderWidth;
    private ColorStateList mBorderColor;

    private boolean mRoundBackground = false;
    private boolean mOval = false;

    private Drawable mDrawable;
    private Drawable mBackgroundDrawable;

    private ScaleType mScaleType;
    private boolean mIsNoBottomRadius = false;

    private static final ScaleType[] sScaleTypeArray = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    public RoundedImageView(Context context) {
        super(context);
        mCornerRadius = DEFAULT_RADIUS;
        mBorderWidth = DEFAULT_BORDER;
        mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        refreshDrawableState();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }

        mCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);

        // don't allow negative values for radius and border
        if (mCornerRadius < 0) {
            mCornerRadius = DEFAULT_RADIUS;
        }
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER;
        }

        mBorderColor = a.getColorStateList(R.styleable.RoundedImageView_border_color);
        if (mBorderColor == null) {
            mBorderColor = ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        }

        mRoundBackground = a.getBoolean(R.styleable.RoundedImageView_round_background, false);
        mOval = a.getBoolean(R.styleable.RoundedImageView_is_oval, false);
        mIsNoBottomRadius = !(a.getBoolean(R.styleable.RoundedImageView_bottom_round,true));

        if (mDrawable instanceof RoundedDrawable) {
            updateDrawableAttrs((RoundedDrawable) mDrawable);
        }

        if (mRoundBackground) {
            if (!(mBackgroundDrawable instanceof RoundedDrawable)) {
                setBackgroundDrawable(mBackgroundDrawable);
            }
            if (mBackgroundDrawable instanceof RoundedDrawable) {
                updateDrawableAttrs((RoundedDrawable) mBackgroundDrawable);
            }
        }
        refreshDrawableState();
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    /**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     *
     * @param scaleType The desired scaling mode.
     * @attr ref android.R.styleable#ImageView_scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            if (mDrawable instanceof RoundedDrawable
                    && ((RoundedDrawable) mDrawable).getScaleType() != scaleType) {
                ((RoundedDrawable) mDrawable).setScaleType(scaleType);
            }

            if (mBackgroundDrawable instanceof RoundedDrawable
                    && ((RoundedDrawable) mBackgroundDrawable).getScaleType() != scaleType) {
                ((RoundedDrawable) mBackgroundDrawable).setScaleType(scaleType);
            }
            setWillNotCacheDrawing(true);
            requestLayout();
            invalidate();
        }
    }

    /**
     * Return the current scale type in use by this ImageView.
     *
     * @attr ref android.R.styleable#ImageView_scaleType
     * @see ScaleType
     */
    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            mDrawable = RoundedDrawable.fromDrawable(drawable, mCornerRadius, mBorderWidth, mBorderColor, mOval);
            updateDrawableAttrs((RoundedDrawable) mDrawable);
        } else {
            mDrawable = null;
        }
        super.setImageDrawable(mDrawable);
    }
   
    @Override
	public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
        	mDrawable = new RoundedDrawable(bm, mCornerRadius, mBorderWidth, mBorderColor, mOval);
        	updateDrawableAttrs((RoundedDrawable) mDrawable);
        } else {
            mDrawable = null;
        }
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    private void updateDrawableAttrs(RoundedDrawable drawable) {
        drawable.setScaleType(mScaleType);
        drawable.setCornerRadius(mCornerRadius);
        drawable.setBorderWidth(mBorderWidth);
        drawable.setBorderColors(mBorderColor);
        drawable.setOval(mOval);
        drawable.setmIsNoBottomRadius(mIsNoBottomRadius);
    }

    @Override
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        if (mRoundBackground && background != null) {
            mBackgroundDrawable = RoundedDrawable.fromDrawable(background, mCornerRadius, mBorderWidth, mBorderColor, mOval);
            updateDrawableAttrs((RoundedDrawable) mBackgroundDrawable);
        } else {
            mBackgroundDrawable = background;
        }
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public int getBorder() {
        return mBorderWidth;
    }

    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public void setCornerRadius(int radius) {
        if (mCornerRadius == radius) {
            return;
        }

        mCornerRadius = radius;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setCornerRadius(radius);
        }
        if (mRoundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(radius);
        }
    }

    public void setBorderWidth(int width) {
        if (mBorderWidth == width) {
            return;
        }

        mBorderWidth = width;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setBorderWidth(width);
        }
        if (mRoundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(width);
        }
        invalidate();
    }

    public void setBorderColor(int color) {
        setBorderColors(ColorStateList.valueOf(color));
    }

    public void setBorderColors(ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }

        mBorderColor = colors != null ? colors : ColorStateList.valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setBorderColors(colors);
        }
        if (mRoundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderColors(colors);
        }
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    public void setOval(boolean oval) {
        mOval = oval;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setOval(oval);
        }
        if (mRoundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setOval(oval);
        }
        invalidate();
    }

    public boolean isOval() {
        return mOval;
    }
    
    public boolean ismIsNoBottomRadius() {
		return mIsNoBottomRadius;
	}

	public void setmIsNoBottomRadius(boolean mIsNoBottomRadius) {
		this.mIsNoBottomRadius = mIsNoBottomRadius;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setmIsNoBottomRadius(mIsNoBottomRadius);
        }
        if (mRoundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setmIsNoBottomRadius(mIsNoBottomRadius);
        }
        invalidate();
	}

	@Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageDrawable(getDrawable());
    }

    public boolean isRoundBackground() {
        return mRoundBackground;
    }

    public void setRoundBackground(boolean roundBackground) {
        if (mRoundBackground == roundBackground) {
            return;
        }

        mRoundBackground = roundBackground;
        if (roundBackground) {
            if (mBackgroundDrawable instanceof RoundedDrawable) {
                updateDrawableAttrs((RoundedDrawable) mBackgroundDrawable);
            } else {
                setBackgroundDrawable(mBackgroundDrawable);
            }
        } else if (mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(0);
            ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(0);
        }

        invalidate();
    }

//	@Override
//	protected void onDraw(Canvas canvas) {
//		int paddingLeft = getPaddingLeft()-8;
//		int paddingRight = canvas.getWidth() - getPaddingRight() + 8;
//		int paddingTop = 0;
//		int paddingBottom = getHeight();
//		RectF r = new RectF(paddingLeft, paddingTop, paddingRight, paddingBottom);
//		Paint paint = new Paint();
//		paint.setColor(Color.BLACK);
//		paint.setAlpha(50);
//		paint.setShadowLayer(15, 5, 5, Color.BLACK);
//		canvas.drawRect(r, paint);
//		super.onDraw(canvas);
//		Bitmap bitmap = null;
//	    if (mDrawable instanceof RoundedDrawable) {
//   bitmap=((RoundedDrawable) mDrawable).getBitmap();
//     }
//		
//		
//		float x_left = 0;
//		float x_right = getWidth();
//		float y_top = paddingBottom -20;
//		float y_bottom = paddingBottom + 2;
//		RectF rectF = new RectF(x_left, y_top, x_right, y_bottom);
//		if (bitmap!=null) {
//			canvas.drawBitmap(bitmap, null, rectF, null);
//		}
//		
//	}
//	@Override
//	protected void onDraw(Canvas canvas) {
////		super.onDraw(canvas);
//	      Paint paint1 = new Paint();  
//          // 设定颜色  
//          paint1.setColor(0xFFFFFF00);  
//          // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)  
//          paint1.setShadowLayer(5, 3, 3, 0xFFFF00FF);  
//          // 实心矩形& 其阴影  
//          canvas.drawText("test", 20,40,paint1);  
//          
//          
//          Paint paint2 = new Paint();  
//          paint2.setTextSize(100);
//          paint2.setColor(Color.GREEN);  
//          paint2.setShadowLayer(10, 5, 2, Color.BLUE);  
//          canvas.drawText("test", 20,60,paint2);  
//            
//          Paint paint3 = new Paint();  
//          paint3.setColor(Color.RED);  
//          paint3.setShadowLayer(30, 5, 2, Color.GREEN);  
//          canvas.drawCircle(50, 130,30, paint3);  
//	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		Bitmap bitmap = null;
//	       if (mDrawable instanceof RoundedDrawable) {
//      bitmap=((RoundedDrawable) mDrawable).getBitmap();
//}
//		
//     Paint paint4 = new Paint();  
//     paint4.setColor(Color.YELLOW);
//     paint4.setShadowLayer(5, 3, 3, Color.WHITE);  
//     if (bitmap!=null) {
//     	  canvas.drawBitmap(bitmap, 1024, 1024, paint4); 
//     	  Log.i("执行了", "执行了");
//		}
//		
//
//      
//	}

    
    
    

    
//    //阴影
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		Rect rect1 = getRect(canvas);
//		Paint paint = new Paint();
//		paint.setColor(Color.BLACK);
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setShadowLayer(25, 2, 2, Color.YELLOW);
//		//		paint.setTextSize(10);
////		paint.setAlpha(80);
//		// 画边框
//		canvas.drawRect(rect1, paint);
//		paint.setColor(Color.LTGRAY);
//		// 画一条竖线,模拟右边的阴影
////		canvas.drawLine(rect1.right + 1, rect1.top + 2, rect1.right + 1,rect1.bottom + 2, paint);
//		// 画一条横线,模拟下边的阴影
////		canvas.drawLine(rect1.left + 2, rect1.bottom + 1, rect1.right + 2,rect1.bottom + 1, paint);
//		
//		// 画一条竖线,模拟右边的阴影
////		canvas.drawLine(rect1.right + 2, rect1.top + 3, rect1.right + 2,rect1.bottom + 3, paint);
//		// 画一条横线,模拟下边的阴影
////		canvas.drawLine(rect1.left + 3, rect1.bottom + 2, rect1.right + 3,rect1.bottom + 2, paint);
//
//	}
//	
//	Rect getRect(Canvas canvas) {
//		Rect rect = canvas.getClipBounds();
//		rect.bottom -= getPaddingBottom();
//		rect.right -= getPaddingRight();
//		rect.left += getPaddingLeft();
//		rect.top += getPaddingTop();
//		return rect;
//	}
//    //阴影    
    
    
    
    
}
