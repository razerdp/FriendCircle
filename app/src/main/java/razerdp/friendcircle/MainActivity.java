package razerdp.friendcircle;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.api.ptrwidget.OnLoadMoreRefreshListener;
import razerdp.friendcircle.api.ptrwidget.OnPullDownRefreshListener;
import razerdp.friendcircle.widget.ptrwidget.FriendCirclePtrListView;

public class MainActivity extends AppCompatActivity {
    private FriendCirclePtrListView mFriendCirclePtrListView;
    private ImageView rotateIcon;
    private RelativeLayout actionBar;
    private List<String> test=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar= (RelativeLayout) findViewById(R.id.action_bar);
        rotateIcon= (ImageView) findViewById(R.id.rotate_icon);
        mFriendCirclePtrListView= (FriendCirclePtrListView) findViewById(R.id.listview);
        mFriendCirclePtrListView.setRotateIcon(rotateIcon);
        initTestData();

        View header= LayoutInflater.from(this).inflate(R.layout.item_header,null,false);
        mFriendCirclePtrListView.addHeaderView(header);

        final testAdapter adapter=new testAdapter();
        mFriendCirclePtrListView.setAdapter(adapter);


        mFriendCirclePtrListView.setOnPullDownRefreshListener(new OnPullDownRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFriendCirclePtrListView.refreshComplete();
                    }
                },1800);
            }
        });
        mFriendCirclePtrListView.setOnLoadMoreRefreshListener(new OnLoadMoreRefreshListener() {
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1500);
                        for (int i=0,size=test.size();i<20;i++){
                            test.add("test"+(size+i));
                        }
                    }
                }).start();

                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFriendCirclePtrListView.refreshComplete();
                        adapter.notifyDataSetChanged();
                        mFriendCirclePtrListView.setHasMore(false);
                    }
                },3000);
            }
        });
        mFriendCirclePtrListView.setHasMore(true);

    }

    // TODO: 2016/2/10
    //暂行测试数据，往后改
    private void initTestData() {
        for (int i=0;i<20;i++){
            test.add("test"+i);
        }
    }
    class testAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return test.size();
        }

        @Override
        public String getItem(int position) {
            return test.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh=null;
            if (convertView==null){
                convertView=LayoutInflater.from(MainActivity.this).inflate(R.layout.item_test,parent,false);
                vh=new ViewHolder();
                vh.mTextView= (TextView) convertView.findViewById(R.id.test);
                convertView.setTag(vh);
            }else {
                vh= (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));

            return convertView;
        }
        class ViewHolder{
            public TextView mTextView;
        }

    }
}
