package razerdp.friendcircle.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.friendcircle.R;
import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;
import razerdp.github.com.baseuilib.widget.common.TitleBar;
import razerdp.github.com.baselibrary.utils.ui.SwitchActivityTransitionUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;

/**
 * Created by 大灯泡 on 2017/3/1.
 * <p>
 * 发布朋友圈页面
 */

public class PublishActivity extends BaseTitleBarActivity {

    public static final String TAG_MODE = "tag_mode";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Mode.TEXT, Mode.MULTI})
    public @interface Mode {

        //文字
        int TEXT = 0x10;
        //图文
        int MULTI = 0x11;
    }

    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        mode = getIntent().getIntExtra(TAG_MODE, -1);
        if (mode == -1) {
            finish();
            return;
        }
        initView();
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    private void initView() {
        initTitle();

    }

    //title init
    private void initTitle() {
        setTitle(mode == Mode.TEXT ? "发表文字" : null);
        setTitleRightTextColor(mode != Mode.TEXT);
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleLeftText("取消");
        setTitleLeftIcon(0);
        setTitleRightText("发送");
        setTitleRightIcon(0);
    }


    private void setTitleRightTextColor(boolean canClick) {
        setRightTextColor(canClick ? UIHelper.getResourceColor(R.color.wechat_green_bg) : UIHelper.getResourceColor(R.color.wechat_green_transparent));
    }


    @Override
    public void finish() {
        super.finish();
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }
}
