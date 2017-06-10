package example.com.step.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import example.com.step.R;
import example.com.step.bean.RunScoresBeans;
import example.com.step.service.GsonService;
import example.com.step.track.HistoryTrackData;
import example.com.step.util.RunFileDateUtil;

/**
 * Created by qinghua on 2016/11/6.
 */

public class RunActivity extends Activity  implements View.OnClickListener{

    private RunFileDateUtil fileDateUtil;//进行文件操作的对象
    private RunScoresBeans RunDate=new RunScoresBeans();//存放跑步数据的对象
    /**
     * 计算距离相关
     */
    private double distance_sums=0;//总距离
    private float avrage_speed;//平均速度
    TextView tv_run_distance,tv_avragespeed;//总路程，平均速度
    /**
     * 计时相关
     */
    private Timer timer1;
    private TextView tv_runTime;
    private TimerTask timerTask;
    private int cnt = 0;
    private ImageView iv_run_continue,iv_run_over,iv_run_pause;
    private MapView mMapView=null;//定义地图主控件对象
    private BaiduMap baiduMap;//定义百度地图对象
    private Context context;
    /**
     * 绘制轨迹相关
     *
     */
    private static BitmapDescriptor bmStart;    // 起点图标
    private static BitmapDescriptor bmEnd;    // 终点图标
    private static MarkerOptions startMarker = null;    // 起点图标覆盖物
    private static MarkerOptions endMarker = null;    // 终点图标覆盖物
    public static PolylineOptions polyline = null;    // 路线覆盖物
    private MapStatusUpdate msUpdate = null;
    private static MarkerOptions markerOptions = null;
    //定位相关
    private LocationClient mLocationClient;//定位服务的客户端对象
    private MyLocationListener myLocationListener;//创建定位监听器的对象
    private boolean isFirstIn=true;

    private List<LatLng> latLngList = new ArrayList<LatLng>();//存放历史轨迹

    /**
     * 鹰眼服务相关
     */
    private Trace trace;  // 实例化轨迹服务
    private LBSTraceClient client;  // 实例化轨迹服务客户端
    long serviceId  = 128194; //鹰眼服务ID
    String entityName = "my";    //entity标识
    int  traceType = 2;//轨迹服务类型（0 : 不上传位置数据，也不接收报警信息； 1 : 不上传位置数据，但接收报警信息；2 : 上传位置数据，且接收报警信息）
    int gatherInterval = 2;  // 采集周期
    int packInterval = 2;    // 打包周期
    int protocolType = 1;    // http协议类型

    /**
     * 轨迹查询相关
     */
    int simpleReturn = 0;  // 是否返回精简结果
    int isProcessed = 1;    // 是否纠偏
    String processOption = "need_denoise=1,need_vacuate=0,need_mapmatch=0";    // 纠偏选项
    int startTime ;    // 开始时间
    int endTime;    // 结束时间

    int pageSize = 5000;    // 分页大小
    int pageIndex = 1;    // 分页索引

    /**
     *   实例化开启轨迹服务回调接口
     */
    OnStartTraceListener startTraceListener = new OnStartTraceListener() {

        //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
        @Override
        public void onTraceCallback(int arg0, String arg1) {
            Log.d("zqh","轨迹成功开启");
            if(arg0 == 0 || arg0 == 10006){

            }
        }
        //轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
        @Override
        public void onTracePushCallback(byte arg0, String arg1) {
            Log.d("zqh","onTracePushCallback"+arg1);
        }
    };

