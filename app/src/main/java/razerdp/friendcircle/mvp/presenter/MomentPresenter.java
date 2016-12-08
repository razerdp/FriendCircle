package razerdp.friendcircle.mvp.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.config.Define;
import razerdp.friendcircle.mvp.callback.OnLikeChangeCallback;
import razerdp.friendcircle.mvp.model.CommentImpl;
import razerdp.friendcircle.mvp.model.LikeImpl;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.mvp.view.IMomentView;
import razerdp.friendcircle.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 朋友圈presenter
 */

public class MomentPresenter implements IMomentPresenter {
    private IMomentView momentView;
    private CommentImpl commentModel;
    private LikeImpl likeModel;

    public MomentPresenter() {
        this(null);
    }

    public MomentPresenter(IMomentView momentView) {
        this.momentView = momentView;
        commentModel = new CommentImpl();
        likeModel = new LikeImpl();
    }

    @Override
    public IBasePresenter<IMomentView> bindView(IMomentView view) {
        this.momentView = view;
        return this;
    }

    @Override
    public IBasePresenter<IMomentView> unbindView() {
        return this;
    }

    @Override
    public void addLike(final int viewHolderPos, String momentid, final List<UserInfo> currentLikeUserList) {
        likeModel.addLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLike() {
                List<UserInfo> resultLikeList = new ArrayList<UserInfo>();
                if (!ToolUtil.isListEmpty(currentLikeUserList)) {
                    resultLikeList.addAll(currentLikeUserList);
                }
                boolean hasLocalLiked = findPosByUserid(resultLikeList, LocalHostManager.INSTANCE.getUserid()) > -1;
                if (!hasLocalLiked) {
                    resultLikeList.add(0, LocalHostManager.INSTANCE.getLocalHostUser());
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

            @Override
            public void onUnLike() {

            }

        });
    }

    @Override
    public void unLike(final int viewHolderPos, String momentid, final List<UserInfo> currentLikeUserList) {
        likeModel.unLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLike() {

            }

            @Override
            public void onUnLike() {
                List<UserInfo> resultLikeList = new ArrayList<UserInfo>();
                if (!ToolUtil.isListEmpty(currentLikeUserList)) {
                    resultLikeList.addAll(currentLikeUserList);
                }
                final int localLikePos = findPosByUserid(resultLikeList, LocalHostManager.INSTANCE.getUserid());
                if (localLikePos > -1) {
                    resultLikeList.remove(localLikePos);
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }

        });
    }


    /**
     * 从用户列表寻找符合userid的对象的index
     *
     * @param userInfoList
     * @param userid
     * @return -1:找不到
     */
    private int findPosByUserid(List<UserInfo> userInfoList, String userid) {
        int result = -1;
        if (ToolUtil.isListEmpty(userInfoList)) return result;
        for (int i = 0; i < userInfoList.size(); i++) {
            UserInfo userinfo = userInfoList.get(i);
            if (TextUtils.equals(userinfo.getUserid(), userid)) {
                result = i;
                break;
            }
        }
        return result;
    }


    //------------------------------------------interface impl-----------------------------------------------
}
