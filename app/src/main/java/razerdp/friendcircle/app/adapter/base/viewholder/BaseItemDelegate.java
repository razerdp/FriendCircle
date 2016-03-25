package razerdp.friendcircle.app.adapter.base.viewholder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.config.CommonValue;
import razerdp.friendcircle.app.config.DynamicType;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.DynamicInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.presenter.DynamicPresenterImpl;
import razerdp.friendcircle.utils.TimeUtil;
import razerdp.friendcircle.widget.ClickShowMoreLayout;
import razerdp.friendcircle.widget.SuperImageView;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;
import razerdp.friendcircle.widget.popup.CommentPopup;
import razerdp.friendcircle.widget.praisewidget.PraiseWidget;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 基本item封装，往后的朋友圈item都会继承本类
 *
 * weblink:http://www.jianshu.com/p/720d5a7c75a7
 */
public abstract class BaseItemDelegate implements BaseItemView<MomentsInfo>,
        View.OnClickListener,
        View.OnLongClickListener,
        ViewGroup.OnHierarchyChangeListener {
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

    private DynamicPresenterImpl mPresenter;
    private MomentsInfo mInfo;
    private int curPos;

    //评论区的view对象池
    private static final CommentPool COMMENT_TEXT_POOL = new CommentPool(35);

    public BaseItemDelegate() {
    }

    public BaseItemDelegate(Activity context) {
        this.context = context;
    }

    private CommentPopup mCommentPopup;

    @Override
    public void onBindData(int position, @NonNull View v, @NonNull MomentsInfo data, final int dynamicType) {
        mInfo = data;
        curPos=position;
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

        if (data.dynamicInfo.dynamicType == DynamicType.TYPE_ONLY_CHAR) {
            contentLayout.setVisibility(View.GONE);
        }
        else {
            contentLayout.setVisibility(View.VISIBLE);
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
        if (avatar == null) avatar = (SuperImageView) v.findViewById(R.id.avatar);
        if (nick == null) nick = (TextView) v.findViewById(R.id.nick);
        if (textField == null) textField = (ClickShowMoreLayout) v.findViewById(R.id.item_text_field);

        if (contentLayout == null) contentLayout = (RelativeLayout) v.findViewById(R.id.content);

        if (createTime == null) createTime = (TextView) v.findViewById(R.id.create_time);
        if (commentImage == null) commentImage = (ImageView) v.findViewById(R.id.comment_press);
        if (commentButton == null) commentButton = (FrameLayout) v.findViewById(R.id.comment_button);
        if (commentAndPraiseLayout == null) {
            commentAndPraiseLayout = (LinearLayout) v.findViewById(R.id.comment_praise_layout);
        }
        if (praiseWidget == null) praiseWidget = (PraiseWidget) v.findViewById(R.id.praise);
        if (line == null) line = v.findViewById(R.id.divider);
        if (commentLayout == null) commentLayout = (LinearLayout) v.findViewById(R.id.comment_layout);
        if (mCommentPopup == null) mCommentPopup = new CommentPopup(context);

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
         *
         * 2016-02-26:复用池进一步优化
         * */
        final int childCount = commentLayout.getChildCount();
        commentLayout.setOnHierarchyChangeListener(this);
        if (childCount < commentList.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = COMMENT_TEXT_POOL.get();
                if (commentWidget == null) {
                    commentWidget = new CommentWidget(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 1;
                    params.bottomMargin = 1;
                    commentWidget.setLayoutParams(params);
                    commentWidget.setLineSpacing(4, 1);
                }
                Log.d("commentTextViewId"," *********         textview id ======  "+commentWidget.hashCode());
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
        switch (v.getId()) {
            // 评论按钮
            case R.id.comment_button:
                if (mInfo == null) return;
                mCommentPopup.setDynamicInfo(mInfo.dynamicInfo);
                mCommentPopup.setOnCommentPopupClickListener(mPopupClickListener);
                mCommentPopup.showPopupWindow(commentImage);
                break;
            default:
                break;
        }
    }

    private CommentPopup.OnCommentPopupClickListener mPopupClickListener=new CommentPopup.OnCommentPopupClickListener() {
        @Override
        public void onLikeClick(View v, DynamicInfo info) {
            if (mPresenter != null) {
                switch (info.praiseState) {
                    case CommonValue.NOT_PRAISE:
                        mPresenter.addPraise(curPos,info.dynamicId);
                        break;
                    case CommonValue.HAS_PRAISE:
                        mPresenter.cancelPraise(curPos,info.dynamicId);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onCommentClick(View v, DynamicInfo info) {
            if (mPresenter!=null){
                mPresenter.showInputBox(curPos,null,info);
            }

        }
    };

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof CommentWidget) COMMENT_TEXT_POOL.put((CommentWidget) child);
    }

    public void clearCommentPool() {
        COMMENT_TEXT_POOL.clearPool();
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

    @Override
    public void setPresenter(DynamicPresenterImpl presenter) {
        this.mPresenter=presenter;
    }

    @Override
    public DynamicPresenterImpl getPresenter() {
        return mPresenter;
    }

    protected abstract void bindData(int position, @NonNull View v, @NonNull MomentsInfo data, final int dynamicType);

    //=============================================================pool class
    static class CommentPool {
        private CommentWidget[] CommentPool;
        private int size;
        private int curPointer = -1;

        public CommentPool(int size) {
            this.size = size;
            CommentPool = new CommentWidget[size];
        }

        public synchronized CommentWidget get() {
            if (curPointer == -1 || curPointer > CommentPool.length) return null;
            CommentWidget commentTextView = CommentPool[curPointer];
            CommentPool[curPointer] = null;
            //Log.d("itemDelegate","复用成功---- 当前的游标为： "+curPointer);
            curPointer--;
            return commentTextView;
        }

        public synchronized boolean put(CommentWidget commentTextView) {
            if (curPointer == -1 || curPointer < CommentPool.length - 1) {
                curPointer++;
                CommentPool[curPointer] = commentTextView;
                //Log.d("itemDelegate","入池成功---- 当前的游标为： "+curPointer);
                return true;
            }
            return false;
        }

        public void clearPool() {
            for (int i = 0; i < CommentPool.length; i++) {
                CommentPool[i] = null;
            }
            curPointer = -1;
        }
    }
}
