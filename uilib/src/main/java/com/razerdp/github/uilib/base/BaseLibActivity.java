package com.razerdp.github.uilib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.utils.log.KLog;
import com.razerdp.github.lib.utils.ToolUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * BaseLibActivity
 */

public abstract class BaseLibActivity<T extends BaseLibActivity.IntentData> extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    public static final String KEY_INTENT = "KEY_INTENT";
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("当前打开 :  " + KLog.wrapLocation(getClass(), 1));
        onHandleIntent(getIntent());
        if (layoutId() != 0) {
            setContentView(layoutId());
        }
    }

    public void onHandleIntent(Intent intent) {

    }

    @LayoutRes
    public abstract int layoutId();

    protected abstract void onInitView(View rootView);

    public BaseLibActivity getContext() {
        return BaseLibActivity.this;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onAttachContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onAttachContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onAttachContentView();
    }

    protected void onAttachContentView() {
        //防止多次调用onInitView，一般activity只会set一次contentView
        if (mUnbinder != null) return;
        mUnbinder = ButterKnife.bind(this);
        onInitView(getWindow().getDecorView());
    }


    /**
     * Activity传参协议
     * 所有属于本类的参数将一并传入，parcelable需要特殊处理
     */
    public static abstract class IntentData implements Serializable {
        int requestCode = -1;
        //该map不参与serializable的序列化，仅仅在put into intent之前和新activity获取到intent之后存在
        private HashMap<String, Parcelable> mParcelableMap;

        public int getRequestCode() {
            return requestCode;
        }

        public IntentData setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }


        public <P extends Parcelable> IntentData appendParcelable(@NonNull P who) {
            Class cls = who.getClass();
            return appendParcelable(cls.getName(), who);
        }

        public <P extends Parcelable> IntentData appendParcelable(@NonNull String key, @NonNull P who) {
            if (mParcelableMap == null) {
                mParcelableMap = new HashMap<>();
            }
            mParcelableMap.put(key, who);
            return this;
        }

        public final Intent writeToIntent(@NonNull Intent intent) {
            if (!ToolUtil.isEmpty(mParcelableMap)) {
                for (Map.Entry<String, Parcelable> entry : mParcelableMap.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
                //put 完之后必须清楚parcelablemap，否则Serializable和Parcelable一起存在的时候会冲突
                mParcelableMap.clear();
                mParcelableMap = null;
            }

            //处理完parcelable再处理自己（Serializable）
            intent.putExtra(KEY_INTENT, this);
            return intent;
        }

        @Nullable
        public <P extends Parcelable> P getParcel(Activity act, Class<P> parcelClass) {
            return getParcel(act, parcelClass.getName());
        }

        @Nullable
        public <P extends Parcelable> P getParcel(Activity act, String key) {
            return act.getIntent().getParcelableExtra(key);
        }
    }
}
