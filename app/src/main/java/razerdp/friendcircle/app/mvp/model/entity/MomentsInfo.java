package razerdp.friendcircle.app.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈数据实体
 *
 * weblink:http://www.jianshu.com/p/94403e45fbef
 */
public class MomentsInfo implements Serializable {
    public UserInfo userInfo;
    public DynamicInfo dynamicInfo;
    public String textField;
    public List<UserInfo> praiseList;
    public List<CommentInfo> commentList;
    public ContentInfo content;
}
