package razerdp.friendcircle.ui.adapteritem;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.ArrayList;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.adapter.base.viewholder.BaseItemDelegate;
import razerdp.friendcircle.app.config.DynamicType;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.utils.ImgUtil;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.imageview.ZoomImageView;

/**
 * Created by 大灯泡 on 2016/2/27.
 * 图文（单张）的朋友圈item
 * type=9{@link DynamicType}
 */
public class ItemWithImgSingle extends BaseItemDelegate {

    private int maxWidth;
    private int maxHeight;
    private ZoomImageView mImageView;
    private float ratio;
    int width = 0;

    private ArrayList<String> mUrls=new ArrayList<>();
    private ArrayList<Rect> mRects=new ArrayList<>();

    public ItemWithImgSingle() {}

    @Override
    public void onFindView(@NonNull View parent) {
        if (mImageView == null) mImageView = (ZoomImageView) parent.findViewById(R.id.img_single);
        mImageView.setOnClickListener(this);
        if (maxWidth == 0) {
            maxWidth = UIHelper.getScreenPixWidth(context) - UIHelper.dipToPx(context, 90f);
        }
        if (maxHeight == 0) {
            maxHeight = UIHelper.dipToPx(context, 175f);
        }
        ratio = maxWidth / maxHeight;
    }


    @Override
    protected void bindData(int position, @NonNull View v, @NonNull final MomentsInfo data, int dynamicType) {
        final String imgUrl = data.content.imgurl.get(0);
        mUrls.clear();
        mUrls.addAll(data.content.imgurl);
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(context).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource.getWidth() >= maxWidth) {
                        width = maxWidth;
                    }
                    else {
                        width = resource.getWidth();
                    }
                    try {
                        mImageView.setImageBitmap(ImgUtil.ScaleBitmap(resource, width, (int) (width * ratio)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("ItemWithImgSingle", "有可能是原图被回收了。该动态的nick=  " + data.userInfo.nick);
                    }
                }
            });
        }
    }

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_img_single;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_single:
                Rect bound=new Rect();
                v.getGlobalVisibleRect(bound);
                mRects.clear();
                mRects.add(0,bound);
                int pos=0;
                getPresenter().shoPhoto(mUrls,mRects,pos);
                break;
        }
    }
}
