package razerdp.friendcircle.app.net.request;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.utils.ToolUtil;
import rx.Observable;
import rx.Observer;
import rx.android.plugins.RxAndroidPlugins;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 朋友圈时间线请求
 */

public class MomentsRequest extends BaseRequestClient<List<MomentsInfo>> {

    private int count = 10;
    private int cursor = 0;

    public MomentsRequest() {
    }

    public MomentsRequest setCount(int count) {
        this.count = count;
        return this;
    }


    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        final List<MomentsInfo> moments = new ArrayList<>();
        BmobQuery<MomentsInfo> query = new BmobQuery<>();
        query.include("author,hostinfo");
        query.findObjects(new FindListener<MomentsInfo>() {
            @Override
            public void done(List<MomentsInfo> list, BmobException e) {
                if (!ToolUtil.isListEmpty(list)) {
                    queryCommentAndLikes(list);
                }
            }
        });

    }

    private void queryCommentAndLikes(List<MomentsInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            final MomentsInfo momentsInfo = list.get(i);
            BmobQuery<UserInfo> likesQuery = new BmobQuery<>();
            likesQuery.addWhereRelatedTo("likes", new BmobPointer(momentsInfo));
            likesQuery.findObjects(new FindListener<UserInfo>() {
                @Override
                public void done(List<UserInfo> list, BmobException e) {
                    if (!ToolUtil.isListEmpty(list)) {
                        momentsInfo.setLikesList(list);
                    }
                }
            });

            BmobQuery<CommentInfo> commentQuery = new BmobQuery<>();
            commentQuery.addWhereEqualTo("moment", momentsInfo);
            commentQuery.findObjects(new FindListener<CommentInfo>() {
                @Override
                public void done(List<CommentInfo> list, BmobException e) {
                    if (!ToolUtil.isListEmpty(list)) {
                        momentsInfo.setCommentList(list);
                    }
                }
            });
        }
    }

}
