package razerdp.friendcircle.ui.adapter.itemview;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.adapter.view.BaseItemDelegate;
import razerdp.friendcircle.api.data.entity.MomentsInfo;
import razerdp.friendcircle.utils.ImgUtil;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.SuperImageView;

/**
 * Created by 大灯泡 on 2016/2/27.
 * 图文（单张）
 * type=9
 */
public class ItemWithImgSingle extends BaseItemDelegate {

    private int maxWidth;
    private int maxHeight;
    private ImageView mImageView;
    private float ratio;
    int width=0;

    public ItemWithImgSingle(){}

    @Override
    public void onFindView(@NonNull View parent) {
        mImageView= (ImageView) parent.findViewById(R.id.img_single);
        if (maxWidth==0){
            maxWidth= UIHelper.getScreenPixWidth(context)-UIHelper.dipToPx(context,90f);
        }
        if (maxHeight==0){
            maxHeight=UIHelper.dipToPx(context,175f);
        }
        ratio=maxWidth/maxHeight;
    }

    @Override
    protected void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, int dynamicType) {
        final String imgUrl=data.content.imgurl.get(0);
        if (!TextUtils.isEmpty(imgUrl)){
            Glide.with(context).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource.getWidth() >= maxWidth) {
                        width = maxWidth;
                    }
                    else {
                        width = resource.getWidth();
                    }
                    resource.recycle();
                    Glide.with(context)
                         .load(imgUrl)
                         .dontAnimate()
                         .crossFade()
                         .override(width,
                                 (int) (width * ratio))
                         .into(mImageView);

                }
            });
        }
    }

    @Override
    public int getViewRes() {
        return R.layout.dynamic_item_with_img_single;
    }

}
