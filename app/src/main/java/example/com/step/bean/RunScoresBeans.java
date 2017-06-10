package example.com.step.bean;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qinghua on 2016/11/18.
 * 存放跑步数据的实体类
 */

public class RunScoresBeans implements Serializable {
    private String run_startTime;//跑步开始时间
    private String Run_overTime;//跑步结束时// 间
    private String timeLong;//跑步用时

    public String getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(String timeLong) {
        this.timeLong = timeLong;
    }

    private Double Average_speed;//跑步的平均速度
    private int Run_Distance;//跑步的总路程
    private String run_lat_list;//跑步过程的实时经纬度
    public String getRun_startTime() {
        return run_startTime;
    }

    public void setRun_startTime(String run_startTime) {
        this.run_startTime = run_startTime;
    }

    public String getRun_overTime() {
        return Run_overTime;
    }

    public void setRun_overTime(String run_overTime) {
        this.Run_overTime = run_overTime;
    }

    public Double getAverage_speed() {
        return Average_speed;
    }

    public void setAverage_speed(Double average_speed) {
        this.Average_speed = average_speed;
    }

    public int getRun_Distance() {
        return Run_Distance;
    }

    public void setRun_Distance(int run_Distance) {
        this.Run_Distance = run_Distance;
    }

    public String getRun_lat_list() {
        return run_lat_list;
    }

    public void setRun_lat_list(String run_lat_list) {
        this.run_lat_list = run_lat_list;
    }
}
