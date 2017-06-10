package example.com.step;

import android.app.Application;
import android.content.Context;

/**
 * Created by qinghua on 2017/5/23.
 *
 */

public class MyApplication extends Application{
    private static Context app_Context;
    @Override
    public void onCreate() {
        super.onCreate();
        app_Context=getApplicationContext();
    }

    public static Context getApp_Context() {
        return app_Context;
    }

    public static void setApp_Context(Context app_Context) {
        MyApplication.app_Context = app_Context;
    }
}
