package example.com.step.bean;

import java.io.Serializable;

/**
 * Created by qinghua on 2016/11/24.
 */

public class MyLatlng implements Serializable {
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double  longitude;
}
