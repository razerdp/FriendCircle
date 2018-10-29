package razerdp.friendcircle.activity.circledemo;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.razerdp.github.com.common.MomentsType;
import com.razerdp.github.com.common.entity.CommentInfo;
import com.razerdp.github.com.common.entity.LikesInfo;
import com.razerdp.github.com.common.entity.MomentsInfo;
import com.razerdp.github.com.common.entity.UserInfo;
import com.razerdp.github.com.common.entity.other.ServiceInfo;
import com.razerdp.github.com.common.manager.LocalHostManager;
import com.razerdp.github.com.common.request.MomentsRequest;
import com.razerdp.github.com.common.router.RouterList;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.exception.BmobException;
import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.friendcircle.activity.ActivityLauncher;
import razerdp.friendcircle.app.manager.ServiceInfoManager;
import razerdp.friendcircle.app.manager.UpdateInfoManager;
import razerdp.friendcircle.app.mvp.presenter.impl.MomentPresenter;
import razerdp.friendcircle.app.mvp.view.IMomentView;
import razerdp.friendcircle.ui.adapter.CircleMomentsAdapter;
import razerdp.friendcircle.ui.viewholder.EmptyMomentsVH;
import razerdp.friendcircle.ui.viewholder.MultiImageMomentsVH;
import razerdp.friendcircle.ui.viewholder.TextOnlyMomentsVH;
import razerdp.friendcircle.ui.viewholder.WebMomentsVH;
import razerdp.github.com.lib.common.entity.ImageInfo;
import razerdp.github.com.lib.interfaces.SingleClickListener;
import razerdp.github.com.lib.manager.KeyboardControlMnanager;
import razerdp.github.com.lib.network.base.OnResponseListener;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.ui.base.BaseTitleBarFragment;
import razerdp.github.com.ui.helper.PhotoHelper;
import razerdp.github.com.ui.imageloader.ImageLoadMnanger;
import razerdp.github.com.ui.util.AnimUtils;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.commentwidget.CommentBox;
import razerdp.github.com.ui.widget.commentwidget.CommentWidget;
import razerdp.github.com.ui.widget.commentwidget.IComment;
import razerdp.github.com.ui.widget.common.TitleBar;
import razerdp.github.com.ui.widget.popup.SelectPhotoMenuPopup;
import razerdp.github.com.ui.widget.pullrecyclerview.CircleRecyclerView;
import razerdp.github.com.ui.widget.pullrecyclerview.interfaces.OnRefreshListener2;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
public class FriendCircleFragmentDemo extends BaseTitleBarFragment implements OnRefreshListener2, IMomentView, CircleRecyclerView.OnPreDispatchTouchListener {

    private static final int REQUEST_REFRESH = 0x10;
    private static final int REQUEST_LOADMORE = 0x11;


    private int clickServiceCount = 0;
    private RelativeLayout mTipsLayout;
    private TextView mServiceTipsView;
    private ImageView mCloseImageView;

    private CircleRecyclerView circleRecyclerView;
    private CommentBox commentBox;
    private HostViewHolder hostViewHolder;
    private CircleMomentsAdapter adapter;
    private List<MomentsInfo> momentsInfoList;
    //request
    private MomentsRequest momentsRequest;
    private MomentPresenter presenter;

