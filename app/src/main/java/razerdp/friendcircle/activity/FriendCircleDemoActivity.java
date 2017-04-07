package razerdp.friendcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.bmob.BmobInitHelper;
import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.app.manager.UpdateInfoManager;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.LikesInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.mvp.presenter.MomentPresenter;
import razerdp.friendcircle.app.mvp.view.IMomentView;
import razerdp.friendcircle.app.net.request.MomentsRequest;
import razerdp.friendcircle.app.net.request.SimpleResponseListener;
import razerdp.friendcircle.config.MomentsType;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.friendcircle.ui.adapter.CircleMomentsAdapter;
import razerdp.friendcircle.activity.publish.PublishActivity;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.friendcircle.ui.viewholder.EmptyMomentsVH;
import razerdp.friendcircle.ui.viewholder.MultiImageMomentsVH;
import razerdp.friendcircle.ui.viewholder.TextOnlyMomentsVH;
import razerdp.friendcircle.ui.viewholder.WebMomentsVH;
import razerdp.friendcircle.ui.widget.commentwidget.CommentBox;
import razerdp.friendcircle.ui.widget.commentwidget.CommentWidget;
import razerdp.friendcircle.ui.widget.popup.RegisterPopup;
import razerdp.friendcircle.ui.widget.popup.SelectPhotoMenuPopup;
import razerdp.github.com.baselibrary.manager.KeyboardControlMnanager;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;
import razerdp.github.com.baseuilib.widget.common.TitleBar;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.CircleRecyclerView.OnPreDispatchTouchListener;
import razerdp.github.com.baseuilib.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;

/**
 * Created by 大灯泡 on 2016/10/26.
 * <p>
 * 朋友圈主界面
 */

public class FriendCircleDemoActivity extends BaseTitleBarActivity implements OnRefreshListener2, IMomentView, OnPreDispatchTouchListener {

    private static final int REQUEST_REFRESH = 0x10;
    private static final int REQUEST_LOADMORE = 0x11;


