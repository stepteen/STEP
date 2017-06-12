package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import example.com.step.R;
import example.com.step.bean.MessageBean;
import example.com.step.bean.NewsEntity;

/**
 * Created by qinghua on 2016/12/10.
 */

public class DetailMessageActivity extends Activity {

    private MessageBean messageBean=new MessageBean();
    private WebView webView_news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailnews_layout);
        getDate();  //获取数据
        initView();
    }
    private void getDate() {
        Intent intent=getIntent();
        messageBean= (MessageBean) intent.getSerializableExtra("messageBean");
    }
    /**
     * 实现返回上一个网页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView_news.canGoBack()) {
            webView_news.goBack();// 返回前一个页面
            return true;
        }
        else {
            finish();
            return  false;
        }
    }

    private void initView() {
        webView_news= (WebView) findViewById(R.id.webView_news);
        WebSettings webSettings = webView_news.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webView_news.loadUrl(messageBean.getUrl());
        webView_news.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }

}
