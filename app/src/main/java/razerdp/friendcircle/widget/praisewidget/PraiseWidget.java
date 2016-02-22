package razerdp.friendcircle.widget.praisewidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;
import java.util.List;
import razerdp.friendcircle.R;
import razerdp.friendcircle.api.data.model.PraiseInfo;
import razerdp.friendcircle.widget.SpannableStringBuilderAllVer;

/**
 * Created by 大灯泡 on 2016/2/21.
 * 点赞显示控件
 */
public class PraiseWidget extends TextView {
    private static final String TAG = "PraiseWidget";

    //点赞名字展示的默认颜色
    private int textColor = 0xff517fae;
    //点赞列表心心默认图标
    private int iconRes = R.drawable.icon_like;
    //默认字体大小
    private int textSize = 16;
    //默认点击背景
    private int clickBg = 0x00000000;

    private List<PraiseInfo> datas;

    private static final LruCache<String, SpannableStringBuilderAllVer> praiseCache
            = new LruCache<String, SpannableStringBuilderAllVer>(50) {
        @Override
        protected int sizeOf(String key, SpannableStringBuilderAllVer value) {
            return 1;
        }
    };

    public PraiseWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PraiseWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PraiseWidget);
        textColor = a.getColor(R.styleable.PraiseWidget_font_color, 0xff517fae);
        textSize = a.getDimensionPixelSize(R.styleable.PraiseWidget_font_size, 16);
        clickBg = a.getColor(R.styleable.PraiseWidget_click_bg_color, 0x00000000);
        iconRes = a.getResourceId(R.styleable.PraiseWidget_like_icon, R.drawable.icon_like);
        a.recycle();
        //如果不设置，clickableSpan不能响应点击事件
        this.setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(clickBg);
    }

    public void setDatas(List<PraiseInfo> datas) {
        this.datas = datas;
        onPreDraw();
    }

    @Override
    public boolean onPreDraw() {
        if (datas == null || datas.size() == 0) {
            return super.onPreDraw();
        }
        else {
            createSpanStringBuilder(datas);
            return true;
        }
    }

    private void createSpanStringBuilder(List<PraiseInfo> datas) {
        if (datas == null || datas.size() == 0) return;
        String key = Integer.toString(datas.hashCode() + datas.size());
        SpannableStringBuilderAllVer spanStrBuilder = praiseCache.get(key);
        if (spanStrBuilder == null) {
            ImageSpan icon = new ImageSpan(getContext(), iconRes, TEXT_ALIGNMENT_GRAVITY);
            //因为spanstringbuilder不支持直接append span，所以通过spanstring转换
            SpannableString iconSpanStr = new SpannableString(" ");
            iconSpanStr.setSpan(icon, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spanStrBuilder = new SpannableStringBuilderAllVer(iconSpanStr);
            //给出两个空格，点赞图标后
            spanStrBuilder.append("  ");
            for (int i = 0; i < datas.size(); i++) {
                ClickEvent clickEvent = new ClickEvent.Builder(getContext(), datas.get(i)).setTextSize(textSize)
                                                                                          .build();
                spanStrBuilder.append(datas.get(i).userNick, clickEvent, 0);
                if (i != datas.size() - 1) spanStrBuilder.append(", ");
                else spanStrBuilder.append("\0");
            }
            praiseCache.put(key, spanStrBuilder);
        }
        setText(spanStrBuilder);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        praiseCache.evictAll();
        if (praiseCache.size() == 0) {
            Log.d(TAG, "clear cache success!");
        }
    }
}
