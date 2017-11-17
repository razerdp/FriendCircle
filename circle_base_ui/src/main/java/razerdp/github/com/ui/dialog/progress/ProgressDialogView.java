package razerdp.github.com.ui.dialog.progress;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.widget.common.CircleProgressView;

/**
 * Created by 大灯泡 on 2017/4/5.
 */

public class ProgressDialogView extends LinearLayout {
    private TextView progressMessage;
    private CircleProgressView circleProgressView;

    public ProgressDialogView(Context context) {
        super(context);
        init();
    }

    public ProgressDialogView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.dialog_progress, this);
        progressMessage = (TextView) findViewById(R.id.progress_tips);
        circleProgressView = (CircleProgressView) findViewById(R.id.dialog_progress);
    }

    public void setMessage(String message) {
        progressMessage.setText(message);
    }

    public void setProgress(int progress) {
        circleProgressView.setCurrentPresent(progress);
    }
}
