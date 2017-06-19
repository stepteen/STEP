package example.com.step.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.contract.ContractData;
import example.com.step.service.PushTestReceiver;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import java.io.IOException;
import example.com.step.R;
import example.com.step.bean.User;
import example.com.step.util.DlgLoading;
import example.com.step.util.Md5;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qinghua on 2016/11/18.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private TextView tv_zhuce,tv_forgetPw;
    private Button btn_login;
    private PushTestReceiver  pushTestReceiver=new PushTestReceiver();
    private EditText et_zhanghao,et_password;
    private LinearLayout mLinearLayout;

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           switch(msg.what)
           {
               case 0:
                   Toast.makeText(LoginActivity.this,"账号或密码错误!",Toast.LENGTH_SHORT).show();
                   break;
               case 1:
                   Toast.makeText(LoginActivity.this,"登陆成功!",Toast.LENGTH_SHORT).show();
                   break;
               case 2:
                   Toast.makeText(LoginActivity.this,"网络异常!",Toast.LENGTH_SHORT).show();
                   break;
           }
        }
    };
    private DlgLoading mLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_login);
        PushManager.startWork(LoginActivity.this, PushConstants.LOGIN_TYPE_API_KEY,"D9wrN7mzYLYXljgGVO7jjUH9N1sTzLHT");
        initView();
    }

    private void controlKeyboardLayout(final View root) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adjustView(root);
            }
        });
    }

    private void adjustView(final View root) {
        Rect rect = new Rect();
        root.getWindowVisibleDisplayFrame(rect);
        ViewGroup.LayoutParams lp;
        lp = mLinearLayout.getLayoutParams();
        lp.height = rect.bottom;
        mLinearLayout.setLayoutParams(lp);
    }

    private void initView() {
        mLinearLayout= (LinearLayout) findViewById(R.id.ll_contains_login);
        mLoading = new DlgLoading((LoginActivity.this));
        btn_login= (Button) findViewById(R.id.btn_login);
        et_zhanghao= (EditText) findViewById(R.id.et_zhanghao);
        et_password= (EditText) findViewById(R.id.et_password);
        tv_zhuce= (TextView) findViewById(R.id.zhuce);
        tv_forgetPw= (TextView) findViewById(R.id.tv_wangjimima);
        btn_login.setOnClickListener(this);
        tv_zhuce.setOnClickListener(this);
        tv_forgetPw.setOnClickListener(this);

        controlKeyboardLayout(findViewById(R.id.ll_step_login));
        //全透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_login:
                if(islegal()) {
                    LoginpostAsynHttp(et_zhanghao.getText().toString(),et_password.getText().toString());
                }
                break;
            case R.id.zhuce:
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_wangjimima:
                Intent intent1=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 判断输入的账号密码是否合法
     */
    private boolean islegal()
    {
        if(et_zhanghao.getText().toString().isEmpty())
        {
            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(et_password.getText().toString().isEmpty())
        {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    /**
     * post请求服务器
     */
    private void LoginpostAsynHttp(String zhanghao,String mima) {
        String url= ContractData.URL+"/user/login";
        mLoading.show("正在登录...");
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.PHONENUM, zhanghao)
                .add(ContractData.USERPASSWORD,Md5.digest(mima.getBytes()))
                .add(ContractData.CHANNELID,pushTestReceiver.getChannelId()+"")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoading.dismiss();
                Log.d("zqh","str="+e.getMessage());
                new Thread()
                {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                        super.run();
                    }
                }.start();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("zqh","str="+str);
                mLoading.dismiss();
                if(!parseResultData(str))
                {
                    new Thread()
                    {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                            super.run();
                        }
                    }.start();
                }
                else{
                   mLoading.dismiss();
                    new Thread()
                    {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                            super.run();
                        }
                    }.start();
                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this,MainViewActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    boolean parseResultData(String resultData) {
        //先将返回值解析为ResultData
        ResultData data = ResultDataUtils.paraResult(resultData);
        if (data.getStatus().equals(ResultData.ERROR)) {
            return false;
        }
        //保存accesstoken 和usermsg
        SharedPreferences sp = getSharedPreferences("User",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putString("accessToken",data.getAccessToken());
        editor.putString("userInfo",data.getData().toString());
        Log.d("zqh",data.getData().toString());
        editor.commit();
        return true;
    }

    private void showSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_zhanghao.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSoftInput();
    }
}
