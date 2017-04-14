package razerdp.github.com.baseuilib.widget.imageview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.List;

import razerdp.github.com.baselibrary.utils.SimpleObjectPool;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2017/4/14.
 * <p>
 * 发布动态的预览图片控件
 */

public class PreviewImageView<T> extends FrameLayout {

    private static final String TAG = "PreviewImageView";
    private static final int DEFAULT_MAX_PHOTO_COUNT = 9;
    public static final int ADD_IMAGE_ID = View.generateViewId();
    private static final int DEFAULT_PADDING = 16;

    private SimpleObjectPool<ImageView> ivPool;
    private TableLayout tableLayout;
    private List<T> datas;
    private ImageView addImageView;

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
        tableLayout = new TableLayout(context);
        addView(tableLayout);
        addImageView.setImageResource(R.drawable.ic_add_photo);
        addImageView.setId(ADD_IMAGE_ID);
        ivPool = new SimpleObjectPool<>(DEFAULT_MAX_PHOTO_COUNT);
    }


    public void setDatas(List<T> datas, @NonNull OnLoadPhotoListener<T> onLoadPhotoListener) {
        this.datas = datas;
        setOnLoadPhotoListener(onLoadPhotoListener);
        callToUpdateData();
    }

    private void callToUpdateData() {
        if (isListEmpty(datas)) {
            clearViews();
        }
    }

    private TableRow[] generateTableRows() {
        TableRow[] tableRows = new TableRow[3];
        if (isListEmpty(datas)) {
            TableRow tableRow = new TableRow(getContext());
            FrameLayout layout = new FrameLayout(getContext());
            layout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
            layout.addView(getImageViewWithOutParent(addImageView));
            tableRow.addView(layout);
        } else {
            boolean addImage = checkNeedAddImageView();
            for (int i = 0; i < datas.size(); i++) {
                final int rowIndex = i / 4;
                final T data = datas.get(i);
                TableRow tableRow = tableRows[rowIndex];
                if (tableRow == null) {
                    tableRow = new TableRow(getContext());
                    tableRows[rowIndex] = tableRow;
                }
                ImageView imageView = ivPool.get();
                FrameLayout layout = new FrameLayout(getContext());
                layout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
                if (onLoadPhotoListener != null) {
                    imageView = onLoadPhotoListener.onPhotoLoading(i, data, imageView);
                }
                layout.addView(getImageViewWithOutParent(imageView));
                tableRow.addView(layout);
            }
        }
        return tableRows;
    }


    private boolean checkNeedAddImageView() {
        return isListEmpty(datas) || datas.size() < DEFAULT_MAX_PHOTO_COUNT;
    }

    public void clearViews() {
        tableLayout.removeAllViewsInLayout();
        getImageViewWithOutParent(addImageView);
        tableLayout.requestLayout();
    }

    public ImageView getImageViewWithOutParent(ImageView imageView) {
        if (imageView.getParent() != null) {
            ((ViewGroup) imageView.getParent()).removeView(imageView);
        }
        return imageView;
    }

    boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }


    private OnLoadPhotoListener onLoadPhotoListener;

    public OnLoadPhotoListener getOnLoadPhotoListener() {
        return onLoadPhotoListener;
    }

    public void setOnLoadPhotoListener(OnLoadPhotoListener onLoadPhotoListener) {
        this.onLoadPhotoListener = onLoadPhotoListener;
    }

    public interface OnLoadPhotoListener<T> {
        ImageView onPhotoLoading(int pos, T data, ImageView imageView);
    }

}
