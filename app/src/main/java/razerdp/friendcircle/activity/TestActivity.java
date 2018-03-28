package razerdp.friendcircle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.razerdp.github.com.common.entity.CommentInfo;

import java.util.List;

import razerdp.friendcircle.R;
import razerdp.github.com.lib.base.BaseActivity;
import razerdp.github.com.lib.utils.GsonUtil;
import razerdp.github.com.ui.widget.commentwidget.CommentContentsLayout;

/**
 * Created by 大灯泡 on 2018/3/28.
 */
public class TestActivity extends BaseActivity {
    private CommentContentsLayout commentLayout;


    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        commentLayout = (CommentContentsLayout) findViewById(R.id.comment_layout);

        List<CommentInfo> commentInfos = GsonUtil.INSTANCE.toList(FakeData.data, CommentInfo.class);
        commentLayout.setMode(CommentContentsLayout.Mode.EXPANDABLE);
        commentLayout.addComments(commentInfos);
    }
}
