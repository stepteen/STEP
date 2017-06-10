package example.com.step.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.android.pushservice.PushMessageReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import example.com.step.MyApplication;
import example.com.step.activity.DetailNewsActivity;
import example.com.step.bean.MessageBean;
import example.com.step.bean.NewsEntity;
import example.com.step.util.MessageServiceUtil;

/**
 * Created by qinghua on 2016/8/9.
 */

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 *
 * 返回值中的errorCode，解释如下：
 0 绑定成功
 10001 当前网络不可用，请检查网络
 10002 服务不可用，连接server失败
 10003 服务不可用，503错误
 10101 应用集成方式错误，请检查各项声明和权限
 20001 未知错误
 30600 服务内部错误
 30601 非法函数请求，请检查您的请求内容
 30602 请求参数错误，请检查您的参数
 30603 非法构造请求，服务端验证失败
 30605 请求的数据在服务端不存在
 30608 绑定关系不存在或未找到
 30609 一个百度账户绑定设备超出个数限制(多台设备登录同一个百度账户)
 *
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */
public class PushTestReceiver extends PushMessageReceiver {

    private MessageServiceUtil msgSqlUtil;
    private static  Context context;
    private static NewsEntity newsEntity=new NewsEntity();
    private static MessageBean messageBean=new MessageBean();
    private static String channelId;

    public static String getChannelId() {
        return channelId;
    }

    public static void setChannelId(String channelId) {
        PushTestReceiver.channelId = channelId;
    }

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        String responseString = "onBind errorCode=" + i + " appid="
                + s + " userId=" + s1 + " channelId=" + s2
                + " requestId=" + s3;
        Log.e("xyz", responseString);
        if(i==0)
        {
            setChannelId(s2);
            this.context=context;
            Log.e("xyz", "绑定成功");
        }

    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        String responseString = "onUnbind errorCode=" + i
                + " requestId = " + s;
        Log.d(TAG, responseString);

        if (i == 0) {
            // 解绑定成功
            Log.d("xyz", "解绑成功");
        }
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
        String responseString = "onSetTags errorCode=" + i
                + " sucessTags=" + list + " failTags=" + list1
                + " requestId=" + s;
        Log.d("xyz", responseString);
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
        String responseString = "onSetTags errorCode=" + i
                + " sucessTags=" + list + " failTags=" + list1
                + " requestId=" + s;
        Log.d("xyz", responseString);
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
        String responseString = "onListTags errorCode=" + i + " tags="
                + list;
        Log.d("xyz","onListTags"+ responseString);
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        String messageString = "透传消息 message=\"" + s
                + "\" customContentString=" + s1;
        Log.d("xyz", "onMessage"+messageString);

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        String notifyString = "onNotificationClicked  title=\"" + s
                + "\" description=\"" + s1 + "\" customContent="
                + s2;
        Log.d("xyz","onNotificationClicked"+ notifyString);
        Intent intent=new Intent(context, DetailNewsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("news",newsEntity);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

        String notifyString = "onNotificationArrived  title=\"" + s
                + "\" description=\"" + s1 + "\" customContent="
                + s2;
        Log.d("hhh", "onNotificationArrived=" + notifyString);
        SimpleDateFormat format=new SimpleDateFormat("MM-dd");
        String str=format.format(new Date());
        Log.d("hhh", str);
        if(!s2.isEmpty())
        {
            newsEntity= JSON.parseObject(s2,NewsEntity.class);
            messageBean.setM_name("咨询小助手");
            messageBean.setM_content(newsEntity.getLinkText());
            messageBean.setUrl(newsEntity.getLinkHref());
            messageBean.setMsg_date(str);
            messageBean.setmIndex(0);
            msgSqlUtil=new MessageServiceUtil(MyApplication.getApp_Context());
            msgSqlUtil.save(messageBean);
        }

    }

    public static Context getContext() {
        return context;
    }
}