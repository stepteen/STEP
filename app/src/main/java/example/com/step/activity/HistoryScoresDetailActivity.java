package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.step.R;
import example.com.step.bean.MyLatlng;
import example.com.step.bean.RunScoresBeans;
import example.com.step.util.DlgLoading;

/**
 * Created by qinghua on 2016/11/24.
 */

public class HistoryScoresDetailActivity extends Activity implements View.OnClickListener {

    private ImageView iv_history_exit,iv_detail_share;
    private List<MyLatlng> trackList=new ArrayList<MyLatlng>();//轨迹数据
    private  RunScoresBeans Rrun_date=new RunScoresBeans();//跑步数据
    private MapView mMapView=null;//定义地图主控件对象
    private BaiduMap baiduMap;//定义百度地图对象
    private String filePath="/mnt/sdcard/historyPicture.png";
    //定位相关
    private LocationClient mLocationClient;//定位服务的客户端对象
    private boolean isFirstIn=true;
    private DlgLoading dlgLoading;

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

    private TextView tv_history_date,tv_history_luceng,tv_history_time,tv_history_sudu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.historyscoresdetailactivity);
        obtainMsg();
        inniView();
        initLocation();
        if(trackList!=null) {
            drawHistoryTrack(trackList);
        }
        initRunDate();
    }

    private void initRunDate() {
        tv_history_date.setText(Rrun_date.getRun_startTime());
        tv_history_sudu.setText(Rrun_date.getAverage_speed()+"");
        tv_history_time.setText(Rrun_date.getTimeLong());
        tv_history_luceng.setText(Rrun_date.getRun_Distance()+"");
    }

    /**
     * 获取从其他活动传来的数据
     */
    private void obtainMsg() {
        Intent intent = this.getIntent();
        trackList = (List<MyLatlng>) intent.getSerializableExtra("track");
        Rrun_date= (RunScoresBeans) intent.getSerializableExtra("run_data");
    }
    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(List<MyLatlng> points) {
        // 绘制新覆盖物前，清空之前的覆盖物
        baiduMap.clear();
        List<LatLng> m_track=new ArrayList<LatLng>();
        for(int i=0;i<points.size();i++)
        {
            m_track.add(new LatLng(points.get(i).getLatitude(),points.get(i).getLongitude()));
        }
        if (points.size() == 1) {
            points.add(points.get(0));
        }
        if (points == null || points.size() == 0) {
            Toast.makeText(getApplicationContext(),"当前查询无轨迹点",Toast.LENGTH_SHORT).show();
            resetMarker();
        } else if (points.size() > 1) {
            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);


            // 添加终点图标
            endMarker = new MarkerOptions()
                    .position(m_track.get(m_track.size()-1)).icon(bmStart)
                    .zIndex(9).draggable(true);
            // 添加起点图标
            startMarker= new MarkerOptions().position(m_track.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(m_track);
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
        dlgLoading=new DlgLoading(HistoryScoresDetailActivity.this);
        tv_history_date= (TextView) findViewById(R.id.tv_detail_history_date);
        tv_history_luceng= (TextView) findViewById(R.id.tv_detail_history_luceng);
        tv_history_sudu= (TextView) findViewById(R.id.tv_detail_history_sudu);
        tv_history_time= (TextView) findViewById(R.id.tv_detail_history_time);
        iv_history_exit= (ImageView) findViewById(R.id.detail_history_exit);
        iv_detail_share= (ImageView) findViewById(R.id.detail_share);
        iv_history_exit.setOnClickListener(this);
        iv_detail_share.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.history_bmapView);//获取地图控件引用
        baiduMap = mMapView.getMap();//获取地图的控制器
        // 构造一个更新地图的msu对象，然后设置该对象为缩放等级15.0
        MapStatusUpdate mapStatusUpdateFactory = MapStatusUpdateFactory.zoomTo(18f);
        //设置地图状态。
        baiduMap.setMapStatus(mapStatusUpdateFactory);
    }

    private void initLocation()
    {
        mLocationClient=new LocationClient(this);//初始化务的客户端对象
        LocationClientOption option=new LocationClientOption();// 创建定位参数对象
        option.setCoorType("bd09ll");//设置坐标类型
        option.setIsNeedAddress(true);//设置是否需要地址信息
        option.setOpenGps(true);//是否要打开GPS定位
        option.setScanSpan(1000);//设置扫描间隔
        mLocationClient.setLocOption(option);//设置定位参数对象给mLocationClient
    }

    @Override
    protected void onStart() {

        baiduMap.setMyLocationEnabled(true);
        MapStatusUpdate factory= MapStatusUpdateFactory.newLatLng(new LatLng(trackList.get(0).getLatitude(),trackList.get(0).getLongitude()));
        baiduMap.animateMapStatus(factory);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //停止定位
        baiduMap.setMyLocationEnabled(false);
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
            case R.id.detail_history_exit:
                     finish();
                     break;
            case R.id.detail_share:
                dlgLoading.show("跳转中...");
                saveMapImage();
                Intent intent=new Intent(HistoryScoresDetailActivity.this,SendDynamicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("filePath",filePath);
                bundle.putSerializable("Rrun_date",Rrun_date);
                intent.putExtras(bundle);
                dlgLoading.dismiss();
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 保存要分享的图片
     */
    private void saveMapImage() {
        baiduMap.snapshotScope(null, new BaiduMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                File file = new File(filePath);
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    HistoryScoresDetailActivity.this.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
                }
            }
        });
    }
}