    private CircleRecyclerView circleRecyclerView;
    private CommentBox commentBox;
    private HostViewHolder hostViewHolder;
    private CircleMomentsAdapter adapter;
    private List<MomentsInfo> momentsInfoList;
    //request
    private MomentsRequest momentsRequest;
    private MomentPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateInfoManager.INSTANCE.init(this);
        momentsInfoList = new ArrayList<>();
        momentsRequest = new MomentsRequest();
        initView();
        initKeyboardHeightObserver();

    }

    @Override
    public void onHandleIntent(Intent intent) {

    }


    private void initView() {
        setTitle("朋友圈");
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleRightIcon(R.drawable.ic_camera);
        setTitleLeftText("发现");
        setTitleLeftIcon(R.drawable.back_left);
        presenter = new MomentPresenter(this);

        hostViewHolder = new HostViewHolder(this);
        circleRecyclerView = (CircleRecyclerView) findViewById(R.id.recycler);
        circleRecyclerView.setOnRefreshListener(this);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());

        commentBox = (CommentBox) findViewById(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        CircleMomentsAdapter.Builder<MomentsInfo> builder = new CircleMomentsAdapter.Builder<>(this);
        builder.addType(EmptyMomentsVH.class, MomentsType.EMPTY_CONTENT, R.layout.moments_empty_content)
               .addType(MultiImageMomentsVH.class, MomentsType.MULTI_IMAGES, R.layout.moments_multi_image)
               .addType(TextOnlyMomentsVH.class, MomentsType.TEXT_ONLY, R.layout.moments_only_text)
               .addType(WebMomentsVH.class, MomentsType.WEB, R.layout.moments_web)
               .setData(momentsInfoList)
               .setPresenter(presenter);
        adapter = builder.build();
        circleRecyclerView.setAdapter(adapter);
        circleRecyclerView.autoRefresh();

    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = commentBox.getCommentType();
                if (isVisible) {
                    //定位评论框到view
                    anchorView = alignCommentBoxToView(commentType);
                } else {
                    //定位到底部
                    commentBox.dismissCommentBox(false);
                    alignCommentBoxToViewWhenDismiss(commentType, anchorView);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_REFRESH);
        momentsRequest.setCurPage(0);
        momentsRequest.execute();
    }

    @Override
    public void onLoadMore() {
        momentsRequest.setOnResponseListener(momentsRequestCallBack);
        momentsRequest.setRequestType(REQUEST_LOADMORE);
        momentsRequest.execute();
    }


    //titlebar click


    @Override
    public void onTitleDoubleClick() {
        super.onTitleDoubleClick();
        if (circleRecyclerView != null) {
            int firstVisibleItemPos = circleRecyclerView.findFirstVisibleItemPosition();
            circleRecyclerView.getRecyclerView().smoothScrollToPosition(0);
            if (firstVisibleItemPos > 1) {
                circleRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circleRecyclerView.autoRefresh();
                    }
                }, 200);
            }
        }

    }

    @Override
    public void onTitleLeftClick() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) { // 后退阻断
            UIHelper.ToastMessage("这是朋友圈工程哦，不是整个微信哦~再点一次退出");
            lastClickBackTime = System.currentTimeMillis();
        } else { // 关掉app
            super.onBackPressed();
        }
    }

    @Override
    public void onTitleRightClick() {
        new SelectPhotoMenuPopup(this).setOnSelectPhotoMenuClickListener(new SelectPhotoMenuPopup.OnSelectPhotoMenuClickListener() {
            @Override
            public void onShootClick() {
                UIHelper.ToastMessage("跳到拍摄页面");
            }

            @Override
            public void onAlbumClick() {
                ActivityLauncher.startToPhotoSelectActivity(getActivity());
            }
        }).showPopupWindow();
    }

    @Override
    public boolean onTitleRightLongClick() {
        ActivityLauncher.startToPublishActivityWithResult(this, PublishActivity.Mode.TEXT, 0);
        return true;
    }

    //call back block
    //==============================================
    private SimpleResponseListener<List<MomentsInfo>> momentsRequestCallBack = new SimpleResponseListener<List<MomentsInfo>>() {
        @Override
        public void onSuccess(List<MomentsInfo> response, int requestType) {
            circleRecyclerView.compelete();
            switch (requestType) {
                case REQUEST_REFRESH:
                    if (!ToolUtil.isListEmpty(response)) {
                        KLog.i("firstMomentid", "第一条动态ID   >>>   " + response.get(0).getMomentid());
                        hostViewHolder.loadHostData(LocalHostManager.INSTANCE.getLocalHostUser());
                        adapter.updateData(response);
                    }
                    checkRegister();
                    break;
                case REQUEST_LOADMORE:
                    adapter.addMore(response);
                    break;
            }
        }

        @Override
        public void onError(BmobException e, int requestType) {
            super.onError(e, requestType);
            circleRecyclerView.compelete();
        }
    };


    //=============================================================View's method
    @Override
    public void onLikeChange(int itemPos, List<LikesInfo> likeUserList) {
        MomentsInfo momentsInfo = adapter.findData(itemPos);
        if (momentsInfo != null) {
            momentsInfo.setLikesList(likeUserList);
            adapter.notifyItemChanged(itemPos);
        }
    }

    @Override
    public void onCommentChange(int itemPos, List<CommentInfo> commentInfoList) {
        MomentsInfo momentsInfo = adapter.findData(itemPos);
        if (momentsInfo != null) {
            momentsInfo.setCommentList(commentInfoList);
            adapter.notifyItemChanged(itemPos);
        }
    }

    @Override
    public void showCommentBox(int itemPos, String momentid, CommentWidget commentWidget) {
        commentBox.setDataPos(itemPos);
        commentBox.setCommentWidget(commentWidget);
        commentBox.toggleCommentBox(momentid, commentWidget == null ? null : commentWidget.getData(), false);
    }

    @Override
    public boolean onPreTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }

    //=============================================================tool method

    int[] momentsViewLocation;
    int[] commentWidgetLocation;
    int[] commentBoxViewLocation;

    /**
     * 定位评论框到点击的view
     *
     * @param commentType
     * @return
     */
    private View alignCommentBoxToView(int commentType) {
        // FIXME: 2016/12/13 有可能会获取不到itemView，特别是当view没有完全visible的时候。。。。暂无办法解决
        int firstPos = circleRecyclerView.findFirstVisibleItemPosition();
        int itemPos = commentBox.getDataPos() - firstPos + circleRecyclerView.getHeaderViewCount();
        final View itemView = circleRecyclerView.getRecyclerView().getChildAt(itemPos);
        if (itemView == null) {
            KLog.e("获取不到itemView，pos = " + itemPos);
            return null;
        }
        if (commentType == CommentBox.CommentType.TYPE_CREATE) {
            //对齐到动态底部
            int scrollY = calcuateMomentsViewOffset(itemView);
            circleRecyclerView.getRecyclerView().smoothScrollBy(0, scrollY);
            return itemView;
        } else {
            //对齐到对应的评论
            CommentWidget commentWidget = commentBox.getCommentWidget();
            if (commentWidget == null) return null;
            int scrollY = calcuateCommentWidgetOffset(commentWidget);
            circleRecyclerView.getRecyclerView().smoothScrollBy(0, scrollY);
            return commentWidget;
        }

    }

    /**
     * 输入法消退时，定位到与底部相隔一个评论框的位置
     *
     * @param commentType
     * @param anchorView
     */
    private void alignCommentBoxToViewWhenDismiss(int commentType, View anchorView) {
        if (anchorView == null) return;
        int decorViewHeight = getWindow().getDecorView().getHeight();
        int alignScrollY;
        if (commentType == CommentBox.CommentType.TYPE_CREATE) {
            alignScrollY = decorViewHeight - anchorView.getBottom() - commentBox.getHeight();
        } else {
            Rect rect = new Rect();
            anchorView.getGlobalVisibleRect(rect);
            alignScrollY = decorViewHeight - rect.bottom - commentBox.getHeight();
        }
        circleRecyclerView.getRecyclerView().smoothScrollBy(0, -alignScrollY);
    }

    /**
     * 计算回复评论的偏移
     *
     * @param commentWidget
     * @return
     */
    private int calcuateCommentWidgetOffset(CommentWidget commentWidget) {
        if (commentWidgetLocation == null) commentWidgetLocation = new int[2];
        if (commentWidget == null) return 0;
        commentWidget.getLocationInWindow(commentWidgetLocation);
        return commentWidgetLocation[1] + commentWidget.getHeight() - getCommentBoxViewTopInWindow();
    }

    /**
     * 计算动态评论的偏移
     *
     * @param momentsView
     * @return
     */
    private int calcuateMomentsViewOffset(View momentsView) {
        if (momentsViewLocation == null) momentsViewLocation = new int[2];
        if (momentsView == null) return 0;
        momentsView.getLocationInWindow(momentsViewLocation);
        return momentsViewLocation[1] + momentsView.getHeight() - getCommentBoxViewTopInWindow();
    }

    /**
     * 获取评论框的顶部（因为getTop不准确，因此采取 getLocationInWindow ）
     *
     * @return
     */
    private int getCommentBoxViewTopInWindow() {
        if (commentBoxViewLocation == null) commentBoxViewLocation = new int[2];
        if (commentBox == null) return 0;
        if (commentBoxViewLocation[1] != 0) return commentBoxViewLocation[1];
        commentBox.getLocationInWindow(commentBoxViewLocation);
        return commentBoxViewLocation[1];
    }


    private void checkRegister() {
        boolean hasCheckRegister = (boolean) AppSetting.loadBooleanPreferenceByKey(AppSetting.CHECK_REGISTER, false);
        if (!hasCheckRegister) {
            RegisterPopup registerPopup = new RegisterPopup(FriendCircleDemoActivity.this);
            registerPopup.setOnRegisterSuccess(new RegisterPopup.onRegisterSuccess() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    hostViewHolder.loadHostData(userInfo);
                    UpdateInfoManager.INSTANCE.showUpdateInfo();
                }
            });
            registerPopup.showPopupWindow();
        }else {
            UpdateInfoManager.INSTANCE.showUpdateInfo();
        }
    }

    private long lastClickBackTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) { // 后退阻断
            UIHelper.ToastMessage("再点一次退出");
            lastClickBackTime = System.currentTimeMillis();
        } else { // 关掉app
            super.onBackPressed();
        }
    }

    //=============================================================call back
    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox.OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, String momentid, String commentAuthorId, String commentContent) {
            if (TextUtils.isEmpty(commentContent)) return;
            int itemPos = commentBox.getDataPos();
            if (itemPos < 0 || itemPos > adapter.getItemCount()) return;
            List<CommentInfo> commentInfos = adapter.findData(itemPos).getCommentList();
            presenter.addComment(itemPos, momentid, commentAuthorId, commentContent, commentInfos);
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };


    private static class HostViewHolder {
        private View rootView;
        private ImageView friend_wall_pic;
        private ImageView friend_avatar;
        private ImageView message_avatar;
        private TextView message_detail;
        private TextView hostid;

        public HostViewHolder(Context context) {
            this.rootView = LayoutInflater.from(context).inflate(R.layout.circle_host_header, null);
            this.hostid = (TextView) rootView.findViewById(R.id.host_id);
            this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
            this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
            this.message_avatar = (ImageView) rootView.findViewById(R.id.message_avatar);
            this.message_detail = (TextView) rootView.findViewById(R.id.message_detail);
        }

        public void loadHostData(UserInfo hostInfo) {
            if (hostInfo == null) return;
            ImageLoadMnanger.INSTANCE.loadImage(friend_wall_pic, hostInfo.getCover());
            ImageLoadMnanger.INSTANCE.loadImage(friend_avatar, hostInfo.getAvatar());
            hostid.setText("您的测试ID为: " + hostInfo.getUserid() + '\n' + "您的测试用户名为: " + hostInfo.getNick());
        }

        public View getView() {
            return rootView;
        }

    }
}
