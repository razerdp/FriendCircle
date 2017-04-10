package razerdp.friendcircle.app.net.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.LikesInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo.MomentsFields;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.net.base.BaseRequestClient;

import static razerdp.friendcircle.app.mvp.model.entity.CommentInfo.CommentFields.AUTHOR_USER;
import static razerdp.friendcircle.app.mvp.model.entity.CommentInfo.CommentFields.MOMENT;
import static razerdp.friendcircle.app.mvp.model.entity.CommentInfo.CommentFields.REPLY_USER;


/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 朋友圈时间线请求
 */

public class MomentsRequest extends BaseRequestClient<List<MomentsInfo>> {

    private int count = 10;
    private int curPage = 0;

    private static boolean isFirstRequest = true;

    public MomentsRequest() {
    }

    public MomentsRequest setCount(int count) {
        this.count = (count <= 0 ? 10 : count);
        return this;
    }

    public MomentsRequest setCurPage(int page) {
        this.curPage = page;
        return this;
    }


    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        BmobQuery<MomentsInfo> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include(MomentsFields.AUTHOR_USER + "," + MomentsFields.HOST);
        query.setLimit(count);
        query.setSkip(curPage * count);
        query.setCachePolicy(isFirstRequest? BmobQuery.CachePolicy.CACHE_ELSE_NETWORK: BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<MomentsInfo>() {
            @Override
            public void done(List<MomentsInfo> list, BmobException e) {
                if (!ToolUtil.isListEmpty(list)) {
                    queryCommentAndLikes(list);
                }
            }
        });

    }

    private void queryCommentAndLikes(final List<MomentsInfo> momentsList) {
        /**
         * 因为bmob不支持在查询时把关系表也一起填充查询，因此需要手动再查一次，同时分页也要手动实现。。
         * oRz，果然没有自己写服务器来的简单，好吧，都是在下没钱的原因，我的锅
         */
        final List<CommentInfo> commentInfoList = new ArrayList<>();
        final List<LikesInfo> likesInfoList = new ArrayList<>();

        final boolean[] isCommentRequestFin = {false};
        final boolean[] isLikesRequestFin = {false};

        BmobQuery<CommentInfo> commentQuery = new BmobQuery<>();
        commentQuery.include(MOMENT + "," + REPLY_USER + "," + AUTHOR_USER);
        List<String> id = new ArrayList<>();
        for (MomentsInfo momentsInfo : momentsList) {
            id.add(momentsInfo.getObjectId());
        }
        commentQuery.addWhereContainedIn(CommentInfo.CommentFields.MOMENT, id);
        commentQuery.order("createdAt");
        commentQuery.setLimit(1000);//默认只有100条数据，最多1000条
        commentQuery.setCachePolicy(isFirstRequest? BmobQuery.CachePolicy.CACHE_ELSE_NETWORK: BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        commentQuery.findObjects(new FindListener<CommentInfo>() {
            @Override
            public void done(List<CommentInfo> list, BmobException e) {
                isCommentRequestFin[0] = true;
                if (!ToolUtil.isListEmpty(list)) {
                    commentInfoList.addAll(list);
                }
                mergeData(isCommentRequestFin[0], isLikesRequestFin[0], commentInfoList, likesInfoList, momentsList, e);
            }
        });

        BmobQuery<LikesInfo> likesInfoBmobQuery = new BmobQuery<>();
        likesInfoBmobQuery.include(LikesInfo.LikesField.MOMENTID + "," + LikesInfo.LikesField.USERID);
        likesInfoBmobQuery.addWhereContainedIn(LikesInfo.LikesField.MOMENTID, id);
        likesInfoBmobQuery.order("createdAt");
        likesInfoBmobQuery.setLimit(1000);
        likesInfoBmobQuery.setCachePolicy(isFirstRequest? BmobQuery.CachePolicy.CACHE_ELSE_NETWORK: BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        likesInfoBmobQuery.findObjects(new FindListener<LikesInfo>() {
            @Override
            public void done(List<LikesInfo> list, BmobException e) {
                isLikesRequestFin[0] = true;
                if (!ToolUtil.isListEmpty(list)) {
                    likesInfoList.addAll(list);
                }
                mergeData(isCommentRequestFin[0], isLikesRequestFin[0], commentInfoList, likesInfoList, momentsList, e);
            }
        });

    }


    private void mergeData(boolean isCommentRequestFin,
                           boolean isLikeRequestFin,
                           List<CommentInfo> commentInfoList,
                           List<LikesInfo> likesInfoList,
                           List<MomentsInfo> momentsList,
                           BmobException e) {
        if (!isCommentRequestFin || !isLikeRequestFin) return;
        if (e != null) {
            onResponseError(e, getRequestType());
            return;
        }
        if (ToolUtil.isListEmpty(momentsList)) {
            onResponseError(new BmobException("动态数据为空"), getRequestType());
            return;
        }
        curPage++;

        HashMap<String, MomentsInfo> map = new HashMap<>();
        for (MomentsInfo momentsInfo : momentsList) {
            map.put(momentsInfo.getMomentid(), momentsInfo);
        }

        for (CommentInfo commentInfo : commentInfoList) {
            MomentsInfo info = map.get(commentInfo.getMoment().getMomentid());
            if (info != null) {
                info.addComment(commentInfo);
            }
        }

        for (LikesInfo likesInfo : likesInfoList) {
            MomentsInfo info = map.get(likesInfo.getMomentsid());
            if (info != null) {
                info.addLikes(likesInfo);
            }
        }

        onResponseSuccess(momentsList, getRequestType());

    }

    @Override
    protected void onResponseSuccess(List<MomentsInfo> response, int requestType) {
        super.onResponseSuccess(response, requestType);
        isFirstRequest = false;
    }
}
