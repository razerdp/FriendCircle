package razerdp.friendcircle.mvp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by 大灯泡 on 2016/12/6.
 */

public interface IComment {


    /**
     * 添加评论
     */
    void addComment(@NonNull String momentsId, @NonNull String content, @NonNull String authorId, @Nullable String replyUserId);

    void removeComment();
}
