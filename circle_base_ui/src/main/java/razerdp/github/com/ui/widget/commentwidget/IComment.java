package razerdp.github.com.ui.widget.commentwidget;

/**
 * Created by 大灯泡 on 2017/11/17.
 */

public interface IComment<T> {

    /**评论创建者*/
    String getCommentCreatorName();
    /**评论回复人*/
    String getReplyerName();
    /**评论内容*/
    String getCommentContent();

    T getData();

}
