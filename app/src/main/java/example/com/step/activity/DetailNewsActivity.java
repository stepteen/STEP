package example.com.step.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.daimajia.numberprogressbar.NumberProgressBar;

import example.com.step.R;
import example.com.step.bean.NewsEntity;
import example.com.step.util.DlgLoading;

/**
 * Created by qinghua on 2016/12/10.
 */

public class DetailNewsActivity extends Activity {

    private NewsEntity newsEntity=new NewsEntity();
    private WebView webView_news;
    private NumberProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailnews_layout);
        getDate();  //获取新闻信息
        initView();
    }
    private void getDate() {
        Intent intent=getIntent();
        newsEntity= (NewsEntity) intent.getSerializableExtra("news");
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
        progressBar= (NumberProgressBar) findViewById(R.id.number_progress_bar);
        webView_news= (WebView) findViewById(R.id.webView_news);
        WebSettings webSettings = webView_news.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webView_news.loadUrl(newsEntity.getLinkHref());
        webView_news.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webView_news.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                    progressBar.setProgress(newProgress);

            }

        });

    }

}
