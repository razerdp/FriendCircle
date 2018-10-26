package razerdp.github.com.lib.manager.compress;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/10.
 */
public class CompressTaskQueue extends BaseCompressTaskHelper<List<CompressOption>> {

    private List<CompressTaskHelper> mTaskHelpers;
    private List<CompressResult> result;
    private int taskSize;
    private volatile boolean abort = false;

    CompressTaskQueue(Context context, List<CompressOption> options, OnCompressListener onCompressListener) {
        super(context, options, onCompressListener);
    }


    @Override
    public void start() {
        if (ToolUtil.isListEmpty(data)) {
            callError("配置为空");
            return;
        }
        if (mTaskHelpers == null) {
            prepare();
        }
        startInternal();
    }

    private void prepare() {
        mTaskHelpers = new ArrayList<>();
        result = new ArrayList<>();
        for (CompressOption option : data) {
            //把每个单张图片的task的listener切换为本类的listener
            mTaskHelpers.add(new CompressTaskHelper(mContext, option, mOnCompressListener));
        }
        taskSize = mTaskHelpers.size();
    }

    private void startInternal() {
        if (ToolUtil.isListEmpty(mTaskHelpers) && result.size() == taskSize) {
            //如果已经全部完成，并检查之后,则意味着已经success了
            return;
        }
        if (abort) {
            mTaskHelpers.clear();
            return;
        }
        //否则移除第一个并开始执行
        mTaskHelpers.remove(0).start();
        callCompress(taskSize - mTaskHelpers.size(), taskSize);
    }


    //-----------------------------------------single listener-----------------------------------------
    private OnCompressListener mOnCompressListener = new OnCompressListener() {

        @Override
        public void onRotate(int picIndex, int width, int height) {
            callRotated(taskSize - mTaskHelpers.size(), width, height);
        }


        @Override
        public void onSuccess(List<CompressResult> imagePath) {
            result.add(imagePath.get(0));
            if (result.size() == taskSize) {
                callSuccess(result);
                return;
            }
            startInternal();
        }

        @Override
        public void onCompress(long current, long target) {

        }

        @Override
        public void onError(String tag) {
            abort = true;
            callError(tag);
        }
    };
}
