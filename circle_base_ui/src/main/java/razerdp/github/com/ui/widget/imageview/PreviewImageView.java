package razerdp.github.com.ui.widget.imageview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.utils.SimpleObjectPool;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2017/4/14.
 * <p>
 * 发布动态的预览图片控件
 */

public class PreviewImageView<T> extends FlowLayout implements ViewGroup.OnHierarchyChangeListener {

    private static final String TAG = "PreviewImageView";
    private static final int DEFAULT_MAX_PHOTO_COUNT = 9;
    public static final int ADD_IMAGE_ID = View.generateViewId();
    private static final int DEFAULT_PADDING = 16;

    private SimpleObjectPool<ImageView> ivPool;
    private List<T> datas;
    private ImageView addImageView;
    private int mImageSize;
    private volatile boolean fillViewInMeasure = false;
    private OnLoadPhotoListener onLoadPhotoListener;
    private OnPhotoClickListener mOnPhotoClickListener;
    private OnClickListener mOnAddImageClickListener;

    public PreviewImageView(@NonNull Context context) {
        this(context, null);
    }

    public PreviewImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setOnHierarchyChangeListener(this);
        ivPool = new SimpleObjectPool<>(ImageView.class, DEFAULT_MAX_PHOTO_COUNT);
        datas = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mImageSize == 0) {
            mImageSize = (width - getPaddingLeft() - getPaddingRight()) / 3 - DEFAULT_PADDING * 2;
        }
        if (addImageView == null) {
            initAddImageView();
            addView(addImageView, generateDefaultImageSizeLayoutParams());
        }
        if (fillViewInMeasure) {
            fillViewInMeasure = false;
            fillView();
        }
    }

    public void setDatas(List<T> datas, @NonNull OnLoadPhotoListener<T> onLoadPhotoListener) {
        if (datas == null) return;
        this.datas.clear();
        this.datas.addAll(datas);
        setOnLoadPhotoListener(onLoadPhotoListener);
        callToUpdateData();
    }


    public void deleteData(int pos) {
        if (datas == null) return;
        if (pos < 0 || pos > datas.size()) return;
        datas.remove(pos);
        callToUpdateData();
    }


    public void addData(List<T> datas) {
        if (ToolUtil.isListEmpty(datas)) return;
        final boolean hasRest = DEFAULT_MAX_PHOTO_COUNT - this.datas.size() > 0;
        if (!hasRest) return;
        for (T data : datas) {
            addData(data, false);
        }
        callToUpdateData();
    }

    public void addData(T data) {
        addData(data, true);
    }

    private void addData(T data, boolean needUpdataView) {
        this.datas.add(data);
        if (needUpdataView) {
            callToUpdateData();
        }
    }

    public int getRestPhotoCount() {
        return DEFAULT_MAX_PHOTO_COUNT - datas.size();
    }

    private void callToUpdateData() {
        if (isListEmpty(datas)) {
            clearViews();
            if (checkAddActionImageView()) {
                addView(getImageViewWithOutParent(addImageView));
            }
        } else {
            if (mImageSize == 0) {
                fillViewInMeasure = true;
            } else {
                fillViewInMeasure = false;
                fillView();
            }
        }
    }

    private void fillView() {
        if (onLoadPhotoListener == null) {
            throw new NullPointerException("OnLoadPhotoListener must not be null,please check");
        }
        removeAllViewsInLayout();
        int addImageViewPos = -1;
        for (int i = 0; i < datas.size(); i++) {
            ImageView iv = ivPool.get();
            if (iv == null) {
                iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            final int pos = i;
            final ImageView targetImageView = iv;
            final T data = datas.get(pos);
            onLoadPhotoListener.onPhotoLoading(pos, data, targetImageView);
            addViewInLayout(targetImageView, pos, generateDefaultImageSizeLayoutParams(), true);
            addImageViewPos = pos;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPhotoClickListener != null) {
                        mOnPhotoClickListener.onPhotoClickListener(pos, data, targetImageView);
                    }
                }
            });
        }
        if (checkAddActionImageView()) {
            addViewInLayout(addImageView, addImageViewPos + 1, generateDefaultImageSizeLayoutParams(), true);
        }
        requestLayout();
    }

    private FlowLayout.LayoutParams generateDefaultImageSizeLayoutParams() {
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(mImageSize, mImageSize);
        params.leftMargin = DEFAULT_PADDING;
        params.topMargin = DEFAULT_PADDING;
        return params;
    }

    public void clearViews() {
        removeAllViewsInLayout();
        getImageViewWithOutParent(addImageView);
        requestLayout();
    }

    public List<T> getDatas() {
        if (datas == null) return null;
        return new ArrayList<>(datas);
    }

    public ImageView getImageViewWithOutParent(ImageView imageView) {
        if (imageView == null) return null;
        if (imageView.getParent() != null) {
            ((ViewGroup) imageView.getParent()).removeView(imageView);
        }
        return imageView;
    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    private boolean checkAddActionImageView() {
        initAddImageView();
        return isListEmpty(datas) || (datas != null && datas.size() < 9);
    }

    private void initAddImageView() {
        if (addImageView == null) {
            addImageView = new ImageView(getContext());
            addImageView.setBackgroundColor(Color.parseColor("#f2f2f2"));
            addImageView.setImageResource(R.drawable.ic_add_photo);
            addImageView.setId(ADD_IMAGE_ID);
            addImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAddImageClickListener != null) {
                        mOnAddImageClickListener.onClick(v);
                    }
                }
            });
        }
    }

    public OnLoadPhotoListener getOnLoadPhotoListener() {
        return onLoadPhotoListener;
    }

    public void setOnLoadPhotoListener(OnLoadPhotoListener<T> onLoadPhotoListener) {
        this.onLoadPhotoListener = onLoadPhotoListener;
    }

    public OnPhotoClickListener getOnPhotoClickListener() {
        return mOnPhotoClickListener;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener<T> onPhotoClickListener) {
        mOnPhotoClickListener = onPhotoClickListener;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof ImageView) {
            ImageView iv = (ImageView) child;
            if (iv != addImageView) {
                ivPool.put(iv);
            }
        }
    }

    public interface OnLoadPhotoListener<T> {
        void onPhotoLoading(int pos, T data, @NonNull ImageView imageView);
    }

    public interface OnPhotoClickListener<T> {
        void onPhotoClickListener(int pos, T data, @NonNull ImageView imageView);
    }

    public void setOnAddPhotoClickListener(OnClickListener onAddPhotoClickListener) {
        mOnAddImageClickListener = onAddPhotoClickListener;
    }

}