    private CircleViewHelper mViewHelper;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onInitView(View rootView) {
        momentsInfoList = new ArrayList<>();
        momentsRequest = new MomentsRequest();
        initView(rootView);
        initKeyboardHeightObserver();
        UIHelper.ToastMessage("请尽量不要上传黄图，谢谢");

        UpdateInfoManager.INSTANCE.init(getActivity(), new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                delayCheckServiceInfo();
            }
        });
    }

    private void initView(View rootView) {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(getActivity());
        }
        setTitle("朋友圈");
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleRightIcon(R.drawable.ic_camera);
        setTitleLeftText("发现");
        setTitleLeftIcon(R.drawable.back_left);
        presenter = new MomentPresenter(this);

        hostViewHolder = new HostViewHolder(getContext());
        circleRecyclerView = findView(R.id.recycler);
        circleRecyclerView.setOnRefreshListener(this);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());

        mTipsLayout = findView(R.id.tips_layout);
        mServiceTipsView = findView(R.id.service_tips);
        mCloseImageView = findView(R.id.iv_close);

        commentBox = findView(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        adapter = new CircleMomentsAdapter(getContext(), momentsInfoList, presenter);
        adapter.addViewHolder(EmptyMomentsVH.class, MomentsType.EMPTY_CONTENT)
                .addViewHolder(MultiImageMomentsVH.class, MomentsType.MULTI_IMAGES)
                .addViewHolder(TextOnlyMomentsVH.class, MomentsType.TEXT_ONLY)
                .addViewHolder(WebMomentsVH.class, MomentsType.WEB);
        circleRecyclerView.setAdapter(adapter);
        circleRecyclerView.autoRefresh();

    }

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(getActivity(), new KeyboardControlMnanager.OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = commentBox.getCommentType();
                if (isVisible) {
                    //定位评论框到view
                    anchorView = mViewHelper.alignCommentBoxToView(circleRecyclerView, commentBox, commentType);
                } else {
                    //定位到底部
                    commentBox.dismissCommentBox(false);
                    mViewHelper.alignCommentBoxToViewWhenDismiss(circleRecyclerView, commentBox, commentType, anchorView);
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

    private long lastClickBackTime;

    @Override
    public void onTitleLeftClick() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) { // 后退阻断
            UIHelper.ToastMessage("再点一次返回Activity");
            lastClickBackTime = System.currentTimeMillis();
        } else { // 关掉app
            super.onTitleLeftClick();
        }
    }

    @Override
    public void onTitleRightClick() {
        new SelectPhotoMenuPopup(getActivity()).setOnSelectPhotoMenuClickListener(new SelectPhotoMenuPopup.OnSelectPhotoMenuClickListener() {
            @Override
            public void onShootClick() {
                PhotoHelper.fromCamera(this, false);
            }

            @Override
            public void onAlbumClick() {
                ActivityLauncher.startToPhotoSelectActivity(getActivity(), RouterList.PhotoSelectActivity.requestCode);
            }
        }).showPopupWindow();
    }

    @Override
    public boolean onTitleRightLongClick() {
        ActivityLauncher.startToPublishActivityWithResult(getActivity(), RouterList.PublishActivity.MODE_TEXT, null, RouterList.PublishActivity.requestCode);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoHelper.handleActivityResult(this, requestCode, resultCode, data, new PhotoHelper.PhotoCallback() {
            @Override
            public void onFinish(String filePath) {
                List<ImageInfo> selectedPhotos = new ArrayList<ImageInfo>();
                selectedPhotos.add(new ImageInfo(filePath, null, null, 0, 0));
                ActivityLauncher.startToPublishActivityWithResult(getActivity(),
                        RouterList.PublishActivity.MODE_MULTI,
                        selectedPhotos,
                        RouterList.PublishActivity.requestCode);
            }

            @Override
            public void onError(String msg) {
                UIHelper.ToastMessage(msg);
            }
        });
        if (requestCode == RouterList.PhotoSelectActivity.requestCode && resultCode == RESULT_OK) {
            List<ImageInfo> selectedPhotos = data.getParcelableArrayListExtra(RouterList.PhotoSelectActivity.key_result);
            if (selectedPhotos != null) {
                ActivityLauncher.startToPublishActivityWithResult(getActivity(), RouterList.PublishActivity.MODE_MULTI, selectedPhotos, RouterList.PublishActivity.requestCode);
            }
        }

        if (requestCode == RouterList.PublishActivity.requestCode && resultCode == RESULT_OK) {
            circleRecyclerView.autoRefresh();
        }
    }

    //request
    //==============================================
    private OnResponseListener.SimpleResponseListener<List<MomentsInfo>> momentsRequestCallBack = new OnResponseListener.SimpleResponseListener<List<MomentsInfo>>() {
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
    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid, CommentWidget commentWidget) {
        if (viewHolderRootView != null) {
            mViewHelper.setCommentAnchorView(viewHolderRootView);
        } else if (commentWidget != null) {
            mViewHelper.setCommentAnchorView(commentWidget);
        }
        mViewHelper.setCommentItemDataPosition(itemPos);
        commentBox.toggleCommentBox(momentid, commentWidget == null ? null : commentWidget.getData(), false);
    }

    @Override
    public void onDeleteMomentsInfo(@NonNull MomentsInfo momentsInfo) {
        int pos = adapter.getDatas().indexOf(momentsInfo);
        if (pos < 0) return;
        adapter.deleteData(pos);
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


    //服务器消息检查，非项目所需↓
    private void delayCheckServiceInfo() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        checkServiceInfo();
                    }
                });
    }

    private void checkServiceInfo() {
        ServiceInfoManager.INSTANCE.check(new ServiceInfoManager.OnCheckServiceInfoListener() {
            @Override
            public void onCheckFinish(@Nullable final ServiceInfo serviceInfo) {
                if (serviceInfo != null) {
                    mServiceTipsView.setText(serviceInfo.getTips());
                    mServiceTipsView.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            ActivityLauncher.startToServiceInfoActivity(getActivity(), serviceInfo);
                            clickServiceCount++;
                            applyClose();
                        }
                    });
                    mTipsLayout.animate()
                            .alpha(1)
                            .translationY(UIHelper.dipToPx(50))
                            .setDuration(800)
                            .setInterpolator(new DecelerateInterpolator())
                            .setListener(new AnimUtils.SimpleAnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    mTipsLayout.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mServiceTipsView.requestFocus();
                                }
                            }).start();
                }
            }
        });
    }

    private void applyClose() {
        if (clickServiceCount < 3) return;
        mCloseImageView.setImageResource(R.drawable.ic_close_black);
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTipsLayout.animate()
                        .alpha(0)
                        .translationY(0)
                        .setDuration(800)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(new AnimUtils.SimpleAnimatorListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mTipsLayout.setVisibility(View.GONE);
                            }
                        }).start();
            }
        });
    }

    //服务器消息检查，非项目所需↑

    //=============================================================call back
    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox.OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, IComment comment, String commentContent) {
            if (TextUtils.isEmpty(commentContent)) {
                commentBox.dismissCommentBox(true);
                return;
            }
            int itemPos = mViewHelper.getCommentItemDataPosition();
            if (itemPos < 0 || itemPos > adapter.getItemCount()) return;
            List<CommentInfo> commentInfos = adapter.findData(itemPos).getCommentList();
            String userid = (comment instanceof CommentInfo) ? ((CommentInfo) comment).getAuthor().getUserid() : null;
            presenter.addComment(itemPos, commentBox.getMomentid(), userid, commentContent, commentInfos);
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
            hostid.setText(String.format("您的测试ID为: %s\n您的测试用户名为: %s", hostInfo.getUserid(), hostInfo.getNick()));
        }

        public View getView() {
            return rootView;
        }

    }
}


