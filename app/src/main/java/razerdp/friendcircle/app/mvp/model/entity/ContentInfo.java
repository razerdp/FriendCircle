package razerdp.friendcircle.app.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈内容部分的实体
 *
 * weblink:http://www.jianshu.com/p/94403e45fbef
 */
public class ContentInfo implements Serializable {
    public List<String> imgurl;
    public String webUrl;
    public String webTitle;
    public long dynamicid;
    public String webImg;
}
