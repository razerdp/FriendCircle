package razerdp.friendcircle.api.adapter.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.data.model.CommentInfo;
import razerdp.friendcircle.api.data.model.MomentsInfo;
import razerdp.friendcircle.utils.TimeUtil;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.ClickShowMoreLayout;
import razerdp.friendcircle.widget.SuperImageView;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;
import razerdp.friendcircle.widget.praisewidget.PraiseWidget;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 基本item封装
 */
public abstract class BaseItemDelegate
        implements BaseItemView<MomentsInfo>, View.OnClickListener, View.OnLongClickListener {
    protected Activity context;
    //顶部
    protected SuperImageView avatar;
    protected TextView nick;
    protected ClickShowMoreLayout textField;
    //底部
    protected TextView createTime;
    protected ImageView commentImage;
    protected FrameLayout commentButton;
    protected LinearLayout commentAndPraiseLayout;
    protected PraiseWidget praiseWidget;
    protected View line;
    protected LinearLayout commentLayout;

    //中间内容层
    protected RelativeLayout contentLayout;

    private MomentsInfo mInfo;

    public BaseItemDelegate() {
    }

    public BaseItemDelegate(Activity context) {
        this.context = context;
    }

    @Override
    public void onBindData(int position, @NonNull View v, @NonNull MomentsInfo data, final int dynamicType) {
        mInfo = data;
        //初始化共用部分
        bindView(v);
        bindShareData(data);
        bindData(position, v, data, dynamicType);
    }

    /** 共有数据绑定 */
    private void bindShareData(MomentsInfo data) {
        avatar.loadImageDefault(data.userInfo.avatar);
        nick.setText(data.userInfo.nick);
        textField.setText(data.textField);

        if (TextUtils.isEmpty(data.textField) && contentLayout != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
            params.topMargin = -UIHelper.dipToPx(context, 8);
            contentLayout.setLayoutParams(params);
        }
        else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
            params.topMargin = 0;
            contentLayout.setLayoutParams(params);
        }
        createTime.setText(TimeUtil.getTimeString(data.dynamicInfo.createTime));
        setCommentPraiseLayoutVisibility(data);
        //点赞
        praiseWidget.setDatas(data.praiseList);
        //评论
        addCommentWidget(data.commentList);
    }

    /** 绑定共用部分 */
    private void bindView(View v) {
        avatar = (SuperImageView) v.findViewById(R.id.avatar);
        nick = (TextView) v.findViewById(R.id.nick);
        textField = (ClickShowMoreLayout) v.findViewById(R.id.item_text_field);

        contentLayout = (RelativeLayout) v.findViewById(R.id.content);

        createTime = (TextView) v.findViewById(R.id.create_time);
        commentImage = (ImageView) v.findViewById(R.id.comment_press);
        commentButton = (FrameLayout) v.findViewById(R.id.comment_button);
        commentAndPraiseLayout = (LinearLayout) v.findViewById(R.id.comment_praise_layout);
        praiseWidget = (PraiseWidget) v.findViewById(R.id.praise);
        line = v.findViewById(R.id.divider);
        commentLayout = (LinearLayout) v.findViewById(R.id.comment_layout);

        avatar.setOnClickListener(this);
        nick.setOnClickListener(this);
        textField.setOnLongClickListener(this);

        commentButton.setOnClickListener(this);
    }

    /** 是否有点赞或者评论 */
    private void setCommentPraiseLayoutVisibility(MomentsInfo data) {
        if ((data.commentList == null || data.commentList.size() == 0) &&
                (data.praiseList == null || data.praiseList.size() == 0)) {
            //全空，取消显示
            commentAndPraiseLayout.setVisibility(View.GONE);
        }
        else {
            //某项不空，则展示layout
            commentAndPraiseLayout.setVisibility(View.VISIBLE);
            //点赞或者评论某个为空，分割线不展示
            if (data.commentList == null || data.commentList.size() == 0 ||
                    data.praiseList == null || data.praiseList.size() == 0) {
                line.setVisibility(View.GONE);
            }
            else {
                line.setVisibility(View.VISIBLE);
            }
            //点赞为空，取消点赞控件的可见性
            if (data.praiseList == null || data.praiseList.size() == 0) {
                praiseWidget.setVisibility(View.GONE);
            }
            else {
                praiseWidget.setVisibility(View.VISIBLE);
            }
            //评论
            if (data.commentList == null || data.commentList.size() == 0) {
                commentLayout.setVisibility(View.GONE);
            }
            else {
                commentLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addCommentWidget(List<CommentInfo> commentList) {
        if (commentList == null || commentList.size() == 0) return;
        /**
         * 优化方案：
         * 因为是在listview里面，那么复用肯定有，意味着滑动的时候必须要removeView或者addView
         * 但为了性能提高，不可以直接removeAllViews
         * 于是采取以下方案：
         *    根据现有的view进行remove/add差额
         *    然后统一设置
         * */
        final int childCount = commentLayout.getChildCount();
        if (childCount < commentList.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = new CommentWidget(context);
                commentWidget.setOnClickListener(this);
                commentWidget.setOnLongClickListener(this);
                commentLayout.addView(commentWidget);
            }
        }
        else if (childCount > commentList.size()) {
            //当前的view的数目比list的长度大，则减去对应的view
            commentLayout.removeViews(commentList.size(), childCount - commentList.size());
        }
        //绑定数据
        for (int n = 0; n < commentList.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) commentLayout.getChildAt(n);
            if (commentWidget != null) commentWidget.setCommentText(commentList.get(n));
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    //=============================================================
    @Override
    public Activity getActivityContext() {
        return context;
    }

    @Override
    public void setActivityContext(Activity context) {
        this.context = context;
    }

    protected abstract void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, final int dynamicType);
}
