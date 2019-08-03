package com.razerdp.github.uilib.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.utils.log.KLog;
import com.razerdp.github.lib.utils.UnsafeUtil;
import com.razerdp.github.lib.utils.joor.Reflect;
import com.razerdp.github.lib.utils.joor.ReflectException;
import com.razerdp.github.uilib.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
public class MultiRecyclerViewAdapter<T extends MultiType> extends BaseRecyclerViewAdapter<T> {

    private static HashMap<String, Integer> mViewHolderIdCache = new HashMap<>();
    private SparseArray<ViewHolderInfo> mHolderInfos = new SparseArray<>();
    private static final Class<?>[] INSTANCE_TYPE = {View.class};

    private WeakReference<Object> outher;

    public MultiRecyclerViewAdapter(Context context) {
        super(context);
    }

    public MultiRecyclerViewAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    public MultiRecyclerViewAdapter appendHolder(Class<? extends BaseMultiRecyclerViewHolder> holderClass) {
        return appendHolder(holderClass, holderClass.hashCode());
    }

    public MultiRecyclerViewAdapter appendHolder(Class<? extends BaseMultiRecyclerViewHolder> holderClass, int... viewType) {
        for (int i : viewType) {
            if (mHolderInfos.get(i) == null) {
                mHolderInfos.put(i, new ViewHolderInfo(holderClass, i));
            }
        }
        return this;
    }

    public MultiRecyclerViewAdapter outher(Object object) {
        this.outher = new WeakReference<>(object);
        return this;
    }

    @Override
    public int onGetViewType(T data, int position) {
        return data.getItemType();
    }

    @Override
    public int holderLayout(int viewType) {
        ViewHolderInfo info = mHolderInfos.get(viewType);
        return info == null ? 0 : info.getLayoutId();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder createViewHolder(@NonNull ViewGroup parent, @NonNull View itemView, int viewType) {
        KLog.i(TAG, "创建multitype viewholder：", viewType);
        if (mHolderInfos.size() < 0) {
            KLog.e(TAG, "holder没有注册，请调用#appendHolder添加holder信息");
            return createEmptyHolder();
        }
        ViewHolderInfo info = mHolderInfos.get(viewType);
        if (info == null) {
            KLog.e(TAG, "无法获取该viewType的holder信息", viewType);
            return createEmptyHolder();
        }
        final int id = info.getLayoutId();

        if (id == 0) {
            KLog.e(TAG, "id为0", info.getHolderClass());
            return createEmptyHolder();
        }

        if (itemView == null) {
            itemView = ViewUtil.inflate(id, parent, false);
        }

        try {
            return Reflect.on(info.getHolderClass()).create(INSTANCE_TYPE, itemView).get();
        } catch (ReflectException e) {
            //考虑到非静态内部类的情况，创建viewholder的时候需要传入外部类的对象
            if (outher != null && outher.get() != null) {
                //如果是创建在内部类里面的viewholder，则需要绑定outher
                Class<?>[] types = {outher.get().getClass(), View.class};
                return Reflect.on(info.getHolderClass()).create(types, outher.get(), itemView).get();
            }
            //其余情况皆是Context的内部类
            Class<?>[] types = {getContext().getClass(), View.class};
            return Reflect.on(info.getHolderClass()).create(types, getContext(), itemView).get();
        }
    }


    private static class ViewHolderInfo {
        final Class<? extends BaseMultiRecyclerViewHolder> mHolderClass;
        final int viewType;

        int layoutId;

        public ViewHolderInfo(Class<? extends BaseMultiRecyclerViewHolder> holderClass, int viewType) {
            mHolderClass = holderClass;
            this.viewType = viewType;
        }

        public int getLayoutId() {
            if (layoutId == 0) {
                searchLayout();
            }
            return layoutId;
        }

        public Class<? extends BaseMultiRecyclerViewHolder> getHolderClass() {
            return mHolderClass;
        }

        public int getViewType() {
            return viewType;
        }

        static String generateKey(Class clazz, int viewType) {
            return clazz.getName() + "$Type:" + viewType;
        }

        void searchLayout() {
            if (layoutId != 0) return;
            if (mHolderClass != null) {
                final String key = generateKey(mHolderClass, viewType);
                if (mViewHolderIdCache.containsKey(key)) {
                    layoutId = mViewHolderIdCache.get(key);
                    if (layoutId != 0) {
                        KLog.i("ViewHolderInfo", "id from cache : " + layoutId);
                        return;
                    }
                }
                //利用unsafe绕过构造器创建对象，该对象仅用于获取layoutid
                BaseMultiRecyclerViewHolder holder = UnsafeUtil.unsafeInstance(mHolderClass);
                if (holder == null) return;
                layoutId = holder.layoutId();
                if (layoutId != 0) {
                    mViewHolderIdCache.put(key, layoutId);
                }
                holder = null;
            }
        }
    }
}
