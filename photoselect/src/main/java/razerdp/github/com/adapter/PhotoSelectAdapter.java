package razerdp.github.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewAdapter;
import razerdp.github.com.baseuilib.baseadapter.BaseRecyclerViewHolder;
import razerdp.github.com.baseuilib.widget.imageview.CheckImageView;
import razerdp.github.com.model.PhotoBrowserInfo;
import razerdp.github.com.photoselect.PhotoMultiBrowserActivity;
import razerdp.github.com.photoselect.R;
import razerdp.github.com.router.RouterList;

import static razerdp.github.com.photoselect.fragment.PhotoGridFragement.MAX_COUNT;

/**
 * Created by 大灯泡 on 2017/3/24.
 * <p>
 * 图片选择adapter
 */

public class PhotoSelectAdapter extends BaseRecyclerViewAdapter<LocalPhotoManager.ImageInfo> {
    private static final String TAG = "PhotoSelectAdapter";

    private final int itemDecoration;
    private List<LocalPhotoManager.ImageInfo> selectedRecordLists;
    private boolean selectable = true;
    private String curAlbumName;

    public PhotoSelectAdapter(@NonNull Context context, int itemDecoration, @NonNull List<LocalPhotoManager.ImageInfo> datas) {
        super(context, datas);
        this.itemDecoration = itemDecoration;
        this.selectedRecordLists = new ArrayList<>();
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

    /**
     *
     * 这里不可以用list.remove(Object)，因为当进行过选择之后，内部的实例不一定是同一个对象了。
     *
     * {@link razerdp.github.com.photoselect.fragment.PhotoGridFragement#updateSelectList(List)}以及{@link PhotoMultiBrowserActivity#finish()}
     */
    private void onUnSelectPhoto(LocalPhotoManager.ImageInfo info) {
        if (info == null) return;
        if (selectedRecordLists.size() <= 0) return;
        for (int i = 0; i < selectedRecordLists.size(); i++) {
            LocalPhotoManager.ImageInfo selectedInfo = selectedRecordLists.get(i);
            if (selectedInfo.compareTo(info) == 0) {
                selectedRecordLists.remove(i);
                break;
            }
        }
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

    public void updateSelections(List<LocalPhotoManager.ImageInfo> newDatas) {
        if (newDatas != null) {
            selectedRecordLists.clear();
            selectedRecordLists.addAll(newDatas);
            selectable = selectedRecordLists.size() != MAX_COUNT;
        }
    }

    public String getCurAlbumName() {
        return curAlbumName;
    }

    public void setCurAlbumName(String curAlbumName) {
        this.curAlbumName = curAlbumName;
    }

    public List<LocalPhotoManager.ImageInfo> getSelectedRecordLists() {
        return new ArrayList<>(selectedRecordLists);
    }

    private void clearSelectRecord() {
        selectedRecordLists.clear();
        selectable = true;
    }

    private class InnerViewHolder extends BaseRecyclerViewHolder<LocalPhotoManager.ImageInfo> {

        private CheckImageView checkImageView;
        private InnerClickEventClass clickEventClass;
        private View maskView;

        InnerViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            checkImageView = (CheckImageView) findViewById(R.id.iv_photo_select);
            maskView = findViewById(R.id.iv_photo_mask);
            setCheckImageViewLayoutParams();
            clickEventClass = new InnerClickEventClass();
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
            boolean isSelected = checkIsSelect(data);
            checkImageView.setCanSelect(isSelected || selectable);
            ViewUtil.setViewsVisible((isSelected || selectable) ? View.GONE : View.VISIBLE, maskView);
            checkImageView.setSelected(isSelected);
            ImageLoadMnanger.INSTANCE.loadImage(checkImageView, url);
            setupSelectChangeListener(data);
            clickEventClass.setCurPos(position);
            checkImageView.setOnClickListener(clickEventClass);

        }

        private void setupSelectChangeListener(LocalPhotoManager.ImageInfo data) {
            CheckImageView.OnSelectedChangeListener listener = checkImageView.getOnSelectedChangeListener();
            InnerSelectChangeClass innerSelectChangeClass;
            if (!(listener instanceof InnerSelectChangeClass)) {
                innerSelectChangeClass = new InnerSelectChangeClass();
            } else {
                innerSelectChangeClass = (InnerSelectChangeClass) listener;
            }
            checkImageView.setOnSelectedChangeListener(innerSelectChangeClass);
            innerSelectChangeClass.setData(data);
        }

        private boolean checkIsSelect(LocalPhotoManager.ImageInfo imageInfo) {
            if (imageInfo == null) return false;
            if (ToolUtil.isListEmpty(selectedRecordLists)) return false;
            for (LocalPhotoManager.ImageInfo localSelectedPhoto : selectedRecordLists) {
                if (localSelectedPhoto.compareTo(imageInfo) == 0) {
                    return true;
                }
            }
            return false;
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

        class InnerClickEventClass implements View.OnClickListener {

            private int curPos;

            @Override
            public void onClick(View v) {
                PhotoBrowserInfo info = PhotoBrowserInfo.create(curPos, curAlbumName, selectedRecordLists);
                ARouter.getInstance()
                       .build(RouterList.PhotoMultiBrowserActivity.path)
                       .withParcelable(RouterList.PhotoMultiBrowserActivity.key_browserinfo, info)
                       .withInt(RouterList.PhotoMultiBrowserActivity.key_maxSelectCount, MAX_COUNT)
                       .navigation((Activity) getContext(), RouterList.PhotoMultiBrowserActivity.requestCode);
            }

            public void setCurPos(int curPos) {
                this.curPos = curPos;
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
