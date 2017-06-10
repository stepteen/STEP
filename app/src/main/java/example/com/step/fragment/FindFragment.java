package example.com.step.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import example.com.step.R;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.activity.DetailNewsActivity;
import example.com.step.adapter.NewsAdapter;
import example.com.step.bean.NewsEntity;
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
public class FindFragment extends Fragment implements View.OnClickListener,LoadListView.ILoadListener{

    private SharedPreferences sp;
    private ImageView iv_scan;
    private View newsView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadListView lv_newsListView;
    private NewsAdapter newsAdapter;
    private List<NewsEntity> newsData;
    private List<NewsEntity> lishiData;
    private DlgLoading dlgLoading;
    private  String m_strurl="http://image95.360doc.com/DownloadImg/2016/03/0308/67030089_1.jpg";
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                case 0:
                    newsAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    newsAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newsView=inflater.inflate(R.layout.findfragment,container,false);

        return newsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        SetClick();
        dlgLoading.show("加载中...");
        postAsynHttp();
        super.onActivityCreated(savedInstanceState);
    }
    private void setData() {
        newsData= new ArrayList<NewsEntity>();
        lishiData=new ArrayList<NewsEntity>();
       // newsData.add(new NewsEntity("http://fitness.39.net/a/161209/5109459.html","骑自行车能减肥吗？可开发大脑瘦身减肥",m_strurl,"11.20"));
       // newsData.add(new NewsEntity("http://fitness.39.net/a/161207/5107705.html","呼啦圈能瘦腰吗？记住这7点注意事项",m_strurl,"11.20"));
    }

    private void initView() {
        dlgLoading=new DlgLoading(getContext());
        sp = getContext().getSharedPreferences("User",MODE_PRIVATE);
        setData();
        iv_scan= (ImageView) newsView.findViewById(R.id.iv_scan);
        lv_newsListView= (LoadListView) newsView.findViewById(R.id.lv_newsListView);
        newsAdapter=new NewsAdapter(getContext(),newsData,lv_newsListView);
        lv_newsListView.setAdapter(newsAdapter);
        lv_newsListView.setInterface(this);
        initRefreshLayout();
    }
    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) newsView.findViewById(R.id.swipeLayout);
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
                        postAsynHttp();
                        //开启线程休眠3秒
                        try {
                            Thread.sleep(3000);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

    }
    private void SetClick()
    {
        lv_newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getContext(), DetailNewsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("news",newsData.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void postAsynHttp() {

        String httpUrl = ContractData.URL+"/newsentity/get";
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.ACCESSTOKEN, sp.getString("accessToken",""))
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zzz", "网络请求失败");
                dlgLoading.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                Log.d("zzz","获取成功");
                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    lishiData.clear();
                    Log.d("zzz",(String) data.getData());
                    lishiData.addAll(JSONObject.parseArray((String) data.getData(), NewsEntity.class));
                    if(!lishiData.isEmpty())
                    {
                        newsData.clear();
                        newsData.addAll(lishiData);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message=new Message();
                                message.what=0;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    boolean parseResultData(String resultData) {
        //先将返回值解析为ResultData
        ResultData data = ResultDataUtils.paraResult(resultData);
        if (data.getStatus().equals(ResultData.ERROR)) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

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
                newsAdapter.notifyDataSetChanged();
                //actList.setAdapter(mListAdapter);
                //通知listview加载完毕
                lv_newsListView.loadComplete();
            }
        }, 2000);
    }
}
