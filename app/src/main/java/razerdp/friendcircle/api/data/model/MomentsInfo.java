package razerdp.friendcircle.api.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/25.
 */
public class MomentsInfo implements Serializable {
    public UserInfo userInfo;
    public DynamicInfo dynamicInfo;
    public String textField;
    public List<UserInfo> praiseList;
    public List<CommentInfo> commentList;
    public ContentInfo content;
}