    /**
     *   实例化停止轨迹服务回调接口
     */
    OnStopTraceListener stopTraceListener = new OnStopTraceListener(){
        // 轨迹服务停止成功
        @Override
        public void onStopTraceSuccess() {
            Log.d("zqh","轨迹成功关闭");


        }
        // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
        @Override
        public void onStopTraceFailed(int arg0, String arg1) {
            Log.d("zqh","onStopTraceFailed"+arg1);
        }
    };
    /**
     * 在OnTrackListener的onQueryHistoryTrackCallback()回调接口中，判断是否已查询完毕。
     * @param savedInstanceState
     */
    OnTrackListener trackListener = new OnTrackListener() {
        @Override
        public void onQueryHistoryTrackCallback(String s) {
            Log.d("zqh","HistoryTrack is:"+s);
            HistoryTrackData historyTrackData = GsonService.parseJson(s,
                    HistoryTrackData.class);
            if (historyTrackData != null && historyTrackData.getStatus() == 0) {
                if (historyTrackData.getListPoints() != null) {
                    latLngList.clear();
                    latLngList.addAll(historyTrackData.getListPoints());
                    latLngList.remove(0);
                }
                if(!latLngList.isEmpty())
                {
                    distance_sums=historyTrackData.distance;
                    avrage_speed= (float) ((distance_sums/cnt)*3.6);
                    distance_sums=((int)(distance_sums*100))/100;//保留两位小数
                    avrage_speed=((int)(avrage_speed*100))/100;
                    // 绘制历史轨迹
                    drawHistoryTrack(latLngList, historyTrackData.distance);
                }

            }
        }

        @Override
        public void onRequestFailedCallback(String s) {
            Log.d("zqh",s);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message message=new Message();
            switch(msg.what) {
                case 1:
                    tv_avragespeed.setText(avrage_speed+"");
                    tv_run_distance.setText(distance_sums+"");
                    threadHandler.sendMessageDelayed(message,2000);
                    break;
                default:
                    break;
            }

        }
    };
    //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
   private SpeechSynthesizer mTts=null;
    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener(){
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {

            Log.d("xyz","error="+error);
        }
        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {


        }
        //开始播放
        public void onSpeakBegin() {
            Log.d("xyz","播放开始");

        }
        //暂停播放
        public void onSpeakPaused() {

            Log.d("xyz","播放暂停");
        }
        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
    };

