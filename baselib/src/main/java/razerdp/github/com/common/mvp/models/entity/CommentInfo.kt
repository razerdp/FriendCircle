package razerdp.github.com.common.mvp.models.entity

import android.text.TextUtils

import cn.bmob.v3.BmobObject
import razerdp.github.com.common.manager.LocalHostManager

/**
 * Created by 大灯泡 on 2016/10/27.
 *
 *
 * 评论
 */

 class CommentInfo : BmobObject() {


    var moment: MomentsInfo? = null
    var content: String? = null
    var author: UserInfo? = null
    var reply: UserInfo? = null

    val commentid: String
        get() = objectId

    interface CommentFields {
        companion object {
            val REPLY_USER = "reply"
            val MOMENT = "moment"
            val CONTENT = "content"
            val AUTHOR_USER = "author"
        }
    }

    fun canDelete(): Boolean {
        return author != null && TextUtils.equals(author!!.userid, LocalHostManager.INSTANCE.userid)
    }


    override fun toString(): String {
        return "CommentInfo{" +
                "moment=" + moment +
                ", content='" + content + '\'' +
                ", author=" + author!!.toString() + '\n' +
                ", reply=" + (if (reply == null) "null" else reply!!.toString()) + '\n' +
                '}'
    }
}
