package razerdp.friendcircle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import razerdp.friendcircle.app.imageload.ImageLoadMnanger;
import razerdp.friendcircle.app.net.request.MomentsRequest;
import razerdp.friendcircle.app.net.request.SimpleResponseListener;
import razerdp.friendcircle.config.MomentsType;
import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.mvp.presenter.MomentPresenter;
import razerdp.friendcircle.mvp.view.IMomentView;
import razerdp.friendcircle.ui.adapter.CircleMomentsAdapter;
import razerdp.friendcircle.ui.viewholder.EmptyMomentsVH;
import razerdp.friendcircle.ui.viewholder.MultiImageMomentsVH;
import razerdp.friendcircle.ui.viewholder.TextOnlyMomentsVH;
import razerdp.friendcircle.ui.viewholder.WebMomentsVH;
import razerdp.friendcircle.utils.ToolUtil;
import razerdp.friendcircle.ui.widget.commentwidget.CommentBox;
import razerdp.friendcircle.ui.widget.commentwidget.CommentWidget;
import razerdp.friendcircle.ui.widget.pullrecyclerview.CircleRecyclerView;
import razerdp.friendcircle.ui.widget.pullrecyclerview.CircleRecyclerView.OnPreDispatchTouchListener;
import razerdp.friendcircle.ui.widget.pullrecyclerview.interfaces.OnRefreshListener2;

/**
 * Created by 大灯泡 on 2016/10/26.
 * <p>
 * 朋友圈主界面
 */

public class FriendCircleDemoActivity extends AppCompatActivity implements OnRefreshListener2, IMomentView, OnPreDispatchTouchListener {

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
        momentsInfoList = new ArrayList<>();
        momentsRequest = new MomentsRequest();
        initView();

    }

    private void initView() {
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
                        hostViewHolder.loadHostData(response.get(0).getHostinfo());
                        adapter.updateData(response);
                    }
                    break;
                case REQUEST_LOADMORE:
                    KLog.i("loadmore compelete");
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
    public void onLikeChange(int itemPos, List<UserInfo> likeUserList) {
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
        commentBox.toggleCommentBox(momentid, commentWidget == null ? null : commentWidget.getData(), false);
    }

    @Override
    public boolean onTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }


    //=============================================================call back
    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox.OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(View v, String momentid, String commentAuthorId, String commentContent) {
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

        public HostViewHolder(Context context) {
            this.rootView = LayoutInflater.from(context).inflate(R.layout.circle_host_header, null);
            this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
            this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
            this.message_avatar = (ImageView) rootView.findViewById(R.id.message_avatar);
            this.message_detail = (TextView) rootView.findViewById(R.id.message_detail);
        }

        public void loadHostData(UserInfo hostInfo) {
            if (hostInfo == null) return;
            ImageLoadMnanger.INSTANCE.loadImage(friend_wall_pic, hostInfo.getCover());
            ImageLoadMnanger.INSTANCE.loadImage(friend_avatar, hostInfo.getAvatar());
        }

        public View getView() {
            return rootView;
        }

    }
}
