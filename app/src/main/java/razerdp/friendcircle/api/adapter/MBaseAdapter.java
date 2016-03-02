package razerdp.friendcircle.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2016/2/18.
 * adapter抽象
 */
public abstract class MBaseAdapter<T, V extends MViewHolder> extends BaseAdapter {
    protected List<T> datas;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public MBaseAdapter(Context context, List<T> datas) {
        mContext = context;
        this.datas=new ArrayList<>();
        this.datas.addAll(datas);
        mInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        V holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(getLayoutRes(),parent,false);
            holder = initViewHolder();
            holder.onInFlate(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (V) convertView.getTag();
        }
        onBindView(position, getItem(position), holder);
        return convertView;
    }

    public abstract int getLayoutRes();

    public abstract V initViewHolder();

    public abstract void onBindView(int position, T data, V holder);
}
