package razerdp.friendcircle.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import razerdp.friendcircle.widget.MPhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 大灯泡 on 2016/4/12.
 * 图片浏览窗口的adapter
 */
public class PhotoBoswerPagerAdapter extends PagerAdapter {
    private static final String TAG = "PhotoBoswerPagerAdapter";

    private static ArrayList<MPhotoView> sMPhotoViewPool;
    private static final int sMPhotoViewPoolSize = 10;

    //=============================================================datas
    private ArrayList<String> photoAddress;
    private ArrayList<Rect> originViewBounds;
    //=============================================================bounds

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private TextView mProgressTextView;

    public PhotoBoswerPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        photoAddress = new ArrayList<>();
        originViewBounds = new ArrayList<>();

        sMPhotoViewPool = new ArrayList<>();
        //buildProgressTV(context);
        buildMPhotoViewPool(context);
    }

    private void buildMPhotoViewPool(Context context) {
        for (int i = 0; i < sMPhotoViewPoolSize; i++) {
            MPhotoView sPhotoView = new MPhotoView(context);
            sPhotoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            sMPhotoViewPool.add(sPhotoView);
        }
    }

    public void resetDatas(@NonNull ArrayList<String> newAddress, @NonNull ArrayList<Rect> newOriginViewBounds)
            throws IllegalArgumentException {
        if (newAddress.size() != newOriginViewBounds.size() || newAddress.size() <= 0 ||
                newOriginViewBounds.size() <= 0) {
            throw new IllegalArgumentException("图片地址和图片的位置缓存不对等或某一个为空");
        }

        photoAddress.clear();
        originViewBounds.clear();

        photoAddress.addAll(newAddress);
        originViewBounds.addAll(newOriginViewBounds);
    }

    //用于展示进度的textview，因为Glide要弄进度比较麻烦，所以暂时不启用
    private void buildProgressTV(Context context) {
        mProgressTextView = new TextView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        mProgressTextView.setTextColor(Color.WHITE);
        mProgressTextView.getPaint().setTypeface(Typeface.DEFAULT_BOLD);
        mProgressTextView.setTextSize(18);
        mProgressTextView.setVisibility(View.GONE);

        mProgressTextView.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return photoAddress.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MPhotoView mPhotoView = sMPhotoViewPool.get(position);
        if (mPhotoView == null) {
            mPhotoView = new MPhotoView(mContext);
            mPhotoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        Glide.with(mContext).load(photoAddress.get(position)).into(mPhotoView);
        container.addView(mPhotoView);
        return mPhotoView;
    }

    int[] pos = new int[1];

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        pos[0] = position;
        if (object instanceof MPhotoView) {
            MPhotoView photoView = (MPhotoView) object;
            if (photoView.getOnViewTapListener() == null) {
                photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        if (mOnPhotoViewClickListener != null) {
                            mOnPhotoViewClickListener.onPhotoViewClick(view, originViewBounds.get(pos[0]), pos[0]);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //push2ViewPool(new WeakReference<MPhotoView>((MPhotoView) object));
        //((MPhotoView) object).setImageBitmap(null);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //=============================================================点击消失的interface

    private OnPhotoViewClickListener mOnPhotoViewClickListener;

    public OnPhotoViewClickListener getOnPhotoViewClickListener() {
        return mOnPhotoViewClickListener;
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener onPhotoViewClickListener) {
        mOnPhotoViewClickListener = onPhotoViewClickListener;
    }

    public interface OnPhotoViewClickListener {
        void onPhotoViewClick(View view, Rect originBound, int curPos);
    }

    //=============================================================destroy
    public void destroy(){
        for (MPhotoView photoView : sMPhotoViewPool) {
            photoView.destroy();
        }
        sMPhotoViewPool.clear();
        sMPhotoViewPool=null;
    }
}
