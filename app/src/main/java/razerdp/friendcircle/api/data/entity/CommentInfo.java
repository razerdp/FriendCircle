package razerdp.friendcircle.api.data.entity;

import razerdp.friendcircle.widget.commentwidget.CommentWidget;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论用的bean
 * @see CommentWidget
 */
public class CommentInfo  {

    /**
     * nick : 诗雁
     * avatar : http://img4.duitang.com/uploads/item/201601/11/20160111175420_ZmTzU.jpeg
     * userId : 1006
     */

    public UserInfo userA;
    /**
     * nick : 羽翼君
     * avatar : http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg
     * userId : 1001
     */

    public UserInfo userB;
    /**
     * userA : {"nick":"诗雁","avatar":"http://img4.duitang.com/uploads/item/201601/11/20160111175420_ZmTzU.jpeg","userId":1006}
     * userB : {"nick":"羽翼君","avatar":"http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg","userId":1001}
     * commentId : 1
     * content : 哇，好巧-V-
     * candelete : 1
     * createTime : 1454483715
     */

    public long commentId;
    public String content;
    public int candelete;
    public long createTime;
}
