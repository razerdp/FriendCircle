package razerdp.github.com.lib.network.okhttp;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by 大灯泡 on 2019/2/13.
 */
public class ProgressResponseBodyWrapper extends ResponseBody {

    private ResponseBody target;
    private DownloadListener mDownloadListener;
    private BufferedSource mBufferedSource;

    public ProgressResponseBodyWrapper(ResponseBody target, DownloadListener downloadListener) {
        this.target = target;
        mDownloadListener = downloadListener;
    }

    public ProgressResponseBodyWrapper setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
        return this;
    }

    @Override
    public MediaType contentType() {
        return target.contentType();
    }

    @Override
    public long contentLength() {
        return target.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(wrapSource(target.source()));
        }
        return mBufferedSource;
    }

    /**
     * 包裹原response的读写流
     *
     * @param source
     * @return
     */
    private Source wrapSource(Source source) {
        return new ForwardingSource(source) {
            long totalReade = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long readeBytes = super.read(sink, byteCount);
                //readeBytes=-1意味着读取完成或者contentLength不知道长度
                totalReade += readeBytes == -1 ? 0 : readeBytes;
                if (mDownloadListener != null) {
                    final int progress = (int) (totalReade * 100 / contentLength());
                    mDownloadListener.onProgress(progress);
                    if (progress>=100){
                        mDownloadListener = null;
                    }
                }

                return readeBytes;
            }
        };
    }
}
