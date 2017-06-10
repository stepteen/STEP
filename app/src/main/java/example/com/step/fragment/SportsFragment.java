package example.com.step.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import example.com.step.R;
import example.com.step.activity.CountdownActivity;
import example.com.step.bean.RunScoresBeans;
import example.com.step.util.RunFileDateUtil;


/**
 * Created by qinghua on 2016/9/4.
 */
public class SportsFragment extends Fragment  implements View.OnClickListener{


    private TextView tv_timelong,tv_mileage,tv_run_times;
    private  List<String>  file_paths = new ArrayList<String>();//存放文件的路径
    private RunFileDateUtil runFileDateUtil=new RunFileDateUtil();//创建文件操作的对象
    private List<RunScoresBeans> run_data;
    private ImageView iv_begin_step;
    private View myview;

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    float liceng=0;
                    float second=0;
                    for(int i=0;i<run_data.size();i++)
                    {
                        liceng=liceng+run_data.get(i).getRun_Distance();
                        second=second+StringToSecond(run_data.get(i).getTimeLong());
                    }
                    tv_mileage.setText(baoliutwo(liceng/1000));
                    tv_run_times.setText(run_data.size()+"");
                    tv_timelong.setText(baoliutwo(second/3600));
                    file_paths.clear();
                    run_data.clear();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.sportsfragment,container,false);
        initView();
        return myview;
    }

    private void initView() {
        run_data=new ArrayList<RunScoresBeans>();
        iv_begin_step= (ImageView) myview.findViewById(R.id.iv_begin_step);
        tv_timelong= (TextView) myview.findViewById(R.id.tv_timelong);
        tv_mileage= (TextView) myview.findViewById(R.id.tv_mileage);
        tv_run_times= (TextView) myview.findViewById(R.id.tv_run_times);
        iv_begin_step.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        fileAction();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        }).start();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_begin_step:
                Intent intent=new Intent(getContext(), CountdownActivity.class);
                startActivity(intent);
                break;
        }
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
     * 从文件中获取数据
     */
    private void fileAction() {

        runFileDateUtil.getFileDir();
        file_paths.addAll(runFileDateUtil.getPaths());
        StringToObject();
    }

    /**
     *时间格式转化为秒数
     */
    private int StringToSecond(String str)
    {
        int second=0;
        String a;
        int i= str.indexOf(":");
        a=str.substring(0,i);
        str=str.substring(i+1);
        second=Integer.parseInt(a)*3600;
        i= str.indexOf(":");
        a=str.substring(0,i);
        str=str.substring(i+1);
        second=second+Integer.parseInt(a)*60+Integer.parseInt(str);
        return  second;
    }
    /**
     * 保留两位小数
     */
    private String baoliutwo(float scale)
    {
         DecimalFormat   fnum  =   new DecimalFormat("##0.00");
         String   dd=fnum.format(scale);
         return dd;
    }
}
