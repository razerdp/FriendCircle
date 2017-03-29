package razerdp.friendcircle.app.bmob;

import android.os.SystemClock;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.net.request.AddCommentRequest;
import razerdp.friendcircle.app.net.request.AddLikeRequest;
import razerdp.friendcircle.app.net.request.AddMomentsRequest;
import razerdp.friendcircle.app.net.request.SimpleResponseListener;
import razerdp.github.com.baselibrary.utils.ToolUtil;
import razerdp.github.com.net.base.OnResponseListener;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 为bmob数据库初始化的工具类
 */

public class BmobInitHelper {
    private static final String TAG = "BmobInitHelper";

    private List<UserInfo> userInfoList;
    private List<MomentsInfo> momentsList;

    /*
        //构建评论伪数据
        final BmobInitHelper bmobInitHelper=new BmobInitHelper();
        bmobInitHelper.findAllUsers(new SimpleResponseListener() {
            @Override
            public void onSuccess(Object response, int requestType) {
                bmobInitHelper.findAllMoments(new SimpleResponseListener() {
                    @Override
                    public void onSuccess(Object response, int requestType) {
                        KLog.i("initComment","开始");
                        bmobInitHelper.addComments();
                    }
                });
            }
        });*/


    public BmobInitHelper() {
    }

    public void findAllUsers(final SimpleResponseListener simpleResponseListener) {
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

    public void findAllMoments(final SimpleResponseListener simpleResponseListener) {
        BmobQuery<MomentsInfo> momentQuery = new BmobQuery<>();
        momentQuery.order("-createdAt");
        momentQuery.findObjects(new FindListener<MomentsInfo>() {
            @Override
            public void done(List<MomentsInfo> list, BmobException e) {
                if (e == null) {
                    if (momentsList == null) {
                        momentsList = new ArrayList<MomentsInfo>();
                    }
                    momentsList.clear();
                    momentsList.addAll(list);
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


    /**
     * 建立评论伪数据
     */
    public void addComments() {
        if (ToolUtil.isListEmpty(momentsList) || ToolUtil.isListEmpty(userInfoList)) return;
        Random random = new Random();
        //随机为50条动态添加评论
        for (int i = 0; i < 50; i++) {
            int momentIndex = random.nextInt(momentsList.size());
            int authorIndex = random.nextInt(userInfoList.size());
            int commentContentIndex = random.nextInt(BmobTestDatasHelper.getCommentDatas().length);
            int replyIndex = -1;
            boolean isReply = random.nextBoolean();
            if (isReply) {
                do {
                    replyIndex = random.nextInt(userInfoList.size());
                } while (replyIndex == authorIndex);
            }

            String momentid = momentsList.get(momentIndex).getMomentid();
            String authorId = userInfoList.get(authorIndex).getUserid();
            String comment = BmobTestDatasHelper.getCommentText(commentContentIndex);
            String replyUserid;
            if (isReply) {
                String replyCommentContent = BmobTestDatasHelper.getCommentText(random.nextInt(commentContentIndex));
                replyUserid = userInfoList.get(replyIndex).getUserid();
                //先添加评论，稍微延后一点再添加回复
                addComment(momentid, authorId, null, comment);
                SystemClock.sleep(200);
                addComment(momentid, replyUserid, authorId, replyCommentContent);
            } else {
                addComment(momentid, authorId, null, comment);
            }

        }

    }

    public void addLikes() {
        if (ToolUtil.isListEmpty(userInfoList)) return;
        if (ToolUtil.isListEmpty(momentsList)) return;

        Random random = new Random();

        List<String> userList = new ArrayList<>();
        List<String> momentList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            int randomIndex = random.nextInt(100);

            String momentsid = momentsList.get(randomIndex % momentsList.size()).getMomentid();
            if (momentList.contains(momentsid)) {
                continue;
            } else {
                momentList.add(momentsid);
            }

            int likeUsers = random.nextInt(20);

            for (int j = 0; j < likeUsers; j++) {
                int userIndex = random.nextInt(100);
                String userid = userInfoList.get(userIndex % userInfoList.size()).getUserid();
                if (userList.contains(userid)) {
                    continue;
                } else {
                    userList.add(userid);
                }
                AddLikeRequest request = new AddLikeRequest(momentsid).setUserid(userid);
                request.setOnResponseListener(new OnResponseListener.SimpleResponseListener<String>() {
                    @Override
                    public void onError(BmobException e, int requestType) {
                        super.onError(e, requestType);
                        KLog.e(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response, int requestType) {
                        KLog.i(response);
                    }
                });
                request.execute();
            }
            userList.clear();

        }
    }


}
