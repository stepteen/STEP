package example.com.step.util;

import android.content.Context;
import android.content.SharedPreferences;

import example.com.step.MyApplication;


/**
 * Created by qinghua on 2017/3/17.
 * 轻量级存储工具类
 */

public class SharedPreferenceUtil {

    private static String FILENAME="User";

    /**
     * CommitDate该方法是一个有返回值的同步的提交方式，true表示数据保存成功，false表示数据保存失败
     */
    public static boolean CommitDate(String key,String date)
    {
        SharedPreferences sp= MyApplication.getApp_Context().getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,date);
        return editor.commit();
    }

    /**
     *ApplyDate:是一个异步操作的保存数据的方法
     */
    public static void ApplyDate(String key,String date)
    {
        SharedPreferences sp= MyApplication.getApp_Context().getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,date);
        editor.apply();
    }

    /*
    *获取数据
     */
    public static String getDate(String key)
    {
        SharedPreferences sp= MyApplication.getApp_Context().getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        String str=sp.getString(key,"");
        if(!str.isEmpty()){
            return str;
        }
        else {
            return null;
        }
    }
}
