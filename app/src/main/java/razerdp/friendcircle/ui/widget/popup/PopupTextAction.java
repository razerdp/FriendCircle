package razerdp.friendcircle.ui.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.github.com.ui.base.adapter.BaseRecyclerViewAdapter;
import razerdp.github.com.ui.base.adapter.BaseRecyclerViewHolder;
import razerdp.github.com.ui.base.adapter.OnRecyclerViewItemClickListener;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.util.ViewUtil;

/**
 * Created by 大灯泡 on 2018/4/12.
 * <p>
 * 通用的text action
 */

public class PopupTextAction extends BasePopupWindow implements BasePopupWindow.OnBeforeShowCallback {

    private TextView tvTitle;
    private TextView extraAction;
    private View divider;
    private RecyclerView rvContent;
    private InnerAdapter mInnerAdapter;
    private List<InnerItemInfo> datas;
    private CharSequence selectedStr;

    public PopupTextAction(Activity context) {
        super(context);
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        extraAction = (TextView) findViewById(R.id.tv_ex_action);
        divider = findViewById(R.id.divider);

        extraAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionClickedListener != null) {
                    mOnActionClickedListener.onClicked(extraAction.getText(), -1);
                }
                dismiss();
            }
        });

        datas = new ArrayList<>();
        mInnerAdapter = new InnerAdapter(context, datas);
        rvContent.setLayoutManager(new LinearLayoutManager(context));
        rvContent.setAdapter(mInnerAdapter);
        mInnerAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<InnerItemInfo>() {
            @Override
            public void onItemClick(View v, int position, InnerItemInfo data) {
                if (data == null) {
                    dismiss();
                    return;
                }
                if (mOnActionClickedListener != null) {
                    selectedStr = data.getDesc();
                    mOnActionClickedListener.onClicked(data.getDesc(), data.getCode());
                }
                dismissWithOutAnimate();
            }
        });
        setPopupFadeEnable(true);
        setBlurBackgroundEnable(true);
        setOnBeforeShowCallback(this);

        findViewById(R.id.ib_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_text_action);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 500);
    }

    public PopupTextAction addData(CharSequence text, int actionCode) {
        this.datas.add(new InnerItemInfo().setDesc(text).setCode(actionCode));
        return this;
    }

    public PopupTextAction addData(CharSequence text, int actionCode, int textColor) {
        this.datas.add(new InnerItemInfo()
                .setDesc(text)
                .setCode(actionCode)
                .setColor(textColor));
        return this;
    }

    public PopupTextAction addData(CharSequence text, int actionCode, int textColor, int selectedColor) {
        this.datas.add(new InnerItemInfo()
                .setDesc(text)
                .setCode(actionCode)
                .setColor(textColor)
                .setSelectedColor(selectedColor));
        return this;
    }

    public PopupTextAction addData(InnerItemInfo item) {
        if (item != null) {
            this.datas.add(item);
            return this;
        }
        return this;
    }

    public void refreshData(List<InnerItemInfo> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        }
    }

    public void setSelectedText(CharSequence selectedStr) {
        this.selectedStr = selectedStr;
    }

    public PopupTextAction setTitle(CharSequence title) {
        tvTitle.setText(title);
        return this;
    }

    public PopupTextAction setTitleColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public void setExtraActionText(CharSequence actionText) {
        extraAction.setText(actionText);
    }

    public void setExtraActionTextColor(int color) {
        extraAction.setTextColor(color);
    }

    public void setExtraActionVisible(boolean visible) {
        ViewUtil.setViewsVisible(visible ? View.VISIBLE : View.GONE, extraAction, divider);
    }

    public void showPopupWindow(List<InnerItemInfo> newDatas) {
        refreshData(newDatas);
        super.showPopupWindow();
    }

    @Override
    public boolean onBeforeShow(View popupRootView, View anchorView, boolean hasShowAnima) {
        mInnerAdapter.updateData(datas);
        return true;
    }

    public static class InnerItemInfo {
        CharSequence desc;
        int code;
        int color = UIHelper.getColor(R.color.text_black);
        int selectedColor = UIHelper.getColor(R.color.green);

        public CharSequence getDesc() {
            return desc;
        }

        public InnerItemInfo setDesc(CharSequence desc) {
            this.desc = desc;
            return this;
        }

        public int getCode() {
            return code;
        }

        public InnerItemInfo setCode(int code) {
            this.code = code;
            return this;
        }

        public int getColor() {
            return color;
        }

        public InnerItemInfo setColor(int color) {
            this.color = color;
            return this;
        }

        public int getSelectedColor() {
            return selectedColor;
        }

        public InnerItemInfo setSelectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }
    }

    class InnerAdapter extends BaseRecyclerViewAdapter<InnerItemInfo> {
        public InnerAdapter(@NonNull Context context, @NonNull List<InnerItemInfo> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(int position, @NonNull InnerItemInfo data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_pop_action_text;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }

        class InnerViewHolder extends BaseRecyclerViewHolder<InnerItemInfo> {
            public TextView text;

            public InnerViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                this.text = findViewById(R.id.tv_action);
            }

            @Override
            public void onBindData(InnerItemInfo data, int position) {
                text.setText(data.getDesc());
                if (TextUtils.equals(data.getDesc(), selectedStr)) {
                    text.setTextColor(data.getSelectedColor());
                } else {
                    text.setTextColor(data.getColor());
                }
            }

        }

    }

    private OnActionClickedListener mOnActionClickedListener;

    public OnActionClickedListener getOnSelectedListener() {
        return mOnActionClickedListener;
    }

    public PopupTextAction setOnSelectedListener(OnActionClickedListener listener) {
        mOnActionClickedListener = listener;
        return this;
    }

    public interface OnActionClickedListener {
        void onClicked(CharSequence action, int actionCode);
    }
}
