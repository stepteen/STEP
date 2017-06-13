package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import example.com.step.R;
import example.com.step.adapter.HistoryAdapter;
import example.com.step.bean.MyLatlng;
import example.com.step.bean.RunScoresBeans;
import example.com.step.util.RunFileDateUtil;



public class HistoryScores extends Activity {
    private  List<String> file_items = new ArrayList<String>();//存放文件的名字
    private  List<String>  file_paths = new ArrayList<String>();//存放文件的路径
    private  List<List<MyLatlng>> historyTrack=new ArrayList<List<MyLatlng>>();//存放历史轨迹
    private  RunFileDateUtil runFileDateUtil=new RunFileDateUtil();//创建文件操作的对象
    private  ListView lv_historyView;
    private  List<RunScoresBeans> run_data;
    private  HistoryAdapter historyAdapter;
    private  ImageView iv_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyscores_layout);
        initView();
        fileAction();
        addRunData();
    }

    /**
     * 从文件中获取数据
     */
    private void fileAction() {
        LatLng LL= null;
        runFileDateUtil.getFileDir();
        file_items.addAll(runFileDateUtil.getItems());
        file_paths.addAll(runFileDateUtil.getPaths());
        StringToObject();
        JsonToLatLng();
    }

    /**
     * 初始化listview并且为listView的item设置监听
     */
    private void addRunData() {
        if(!run_data.isEmpty()) {
            historyAdapter = new HistoryAdapter(getApplicationContext(), run_data);
        }
        lv_historyView.setAdapter(historyAdapter);
        lv_historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),HistoryScoresDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("track", (Serializable) historyTrack.get(i));
                bundle.putSerializable("run_data", (Serializable) run_data.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        lv_historyView= (ListView) findViewById(R.id.lv_historyView);
        run_data=new ArrayList<RunScoresBeans>();
        iv_return= (ImageView) findViewById(R.id.history_exit);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 将文件中获取的字符串转化为对象
     */
    private void StringToObject()
    {
        for(int i=0;i<file_paths.size();i++)
        {
            try {
                String str=runFileDateUtil.readFile(file_paths.get(i));
                Log.d("zzz",str);
                run_data.add(JSONObject.parseObject(str,RunScoresBeans.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 将JSON字符串转化为LatLng
     */
    private void JsonToLatLng()
    {
        for(int i=0;i<run_data.size();i++)
        {
            historyTrack.add(JSON.parseArray(run_data.get(i).getRun_lat_list(),MyLatlng.class));
        }
    }
}
