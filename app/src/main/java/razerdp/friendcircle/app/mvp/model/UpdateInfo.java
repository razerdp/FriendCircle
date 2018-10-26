package razerdp.friendcircle.app.mvp.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 大灯泡 on 2018/3/8.
 */
public class UpdateInfo extends BmobObject {

    BmobFile file;
    String desc;
    String version;
    int buildCode;

    public BmobFile getFile() {
        return file;
    }

    public UpdateInfo setFile(BmobFile file) {
        this.file = file;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public UpdateInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public UpdateInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public int getBuildCode() {
        return buildCode;
    }

    public UpdateInfo setBuildCode(int buildCode) {
        this.buildCode = buildCode;
        return this;
    }
}
