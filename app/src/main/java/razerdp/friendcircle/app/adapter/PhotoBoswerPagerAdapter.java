package razerdp.friendcircle.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import razerdp.friendcircle.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 大灯泡 on 2016/4/12.
 * 图片浏览窗口的adapter
 */
public class PhotoBoswerPagerAdapter extends PagerAdapter {
    private static final String TAG = "PhotoBoswerPagerAdapter";

    //=============================================================datas
    private ArrayList<String> photoAddress;
    private ArrayList<Rect> originViewBounds;
    //=============================================================bounds

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private TextView mProgressTextView;

    private int childCount=0;

    public PhotoBoswerPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        photoAddress = new ArrayList<>();
        originViewBounds = new ArrayList<>();

        //buildProgressTV(context);

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

        childCount=photoAddress.size();

        notifyDataSetChanged();
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
    public int getItemPosition(Object object) {
        if (childCount>0){
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int curPos=position;
        FrameLayout frameLayout = (FrameLayout) mLayoutInflater.inflate(R.layout.item_photo_boswer, container, false);
        PhotoView photoView = (PhotoView) frameLayout.findViewById(R.id.item_photoview);
        Glide.with(mContext).load(photoAddress.get(position)).into(photoView);
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mOnPhotoViewClickListener != null) {
                    Log.d("position",">>>> "+curPos);
                    mOnPhotoViewClickListener.onPhotoViewClick(view, originViewBounds.get(curPos), curPos);
                }
            }
        });
        container.addView(frameLayout);
        return frameLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
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
}
