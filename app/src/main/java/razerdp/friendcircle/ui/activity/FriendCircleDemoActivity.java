package razerdp.friendcircle.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ScrollingView;
import android.text.TextUtils;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.adapter.PhotoBoswerPagerAdapter;
import razerdp.friendcircle.app.config.CommonValue;
import razerdp.friendcircle.app.https.base.BaseResponse;
import razerdp.friendcircle.app.https.request.FriendCircleRequest;
import razerdp.friendcircle.app.https.request.RequestType;
import razerdp.friendcircle.app.interfaces.OnSoftKeyboardChangeListener;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.friendcircle.app.mvp.model.entity.DynamicInfo;
import razerdp.friendcircle.app.mvp.model.entity.MomentsInfo;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.app.mvp.presenter.DynamicPresenterImpl;
import razerdp.friendcircle.app.mvp.view.DynamicView;
import razerdp.friendcircle.ui.activity.base.FriendCircleBaseActivity;
import razerdp.friendcircle.utils.FriendCircleAdapterUtil;
import razerdp.friendcircle.utils.InputMethodUtils;
import razerdp.friendcircle.utils.PhotoPagerManager;
import razerdp.friendcircle.utils.PreferenceUtils;
import razerdp.friendcircle.utils.ToastUtils;
import razerdp.friendcircle.utils.UIHelper;
import razerdp.friendcircle.widget.HackyViewPager;
import razerdp.friendcircle.widget.commentwidget.CommentWidget;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

/**
 * Created by 大灯泡 on 2016/2/25.
 * 朋友圈demo窗口
 */
