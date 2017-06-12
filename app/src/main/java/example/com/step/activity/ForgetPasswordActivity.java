package example.com.step.activity;

import android.app.Activity;
import android.os.Bundle;
import example.com.step.R;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import example.com.step.Result.ResultData;
import example.com.step.Result.ResultDataUtils;
import example.com.step.contract.ContractData;
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
 * Created by qinghua on 2016/12/6.
 */

public class ForgetPasswordActivity extends Activity implements View.OnClickListener{

    private String APPKEY="19aa9a37d6e5e";
    private String APPSECRET="d2fd551fa6777d5790733703f86c354e";
    private TextView tv_ObtainCode,btn_register;
    private ImageView iv_showorhide,register_return;
    private EditText et_code,et_phone,et_password;
    private int times=60;
    private boolean isFiret=true;
    private DlgLoading dlgLoading;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Message message=new Message();
            switch(msg.what)
            {
                case 0:
                    if(times>0)
                    {
                        tv_ObtainCode.setText(times+"s");
                        tv_ObtainCode.setBackground(getResources().getDrawable(R.drawable.fasong1));
                        tv_ObtainCode.setClickable(false);
                        times--;
                        threadHandler.sendMessageDelayed(message,1000);
                    }
                    else
                    {
                        times=60;
                        tv_ObtainCode.setClickable(true);
                        tv_ObtainCode.setText("获取验证码");
                        tv_ObtainCode.setBackground(getResources().getDrawable(R.drawable.fasong));
                        removeMessages(0);
                    }

                    break;
            }
        }
    };
    EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //验证码验证成功后的操作
                    postAsynHttp(et_phone.getText().toString(),et_password.getText().toString());
                    Log.d("zqh","提交验证码成功");
                    //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Log.d("zqh","获取验证码成功");

                    //获取验证码成功
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dlgLoading.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this,"验证码输入错误",Toast.LENGTH_SHORT).show();
                        threadHandler.removeMessages(0);
                        tv_ObtainCode.setText("获取验证码");
                        tv_ObtainCode.setBackground(getResources().getDrawable(R.drawable.fasong));
                        tv_ObtainCode.setClickable(true);
                        times=60;
                    }
                });
            }
        }
    };

    private Handler threadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword_layout);
        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
        initView();
        ititHandler();
    }

    private void ititHandler() {
        HandlerThread thread=new HandlerThread("JISHI");
        thread.start();
        threadHandler=new Handler(thread.getLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                Message message=new Message();;
                message.what=0;
                handler.sendMessageDelayed(message,1000);
            }
        };
    }

    private void initView() {
        dlgLoading=new DlgLoading(ForgetPasswordActivity.this);
        tv_ObtainCode= (TextView) findViewById(R.id.tv_ObtainCode);
        btn_register= (TextView) findViewById(R.id.btn_register);
        et_code= (EditText) findViewById(R.id.et_code);
        et_phone= (EditText) findViewById(R.id.et_phone);
        et_password= (EditText) findViewById(R.id.et_password);
        iv_showorhide= (ImageView) findViewById(R.id.iv_showorhide);
        register_return= (ImageView) findViewById(R.id.register_return);
        tv_ObtainCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        iv_showorhide.setOnClickListener(this);
        register_return.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_ObtainCode:
                if(et_phone.getText().toString().length()==11)
                {
                    SMSSDK.getVerificationCode("86",et_phone.getText().toString(),new OnSendMessageHandler() {
                        @Override
                        public boolean onSendMessage(String s, String s1) {
                            Log.d("xyz",s+"  "+s1);
                            return false;
                        }
                    });
                    threadHandler.sendEmptyMessage(0);
                }
                else
                {
                    Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_register:
                dlgLoading.show("提交修改数据中");
                SMSSDK.submitVerificationCode("86",et_phone.getText().toString(),et_code.getText().toString());
                break;
            case R.id.iv_showorhide:
                showPassword();
                break;
            case R.id.register_return:
                finish();
                break;
        }
    }
    void showPassword() {
        if (isFiret) {
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            iv_showorhide.setBackgroundResource(R.drawable.showpw);
            isFiret = false;
        } else {
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            iv_showorhide.setBackgroundResource(R.drawable.hidepw);
            isFiret = true;
        }
    }
    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }

    /**
     * post请求服务器
     */
    private void postAsynHttp(String zhanghao,String mima) {
        OkHttpClient mOkHttpClient=new OkHttpClient();
        String url=ContractData.URL+"/user/forgetPwd";
        RequestBody formBody = new FormBody.Builder()
                .add(ContractData.PHONENUM, zhanghao)
                .add(ContractData.NEWPWD, Md5.digest(mima.getBytes()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dlgLoading.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForgetPasswordActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dlgLoading.dismiss();
                ResultData data = ResultDataUtils.paraResult(response.body().string());
                if(!ResultDataUtils.isError(data)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

