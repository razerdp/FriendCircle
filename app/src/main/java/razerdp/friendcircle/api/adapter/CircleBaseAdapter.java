package razerdp.friendcircle.api.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import razerdp.friendcircle.api.adapter.view.BaseItemView;

/**
 * Created by 大灯泡 on 2016/2/16.
 * 适配器抽象
 */
public abstract class CircleBaseAdapter<T> extends BaseAdapter {
    private static final String TAG = "FriendCircleAdapter";
    //数据
    protected List<T> datas;
    //类型集合
    protected HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
    protected Activity context;
    protected LayoutInflater mInflater;

    public CircleBaseAdapter(Activity context, Builder<T> mBuilder) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        datas=mBuilder.datas;
        itemInfos = mBuilder.itemInfos;
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
        BaseItemView view = null;
        if (convertView == null) {
            Class viewClass = itemInfos.get(dynamicType);
            Log.d(TAG,""+viewClass);
            try {
                view = (BaseItemView) viewClass.newInstance();
            } catch (InstantiationException e) {
                Log.e(TAG, "反射创建失败！！！");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Log.e(TAG, "反射创建失败！！！");
                e.printStackTrace();
            }
            if (view != null) {
                convertView = mInflater.inflate(view.getViewRes(), parent, false);
                convertView.setTag(view);
            }
            else {
                throw new NullPointerException("view是空的哦~");
            }
        }
        else {
            view = (BaseItemView) convertView.getTag();
        }
        view.setActivityContext(context);
        view.onFindView(convertView);
        view.onBindData(position, convertView, getItem(position), dynamicType);

        return convertView;
    }

    public static class Builder<T> {
        private HashMap<Integer, Class<? extends BaseItemView<T>>> itemInfos;
        private Activity context;
        private List<T> datas;

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

        public Builder setDatas(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public Builder build() {return this;}
    }
}
