package razerdp.github.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.socks.library.KLog;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewAdapter;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewHolder;
import razerdp.github.com.baseuilib.widget.imageview.CheckImageView;
import razerdp.github.com.photoselect.R;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 图片选择adapter
 */

public class PhotoSelectAdapter extends BaseRecyclerViewAdapter<LocalPhotoManager.ImageInfo> {
    private static final String TAG = "PhotoSelectAdapter";

    private final int itemDecoration;
    private LinkedList<LocalPhotoManager.ImageInfo> selectedRecordLists;
    private static final int MAX_COUNT = 9;
    private boolean selectable = true;

    public PhotoSelectAdapter(@NonNull Context context, int itemDecoration, @NonNull List<LocalPhotoManager.ImageInfo> datas) {
        super(context, datas);
        this.itemDecoration = itemDecoration;
        this.selectedRecordLists = new LinkedList<>();
    }

    @Override
    protected int getViewType(int position, @NonNull LocalPhotoManager.ImageInfo data) {
        return 0;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_photo_select;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        return new InnerViewHolder(rootView, viewType);
    }

    private void onSelectPhoto(LocalPhotoManager.ImageInfo info) {
        if (info == null) return;
        if (!checkSelectListLength()) return;
        selectedRecordLists.add(info);
        callSelectListenerChange();
    }

    private void onUnSelectPhoto(LocalPhotoManager.ImageInfo info) {
        if (info == null) return;
        if (selectedRecordLists.size() <= 0) return;
        selectedRecordLists.remove(info);
        callSelectListenerChange();
    }

    private void callSelectListenerChange() {
        final int size = selectedRecordLists.size();
        boolean hasChangeState = selectable;
        selectable = checkSelectListLength();
        hasChangeState = hasChangeState != selectable;
        if (hasChangeState) {
            notifyDataSetChanged();
        }
        if (onSelectCountChangeLisntenr != null) {
            onSelectCountChangeLisntenr.onSelectCountChange(size);
        }
    }


    private boolean checkSelectListLength() {
        return !(selectedRecordLists.size() >= MAX_COUNT || selectedRecordLists.size() < 0);
    }

    @Override
    public void updateData(List<LocalPhotoManager.ImageInfo> datas) {
        clearSelectRecord();
        super.updateData(datas);
    }

    private void clearSelectRecord(){
        selectedRecordLists.clear();
        selectable = true;
    }


    private class InnerViewHolder extends BaseRecyclerViewHolder<LocalPhotoManager.ImageInfo> {

        private CheckImageView checkImageView;
        private View maskView;

        InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            checkImageView = (CheckImageView) findViewById(R.id.iv_photo_select);
            maskView = findViewById(R.id.iv_photo_mask);
            setCheckImageViewLayoutParams();
        }

        /**
         * 根据屏幕宽度设置iv的大小
         */
        private void setCheckImageViewLayoutParams() {
            int itemDercorationSum = 5 * itemDecoration;
            int screenWidth = UIHelper.getScreenWidthPix(getContext());
            final int size = (screenWidth - itemDercorationSum) >> 2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            params.width = params.height = size;
            checkImageView.setLayoutParams(params);

            FrameLayout.LayoutParams maskParams = new FrameLayout.LayoutParams(size, size);
            maskParams.width = maskParams.height = size;
            maskView.setLayoutParams(maskParams);
        }

        @Override
        public void onBindData(LocalPhotoManager.ImageInfo data, int position) {
            KLog.i(TAG, "pos  >>  " + position);
            String url = TextUtils.isEmpty(data.thumbnailPath) ? data.imagePath : data.thumbnailPath;
            boolean isSelected = selectedRecordLists.contains(data);
            checkImageView.setCanSelect(isSelected || selectable);
            ViewUtil.setViewsVisible((isSelected || selectable) ? View.GONE : View.VISIBLE, maskView);
            checkImageView.setSelected(isSelected);
            ImageLoadMnanger.INSTANCE.loadImage(checkImageView, url);
            setupSelectChangeListener(data);
        }

        private void setupSelectChangeListener(LocalPhotoManager.ImageInfo data) {
            CheckImageView.OnSelectedChangeListener listener = checkImageView.getOnSelectedChangeListener();
            InnerSelectChangeClass innerSelectChangeClass;
            if (!(listener instanceof InnerSelectChangeClass)) {
                innerSelectChangeClass = new InnerSelectChangeClass();
                checkImageView.setOnSelectedChangeListener(innerSelectChangeClass);
            } else {
                innerSelectChangeClass = (InnerSelectChangeClass) listener;
            }
            innerSelectChangeClass.setData(data);
        }

        class InnerSelectChangeClass implements CheckImageView.OnSelectedChangeListener {

            private LocalPhotoManager.ImageInfo data;

            public InnerSelectChangeClass() {
            }

            public LocalPhotoManager.ImageInfo getData() {
                return data;
            }

            public void setData(LocalPhotoManager.ImageInfo data) {
                this.data = data;
            }

            @Override
            public void onSelectChange(boolean isSelect) {
                if (isSelect) {
                    onSelectPhoto(data);
                } else {
                    onUnSelectPhoto(data);
                }
            }
        }

        class InnerClickEventClass implements View.OnClickListener{

            private int curPos;

            @Override
            public void onClick(View v) {

            }
        }
    }

    private OnSelectCountChangeLisntenr onSelectCountChangeLisntenr;

    public OnSelectCountChangeLisntenr getOnSelectCountChangeLisntenr() {
        return onSelectCountChangeLisntenr;
    }

    public void setOnSelectCountChangeLisntenr(OnSelectCountChangeLisntenr onSelectCountChangeLisntenr) {
        this.onSelectCountChangeLisntenr = onSelectCountChangeLisntenr;
    }

    public interface OnSelectCountChangeLisntenr {
        void onSelectCountChange(int count);
    }
}
