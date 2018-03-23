package razerdp.friendcircle.app.mvp.presenter.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.razerdp.github.com.common.entity.CommentInfo;
import com.razerdp.github.com.common.entity.LikesInfo;
import com.razerdp.github.com.common.entity.MomentsInfo;
import com.razerdp.github.com.common.manager.LocalHostManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.mvp.callback.OnCommentChangeCallback;
import razerdp.friendcircle.app.mvp.callback.OnLikeChangeCallback;
import razerdp.friendcircle.app.mvp.presenter.IMomentPresenter;
import razerdp.friendcircle.app.mvp.view.IMomentView;
import razerdp.github.com.lib.mvp.IBasePresenter;
import razerdp.github.com.lib.utils.ToolUtil;
import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 朋友圈presenter
 */

public class MomentPresenter implements IMomentPresenter {
    private IMomentView momentView;
    private CommentImpl commentModel;
    private LikeImpl likeModel;

    public MomentPresenter() {
        this(null);
    }

    public MomentPresenter(IMomentView momentView) {
        this.momentView = momentView;
        commentModel = new CommentImpl();
        likeModel = new LikeImpl();
    }

    @Override
    public IBasePresenter<IMomentView> bindView(IMomentView view) {
        this.momentView = view;
        return this;
    }

    @Override
    public IBasePresenter<IMomentView> unbindView() {
        return this;
    }

    //=============================================================动作控制
    @Override
    public void addLike(final int viewHolderPos, final String momentid, final List<LikesInfo> currentLikeList) {
        likeModel.addLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLike(String likeinfoid) {
                List<LikesInfo> resultLikeList = new ArrayList<LikesInfo>();
                if (!ToolUtil.isListEmpty(currentLikeList)) {
                    resultLikeList.addAll(currentLikeList);
                }
                boolean hasLocalLiked = findLikeInfoPosByUserid(resultLikeList, LocalHostManager.INSTANCE.getUserid()) > -1;
                if (!hasLocalLiked && !TextUtils.isEmpty(likeinfoid)) {
                    LikesInfo info = new LikesInfo();
                    info.setObjectId(likeinfoid);
                    info.setMomentsid(momentid);
                    info.setUserInfo(LocalHostManager.INSTANCE.getLocalHostUser());
                    resultLikeList.add(info);
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

            @Override
            public void onUnLike() {

            }

        });
    }

    @Override
    public void unLike(final int viewHolderPos, String likesid, final List<LikesInfo> currentLikeList) {
        likeModel.unLike(likesid, new OnLikeChangeCallback() {
            @Override
            public void onLike(String likeinfoid) {

            }

            @Override
            public void onUnLike() {
                List<LikesInfo> resultLikeList = new ArrayList<LikesInfo>();
                if (!ToolUtil.isListEmpty(currentLikeList)) {
                    resultLikeList.addAll(currentLikeList);
                }
                final int localLikePos = findLikeInfoPosByUserid(resultLikeList, LocalHostManager.INSTANCE.getUserid());
                if (localLikePos > -1) {
                    resultLikeList.remove(localLikePos);
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

        });
    }

    @Override
    public void addComment(final int viewHolderPos, String momentid, String replyUserid, String commentContent, final List<CommentInfo> currentCommentList) {
        if (TextUtils.isEmpty(commentContent)) return;
        commentModel.addComment(momentid, LocalHostManager.INSTANCE.getUserid(), replyUserid, commentContent, new OnCommentChangeCallback() {
            @Override
            public void onAddComment(CommentInfo response) {
                List<CommentInfo> commentList = new ArrayList<CommentInfo>();
                if (!ToolUtil.isListEmpty(currentCommentList)) {
                    commentList.addAll(currentCommentList);
                }
                commentList.add(response);
                KLog.i("comment", "评论成功 >>>  " + response.toString());
                if (momentView != null) {
                    momentView.onCommentChange(viewHolderPos, commentList);
                }

            }

            @Override
            public void onDeleteComment(String response) {

            }
        });
    }

    @Override
    public void deleteComment(final int viewHolderPos, String commentid, final List<CommentInfo> currentCommentList) {
        if (TextUtils.isEmpty(commentid)) return;
        commentModel.deleteComment(commentid, new OnCommentChangeCallback() {
            @Override
            public void onAddComment(CommentInfo response) {

            }

            @Override
            public void onDeleteComment(String commentid) {
                if (TextUtils.isEmpty(commentid)) return;
                List<CommentInfo> resultLikeList = new ArrayList<CommentInfo>();
                if (!ToolUtil.isListEmpty(currentCommentList)) {
                    resultLikeList.addAll(currentCommentList);
                }
                Iterator<CommentInfo> iterator = resultLikeList.iterator();
                while (iterator.hasNext()) {
                    CommentInfo info = iterator.next();
                    if (TextUtils.equals(info.getCommentid(), commentid)) {
                        iterator.remove();
                        break;
                    }
                }
                KLog.i("comment", "删除评论成功 >>>  " + commentid);
                if (momentView != null) {
                    momentView.onCommentChange(viewHolderPos, resultLikeList);
                }

            }
        });

    }

    @Override
    public void deleteMoments(Context context, @NonNull final MomentsInfo momentsInfo) {
        assert momentsInfo != null : "momentsInfo为空";
        new AlertDialog.Builder(context)
                .setTitle("删除动态")
                .setMessage("确定删除吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        momentsInfo.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null && momentView != null) {
                                    deleteFiles(momentsInfo);
                                    momentView.onDeleteMomentsInfo(momentsInfo);
                                } else {
                                    UIHelper.ToastMessage("删除失败");
                                }
                            }
                        });
                    }
                }).show();
    }

    private void deleteFiles(MomentsInfo momentsInfo) {
        if (momentsInfo == null) return;
        final List<String> pics = momentsInfo.getContent().getPics();
        if (ToolUtil.isListEmpty(pics)) return;
        for (final String pic : pics) {
            BmobFile file = new BmobFile();
            file.setUrl(pic);
            file.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        KLog.d("delPic", "文件删除成功 : " + pic);
                    } else {
                        KLog.d("delPic", "文件删除失败：" + e.getErrorCode() + "," + e.getMessage());
                    }
                }
            });
        }
    }


    public void showCommentBox(@Nullable View viewHolderRootView, int itemPos, String momentid, @Nullable CommentWidget commentWidget) {
        if (momentView != null) {
            momentView.showCommentBox(viewHolderRootView, itemPos, momentid, commentWidget);
        }
    }


    private int findLikeInfoPosByUserid(List<LikesInfo> infoList, String id) {

        int result = -1;
        if (ToolUtil.isListEmpty(infoList)) return result;
        for (int i = 0; i < infoList.size(); i++) {
            LikesInfo info = infoList.get(i);
            if (TextUtils.equals(info.getUserid(), id)) {
                result = i;
                break;
            }
        }
        return result;
    }


    //------------------------------------------interface impl-----------------------------------------------
}
