package example.com.step.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;

import java.util.ArrayList;
import java.util.List;

import example.com.step.track.HistoryTrackData;

/**
 * Created by qinghua on 2017/5/23.
 */

public class RunActivityService extends Service {

    //定位相关
    private LocationClient mLocationClient;//定位服务的客户端对象
    private MyLocationListener myLocationListener;//创建定位监听器的对象
    private boolean isFirstIn=true;
    private MyLocationData data;//定位数据

    /**
     * 计算距离相关
     */
    private double distance_sums=0;//总距离
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
                }

            }
        }

        @Override
        public void onRequestFailedCallback(String s) {
            Log.d("zqh",s);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        startTrack();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
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
    public void queryHistoryTrack(int startTime,int endTime) {
        client.queryHistoryTrack(serviceId , entityName, simpleReturn, isProcessed,
                processOption, startTime, endTime, pageSize, pageIndex, trackListener);
    }

    /**
     * 开启轨迹服务
     */
    public void startTrack(){
        //开启轨迹服务
        client.startTrace(trace, startTraceListener);
    }

    /**
     * 关闭轨迹服务
     */
    public void stopTrack(){
        client.stopTrace(trace,stopTraceListener);// 关闭轨迹服务
    }

    private class MyLocationListener implements BDLocationListener {

        /*
        定位请求回调函数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            data = new MyLocationData.Builder()//
                    // .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            //baiduMap.setMyLocationData(data);
            if (isFirstIn) {
            /*
            //设定中心点坐标
            */
            //获取当前定位的经纬度
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate factory = MapStatusUpdateFactory.newLatLng(latlng);
            //baiduMap.animateMapStatus(factory);
            isFirstIn = false;
            }
        }
    }

    /**
     * 获取历史轨迹
     * @return
     */
    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    /**
     * 获取运动路程
     * @return
     */
    public double getDistance_sums() {
        return distance_sums;
    }

}
