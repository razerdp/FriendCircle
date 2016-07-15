package razerdp.friendcircle.app.adapter.base;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.HashMap;
import java.util.List;
import razerdp.friendcircle.app.adapter.base.viewholder.BaseItemView;
import razerdp.friendcircle.app.mvp.presenter.DynamicPresenterImpl;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 适配器抽象
 *
 * weblink:http://www.jianshu.com/p/720d5a7c75a7
 */
public abstract class CircleBaseAdapter<T> extends BaseAdapter {
    private static final String TAG = "FriendCircleAdapter";
    //数据
    protected List<T> datas;
    //类型集合
    protected HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
    protected Activity context;
    protected LayoutInflater mInflater;

    protected DynamicPresenterImpl mPresenter;

    public CircleBaseAdapter(Activity context, Builder<T> mBuilder) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        datas=mBuilder.datas;
        itemInfos = mBuilder.itemInfos;
        mPresenter=mBuilder.mPresenter;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract int getItemViewType(int position);

    @Override
    public int getViewTypeCount() {return 15;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int dynamicType = getItemViewType(position);
        BaseItemView viewHolderProvider = null;
        if (convertView == null) {
            Class viewClass = itemInfos.get(dynamicType);
            Log.d(TAG,""+viewClass);
            try {
                viewHolderProvider = (BaseItemView) viewClass.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, "反射创建失败！！！");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Log.e(TAG, "反射创建失败！！！");
                e.printStackTrace();
            }
            if (viewHolderProvider != null) {
                convertView = mInflater.inflate(viewHolderProvider.getViewRes(), parent, false);
                convertView.setTag(viewHolderProvider);
            }
            else {
                throw new NullPointerException("view是空的哦~");
            }
        }
        else {
            viewHolderProvider = (BaseItemView) convertView.getTag();
        }
        viewHolderProvider.setActivityContext(context);
        viewHolderProvider.onFindView(convertView);
        viewHolderProvider.onBindData(position, convertView, getItem(position), dynamicType);
        if (viewHolderProvider.getPresenter()==null)viewHolderProvider.setPresenter(mPresenter);

        return convertView;
    }

    public static class Builder<T> {
        private HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
        private Activity context;
        private List<T> datas;
        private DynamicPresenterImpl mPresenter;

        public Builder() {
            itemInfos = new HashMap<>();
        }

        public Builder(List<T> datas) {
            itemInfos = new HashMap<>();
            this.datas = datas;
        }

        public Builder addType(int type, Class<? extends BaseItemView<T>> viewClass) {
            itemInfos.put(type, viewClass);
            return this;
        }

        public Builder setPresenter(DynamicPresenterImpl presenter){
            this.mPresenter=presenter;
            return this;
        }

        public Builder setDatas(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public Builder build() {return this;}
    }
}