public class FriendCircleDemoActivity extends FriendCircleBaseActivity
        implements DynamicView, View.OnClickListener, OnSoftKeyboardChangeListener {
    private FriendCircleRequest mCircleRequest;
    private DynamicPresenterImpl mPresenter;

    private RelativeLayout titleBar;
    private View friendCircleHeader;

    //input views
    private LinearLayout mInputLayout;
    private EditText mInputBox;
    private TextView mSend;

    //草稿
    private String draftStr;

    //输入法可见状态下的偏移量
    private int currentDynamicPos = 0;
    private CommentWidget mCommentWidget;

    //图片浏览的pager
    private PhotoPagerManager mPhotoPagerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new DynamicPresenterImpl(this);
        initView();
        initReq();
        //mListView.manualRefresh();
        UIHelper.observeSoftKeyboard(this, this);
    }

    private void initView() {
        titleBar = (RelativeLayout) findViewById(R.id.action_bar);

        friendCircleHeader = LayoutInflater.from(this).inflate(R.layout.item_header, null, false);
        bindListView(R.id.listview, friendCircleHeader,
                FriendCircleAdapterUtil.getAdapter(this, mMomentsInfos, mPresenter));

        mInputLayout = (LinearLayout) findViewById(R.id.ll_input);
        mInputBox = (EditText) findViewById(R.id.ed_input);
        mSend = (TextView) findViewById(R.id.btn_send);

        mListView.setOnDispatchTouchEventListener(new FriendCirclePtrListView.OnDispatchTouchEventListener() {
            @Override
            public boolean OnDispatchTouchEvent(MotionEvent ev) {
                if (mInputLayout.getVisibility() == View.VISIBLE) {
                    draftStr = mInputBox.getText().toString().trim();
                    mInputLayout.setVisibility(View.GONE);
                    InputMethodUtils.hideInputMethod(mInputBox);
                    return true;
                }
                return false;
            }
        });

        mSend.setOnClickListener(this);

        mPhotoPagerManager = PhotoPagerManager.create(this, (HackyViewPager) findViewById(R.id.photo_pager),
                findViewById(R.id.photo_container));
    }

    private void initReq() {
        mCircleRequest = new FriendCircleRequest(1001, 0, 8);
        mCircleRequest.setOnResponseListener(this);
    }

    @Override
    public ImageView bindRefreshIcon() {
        return (ImageView) findViewById(R.id.rotate_icon);
    }

    @Override
    public void onPullDownRefresh() {
        mCircleRequest.setStart(0);
        mCircleRequest.execute();
    }

    @Override
    public void onLoadMore() {
        mCircleRequest.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                // 发送

                break;
            case R.id.btn_emoji:
                // TODO: 2016/3/17 如果能力足够- -希望能完成
                // emoji表情

                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //=============================================================mvp - view's method

    @Override
    public void refreshPraiseData(int currentDynamicPos,
                                  @CommonValue.PraiseState int praiseState, @NonNull List<UserInfo> praiseList) {
        MomentsInfo info = mAdapter.getItem(currentDynamicPos);
        if (info != null) {
            info.dynamicInfo.praiseState = praiseState;
            if (info.praiseList != null) {
                info.praiseList.clear();
                info.praiseList.addAll(praiseList);
            }
            else {
                info.praiseList = praiseList;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshCommentData(int currentDynamicPos,
                                   @RequestType.CommentRequestType int requestType,
                                   @NonNull List<CommentInfo> commentList) {
        MomentsInfo info = mAdapter.getItem(currentDynamicPos);
        if (info != null) {
            if (info.commentList != null) {
                info.commentList.clear();
                info.commentList.addAll(commentList);
            }
            else {
                info.commentList = commentList;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInputBox(int currentDynamicPos, CommentWidget commentWidget, DynamicInfo dynamicInfo) {
        this.currentDynamicPos = currentDynamicPos;
        this.mCommentWidget = commentWidget;
        if (!TextUtils.isEmpty(draftStr)) {
            mInputBox.setText(draftStr);
            mInputBox.setSelection(draftStr.length());
        }
        mInputLayout.setVisibility(View.VISIBLE);
        InputMethodUtils.showInputMethod(mInputBox);
    }

    @Override
    public void showPhoto(
            @NonNull ArrayList<String> photoAddress, @NonNull ArrayList<Rect> originViewBounds, int curSelectedPos) {
        mPhotoPagerManager.showPhoto(photoAddress, originViewBounds, curSelectedPos);
    }

    //============================================================= 用于计算键盘弹出后listview的偏移量的方法↓↓↓

    @Override
    public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
        Log.d("keyboardheight", "" + softKeybardHeight + "         visible=     " + visible);
        // 保存软键盘高度
        if ((int) PreferenceUtils.INSTANCE.getSharedPreferenceData("KeyBoardHeight", 0) < softKeybardHeight) {
            PreferenceUtils.INSTANCE.setSharedPreferenceData("KeyBoardHeight", softKeybardHeight);
        }

        // listview偏移
        final int offset = calculateListViewOffset(currentDynamicPos, mCommentWidget, softKeybardHeight);
        Log.d("offset", "offset===========    " + offset);
        // http://stackoverflow.com/questions/11431832/android-smoothscrolltoposition-not-working-correctly
        final int pos = currentDynamicPos + mListView.getHeaderViewsCount();
        mListView.smoothScrollToPositionFromTop(pos, offset);
    }

    private int screenHeight = 0;
    private int statusBarHeight = 0;

    private int calculateListViewOffset(int currentDynamicPos, CommentWidget commentWidget, int keyBoardHeight) {
        int result;
        if (screenHeight == 0) screenHeight = UIHelper.getScreenPixHeight(this);
        if (statusBarHeight == 0) statusBarHeight = UIHelper.getStatusHeight(this);

        if (commentWidget == null) {
            // 评论控件为空，证明回复的是整个动态
            result = getOffsetOfDynamic(currentDynamicPos, keyBoardHeight);
        }
        else {
            // 评论控件不空，证明回复的是评论
            result = getOffsetOfComment(currentDynamicPos, commentWidget, keyBoardHeight);
        }
        return result;
    }

    // 得到动态偏移量
    private int getOffsetOfDynamic(int currentDynamicPos, int keyBoardHeight) {
        int result = 0;
        ListView contentListView = null;
        if (mListView.getContentView() instanceof ListView) {
            contentListView = (ListView) mListView.getContentView();
        }

        if (contentListView == null) return 0;

        int firstItemPos = contentListView.getFirstVisiblePosition();
        int dynamicItemHeight = 0;
        View currentDynamicItem = contentListView.getChildAt(
                currentDynamicPos - firstItemPos + contentListView.getHeaderViewsCount());
        if (currentDynamicItem != null) {
            dynamicItemHeight = currentDynamicItem.getHeight();
            Log.d("dynamicItemHeight", "dynamicItemHeight=========    " + dynamicItemHeight);
        }
        int contentHeight = 0;
        contentHeight = screenHeight - keyBoardHeight - mInputLayout.getHeight();
        result = dynamicItemHeight - contentHeight;
        return -result;
    }

    // 得到评论的偏移量
    private int getOffsetOfComment(int currentDynamicPos, CommentWidget commentWidget, int keyBoardHeight) {
        int result = 0;
        int contentHeight;
        contentHeight = screenHeight - keyBoardHeight - mInputLayout.getHeight();
        try {
            //得到评论控件所在父控件（即评论的父控件）
            final LinearLayout parent = (LinearLayout) commentWidget.getParent();
            //得到评论和点赞的父控件
            final LinearLayout praiseAndCommentParent = (LinearLayout) parent.getParent();
            //得到评论和点赞的父控件的父控件
            final RelativeLayout originParent = (RelativeLayout) praiseAndCommentParent.getParent();
            //得到评论和点赞的父控件的父控件的top
            final int originParentTop = originParent.getTop();
            //得到评论和点赞的父控件的top
            final int praiseAndCommentParentTop = praiseAndCommentParent.getTop();
            //得到评论所在父控件的top
            final int parentTop = parent.getTop();
            //得到当前评论控件的底部（相对于LinearLayout）
            final int currentCommentBottom = commentWidget.getBottom();
            //则当前评论控件相对于item来说，其底部=父类LinearLayout.top+当前控件的底部
            final int currentCommentOffset = originParentTop + praiseAndCommentParentTop + parentTop +
                    currentCommentBottom;

            //则偏移量等于offset-contentheight
            result = currentCommentOffset - contentHeight;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FriendCircleDemoAct", "也许是空指针也许是cast错误哦");
        }
        return -result;
    }
}

