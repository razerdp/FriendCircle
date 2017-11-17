package razerdp.github.com.ui.dialog.progress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2017/4/5.
 */

public class ProgressDialogHelper extends Dialog {

    private ProgressDialogView probgressDialogView;
    private String message;
    private boolean isCancelAble;

    public ProgressDialogHelper(@NonNull Context context) {
        this(context, R.style.ProgressDialog);
    }

    public ProgressDialogHelper(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ProgressDialogHelper(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProgressDialogHelper(Context context, String message, boolean cancelAble) {
        this(context);
        this.message = message;
        this.isCancelAble = cancelAble;
        setCancelable(cancelAble);
    }

    public static ProgressDialogHelper create(Context context, String message, boolean cancelAble) {
        return new ProgressDialogHelper(context, message, cancelAble);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        probgressDialogView = new ProgressDialogView(getContext());
        probgressDialogView.setMessage(message);
        setContentView(probgressDialogView);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = lp.height = UIHelper.dipToPx(200);
        dialogWindow.setAttributes(lp);
    }

    public void setMessage(String message) {
        probgressDialogView.setMessage(message);
    }

    public void setProgress(int progress) {
        probgressDialogView.setProgress(progress);
    }
}
