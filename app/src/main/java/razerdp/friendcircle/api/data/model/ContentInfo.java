package razerdp.friendcircle.api.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/25.
 */
public class ContentInfo implements Serializable {
    public List<String> imgurl;
    private String webUrl;
    private String webTitle;
    private long dynamicid;
}
