package razerdp.friendcircle.utils.bmob;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import razerdp.friendcircle.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.request.AddCommentRequest;
import razerdp.friendcircle.app.net.request.AddMomentsRequest;
import razerdp.friendcircle.app.net.request.SimpleResponseListener;
import razerdp.friendcircle.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 为bmob数据库初始化的工具类
 */

public class BmobInitHelper {
    private static final String TAG = "BmobInitHelper";

    private List<UserInfo> userInfoList;

    public BmobInitHelper() {
    }

    public void initUser(final SimpleResponseListener simpleResponseListener) {
        BmobQuery<UserInfo> userQuery = new BmobQuery<>();
        userQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    if (userInfoList == null) {
                        userInfoList = new ArrayList<UserInfo>();
                    }
                    userInfoList.clear();
                    userInfoList.addAll(list);
                    if (simpleResponseListener != null) {
                        simpleResponseListener.onSuccess("成功", 0);
                    }
                }
            }
        });
    }


    public void addComment(String momentid, String authorid, String replyid, String content) {
        if (ToolUtil.isListEmpty(userInfoList)) return;
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent(content);
        addCommentRequest.setMomentsInfoId(momentid);
        addCommentRequest.setAuthorId(authorid);
        addCommentRequest.setReplyUserId(replyid);
        addCommentRequest.setOnResponseListener(new SimpleResponseListener<CommentInfo>() {
            @Override
            public void onSuccess(CommentInfo response, int requestType) {
                KLog.d(TAG, "添加成功  >>>  " + response.getCommentid());
            }
        });
        addCommentRequest.execute();
    }

    public void addMoments() {
        if (ToolUtil.isListEmpty(userInfoList)) return;
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            AddMomentsRequest addMomentsRequest = new AddMomentsRequest();
            int randomAuthPos = random.nextInt(userInfoList.size());
            KLog.d("随机发布者pos  >>>  " + randomAuthPos);
            String randomAuthId = userInfoList.get(randomAuthPos).getUserid();
            String hostid = "MMbKLCCU";
            addMomentsRequest.setAuthId(randomAuthId);
            addMomentsRequest.setHostId(hostid);

            int randomPicture = random.nextInt(9);
            KLog.d("随机插入图片数量  >>>  " + randomPicture);
            for (int j = 0; j < randomPicture; j++) {
                int picturePos = random.nextInt(BmobTestDatasHelper.getImages().length);
                addMomentsRequest.addPicture(BmobTestDatasHelper.getImage(picturePos));
            }
            int randomLikes = random.nextInt(20);
            KLog.d("随机插入点赞人数  >>>  " + randomLikes);
            for (int j = 0; j < randomLikes; j++) {
                int userPos = random.nextInt(userInfoList.size());
                addMomentsRequest.addLikes(userInfoList.get(userPos));
            }

            int randomText = random.nextInt(1000);
            addMomentsRequest.addText(BmobTestDatasHelper.getText(randomText));

            addMomentsRequest.setOnResponseListener(new SimpleResponseListener<String>() {
                @Override
                public void onSuccess(String response, int requestType) {
                    KLog.d(response);
                }
            });
            addMomentsRequest.execute();
        }
       /* AddMomentsRequest addMomentsRequest = new AddMomentsRequest();
        addMomentsRequest.setAuthId(userInfoList.get(10).getUserid())
                         .setHostId("MMbKLCCU")
                         .addPicture("http://qn.ciyo.cn/upload/FgbnwPphrRD46RsX_gCJ8PxMZLNF")
                         .addPicture("http://qn.ciyo.cn/upload/Fne1GYidTXptXawWJ4j9dn26Fyei")
                         .addPicture("http://qn.ciyo.cn/upload/FpKe7Hwks4CKCGwpNKaLzz_9lb2u")
                         .addPicture("http://qn.ciyo.cn/upload/FnkfSeD7GNnoWcn3vlxiGRrN2sgP")
                         .addText("测试一下")
                         .addLikes(userInfoList.get(0))
                         .addLikes(userInfoList.get(1))
                         .addLikes(userInfoList.get(2))
                         .addLikes(userInfoList.get(3));
        addMomentsRequest.setOnResponseListener(new SimpleResponseListener<String>() {
            @Override
            public void onSuccess(String response, int requestType) {
                KLog.d(response);
            }
        });
        addMomentsRequest.execute();*/

    }


}
