package razerdp.github.com.ui.base.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import razerdp.github.com.lib.thirdpart.joor.Reflect;
import razerdp.github.com.lib.thirdpart.joor.ReflectException;

/**
 * Created by 大灯泡 on 2018/4/10.
 */
public class BaseMultiRecyclerViewAdapter<A extends BaseMultiRecyclerViewAdapter, T extends MultiType> extends BaseRecyclerViewAdapter<T> {

    private SparseArray<ViewHolderInfo> mViewHolderInfos;

    public BaseMultiRecyclerViewAdapter(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseMultiRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        super(context, datas);
        init();
    }

    private void init() {
        mViewHolderInfos = new SparseArray<>();
    }

    @Override
    protected int getViewType(int position, @NonNull T data) {
        return data.getItemType();
    }

    @Override
    protected int getLayoutResId(int viewType) {
        ViewHolderInfo viewHolderInfo = mViewHolderInfos.get(viewType);
        return viewHolderInfo == null ? 0 : viewHolderInfo.getLayoutResid();
    }


    @Override
    protected <V extends BaseRecyclerViewHolder> V getViewHolder(ViewGroup parent, View rootView, int viewType) {
        ViewHolderInfo viewHolderInfo = mViewHolderInfos.get(viewType);
        if (viewHolderInfo != null) {
            if (rootView == null) {
                rootView = View.inflate(context, viewHolderInfo.getLayoutResid(), null);
            }
            Class<?>[] types = {context.getClass(), View.class, int.class};
            try {
                return (V) Reflect.on(viewHolderInfo.holderClass).create(types, context, rootView, viewType).<BaseMultiRecyclerViewHolder>get();
            } catch (ReflectException e) {
                Class<?>[] types2 = {View.class, int.class};
                return (V) Reflect.on(viewHolderInfo.holderClass).create(types2, rootView, viewType).<BaseMultiRecyclerViewHolder>get();
            }
        }
        return null;
    }

    public A addViewHolder(Class<? extends BaseMultiRecyclerViewHolder> viewHolderClass, int viewType) {
        mViewHolderInfos.put(viewType, new ViewHolderInfo(viewHolderClass, viewType));
        return slef();
    }

    /**
     * vh的信息类
     */
    private static final class ViewHolderInfo implements Serializable {
        final Class<? extends BaseMultiRecyclerViewHolder> holderClass;
        final int viewType;

        @LayoutRes
        int layoutResid;

        public ViewHolderInfo(Class<? extends BaseMultiRecyclerViewHolder> holderClass, int viewType) {
            this.holderClass = holderClass;
            this.viewType = viewType;
            getLayoutResid();
        }

        public int getLayoutResid() {
            if (layoutResid == 0) {
                layoutResid = getLayoutAnnotationId(holderClass);
            }
            return layoutResid;
        }

        public void setLayoutResid(int layoutResid) {
            this.layoutResid = layoutResid;
        }
    }

    A slef() {
        return (A) this;
    }

    private static int getLayoutAnnotationId(Class<? extends BaseMultiRecyclerViewHolder> vhClass) {
        if (vhClass == null) return 0;

        //获取类上的注解值
        LayoutId layoutIdAnnotation = vhClass.getAnnotation(LayoutId.class);
        if (layoutIdAnnotation == null) {
            throw new NullPointerException("必须在类上注解@LayoutId以标识itemview的布局文件id");
        }

        try {

            return Reflect.on(layoutIdAnnotation).call("id").get();
        } catch (ReflectException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
