package razerdp.friendcircle.activity.other;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.razerdp.github.com.common.entity.PhotoBrowseInfo;
import com.razerdp.github.com.common.entity.other.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.R;
import razerdp.friendcircle.activity.ActivityLauncher;
import razerdp.github.com.ui.base.BaseTitleBarActivity;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.commentwidget.ExTextView;

/**
 * Created by 大灯泡 on 2017/12/20.
 */
public class ServiceInfoActivity extends BaseTitleBarActivity implements View.OnClickListener {
    private ExTextView tvTitle;
    private ExTextView tvContent;

    private ServiceInfo mInfo;
    private LinearLayout mIvContents;
    private ImageView ivWechat;
    private ImageView ivAlipay;
    private ImageView ivGroup;

    @Override
    public void onHandleIntent(Intent intent) {
        mInfo = (ServiceInfo) intent.getSerializableExtra("info");
        if (mInfo == null) finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
        setStatusBarColor(UIHelper.getColor(R.color.action_bar_bg));
    }

    @Override
    protected boolean isTranslucentStatus() {
        return true;
    }

    @Override
    protected boolean isFitsSystemWindows() {
        return true;
    }

    private void initView() {
        setTitle("服务器消息");
        tvTitle = (ExTextView) findViewById(R.id.tv_title);
        tvContent = (ExTextView) findViewById(R.id.tv_content);

        tvTitle.setText(mInfo.getTitle());
        tvContent.setText(mInfo.getContent());
        mIvContents = (LinearLayout) findViewById(R.id.iv_contents);
        ivWechat = (ImageView) findViewById(R.id.iv_wechat);
        ivWechat.setOnClickListener(this);
        ivAlipay = (ImageView) findViewById(R.id.iv_alipay);
        ivAlipay.setOnClickListener(this);
        ivGroup = (ImageView) findViewById(R.id.iv_group);
        ivGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        List<String> urls = new ArrayList<>();
        urls.add(String.valueOf(R.drawable.wechat_full));
        urls.add(String.valueOf(R.drawable.ali_full));
        urls.add(String.valueOf(R.drawable.qqgroup));
        List<Rect> rects = new ArrayList<>();
        rects.add(getDrawableBoundsInView(ivWechat));
        rects.add(getDrawableBoundsInView(ivAlipay));
        rects.add(getDrawableBoundsInView(ivGroup));

        PhotoBrowseInfo info = PhotoBrowseInfo.create(urls, rects, mIvContents.indexOfChild(v) < 0 ? 0 : mIvContents.indexOfChild(v));
        ActivityLauncher.startToPhotoBrosweActivity(this, info);
    }


    private Rect getDrawableBoundsInView(ImageView iv) {
        if (iv != null && iv.getDrawable() != null) {
            Drawable d = iv.getDrawable();
            Rect result = new Rect();
            iv.getGlobalVisibleRect(result);
            Rect tDrawableRect = d.getBounds();
            Matrix drawableMatrix = iv.getImageMatrix();
            float[] values = new float[9];
            if (drawableMatrix != null) {
                drawableMatrix.getValues(values);
            }

            result.left += (int) values[2];
            result.top += (int) values[5];
            result.right = (int) ((float) result.left + (float) tDrawableRect.width() * (values[0] == 0.0F ? 1.0F : values[0]));
            result.bottom = (int) ((float) result.top + (float) tDrawableRect.height() * (values[4] == 0.0F ? 1.0F : values[4]));
            return result;
        } else {
            return null;
        }
    }
}
