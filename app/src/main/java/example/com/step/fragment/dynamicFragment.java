package example.com.step.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.activity.DetailSynamicActivity;
import example.com.step.adapter.LoadListViewAdapter;
import example.com.step.bean.Synamic;
import example.com.step.bean.SynamicItemBean;
import example.com.step.bean.Thumb;
import example.com.step.bean.User;
import example.com.step.contract.ContractData;
import example.com.step.util.DlgLoading;
import example.com.step.widget.LoadListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
/**
 * Created by qinghua on 2016/9/4.
 */
public class dynamicFragment extends Fragment implements LoadListView.ILoadListener {
    private SharedPreferences sp;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadListView dynListView;
    private LoadListViewAdapter loadAdapter;
    List<Synamic> mData;
    private DlgLoading dlgLoading;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 0:
                    dlgLoading.dismiss();
                    loadAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mSwipeRefreshLayout.setRefreshing(false);
                    loadAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private Handler threadHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.playgrounddynamic,container,false);
        dynListView= (LoadListView) view.findViewById(R.id.loadlistview);
        //获取刷新布局实例
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoadListView();
        initRefreshLayout();
    }

    private void initRefreshLayout() {

        //设置刷新时控件的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        //设置刷新控件的大小，只有两个值，DEFAULT和LARGE
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置刷新时的背景颜色
        mSwipeRefreshLayout.setProgressBackgroundColor(R.color.swipe_background_color);
        mSwipeRefreshLayout.setProgressViewEndTarget(true,100);
        //设置刷新事件监听器
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toAddData();
                        //开启线程休眠3秒
                        try {
                            Thread.sleep(3000);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

    }

    private void toAddData() {
        ObtainSynamic();
        //mData.add(0,new Synamic(1,"20","华布莱恩特","出行",m_strurl,m_strurl,20,200));
    }

    private void initLoadListView() {
        dlgLoading=new DlgLoading(getContext());
        sp = getContext().getSharedPreferences("User",MODE_PRIVATE);
        mData=new ArrayList<Synamic>();
        //mData.add(0,new Synamic(1,"20","华布莱恩特","出行",m_strurl,m_strurl,20,200));
        loadAdapter=new LoadListViewAdapter(getContext(),mData,dynListView);
        dynListView.setAdapter(loadAdapter);
        setOnclickListView();
        dynListView.setInterface(this);
        dlgLoading.show("初始化数据...");
        ObtainSynamic();
        initHandler();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setOnclickListView() {

    }

    private void initHandler() {
        HandlerThread thread=new HandlerThread("freshview");
        thread.start();
        threadHandler=new Handler(thread.getLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                Message message=new Message();
                message.what=0;
                mHandler.sendMessageDelayed(message,3000);
            }
        };
    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取更多数据
                //ObtainSynamic();
                //更新listview显示；
                loadAdapter.notifyDataSetChanged();
                //actList.setAdapter(mListAdapter);
                //通知listview加载完毕
                dynListView.loadComplete();
            }
        }, 2000);
    }

    private void getLoadData() {
       // mData.add(new SynamicItemBean("黄豪","20","50"));
    }

    private void ObtainSynamic()
    {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        String url=ContractData.URL+"/synamic/user/Synamic/getAllSynamic";
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.ACCESSTOKEN, sp.getString("accessToken",""))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dlgLoading.dismiss();
                Log.d("xyz","网络请求错误！");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    mData.clear();
                    if(!JSONObject.parseArray((String) data.getData(), Synamic.class).isEmpty())
                    {
                        mData.addAll(JSONObject.parseArray((String) data.getData(), Synamic.class));
                    }

                    threadHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    /**
     * 设置点赞信息
     */
    private void setThumb() {
       // thumb.setSynamicId();
    }
}
