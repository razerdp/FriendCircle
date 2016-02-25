package razerdp.friendcircle.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Created by 大灯泡 on 2016/2/12.
 * 针对glide的一些封装
 */
public class SuperImageView extends ImageView {

    public SuperImageView(Context context) {
        this(context, null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //=============================================================glide method:
    public void loadImage(String imgUrl, boolean needFade, int placeholderRes) {
        if (TextUtils.isEmpty(imgUrl)) return;
        if (!(this instanceof CircleImageView)) {
            if (needFade) {
                if (placeholderRes == 0) {
                    Glide.with(getContext()).load(imgUrl).dontAnimate().crossFade().into(this);
                }
                else {
                    Glide.with(getContext())
                         .load(imgUrl)
                         .dontAnimate()
                         .crossFade()
                         .placeholder(placeholderRes)
                         .into(this);
                }
            }
            else {
                if (placeholderRes == 0) {
                    Glide.with(getContext()).load(imgUrl).dontAnimate().into(this);
                }
                else {
                    Glide.with(getContext()).load(imgUrl).placeholder(placeholderRes).dontAnimate().into
                            (this);
                }
            }
        }else {
            if (needFade) {
                if (placeholderRes == 0) {
                    Glide.with(getContext()).load(imgUrl).crossFade().dontAnimate().into(this);
                }
                else {
                    Glide.with(getContext())
                         .load(imgUrl)
                         .crossFade()
                         .dontAnimate()
                         .placeholder(placeholderRes)
                         .into(this);
                }
            }
            else {
                if (placeholderRes == 0) {
                    Glide.with(getContext()).load(imgUrl).dontAnimate().into(this);
                }
                else {
                    Glide.with(getContext()).load(imgUrl).dontAnimate().placeholder(placeholderRes).into
                            (this);
                }
            }
        }
    }

    public void loadImageNoFade(String url, int placeholder) {
        this.loadImage(url, false, placeholder);
    }

    public void loadImageNoPlaceHolder(String url, boolean fade) {
        this.loadImage(url, fade, 0);
    }

    public void loadImageDefault(String url) {
        this.loadImageNoFade(url, 0);
    }

    public void loadLocalImage(int res){
        Glide.with(getContext()).load(res).into(this);
    }
}