    Handler threadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        SpeechUtility.createUtility(RunActivity.this, SpeechConstant.APPID +"=584506d8"+ SpeechConstant.FORCE_LOGIN +"=true");
        setContentView(R.layout.runactivity);
        inniView();
        initVoice();
        initTrack();
        initLocation();
        startHandler();

    }

    /**
     * 开启获取历史轨迹的线程
     */
    private void startHandler() {
        HandlerThread thread=new HandlerThread("TRACK");
        thread.start();
        threadHandler=new Handler(thread.getLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                endTime= (int)(System.currentTimeMillis()/1000);// 结束时间
                queryHistoryTrack(pageIndex);
                Message message=new Message();
                message.what=1;
                mHandler.sendMessageDelayed(message,2000);
            }
        };
    }

    /**
     * 开启计时器
     */
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_runTime.setText(getStringTime(cnt++));
                    }
                });
            }
        };
        timer1.schedule(timerTask,0,1000);
    }

    /**
     * 时分秒的转化
     * @param cnt
     * @return
     */
    private String getStringTime(int cnt) {
        int hour = cnt/3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA,"%02d:%02d:%02d",hour,min,second);
    }



    /**
     * 停止计时器
     */
    private void  stopTimer() {
        if (!timerTask.cancel())
        {
            timerTask.cancel();
            timer1.cancel();
        }
    }
    /**
     * 初始化轨迹的配置
     */
    private void initTrack() {
        //实例化轨迹服务
        trace = new Trace(getApplicationContext(), serviceId, entityName, traceType);
        // 初始化轨迹服务客户端
        client = new LBSTraceClient(getApplicationContext());
        // 设置采集和打包周期
        client. setInterval(gatherInterval, packInterval);
        // 设置定位模式
        client. setLocationMode(LocationMode.High_Accuracy);
        // 设置http协议类型
        client. setProtocolType (protocolType);
    }

    /**
     *     调用queryHistoryTrack()查询历史轨迹
     */

    private void queryHistoryTrack(int pageIndex) {
        client.queryHistoryTrack(serviceId , entityName, simpleReturn, isProcessed,
                processOption, startTime, endTime, pageSize, pageIndex, trackListener);
    }
    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        baiduMap.clear();

        if (points.size() == 1) {
            points.add(points.get(0));
        }
        if (points == null || points.size() == 0) {
            Toast.makeText(context,"当前查询无轨迹点",Toast.LENGTH_SHORT).show();
            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);

            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);


            // 添加终点图标
            endMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加起点图标

            startMarker= new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));
            addMarker();
        }

    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }
    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            baiduMap.animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            baiduMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            baiduMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            baiduMap.addOverlay(polyline);
        }

    }

    private void  inniView()
    {
        context=getApplicationContext();
        tv_avragespeed= (TextView) findViewById(R.id.tv_sudu);
        tv_run_distance= (TextView) findViewById(R.id.distance);
        tv_runTime = (TextView) findViewById(R.id.tv_longtime);
        timer1 = new Timer();

        iv_run_continue= (ImageView) findViewById(R.id.iv_run_continue);
        iv_run_pause= (ImageView) findViewById(R.id.iv_run_pause);
        iv_run_over= (ImageView) findViewById(R.id.iv_run_over);
        iv_run_continue.setOnClickListener(this);
        iv_run_pause.setOnClickListener(this);
        iv_run_over.setOnClickListener(this);

        mMapView = (MapView) findViewById(R.id.bmapView1);//获取地图控件引用
        baiduMap = mMapView.getMap();//获取地图的控制器
        // 构造一个更新地图的msu对象，然后设置该对象为缩放等级19.0
        MapStatusUpdate mapStatusUpdateFactory = MapStatusUpdateFactory.zoomTo(19f);
        //设置地图状态。
        baiduMap.setMapStatus(mapStatusUpdateFactory);

    }

    /**
     * 初始化语音相关
     */
    private void initVoice()
    {
        mTts = SpeechSynthesizer.createSynthesizer(RunActivity.this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "70");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

    }
    private void initLocation()
    {
        mLocationClient=new LocationClient(this);//初始化务的客户端对象
        myLocationListener=new MyLocationListener();//初始化定位监听器
        mLocationClient.registerLocationListener(myLocationListener);//设置定位监听事件
        LocationClientOption option=new LocationClientOption();// 创建定位参数对象
        option.setCoorType("bd09ll");//设置坐标类型
        option.setIsNeedAddress(true);//设置是否需要地址信息
        option.setOpenGps(true);//是否要打开GPS定位
        option.setScanSpan(1000);//设置扫描间隔
        mLocationClient.setLocOption(option);//设置定位参数对象给mLocationClient
        long time=System.currentTimeMillis();
        startTime= (int)(time/1000)+4;//设置开始时间
        RunDate.setRun_startTime(stampToDate(time+""));
    }


    /*
     * 将时间戳转换为时间
     */
    public  String stampToDate(String s){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(s))));   // 时间戳转换成时间
        return sd;
    }
    @Override
    protected void onStart() {

        baiduMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        //开启轨迹服务
        client.startTrace(trace, startTraceListener);
        mTts.startSpeaking("开始跑步", mSynListener);
        Log.d("xyz","1");
        startTimer();
        threadHandler.sendEmptyMessage(1);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //停止定位
        baiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_run_pause:
                mTts.startSpeaking("运动已暂停", mSynListener);
                stopTimer();
                iv_run_over.setVisibility(View.VISIBLE);
                iv_run_continue.setVisibility(View.VISIBLE);
                iv_run_pause.setVisibility(View.GONE);
                threadHandler.removeMessages(1);
                client.stopTrace(trace,stopTraceListener);// 关闭轨迹服务
                break;
            case R.id.iv_run_continue:
                mTts.startSpeaking("运动已恢复", mSynListener);
                startTimer();
                client.startTrace(trace, startTraceListener);  // 开启轨迹服务
                threadHandler.sendEmptyMessage(1);
                iv_run_over.setVisibility(View.GONE);
                iv_run_continue.setVisibility(View.GONE);
                iv_run_pause.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_run_over:
                stopTimer();
                setRunDate();
                if(RunDate.getRun_Distance()>10) {
                    saveDateToFile();
                }
                else
                {
                    Toast.makeText(RunActivity.this, "跑步距离太短不保存！", Toast.LENGTH_SHORT).show();
                }
                threadHandler.removeMessages(1);
                client.stopTrace(trace,stopTraceListener);// 关闭轨迹服务
                finish();
                break;
        }

    }

    /**
     * 保存数据到文件里
     */
    private void saveDateToFile() {
        fileDateUtil=new RunFileDateUtil();
        File file = this.getFilesDir();
        String str_path="/mnt/sdcard"+file.toString();
        fileDateUtil.writeTxtToFile(JSON.toJSONString(RunDate),str_path,RunDate.getRun_startTime());
        Toast.makeText(this,"数据保存成功",Toast.LENGTH_SHORT).show();
    }

    /**
     * 存放跑步的数据到RuanDate对象里
     */
    private void setRunDate() {
        long time=System.currentTimeMillis();
        RunDate.setRun_overTime(stampToDate(time+""));
        RunDate.setAverage_speed((double) avrage_speed);
        RunDate.setRun_Distance((int) distance_sums);
        RunDate.setRun_lat_list(JSON.toJSONString(latLngList));
        RunDate.setTimeLong(getStringTime(endTime-(startTime-5)));
        Log.d("zzz",RunDate.getTimeLong());
    }

    private class MyLocationListener implements BDLocationListener
    {

        /*
        定位请求回调函数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            MyLocationData data=new MyLocationData.Builder()//
                    // .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(data);
            if(isFirstIn)
            {
            /*
            //设定中心点坐标
            */
                //获取当前定位的经纬度
                LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate factory= MapStatusUpdateFactory.newLatLng(latlng);
                baiduMap.animateMapStatus(factory);
                isFirstIn=false;
                Toast.makeText(getApplicationContext(),location.getAddrStr(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
